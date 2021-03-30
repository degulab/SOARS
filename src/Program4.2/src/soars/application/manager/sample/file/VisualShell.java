/**
 * 
 */
package soars.application.manager.sample.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.manager.sample.main.Constant;
import soars.common.soars.tool.DocumentWriter;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class VisualShell {

	/**
	 * 
	 */
	private File _folder = null;

	/**
	 * 
	 */
	private String _title = "";

	/**
	 * 
	 */
	private String _date = "";

	/**
	 * 
	 */
	private String _author = "";

	/**
	 * 
	 */
	private String _email = "";

	/**
	 * 
	 */
	private String _comment = "";

	/**
	 * 
	 */
	private File _parentDirectory = null;

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * @param folder
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 * @return
	 */
	public static File create(File folder, String title, String date, String author, String email, String comment) {
		VisualShell visualShell = new VisualShell( folder, title, date, author, email, comment);
		return visualShell.make();
	}

	/**
	 * @param folder
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 */
	public VisualShell(File folder, String title, String date, String author, String email, String comment) {
		super();
		_folder = folder;
		_title = title;
		_date = date;
		_author = author;
		_email = email;
		_comment = comment;
		
	}

	/**
	 * @return
	 */
	private File make() {
		if ( !setup_work_directory())
			return null;

		if ( !write( new File( _rootDirectory, Constant._dataFilename))) {
			FileUtility.delete( _parentDirectory, true);
			return null;
		}

		if ( !DocumentWriter.execute( _rootDirectory, new File( _rootDirectory, Constant._dataFilename))) {
			FileUtility.delete( _parentDirectory, true);
			return null;
		}

		File file = create_new_file();
		if ( !ZipUtility.compress( file, _rootDirectory, _parentDirectory)) {
			FileUtility.delete( _parentDirectory, true);
			return null;
		}

		FileUtility.delete( _parentDirectory, true);

		return file;
	}

	/**
	 * @return
	 */
	private boolean setup_work_directory() {
		if ( null != _parentDirectory)
			return true;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._rootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean write(File file) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			if ( null == outputStreamWriter)
				return false;

			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "visual_shell_data", new AttributesImpl());

			write_layer_data( writer);
			write_simulation_data( writer);
			write_log_data( writer);
			write_comment_data( writer);

			writer.endElement( null, null, "visual_shell_data");

			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_layer_data(Writer writer) throws SAXException {
		writer.startElement( null, null, "layer_data", new AttributesImpl());

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", "1");
		writer.writeElement( null, null, "layer", attributesImpl);

		writer.endElement( null, null, "layer_data");
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_simulation_data(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "export_end_time", "", "true");
		attributesImpl.addAttribute( null, null, "random_seed", "", "");
		writer.startElement( null, null, "simulation_data", attributesImpl);

		write_simulation_data( "start", writer);
		write_simulation_data( "step", writer);
		write_simulation_data( "end", writer);
		write_simulation_data( "log_step", writer);

		writer.endElement( null, null, "simulation_data");
	}

	/**
	 * @param name
	 * @param writer
	 * @throws SAXException
	 */
	private void write_simulation_data(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "day", "", "0");
		attributesImpl.addAttribute( null, null, "hour", "", "00");
		attributesImpl.addAttribute( null, null, "minute", "", "00");
		writer.writeElement( null, null, name, attributesImpl);
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_log_data(Writer writer) throws SAXException {
		writer.startElement( null, null, "log_data", new AttributesImpl());

		writer.startElement( null, null, "agent_keyword_data", new AttributesImpl());

		write_log_data( "$Name", "false", writer);
		write_log_data( "$Role", "false", writer);
		write_log_data( "$Spot", "true", writer);

		writer.endElement( null, null, "agent_keyword_data");

		writer.endElement( null, null, "log_data");
	}

	/**
	 * @param name
	 * @param flag
	 * @param writer
	 * @throws SAXException
	 */
	private void write_log_data(String name, String flag, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( name));
		attributesImpl.addAttribute( null, null, "flag", "", flag);
		writer.writeElement( null, null, "agent_keyword", attributesImpl);
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_comment_data(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( _title));
		attributesImpl.addAttribute( null, null, "date", "", Writer.escapeAttributeCharData( _date));
		attributesImpl.addAttribute( null, null, "author", "", Writer.escapeAttributeCharData( _author));
		attributesImpl.addAttribute( null, null, "email", "", Writer.escapeAttributeCharData( _email));
		writer.startElement( null, null, "comment_data", attributesImpl);

		writer.characters( _comment.toCharArray(), 0, _comment.length());

		writer.endElement( null, null, "comment_data");
	}

	/**
	 * @return
	 */
	private File create_new_file() {
		int index = 1;
		while ( true) {
			File file = new File( _folder, "file" + String.valueOf( index++) + ".vsl");
			if ( !file.exists())
				return file;
		}
	}
}
