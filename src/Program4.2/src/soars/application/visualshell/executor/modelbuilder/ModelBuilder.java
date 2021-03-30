/*
 * Created on 2006/02/22
 */
package soars.application.visualshell.executor.modelbuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.executor.common.Parameters;
import soars.application.visualshell.executor.monitor.MonitorPropertyPage;
import soars.application.visualshell.main.Constant;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.utility.tool.clipboard.Clipboard;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * The ModelBuilder runner class.
 * @author kurata / SOARS project
 */
public class ModelBuilder {

	/**
	 * Starts the ModelBuilders with the specified script files.
	 * @param scriptFiles the specified script files
	 */
	public static void start(ScriptFile[] scriptFiles) {
		String current_directory_name = System.getProperty( Constant._soarsHome);
		File current_directory = new File( current_directory_name);
		if ( null == current_directory) {
			remove( scriptFiles);
			return;
		}

		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");

		for ( int i = 0; i < scriptFiles.length; ++i) {
			Parameters parameters = new Parameters();
			if ( !parameters.setup( scriptFiles[ i]))
				continue;

			start( scriptFiles[ i], current_directory, current_directory_name, property_directory_name, memory_size, osname, parameters.get_user_data_directory(), parameters.get_gaming_runner_file());
		}
	}

	/**
	 * @param scriptFiles
	 */
	private static void remove(ScriptFile[] scriptFiles) {
		for ( int i = 0; i < scriptFiles.length; ++i)
			FileUtility.delete( scriptFiles[ i]._path.getParentFile(), true);
	}

	/**
	 * @param scriptFile
	 * @param current_directory
	 * @param current_directory_name
	 * @param property_directory_name
	 * @param memory_size
	 * @param osname
	 * @param user_data_directory
	 * @param gaming_runner_file
	 */
	private static void start(ScriptFile scriptFile, File current_directory, 	String current_directory_name, String property_directory_name, String memory_size, String osname, File user_data_directory, File gaming_runner_file) {
		String[] cmdarray = get_cmdarray( scriptFile, current_directory_name, property_directory_name, memory_size, osname, user_data_directory, gaming_runner_file);

		debug( "ModelBuilder.start", osname, System.getProperty( "os.version"), cmdarray);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray, null, current_directory);
			new StreamPumper( process.getInputStream(), false).start();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			e.printStackTrace();
			FileUtility.delete( scriptFile._path.getParentFile(), true);
			return;
		}
	}

	/**
	 * @param scriptFiles
	 * @param current_directory_name
	 * @param property_directory_name
	 * @param memory_size
	 * @param osname
	 * @param user_data_directory
	 * @param gaming_runner_file
	 * @return
	 */
	private static String[] get_cmdarray(ScriptFile scriptFile, String current_directory_name, String property_directory_name, String memory_size, String osname, File user_data_directory, File gaming_runner_file) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
//			list.add( "-D" + Constant._system_default_file_encoding + "=" + System.getProperty( Constant._system_default_file_encoding, ""));
				list.add( "-X" + Constant._macScreenMenuName + "=SOARS Model Builder");
				//list.add( "-D" + Constant._mac_screen_menu_name + "=SOARS Model Builder");
				//list.add( "-X" + Constant._mac_icon_filename + "=../resource/icon/application/modelbuilder/icon.png");
				list.add( "-D" + Constant._macScreenMenu + "=true");
		} else {
			list.add( Tool.get_java_command());
		}

