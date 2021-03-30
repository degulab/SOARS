/*
 * 2005/05/16
 */
package soars.application.animator.file.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The XML SAX writer for Animator data.
 * @author kurata / SOARS project
 */
public class SaxWriter {

	/**
	 * Returns true if the specified Animator data is saved to the specified file successfully.
	 * @param file the specified file
	 * @return true if the specified Animator data is saved to the specified file successfully
	 */
	public static boolean execute(File file) {
		try {
			OutputStreamWriter outputStreamWriter
				= new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "animation_data", new AttributesImpl());

			if ( !ScenarioManager.get_instance().write( writer))
				return false;

			if ( !ObjectManager.get_instance().write( writer))
				return false;

			if ( !ChartObjectMap.get_instance().write( writer))
				return false;

			writer.endElement( null, null, "animation_data");

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
}
