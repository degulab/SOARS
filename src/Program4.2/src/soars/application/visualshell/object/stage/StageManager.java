/*
 * 2005/05/01
 */
package soars.application.visualshell.object.stage;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.stage.edit.EditStageDlg;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class StageManager {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private StageManager _stageManager = null;

	/**
	 * 
	 */
	public Vector _main_stages = new Vector();

	/**
	 * 
	 */
	public Vector _initial_stages = new Vector();

	/**
	 * 
	 */
	public Vector _terminal_stages = new Vector();

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _stageManager) {
				_stageManager = new StageManager();
			}
		}
	}

	/**
	 * @return
	 */
	public static StageManager get_instance() {
		if ( null == _stageManager) {
			System.exit( 0);
		}

		return _stageManager;
	}

	/**
	 * 
	 */
	public StageManager() {
		super();
	}

	/**
	 * @param stageManager
	 */
	public StageManager(StageManager stageManager) {
		super();
		copy( stageManager);
	}

	/**
	 * @param stageManager
	 */
	private void copy(StageManager stageManager) {
		for ( int i = 0; i < stageManager._main_stages.size(); ++i) {
			Stage stage = ( Stage)stageManager._main_stages.get( i);
			_main_stages.add( new Stage( stage));
		}
		for ( int i = 0; i < stageManager._initial_stages.size(); ++i) {
			Stage stage = ( Stage)stageManager._initial_stages.get( i);
			_initial_stages.add( new Stage( stage));
		}
		for ( int i = 0; i < stageManager._terminal_stages.size(); ++i) {
			Stage stage = ( Stage)stageManager._terminal_stages.get( i);
			_terminal_stages.add( new Stage( stage));
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public Stage get(String name) {
		for ( int i = 0; i < _main_stages.size(); ++i) {
			Stage stage = ( Stage)_main_stages.get( i);
			if ( stage._name.equals( name))
				return stage;
		}
		for ( int i = 0; i < _initial_stages.size(); ++i) {
			Stage stage = ( Stage)_initial_stages.get( i);
			if ( stage._name.equals( name))
				return stage;
		}
		for ( int i = 0; i < _terminal_stages.size(); ++i) {
			Stage stage = ( Stage)_terminal_stages.get( i);
			if ( stage._name.equals( name))
				return stage;
		}
		return null;
	}

	/**
	 * @param name
	 */
	public void append_main_stage(String name) {
		if ( contains_this_name( name))
			return;

		append_main_stage( new Stage( name));
	}

	/**
	 * @param stage
	 */
	public void append_main_stage(Stage stage) {
		if ( !contains( "main stage", stage._name))
			_main_stages.add( stage);
		while ( remove_this_name( _initial_stages, stage._name))
			;
		while ( remove_this_name( _terminal_stages, stage._name))
			;
	}

	/**
	 * @param stage
	 */
	public void append_initial_stage(Stage stage) {
		if ( !contains( "initial stage", stage._name))
			_initial_stages.add( stage);
		while ( remove_this_name( _main_stages, stage._name))
			;
		while ( remove_this_name( _terminal_stages, stage._name))
			;
	}

	/**
	 * @param stage
	 */
	public void append_terminal_stage(Stage stage) {
		if ( !contains( "terminal stage", stage._name))
			_terminal_stages.add( stage);
		while ( remove_this_name( _initial_stages, stage._name))
			;
		while ( remove_this_name( _main_stages, stage._name))
			;
	}

	/**
	 * @param stageManager
	 */
	public void set(StageManager stageManager) {
		cleanup();

		for ( int i = 0; i < stageManager._main_stages.size(); ++i)
			_main_stages.add( ( Stage)stageManager._main_stages.get( i));

		for ( int i = 0; i < stageManager._initial_stages.size(); ++i)
			_initial_stages.add( ( Stage)stageManager._initial_stages.get( i));

		for ( int i = 0; i < stageManager._terminal_stages.size(); ++i)
			_terminal_stages.add( ( Stage)stageManager._terminal_stages.get( i));
	}

	/**
	 * 
	 */
	public void cleanup() {
		for ( int i = 0; i < _main_stages.size(); ++i) {
			Stage stage = ( Stage)_main_stages.get( i);
			stage.cleanup();
		}
		_main_stages.clear();

		for ( int i = 0; i < _initial_stages.size(); ++i) {
			Stage stage = ( Stage)_initial_stages.get( i);
			stage.cleanup();
		}
		_initial_stages.clear();

		for ( int i = 0; i < _terminal_stages.size(); ++i) {
			Stage stage = ( Stage)_terminal_stages.get( i);
			stage.cleanup();
		}
		_terminal_stages.clear();
	}

	/**
	 * 
	 */
	public void update() {
		if ( LayerManager.get_instance().contains_available_chartObject()) {
			if ( !contains_this_name( Constant._initializeChartStageName)) {
				Stage stage = new Stage( Constant._initializeChartStageName);
				_initial_stages.insertElementAt( stage, 0);
			}
			if ( !contains_this_name( Constant._updateChartStageName))
				_main_stages.add( new Stage( Constant._updateChartStageName));
		} else {
			if ( contains_this_name( Constant._initializeChartStageName))
				remove_this_name( Constant._initializeChartStageName);
			if ( contains_this_name( Constant._updateChartStageName))
				remove_this_name( Constant._updateChartStageName);
		}
	}

	/**
	 * @param type
	 * @param name
	 * @return
	 */
	public boolean contains(String type, String name) {
		if ( type.equals( "initial stage"))
			return contains( name, _initial_stages);
		else if ( type.equals( "main stage"))
			return contains( name, _main_stages);
		else if ( type.equals( "terminal stage"))
			return contains( name, _terminal_stages);
		else
			return false;
	}

	/**
	 * @param name
	 * @return
	 */
	private boolean contains_this_name(String name) {
		if ( contains( name, _initial_stages))
			return true;
		if ( contains( name, _main_stages))
			return true;
		if ( contains( name, _terminal_stages))
			return true;
		return false;
	}

	/**
	 * @param name
	 * @param stages
	 * @return
	 */
	private boolean contains(String name, Vector stages) {
		for ( int i = 0; i < stages.size(); ++i) {
			Stage stage = ( Stage)stages.get( i);
			if ( stage._name.equals( name))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 */
	private void remove_this_name(String name) {
		while ( remove_this_name( _initial_stages, name))
			;
		while ( remove_this_name( _terminal_stages, name))
			;
		while ( remove_this_name( _main_stages, name))
			;
	}

	/**
	 * @param stages
	 * @param name
	 * @return
	 */
	private boolean remove_this_name(Vector stages, String name) {
		for ( int i = 0; i < stages.size(); ++i) {
			Stage stage = ( Stage)stages.get( i);
			if ( stage._name.equals( name)) {
				stages.removeElementAt( i);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param new_name
	 * @param original_name
	 * @return
	 */
	public boolean rename(String new_name, String original_name) {
		for ( int i = 0; i < _main_stages.size(); ++i) {
			Stage stage = ( Stage)_main_stages.get( i);
			if ( stage._name.equals( original_name)) {
				stage._name = new_name;
				return true;
			}
		}

		for ( int i = 0; i < _initial_stages.size(); ++i) {
			Stage stage = ( Stage)_initial_stages.get( i);
			if ( stage._name.equals( original_name)) {
				stage._name = new_name;
				return true;
			}
		}

		for ( int i = 0; i < _terminal_stages.size(); ++i) {
			Stage stage = ( Stage)_terminal_stages.get( i);
			if ( stage._name.equals( original_name)) {
				stage._name = new_name;
				return true;
			}
		}

		return false;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean remove(String name) {
		for ( int i = 0; i < _main_stages.size(); ++i) {
			Stage stage = ( Stage)_main_stages.get( i);
			if ( stage._name.equals( name)) {
				_main_stages.removeElementAt( i);
				return true;
			}
		}

		for ( int i = 0; i < _initial_stages.size(); ++i) {
			Stage stage = ( Stage)_initial_stages.get( i);
			if ( stage._name.equals( name)) {
				_initial_stages.removeElementAt( i);
				return true;
			}
		}

		for ( int i = 0; i < _terminal_stages.size(); ++i) {
			Stage stage = ( Stage)_terminal_stages.get( i);
			if ( stage._name.equals( name)) {
				_terminal_stages.removeElementAt( i);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param contains_empty
	 * @return
	 */
	public String[] get_names(boolean contains_empty) {
		Vector stages = new Vector();
		for ( int i = 0; i < _initial_stages.size(); ++i) {
			Stage stage = ( Stage)_initial_stages.get( i);
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			stages.add( stage._name);
		}
		for ( int i = 0; i < _main_stages.size(); ++i) {
			Stage stage = ( Stage)_main_stages.get( i);
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			stages.add( stage._name);
		}
		for ( int i = 0; i < _terminal_stages.size(); ++i) {
			Stage stage = ( Stage)_terminal_stages.get( i);
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			stages.add( stage._name);
		}

		if ( contains_empty && !stages.contains( ""))
			stages.insertElementAt( "", 0);

		return ( String[])stages.toArray( new String[ 0]);
		//return Tool.quick_sort_string( stages, true, false);
	}

	/**
	 * @param editRoleFrame
	 * @param frame
	 */
	public void edit(EditRoleFrame editRoleFrame, Frame frame) {
		EditStageDlg editStageDlg = new EditStageDlg( frame,
			ResourceManager.get_instance().get( "edit.stage.dialog.title"),
			true,
			this,
			editRoleFrame);
		if ( !editStageDlg.do_modal())
			return;
	}

	/**
	 * @return
	 */
	public String get_initial_data() {
		if ( _main_stages.isEmpty() && _initial_stages.isEmpty() && _terminal_stages.isEmpty())
			return "";

		String script1 = "";
		for ( int i = 0; i < _initial_stages.size(); ++i) {
			Stage stage = ( Stage)_initial_stages.get( i);
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			script1 += stage.get_initial_data( ResourceManager.get_instance().get( "initial.data.initial.stage"));
		}

		String script2 = "";
		for ( int i = 0; i < _main_stages.size(); ++i) {
			Stage stage = ( Stage)_main_stages.get( i);
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			script2 += stage.get_initial_data( ResourceManager.get_instance().get( "initial.data.main.stage"));
		}

		String script3 = "";
		for ( int i = 0; i < _terminal_stages.size(); ++i) {
			Stage stage = ( Stage)_terminal_stages.get( i);
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName))
				continue;

			script3 += stage.get_initial_data( ResourceManager.get_instance().get( "initial.data.terminal.stage"));
		}

		if ( script1.equals( "") && script2.equals( "") && script3.equals( ""))
			return "";

		return ( script1 + script2 + script3 + Constant._lineSeparator);
	}

	/**
	 * @param grid
	 * @param ga
	 * @return
	 */
	public String get_script(boolean grid, boolean ga) {
		// TODO
		if ( _main_stages.isEmpty() && _initial_stages.isEmpty() && _terminal_stages.isEmpty()
			&& !LayerManager.get_instance().initial_data_file_exists())
			return "";

		String script1 = ( LayerManager.get_instance().initial_data_file_exists() ? ( Constant._initialDataFileStageName + Constant._lineSeparator) : "");
		for ( int i = 0; i < _initial_stages.size(); ++i) {
			Stage stage = ( Stage)_initial_stages.get( i);
			if ( ( grid || ga) && ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName)))
				continue;

			script1 += stage.get_script();
		}

		String script2 = "";
		for ( int i = 0; i < _main_stages.size(); ++i) {
			Stage stage = ( Stage)_main_stages.get( i);
			if ( ( grid || ga) && ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName)))
				continue;

			script2 += stage.get_script();
		}

		String script3 = "";
		for ( int i = 0; i < _terminal_stages.size(); ++i) {
			Stage stage = ( Stage)_terminal_stages.get( i);
			if ( ( grid || ga) && ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName)))
				continue;

			script3 += stage.get_script();
		}

		String script = "";

		if ( !script1.equals( "")) {
			script += "initialStage" + Constant._lineSeparator;
			script += ( script1 + Constant._lineSeparator);
		}

		if ( !script2.equals( "")) {
			script += "stage" + Constant._lineSeparator;
			script += ( script2 + Constant._lineSeparator);
		}

		if ( !script3.equals( "")) {
			script += "terminalStage" + Constant._lineSeparator;
			script += ( script3 + Constant._lineSeparator);
		}

		return script;
	}

	/**
	 * @param stageNameMap
	 * @param programRootFolder
	 * @param packagePrefix
	 * @return
	 */
	public boolean export_TStages(Map<String, String> stageNameMap, File programRootFolder, String packagePrefix) {
		// TODO Auto-generated method stub
		String text = "package " + packagePrefix + ";\n\n"
			+ "public class TStages {\n";

		List<Stage> initialStages = get( _initial_stages);
		text = get( text, initialStages, stageNameMap, "初期ステージ");
		if ( null == text)
			return false;

		List<Stage> mainStages = get( _main_stages);
		text = get( text, mainStages, stageNameMap, "メインステージ");
		if ( null == text)
			return false;

		List<Stage> terminalStages = get( _terminal_stages);
		text = get( text, terminalStages, stageNameMap, "終了ステージ");
		if ( null == text)
			return false;

		text += "}\n";

		File file = new File( programRootFolder, "TStages.java");
		return FileUtility.write_text_to_file( file, text, "UTF8");
	}

	/**
	 * @param originalStages
	 * @return
	 */
	private List<Stage> get(Vector originalStages) {
		// TODO Auto-generated method stub
		List<Stage> stages = new ArrayList<>();
		for ( int i = 0; i < originalStages.size(); ++i) {
			Stage stage = ( Stage)originalStages.get( i);
			if ( stage._name.equals( Constant._initializeChartStageName)
				|| stage._name.equals( Constant._updateChartStageName)
				|| stage._name.equals( Constant._initialDataFileStageName))
				continue;

			stages.add( stage);
		}
		return stages;
	}

	/**
	 * @param text
	 * @param stages
	 * @param stageNameMap
	 * @param name
	 * @return
	 */
	private String get(String text, List<Stage> stages, Map<String, String> stageNameMap, String title) {
		// TODO Auto-generated method stub
		if ( stages.isEmpty())
			return text;

		text += "\n" + JavaProgramExporter._indents[ 1] + "//\n"
				+ JavaProgramExporter._indents[ 1] + "// " + title + "\n"
				+ JavaProgramExporter._indents[ 1] + "//\n";

		for ( int i = 0; i < stages.size(); ++i) {
			text += "\n" + JavaProgramExporter._indents[ 1] + "/** " + String.valueOf( i + 1) + ". */\n";
			String name = JavaProgramExporter.format_variable_name( stages.get( i)._name, "ST");
			stageNameMap.put( stages.get( i)._name, "TStages." + name);
			text += JavaProgramExporter._indents[ 1] + "public static final String " + name + " = \"" + stages.get( i)._name + "\";\n";
		}

		return text;
	}

	/**
	 * @param writer
	 * @return
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( _main_stages.isEmpty() && _initial_stages.isEmpty() && _terminal_stages.isEmpty())
			return true;

		writer.startElement( null, null, "stage_data", new AttributesImpl());

		for ( int i = 0; i < _main_stages.size(); ++i) {
			Stage stage = ( Stage)_main_stages.get( i);
			stage.write( "stage", writer);
		}

		for ( int i = 0; i < _initial_stages.size(); ++i) {
			Stage stage = ( Stage)_initial_stages.get( i);
			stage.write( "initial_stage", writer);
		}

		for ( int i = 0; i < _terminal_stages.size(); ++i) {
			Stage stage = ( Stage)_terminal_stages.get( i);
			stage.write( "terminal_stage", writer);
		}

		writer.endElement( null, null, "stage_data");

		return true;
	}
}