//	list.add( "-Djava.endorsed.dirs=" + current_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + current_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		if ( null != gaming_runner_file)
			list.add( "-D" + Constant._soarsGamingRunnerFile + "=" + gaming_runner_file.getAbsolutePath());
		list.add( "-D" + Constant._soarsSorFile + "=" + scriptFile._path.getAbsolutePath());
		if ( null != user_data_directory)
			list.add( "-D" + Constant._soarsUserDataDirectory + "=" + user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/"));
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
//		list.add( "-jar");
//		list.add( current_directory_name + "/" + Constant._model_builder_jar_filename);
		list.add( "-cp");
		//list.add( current_directory_name + "/" + Constant._model_builder_jar_filename
		//	+ ( ( null == gaming_runner_file) ? "" : ( File.pathSeparator + current_directory_name + "/../" + Constant._gaming_runner_jar_filename)));
		list.add( current_directory_name + "/" + Constant._soarsEngineJarFilename + File.pathSeparator + current_directory_name + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._soarsEngineGuiMainClassname);
		list.add( scriptFile._path.getAbsolutePath());

		return list.toArray( new String[ 0]);
	}

	/**
	 * Returns true for running the ModelBuilder with the specified script file.
	 * @param scriptFile the specified script file
	 * @param monitorPropertyPage the monitor component to display the log output of the ModelBuilder
	 * @param current_directory the directory which contains the ModelBuilder.jar
	 * @param current_directory_name the name of the directory which contains the ModelBuilder.jar
	 * @return true for running the ModelBuilder with the specified script file
	 */
	public static boolean run(ScriptFile scriptFile, MonitorPropertyPage monitorPropertyPage, File current_directory, String current_directory_name) {
		Parameters parameters = new Parameters();
		if ( !parameters.setup( scriptFile))
			return false;

		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		String[] cmdarray = get_cmdarray( scriptFile._path, current_directory, current_directory_name, property_directory_name, memory_size, osname, parameters.get_user_data_directory(), parameters.get_gaming_runner_file());

		debug( "ModelBuilder.run", osname, System.getProperty( "os.version"), cmdarray);

		try {
			InputStream inputStream, errorStream;

			synchronized( monitorPropertyPage._lock_process) {
				monitorPropertyPage._process = ( Process)Runtime.getRuntime().exec( cmdarray, null, current_directory);
				inputStream = monitorPropertyPage._process.getInputStream();
				errorStream = monitorPropertyPage._process.getErrorStream();
			}

			new StreamPumper( inputStream, false).start();

			String text = read( errorStream, monitorPropertyPage);
			if ( null == text) {
				monitorPropertyPage.append( "");
				return false;
			}

			monitorPropertyPage.append( text);

			if ( -1 == text.indexOf( "Execution Time:"))
				return false;

			//System.out.println( text);
		} catch (IOException e) {
			//e.printStackTrace();
			monitorPropertyPage.append( "");
			return false;
		}

		return true;
	}

	/**
	 * @param script_file
	 * @param current_directory
	 * @param current_directory_name
	 * @param property_directory_name
	 * @param memory_size
	 * @param osname
	 * @param user_data_directory
	 * @param gaming_runner_file
	 * @return
	 */
	private static String[] get_cmdarray(File script_file, File current_directory, String current_directory_name, String property_directory_name, String memory_size, String osname, File user_data_directory, File gaming_runner_file) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
//		list.add( "-D" + Constant._system_default_file_encoding + "=" + System.getProperty( Constant._system_default_file_encoding, ""));
		} else {
			list.add( Tool.get_java_command());
		}

