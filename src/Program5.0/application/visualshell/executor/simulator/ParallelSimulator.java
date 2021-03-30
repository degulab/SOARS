/**
 * 
 */
package soars.application.visualshell.executor.simulator;

import java.io.File;

import soars.application.visualshell.common.file.ScriptFile;
import soars.application.visualshell.executor.common.LogInformationFileMaker;
import soars.application.visualshell.executor.common.Parameters;
import soars.application.visualshell.main.Constant;

/**
 * The class which runs the Simulators in parallel.
 * @author kurata / SOARS project
 */
public class ParallelSimulator implements Runnable {

	/**
	 * 
	 */
	private ScriptFile _scriptFile = null;

	/**
	 * 
	 */
	private Parameters _parameters = null;

	/**
	 * 
	 */
	private boolean _toFile = false;

	/**
	 * Runs Simulator with the specified data.
	 * @param scriptFile the specified script file
	 * @param parameters the simulation parameters
	 * @param toFile
	 * @param direct
	 */
	public static void execute(ScriptFile scriptFile, Parameters parameters, boolean toFile, boolean direct) {
		ParallelSimulator parallelSimulator = new ParallelSimulator( scriptFile, parameters, toFile);
		if ( direct)
			parallelSimulator.run( direct);
		else {
			Thread thread = new Thread( parallelSimulator);
			thread.start();
		}
	}

	/**
	 * @param scriptFile
	 * @param parameters the simulation parameters
	 * @param toFile 
	 */
	protected ParallelSimulator(ScriptFile scriptFile, Parameters parameters, boolean toFile) {
		super();
		_scriptFile = scriptFile;
		_parameters = parameters;
		_toFile = toFile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		run( false);
	}

	/**
	 * @param direct
	 */
	private void run(boolean direct) {
		// TODO Auto-generated method stub
		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		File currentDirectory = new File( currentDirectoryName);
		if ( null == currentDirectory)
			return;

		if ( _toFile) {
			if ( !LogInformationFileMaker.make( _scriptFile._logFolderPath))
				return;
		}

		Simulator.run( _scriptFile, _parameters, currentDirectory, currentDirectoryName, direct);
	}
}
