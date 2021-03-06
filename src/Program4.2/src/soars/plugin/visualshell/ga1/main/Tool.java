/*
 * Created on 2006/06/19
 */
package soars.plugin.visualshell.ga1.main;

import java.awt.Component;
import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;

import soars.common.utility.swing.tool.ExampleFileFilter;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.ssh.filechooser.SftpFileChooser;
import soars.common.utility.tool.ssh.SshTool;

/**
 * @author kurata
 */
public class Tool {

	/**
	 * @param filename
	 * @param title
	 * @param extensions
	 * @param description
	 * @param component
	 * @return
	 */
	public static File get_file(String filename, String title, String[] extensions, String description, Component component) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}

		File file = new File( filename);
		File directory = file.getParentFile();

		if ( null != directory && directory.exists())
			fileChooser.setCurrentDirectory( directory);

		if ( null != file && file.exists())
			fileChooser.setSelectedFile( file);

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component))
			return fileChooser.getSelectedFile();

		return null;
	}

	/**
	 * @param directory_name
	 * @param title
	 * @param component
	 * @return
	 */
	public static File get_directory(String directory_name, String title, Component component) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( null != directory_name && !directory_name.equals( "")) {
			File directory = new File( directory_name);
			if ( directory.exists()) {
				fileChooser.setCurrentDirectory( new File( directory_name + "/../"));
				fileChooser.setSelectedFile( new File( directory_name));
			}
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component))
			return fileChooser.getSelectedFile();

		return null;
	}

	/**
	 * @param directory_name
	 * @param title
	 * @param frame
	 * @param component
	 * @param host
	 * @param account
	 * @param password
	 * @param root_directory
	 * @param username
	 * @return
	 */
	public static String get_remote_directory(String directory_name, String title, Frame frame, Component component, String host, String account, String password, String root_directory, String username) {
		if ( null != directory_name && !directory_name.equals( ""))
			directory_name = ( SshTool.directory_exists( host, account, password, directory_name)
				? directory_name : root_directory + "/" + username);
		else
			directory_name = root_directory + "/" + username;

		SftpFileChooser sftpFileChooser = new SftpFileChooser( frame, title, host, account, password,
			directory_name, root_directory + "/" + username, root_directory + "/" + username);
		sftpFileChooser.setFileSelectionMode( SftpFileChooser.DIRECTORIES_ONLY);

		if ( !sftpFileChooser.do_modal( component))
			return null;

		directory_name = sftpFileChooser._selectedFile;

		return directory_name;
	}

	/**
	 * @param parameters
	 * @param log_textArea
	 * @return
	 */
	static public boolean clear(Parameters parameters, JTextArea log_textArea) {
		if ( Environment.get_instance().get( Environment._grid_key).equals( "true")) {
			SshClient sshClient = SshTool.getSshClient( parameters._grid_portal_ip_address, parameters._username, parameters._password);
			if ( null == sshClient)
				return false;

			SftpClient sftpClient = SshTool.getSftpClient( sshClient);
			if ( null == sftpClient) {
				sshClient.disconnect();
				return false;
			}

			String script_directory = Environment.get_instance().get( Environment._script_directory_key, "");
			if ( !SshTool.delete( sftpClient, script_directory, false)) {
				SshTool.close( sftpClient);
				sshClient.disconnect();
				log_textArea.append( "Could not clear! : " + script_directory + "\n");
				return false;
			}

			String log_directory = Environment.get_instance().get( Environment._log_directory_key, "");
			if ( !SshTool.delete( sftpClient, log_directory, false)) {
				SshTool.close( sftpClient);
				sshClient.disconnect();
				log_textArea.append( "Could not clear! : " + log_directory + "\n");
				return false;
			}

			SshTool.close( sftpClient);
			sshClient.disconnect();
		}

		String local_log_directory = Environment.get_instance().get( Environment._local_log_directory_key, "");
		if ( !FileUtility.delete( new File( local_log_directory), false)) {
			log_textArea.append( "Could not clear! : " + local_log_directory + "\n");
			return false;
		}

		return true;
	}
}