//		list,.add( "-Djava.endorsed.dirs=" + current_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + current_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
		if ( null != gaming_runner_file)
			list.add( "-D" + Constant._soarsGamingRunnerFile + "=" + gaming_runner_file.getAbsolutePath());
		list.add( "-D" + Constant._soarsSorFile + "=" + script_file.getAbsolutePath());
		if ( null != user_data_directory)
			list.add( "-D" + Constant._soarsUserDataDirectory + "=" + user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/"));
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-cp");
		//list.add( current_directory_name + "/" + Constant._model_builder_jar_filename
		//	+ ( ( null == gaming_runner_file) ? "" : ( File.pathSeparator + current_directory_name + "/../" + Constant._gaming_runner_jar_filename)));
		list.add( current_directory_name + "/" + Constant._soarsEngineJarFilename + File.pathSeparator + current_directory_name + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._soarsEngineConsoleMainClassname);
		list.add( "run=" + script_file.getAbsolutePath());

		return list.toArray( new String[ 0]);
	}

	/**
	 * Returns true for running the ModelBuilder with the specified script file.
	 * @param scriptFiles the specified script files
	 * @param monitorPropertyPage the monitor component to display the log output of the ModelBuilder
	 * @param current_directory the directory which contains the ModelBuilder.jar
	 * @param current_directory_name the name of the directory which contains the ModelBuilder.jar
	 * @return true for running the ModelBuilder with the specified script file
	 */
	public static boolean run(ScriptFile[] scriptFiles, MonitorPropertyPage monitorPropertyPage, File current_directory, String current_directory_name) {
		for ( ScriptFile scriptFile:scriptFiles) {
			Parameters parameters = new Parameters();
			if ( !parameters.setup( scriptFile))
				return false;
		}

		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		String[] cmdarray = get_cmdarray( scriptFiles, current_directory, current_directory_name, property_directory_name, memory_size, osname);

		debug( "ModelBuilder.run", osname, System.getProperty( "os.version"), cmdarray);

		try {
			InputStream inputStream, errorStream;

			synchronized( monitorPropertyPage._lock_process) {
				monitorPropertyPage._process = ( Process)Runtime.getRuntime().exec( cmdarray, null, current_directory);
				inputStream = monitorPropertyPage._process.getInputStream();
				errorStream = monitorPropertyPage._process.getErrorStream();
			}

			new StreamPumper( inputStream, false).start();

			String text = read( errorStream, monitorPropertyPage);
			if ( null == text) {
				monitorPropertyPage.append( "");
				return false;
			}

			monitorPropertyPage.append( text);

			if ( -1 == text.indexOf( "Execution Time:"))
				return false;

			synchronized( monitorPropertyPage._lock_process) {
				monitorPropertyPage._process.waitFor();
			}

			//System.out.println( text);
		} catch (IOException e) {
			//e.printStackTrace();
			monitorPropertyPage.append( "");
			return false;
		} catch (InterruptedException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

/**
	 * @param scriptFiles
	 * @param current_directory
	 * @param current_directory_name
	 * @param property_directory_name
	 * @param memory_size
	 * @param osname
	 * @return
	 */
	private static String[] get_cmdarray(ScriptFile[] scriptFiles, File current_directory, String current_directory_name, String property_directory_name, String memory_size, String osname) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
//		list.add( "-D" + Constant._system_default_file_encoding + "=" + System.getProperty( Constant._system_default_file_encoding, ""));
		} else {
			list.add( Tool.get_java_command());
		}

//		list,.add( "-Djava.endorsed.dirs=" + current_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + current_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
//		if ( null != gaming_runner_file)
//			list.add( "-D" + Constant._soars_gaming_runner_file + "=" + gaming_runner_file.getAbsolutePath());
//		list.add( "-D" + Constant._soars_sor_file + "=" + script_file.getAbsolutePath());
//		if ( null != user_data_directory)
//			list.add( "-D" + Constant._soars_user_data_directory + "=" + user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/"));
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-cp");
		//list.add( current_directory_name + "/" + Constant._model_builder_jar_filename
		//	+ ( ( null == gaming_runner_file) ? "" : ( File.pathSeparator + current_directory_name + "/../" + Constant._gaming_runner_jar_filename)));
		list.add( current_directory_name + "/" + Constant._soarsEngineJarFilename + File.pathSeparator + current_directory_name + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._soarsEngineConsoleMainClassname);

		for ( int i = 0; i < scriptFiles.length; ++i) {
			if ( 0 < i)
				list.add( "reset=");

			list.add( "run=" + scriptFiles[ i]._path.getAbsolutePath().replaceAll( "\\\\", "/"));
//			list.add( ( ( 0 == i) ? "run=" : "") + scriptFiles.get( i)._path.getAbsolutePath().replaceAll( "\\\\", "/"));
		}

//		String command = "";
//		for ( int i = 0; i < list.size(); ++i)
//			command += ( 0 == i) ? "" : " " + list.get( i);
//
//		System.out.println( command);

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param inputStream
	 * @param monitorPropertyPage
	 * @return
	 */
	private static String read(InputStream inputStream, MonitorPropertyPage monitorPropertyPage) {
		List<String> list = new ArrayList<String>();
		InputStreamReader inputStreamReader = null;
		try {
//		inputStreamReader = new InputStreamReader( inputStream, "SJIS");
			String charsetName = "";
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))
				charsetName = System.getProperty( Constant._systemDefaultFileEncoding, "");
			else
				charsetName = System.getProperty( "file.encoding", "");

			if ( charsetName.equals( ""))
				inputStreamReader = new InputStreamReader( inputStream);
			else
				inputStreamReader = new InputStreamReader( inputStream, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		try {
			boolean ignore = true;
			String line = "";
			while ( true) {
				int c = inputStreamReader.read();
				if ( -1 == c)
					break;

				line += ( char)c;
				if ( Constant._lineSeparator.charAt( 0) == c) {
//					System.out.println( line);
					if ( line.startsWith( "pleaseprint"))
						ignore = false;
					else 	if ( line.startsWith( "pleaseflush"))
						ignore = true;
					else {
						if ( !ignore)
							monitorPropertyPage.append( line);
						else {
							if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by:"))
								list.add( line);
						}
					}

					line = "";
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}
//		try {
//			String line = "";
//			while ( true) {
//				int c = inputStream.read();
//				if ( -1 == c)
//					break;
//
//				line += ( char)c;
//				if ( Constant._line_separator.charAt( 0) == c) {
//					//System.out.println( line);
//					if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by:"))
//						list.add( line);
//
//					line = "";
//				}
//			}
//		} catch (IOException e) {
//			//e.printStackTrace();
//			return null;
//		}

		boolean emergence = false;
		List<String> lines = new ArrayList<String>();
		for ( int i = list.size() - 1; i >= 0; --i) {
			String line = list.get( i);
			if ( emergence) {
				if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by: java.lang.RuntimeException:"))
					lines.add( 0, line);
			} else {
				if ( line.startsWith( "Execution Time:"))
					lines.add( 0, line);
				else if ( line.startsWith( "Caused by:")) {
					lines.add( 0, line);
					emergence = true;
				}
			}
		}

		String text = "";
		for ( int i = 0; i < lines.size(); ++i) {
			String line = lines.get( i);
			text += line;
		}

		//System.out.println( text);

		return text;
	}

//	/**
//	 * Returns true for running the ModelBuilder with the specified script file on Genetic Algorithm.
//	 * @param scriptFile the specified script file
//	 * @param log_textArea the monitor component to display the log output of the ModelBuilder
//	 * @param process
//	 * @param lock_process synchronized object
//	 * @return true for running the ModelBuilder with the specified script file
//	 */
//	public static boolean run_on_genetic_algorithm(ScriptFile scriptFile, JTextArea log_textArea, List<Process> process, Object lock_process) {
//		String current_directory_name = System.getProperty( Constant._soars_home);
//		File current_directory = new File( current_directory_name);
//		if ( null == current_directory)
//			return false;
//
//		Parameters parameters = new Parameters();
//		if ( !parameters.setup( scriptFile))
//			return false;
//
//		String property_directory_name = System.getProperty( Constant._soars_property);
//		String memory_size = CommonEnvironment.get_instance().get_memory_size();
//		String osname = System.getProperty( "os.name");
//		String[] cmdarray = get_cmdarray( scriptFile._path, current_directory, current_directory_name, property_directory_name, memory_size, osname, parameters.get_user_data_directory(), parameters.get_gaming_runner_file());
//
//		//debug( "ModelBuilder.run", osname, System.getProperty( "os.version"), cmdarray);
//
//		try {
//			InputStream inputStream, errorStream;
//
//			synchronized( lock_process) {
//				process.add( ( Process)Runtime.getRuntime().exec( cmdarray, null, current_directory));
//				inputStream = process.get( 0).getInputStream();
//				errorStream = process.get( 0).getErrorStream();
//			}
//
//			new StreamPumper( inputStream, false).start();
//
//			String text = read( errorStream, log_textArea);
//			if ( null == text) {
//				log_textArea.append( "");
//				return false;
//			}
//
//			log_textArea.append( text);
//
//			if ( -1 == text.indexOf( "Execution Time:"))
//				return false;
//
//			//System.out.println( text);
//		} catch (IOException e) {
//			//e.printStackTrace();
//			log_textArea.append( "");
//			return false;
//		}
//
//		return true;
//	}

	/**
	 * Returns true for running the ModelBuilder with the specified script file on Genetic Algorithm.
	 * @param scriptFiles the specified script files
	 * @param log_textArea the monitor component to display the log output of the ModelBuilder
	 * @param process
	 * @param lock_process synchronized object
	 * @return true for running the ModelBuilder with the specified script file
	 */
	public static boolean run_on_genetic_algorithm(List<ScriptFile> scriptFiles, JTextArea log_textArea, List<Process> process, Object lock_process) {
		String current_directory_name = System.getProperty( Constant._soarsHome);
		File current_directory = new File( current_directory_name);
		if ( null == current_directory)
			return false;

		for ( ScriptFile scriptFile:scriptFiles) {
			Parameters parameters = new Parameters();
			if ( !parameters.setup_for_genetic_algorithm( scriptFile))
				return false;
		}

		String property_directory_name = System.getProperty( Constant._soarsProperty);
		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		String[] cmdarray = get_cmdarray( scriptFiles, current_directory, current_directory_name, property_directory_name, memory_size, osname);

		//debug( "ModelBuilder.run", osname, System.getProperty( "os.version"), cmdarray);

		try {
			InputStream inputStream, errorStream;

			synchronized( lock_process) {
				process.add( ( Process)Runtime.getRuntime().exec( cmdarray, null, current_directory));
				inputStream = process.get( 0).getInputStream();
				errorStream = process.get( 0).getErrorStream();
			}

			new StreamPumper( inputStream, false).start();

			String text = read( errorStream, log_textArea);
			if ( null == text) {
				log_textArea.append( "");
				return false;
			}

			log_textArea.append( text);

			if ( -1 == text.indexOf( "Execution Time:"))
				return false;

			synchronized( lock_process) {
				process.get( 0).waitFor();
			}

			//System.out.println( text);
		} catch (IOException e) {
			//e.printStackTrace();
			log_textArea.append( "");
			return false;
		} catch (InterruptedException e) {
			//e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param scriptFiles
	 * @param current_directory
	 * @param current_directory_name
	 * @param property_directory_name
	 * @param memory_size
	 * @param osname
	 * @return
	 */
	private static String[] get_cmdarray(List<ScriptFile> scriptFiles, File current_directory, String current_directory_name, String property_directory_name, String memory_size, String osname) {
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			//if ( System.getProperty( Constant._system_default_file_encoding, "").equals( ""))
				list.add( "-Dfile.encoding=UTF-8");
//		list.add( "-D" + Constant._system_default_file_encoding + "=" + System.getProperty( Constant._system_default_file_encoding, ""));
		} else {
			list.add( Tool.get_java_command());
		}

//		list,.add( "-Djava.endorsed.dirs=" + current_directory_name + "/../" + Constant._xerces_directory);
		list.add( "-D" + Constant._soarsHome + "=" + current_directory_name);
		list.add( "-D" + Constant._soarsProperty + "=" + property_directory_name);
//		if ( null != gaming_runner_file)
//			list.add( "-D" + Constant._soars_gaming_runner_file + "=" + gaming_runner_file.getAbsolutePath());
//		list.add( "-D" + Constant._soars_sor_file + "=" + script_file.getAbsolutePath());
//		if ( null != user_data_directory)
//			list.add( "-D" + Constant._soars_user_data_directory + "=" + user_data_directory.getAbsolutePath().replaceAll( "\\\\", "/"));
		if ( !memory_size.equals( "0")) {
			list.add( "-D" + Constant._soarsMemorySize + "=" + memory_size);
			list.add( "-Xmx" + memory_size + "m");
		}
		list.add( "-cp");
		//list.add( current_directory_name + "/" + Constant._model_builder_jar_filename
		//	+ ( ( null == gaming_runner_file) ? "" : ( File.pathSeparator + current_directory_name + "/../" + Constant._gaming_runner_jar_filename)));
		list.add( current_directory_name + "/" + Constant._soarsEngineJarFilename + File.pathSeparator + current_directory_name + "/../" + Constant._gamingRunnerJarFilename);
		list.add( Constant._soarsEngineConsoleMainClassname);

		for ( int i = 0; i < scriptFiles.size(); ++i) {
			if ( 0 < i)
				list.add( "reset=");

			list.add( "run=" + scriptFiles.get( i)._path.getAbsolutePath().replaceAll( "\\\\", "/"));
//			list.add( ( ( 0 == i) ? "run=" : "") + scriptFiles.get( i)._path.getAbsolutePath().replaceAll( "\\\\", "/"));
		}

//		String command = "";
//		for ( int i = 0; i < list.size(); ++i)
//			command += ( 0 == i) ? "" : " " + list.get( i);
//
//		System.out.println( command);

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param inputStream
	 * @param log_textArea
	 * @return
	 */
	private static String read(InputStream inputStream, JTextArea log_textArea) {
		InputStreamReader inputStreamReader = null;
		try {
//		inputStreamReader = new InputStreamReader( inputStream, "SJIS");
			String charsetName = "";
			if ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))
				charsetName = System.getProperty( Constant._systemDefaultFileEncoding, "");
			else
				charsetName = System.getProperty( "file.encoding", "");

			if ( charsetName.equals( ""))
				inputStreamReader = new InputStreamReader( inputStream);
			else
				inputStreamReader = new InputStreamReader( inputStream, charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

//		List<String> list = new ArrayList<String>();
		String text = "";
		try {
//			boolean ignore = true;
			String line = "";
			while ( true) {
				int c = inputStreamReader.read();
				if ( -1 == c)
					break;

				line += ( char)c;
				if ( Constant._lineSeparator.charAt( 0) == c) {
//					System.out.println( line);
//					if ( line.startsWith( "pleaseprint"))
//						ignore = false;
//					else 	if ( line.startsWith( "pleaseflush"))
//						ignore = true;
//					else {
//						if ( !ignore)
//							log_textArea.append( line);
//						else {
							if ( line.equals( "") || line.startsWith( "Execution Time:") || line.startsWith( "Caused by:"))
								text += line;
//						}
//					}

					line = "";
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}

		return text;

//		try {
//			String line = "";
//			while ( true) {
//				int c = inputStream.read();
//				if ( -1 == c)
//					break;
//
//				line += ( char)c;
//				if ( Constant._line_separator.charAt( 0) == c) {
//					//System.out.println( line);
//					if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by:"))
//						list.add( line);
//
//					line = "";
//				}
//			}
//		} catch (IOException e) {
//			//e.printStackTrace();
//			return null;
//		}

//		boolean emergence = false;
//		List<String> lines = new ArrayList<String>();
//		for ( int i = list.size() - 1; i >= 0; --i) {
//			String line = list.get( i);
//			if ( emergence) {
//				if ( line.startsWith( "Execution Time:") || line.startsWith( "Caused by: java.lang.RuntimeException:"))
//					lines.add( 0, line);
//			} else {
//				if ( line.startsWith( "Execution Time:"))
//					lines.add( 0, line);
//				else if ( line.startsWith( "Caused by:")) {
//					lines.add( 0, line);
//					emergence = true;
//				}
//			}
//		}
//
//		String text = "";
//		for ( int i = 0; i < lines.size(); ++i)
//			text += lines.get( i);
//
//		//System.out.println( text);
//
//		return text;
	}

	/**
	 * @param type
	 * @param osname
	 * @param osversion
	 * @param cmdarray
	 */
	private static void debug(String type, String osname, String osversion, String[] cmdarray) {
		String text = ""; 
		text += ( "Type : " + type + Constant._lineSeparator);
		text += ( "OS : " + osname + " [" + osversion + "]" + Constant._lineSeparator);
		for ( int i = 0; i < cmdarray.length; ++i)
			text += ( "Parameter : " + cmdarray[ i] + Constant._lineSeparator);

		Clipboard.set( text);
	}
}
