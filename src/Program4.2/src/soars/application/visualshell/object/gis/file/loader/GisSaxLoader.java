/*
 * 2005/04/22
 */
package soars.application.visualshell.object.gis.file.loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.variable.object.base.ObjectBase;
import soars.application.visualshell.object.gis.edit.variable.object.base.SimpleVariableObject;
import soars.application.visualshell.object.gis.edit.variable.object.keyword.KeywordObject;
import soars.application.visualshell.object.gis.edit.variable.object.number.NumberObject;

/**
 * The XML SAX loader for GIS data.
 * @author kurata / SOARS project
 */
public class GisSaxLoader extends DefaultHandler {

	/**
	 * 
	 */
	private GisDataManager _gisDataManager = null;

	/**
	 * 
	 */
	private File _gisDataDirectory = null;

	/**
	 * 
	 */
	private String _version = "2.0";

	/**
	 * 
	 */
	private List<Field> _spotFields = new ArrayList<Field>();

	/**
	 * 
	 */
	private List<ObjectBase> _objectBases = new ArrayList<ObjectBase>();

	/**
	 * 
	 */
	private SimpleVariableObject _simpleVariableObject = null;

	/**
	 * 
	 */
	private ScaleData _scaleData = new ScaleData();

	/**
	 * 
	 */
	private List<FileData> _fileDataList = new ArrayList<FileData>();

	/**
	 * 
	 */
	private String _state = "";

	/**
	 * 
	 */
	static private boolean _result;

	/**
	 * Returns true if loading the specified file is completed successfully.
	 * @param file the specified XML file of GIS data
	 * @param gisDataManager 
	 * @param gisDataDirectory
	 * @return objects if loading the specified file is completed successfully
	 */
	public static Object[] execute(File file, GisDataManager gisDataManager, File gisDataDirectory) {
		_result = false;

		GisSaxLoader gisSaxLoader = new GisSaxLoader( gisDataManager, gisDataDirectory);

		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			saxParser.parse( file, gisSaxLoader);
			gisSaxLoader.at_end_of_load();
		} catch (Exception e) {
			e.printStackTrace();
			gisSaxLoader.at_end_of_load();
			return null;
		}

		if ( !_result)
			return null;

