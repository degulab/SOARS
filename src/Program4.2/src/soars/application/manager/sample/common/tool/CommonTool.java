/**
 * 
 */
package soars.application.manager.sample.common.tool;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import soars.application.manager.sample.main.Environment;

/**
 * @author kurata
 *
 */
public class CommonTool {

	/**
	 * Returns the directory selected by the user.
	 * @param directoryKey the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the directory selected by the user
	 */
	public static File get_directory(String directoryKey, String title, Component component) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		File directory = null;
		String value = Environment.get_instance().get( directoryKey, "");
		if ( null != value && !value.equals( ""))
			directory = new File( value);

		if ( null != directory && directory.exists() && directory.isDirectory()) {
			fileChooser.setCurrentDirectory( new File( directory.getAbsolutePath() + "/../"));
			fileChooser.setSelectedFile( directory);
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			directory = fileChooser.getSelectedFile();
			Environment.get_instance().set( directoryKey, directory.getAbsolutePath());
			return directory;
		}

		return null;
	}
}
