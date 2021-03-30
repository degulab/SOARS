/**
 * 
 */
package soars.application.visualshell.executor.modelbuilder;

import java.io.File;
import java.util.Vector;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.executor.monitor.MonitorPropertyPage;
import soars.application.visualshell.main.Constant;

/**
 * The class which runs the ModelBuilders in parallel.
 * @author kurata / SOARS project
 */
public class ParallelModelBuilder implements Runnable {

	/**
	 * 
	 */
	private ScriptFile _scriptFile = null;

	/**
	 * 
	 */
	private MonitorPropertyPage _monitorPropertyPage = null;

	/**
	 * 
	 */
	static protected Vector _threads = new Vector();

	/**
	 * Returns true if this thread is alive.
	 * @return true if this thread is alive
	 */
	public static boolean isAlive() {
		for ( int i = 0; i < _threads.size(); ++i) {
			Thread thread = ( Thread)_threads.get( i);
			if ( thread.isAlive())
				return true;
		}
		return false;
	}

	/**
	 * Runs ModelBuilder with the specified data.
	 * @param script_file the specified script file
	 * @param monitorPropertyPage the specified monitor component to display the log output of the ModelBuilder
	 */
	public static void execute(ScriptFile scriptFile, MonitorPropertyPage monitorPropertyPage) {
		ParallelModelBuilder parallelModelBuilder = new ParallelModelBuilder( scriptFile, monitorPropertyPage);
		Thread thread = new Thread( parallelModelBuilder);
		monitorPropertyPage._thread = thread;
		//thread.setName();
		thread.start();
		_threads.add( thread);
	}

	/**
	 * Creates the instance of ParallelModelBuilder class with the specified data.
	 * @param script_file the specified script file
	 * @param monitorPropertyPage the specified monitor component to display the log output of the ModelBuilder
	 */
	public ParallelModelBuilder(ScriptFile scriptFile, MonitorPropertyPage monitorPropertyPage) {
		super();
		_scriptFile = scriptFile;
		_monitorPropertyPage = monitorPropertyPage;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String current_directory_name = System.getProperty( Constant._soarsHome);
		File current_directory = new File( current_directory_name);
		if ( null == current_directory)
			return;

		ModelBuilder.run( _scriptFile, _monitorPropertyPage, current_directory, current_directory_name);
		synchronized( _monitorPropertyPage._lock_process) {
			_monitorPropertyPage._process = null;
		}
		_scriptFile.clear_work_directory();
	}
}