		return gisSaxLoader.get();
	}

	/**
	 * @return
	 */
	private Object[] get() {
		return new Object[] { _spotFields, _objectBases, _scaleData, _fileDataList};
	}

	/**
	 * 
	 */
	private void at_end_of_load() {
		if ( !_result)
			return;

		if ( !_gisDataManager.load( _fileDataList, _gisDataDirectory)) {
			_result = false;
			return;
		}
	}

	/**
	 * Creates the XML SAX loader for GIS data.
	 * @param gisDataManager
	 * @param gisDataDirectory
	 */
	public GisSaxLoader(GisDataManager gisDataManager, File gisDataDirectory) {
		super();
		_gisDataManager = gisDataManager;
		_gisDataDirectory = gisDataDirectory;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement( String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {

		super.startElement(arg0, arg1, arg2, arg3);

		if ( arg2.equals( "gis_data")) {
			get_version( arg3);
			_result = true;
			return;
		}

		if ( !_result)
			return;

		if ( arg2.equals( "spot_data")) {
			on_spot_data( arg3);
		} else if ( arg2.equals( "variable_data")) {
			on_variable_data( arg3);
		} else if ( arg2.equals( "keyword")) {
			on_keyword( arg3);
		} else if ( arg2.equals( "number_object")) {
			on_number_object( arg3);
		} else if ( arg2.equals( "field")) {
			on_field( arg3);
		} else if ( arg2.equals( "comment")) {
			on_comment( arg3);
		} else if ( arg2.equals( "scale_data")) {
			on_scale_data( arg3);
		} else if ( arg2.equals( "file_data")) {
			on_file_data( arg3);
		} else if ( arg2.equals( "file")) {
			on_file( arg3);
		}
	}

	/**
	 * @param attributes
	 */
	private void get_version(Attributes attributes) {
		String version = attributes.getValue( "version");
		if ( null == version || version.equals( ""))
			return;

		_version = version;
	}

	/**
	 * @param attributes
	 */
	private void on_spot_data(Attributes attributes) {
		_state = "spot_data";
	}

	/**
	 * @param attributes
	 */
	private void on_variable_data(Attributes attributes) {
		_state = "variable_data";
	}

	/**
	 * @param attributes
	 */
	private void on_keyword(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		_simpleVariableObject = new KeywordObject( name);
	}

	/**
	 * @param attributes
	 */
	private void on_number_object(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String type = attributes.getValue( "type");
		if ( null == type || type.equals( "")) {
			_result = false;
			return;
		}

		_simpleVariableObject = new NumberObject( name, type);
	}

	/**
	 * @param attributes
	 */
	private void on_field(Attributes attributes) {
		String value = attributes.getValue( "flag");
		if ( null == value || ( !value.equals( "true") && !value.equals( "false"))) {
			_result = false;
			return;
		}

		boolean flag = value.equals( "true");

		value = attributes.getValue( "value");
		if ( null == value) {
			_result = false;
			return;
		}

		if ( _state.equals( "spot_data"))
			_spotFields.add( new Field( flag, value));
		else if ( _state.equals( "variable_data")) {
			if ( null == _simpleVariableObject) {
				_result = false;
				return;
			}

			_simpleVariableObject._fields.add( new Field( flag, value));
		} else {
			_result = false;
			return;
		}
	}

	/**
	 * @param attributes
	 */
	private void on_comment(Attributes attributes) {
	}

	/**
	 * @param attributes
	 */
	private void on_scale_data(Attributes attributes) {
		_state = "scale_data";

		String value = attributes.getValue( "type");
		if ( null == value || value.equals( "")) {
			_result = false;
			return;
		}

		try {
			_scaleData._type = Integer.parseInt( value);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			_result = false;
			return;
		}

		if ( 0 == _scaleData._type || 2 == _scaleData._type) {
			value = attributes.getValue( "horizontal");
			if ( null == value) {
				_result = false;
				return;
			}

			if ( !value.equals( "")) {
				try {
					_scaleData._horizontal = Integer.parseInt( value);
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					_result = false;
					return;
				}
			}
		}

		if ( 1 == _scaleData._type || 2 == _scaleData._type) {
			value = attributes.getValue( "vertical");
			if ( null == value) {
				_result = false;
				return;
			}

			if ( !value.equals( "")) {
				try {
					_scaleData._vertical = Integer.parseInt( value);
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					_result = false;
					return;
				}
			}
		}
	}

	/**
	 * @param attributes
	 */
	private void on_file_data(Attributes attributes) {
		_state = "file_data";
	}

	/**
	 * @param attributes
	 */
	private void on_file(Attributes attributes) {
		String name = attributes.getValue( "name");
		if ( null == name || name.equals( "")) {
			_result = false;
			return;
		}

		String role = attributes.getValue( "role");
		if ( null == role) {
			_result = false;
			return;
		}

		_fileDataList.add( new FileData( name, role));
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		if ( !_result)
			return;

		if ( !_state.equals( "variable_data"))
			return;

		if ( null == _simpleVariableObject)
			return;

		String comment = new String( arg0, arg1, arg2);
		_simpleVariableObject._comment += comment;
	}

	/* (Non Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {

		if ( !_result) {
			super.endElement(arg0, arg1, arg2);
			return;
		}

		if ( arg2.equals( "spot_data")) {
			on_spot_data();
		} else if ( arg2.equals( "variable_data")) {
			on_variable_data();
		} else if ( arg2.equals( "keyword")) {
			on_keyword();
		} else if ( arg2.equals( "number_object")) {
			on_number_object();
		} else if ( arg2.equals( "field")) {
			on_field();
		} else if ( arg2.equals( "comment")) {
			on_comment();
		} else if ( arg2.equals( "scale_data")) {
			on_scale_data();
		} else if ( arg2.equals( "file_data")) {
			on_file_data();
		} else if ( arg2.equals( "file")) {
			on_file();
		}

		super.endElement(arg0, arg1, arg2);
	}

	/**
	 * 
	 */
	private void on_spot_data() {
		_state = "";
	}

	/**
	 * 
	 */
	private void on_variable_data() {
		_state = "";
	}

	/**
	 * 
	 */
	private void on_keyword() {
		_objectBases.add( _simpleVariableObject);
		_simpleVariableObject = null;
	}

	/**
	 * 
	 */
	private void on_number_object() {
		_objectBases.add( _simpleVariableObject);
		_simpleVariableObject = null;
	}

	/**
	 * 
	 */
	private void on_field() {
	}

	/**
	 * 
	 */
	private void on_comment() {
	}

	/**
	 * 
	 */
	private void on_scale_data() {
		_state = "";
	}

	/**
	 * 
	 */
	private void on_file_data() {
		_state = "";
	}

	/**
	 * 
	 */
	private void on_file() {
	}
}
