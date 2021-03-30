/**
 * 
 */
package soars.application.manager.sample.property;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import soars.application.manager.sample.main.Constant;
import soars.common.soars.tool.DocumentWriter;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.xml.dom.DomUtility;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class ModelInformation {

	/**
	 * 
	 */
	private File _file = null;

	/**
	 * 
	 */
	private File _parentDirectory = null;

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * 
	 */
	public String _title = "";

	/**
	 * 
	 */
	public String _date = "";

	/**
	 * 
	 */
	public String _author = "";

	/**
	 * 
	 */
	public String _email = "";

	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * @param file
	 * @return
	 */
	public static ModelInformation get(File file) {
		ModelInformation modelInformation = new ModelInformation( file);
		if ( !modelInformation.load())
			return null;

		return modelInformation;
	}

	/**
	 * @param file
	 */
	private ModelInformation(File file) {
		super();
		_file = file;
	}

	/**
	 * @return
	 */
	private boolean load() {
		_parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == _parentDirectory)
			return false;

		if ( !ZipUtility.decompress( _file, _parentDirectory)) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		_rootDirectory = new File( _parentDirectory, Constant._rootDirectoryName);
		if ( !_rootDirectory.exists() || !_rootDirectory.isDirectory()) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		File file = new File( _rootDirectory, Constant._dataFilename);
		if ( !file.exists() || !file.isFile()) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !read( file)) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean read(File file) {
		Document document = DomUtility.read( file);
		if ( null == document)
			return false;

		Element root = document.getDocumentElement();
		if ( null == root)
			return false;

		Node node = XmlTool.get_node( root, "comment_data");
		if ( null == node)
			return false;

		String value = XmlTool.get_attribute( node, "title");
		if ( null != value)
			_title = value;

		value = XmlTool.get_attribute( node, "date");
		if ( null != value)
			_date = value;

		value = XmlTool.get_attribute( node, "author");
		if ( null != value)
			_author = value;

		value = XmlTool.get_attribute( node, "email");
		if ( null != value)
			_email = value;

		value = XmlTool.get_node_value( node);
		if ( null != value)
			_comment = value;

		return true;
	}

	/**
	 * 
	 */
	public void cleanup() {
		if ( null == _parentDirectory)
			return;

		FileUtility.delete( _parentDirectory, true);
	}

	/**
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 * @return
	 */
	public boolean update(String title, String date, String author, String email, String comment) {
		if ( null == _file || !_file.exists() || !_file.isFile())
			return false;

		if ( null == _parentDirectory || !_parentDirectory.exists() || !_parentDirectory.isDirectory())
			return false;

		if ( null == _rootDirectory || !_rootDirectory.exists() || !_rootDirectory.isDirectory())
			return false;

		File file = new File( _rootDirectory, Constant._dataFilename);
		if ( !file.exists() || !file.isFile())
			return false;

		Document document = DomUtility.read( file);
		if ( null == document)
			return false;

		Element root = document.getDocumentElement();
		if ( null == root)
			return false;

		Element element = ( Element)XmlTool.get_node( root, "comment_data");
		if ( null == element)
			return false;

		XmlTool.set_attribute( document, element, "title", title);
		XmlTool.set_attribute( document, element, "date", date);
		XmlTool.set_attribute( document, element, "author", author);
		XmlTool.set_attribute( document, element, "email", email);
		XmlTool.set_text( document, element, comment);

		if ( !DomUtility.write( document, file, "UTF-8", null))
			return false;

		if ( !DocumentWriter.execute( _rootDirectory, file))
			return false;

		if ( !ZipUtility.compress( _file, _rootDirectory, _parentDirectory))
			return false;

		_title = title;
		_date = date;
		_author = author;
		_email = email;
		_comment = comment;

		return true;
	}
}
