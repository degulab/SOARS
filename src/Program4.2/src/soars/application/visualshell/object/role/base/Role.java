/*
 * 2005/05/13
 */
package soars.application.visualshell.object.role.base;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.exporter.java.JavaProgramExporter;
import soars.application.visualshell.file.exporter.java.object.RoleClassCode;
import soars.application.visualshell.file.exporter.java.object.RoleDataSet;
import soars.application.visualshell.file.exporter.java.object.RuleData;
import soars.application.visualshell.file.exporter.java.object.Variable;
import soars.application.visualshell.file.importer.initial.role.RoleData;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.player.base.PlayerBase;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectInObject;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectObject;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectOutObject;
import soars.application.visualshell.object.role.base.object.RuleManager;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class Role extends DrawObject {

	/**
	 * 
	 */
	public RuleManager _ruleManager = null;

	/**
	 * 
	 */
	public ConnectInObject _connectInObject = null;

	/**
	 * 
	 */
	public ConnectOutObject _connectOutObject = null;

	/**
	 * 
	 */
	static public EditRoleFrame _editRoleFrame = null;

	/**
	 * @param role
	 * @return
	 */
	public static Role create_instance(Role role) {
		if ( role instanceof AgentRole)
			return new AgentRole( ( AgentRole)role);
		else if ( role instanceof SpotRole)
			return new SpotRole( ( SpotRole)role);
		else
			return null;
	}

	/**
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2D
	 */
	public Role(int id, String name, Point position, Graphics2D graphics2D) {
		super(id, name, position, graphics2D);
		_ruleManager = new RuleManager();
		_connectInObject = new ConnectInObject( this);
		_connectOutObject = new ConnectOutObject( this);
	}

	/**
	 * @param role
	 */
	public Role(Role role) {
		super(role);
		_ruleManager = new RuleManager( role._ruleManager);
		_connectInObject = new ConnectInObject( role._connectInObject, this);
		_connectOutObject = new ConnectOutObject( role._connectOutObject, this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
		_ruleManager.cleanup();
		disconnect( _connectInObject);
		disconnect( _connectOutObject);
		_connectInObject.cleanup();
		_connectOutObject.cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_name()
	 */
	public String get_name() {
		if ( null == _connectInObject)
			return super.get_name();

		return _connectInObject.get_name();
	}

	/**
	 * @param outObject
	 */
	private void disconnect(ConnectObject connectObject) {
		while ( !connectObject._connectObjects.isEmpty())
			connectObject.disconnect( ( ConnectObject)connectObject._connectObjects.get( 0));
	}

	/**
	 * @param roleData
	 * @return
	 */
	public boolean update(RoleData roleData) {
		return _ruleManager.update( roleData._ruleManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#contains(java.awt.Point)
	 */
	public boolean contains(Point point) {
		if ( super.contains(point))
			return true;

		if ( _connectInObject.contains( point, _position, _dimension))
			return true;

		if ( _connectOutObject.contains( point, _position, _dimension))
			return true;

		return false;
	}

	/**
	 * @param point
	 * @return
	 */
	public boolean isRole(Point point) {
		return super.contains(point);
	}

	/**
	 * @param point
	 * @return
	 */
	public boolean isConnectInObject(Point point) {
		return _connectInObject.contains( point, _position, _dimension);
	}

	/**
	 * @param point
	 * @return
	 */
	public boolean isConnectOutObject(Point point) {
		return _connectOutObject.contains( point, _position, _dimension);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean is_closure(Vector<DrawObject> drawObjects) {
		return _connectInObject.is_closure( drawObjects);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#draw(java.awt.Graphics2D, java.awt.image.ImageObserver)
	 */
	public void draw(Graphics2D graphics2D, ImageObserver imageObserver) {
		super.draw(graphics2D, imageObserver);
		graphics2D.setColor( _imageColor);
		graphics2D.draw3DRect( _position.x, _position.y, _dimension.width, _dimension.height, true);
		graphics2D.draw3DRect( _position.x + 4, _position.y, _dimension.width - 8, _dimension.height, true);

		_connectInObject.draw( _position, _dimension, graphics2D, imageObserver);
		_connectOutObject.draw( _position, _dimension, graphics2D, imageObserver);
	}

	/**
	 * @param graphics2D
	 */
	public void draw_connection(Graphics2D graphics2D) {
		//_connectOutObject.draw_connection( graphics2D);
		_connectInObject.draw_connection( graphics2D);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove()
	 */
	public boolean can_remove() {
		return LayerManager.get_instance().can_remove_role( _name, true, this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_paste()
	 */
	public void on_paste() {
		_ruleManager.on_paste( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_agent_name(java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_agent_name(String headName, Vector ranges) {
		return _ruleManager.can_adjust_agent_name( _name, headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_agent_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_agent_name(String headName, Vector ranges, String newHeadName, Vector newRanges) {
		return _ruleManager.can_adjust_agent_name( _name, headName, ranges, newHeadName, newRanges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_spot_name(String headName, Vector ranges) {
		return _ruleManager.can_adjust_spot_name( _name, headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_spot_name(String headName, Vector ranges, String newHeadName, Vector newRanges) {
		return _ruleManager.can_adjust_spot_name( _name, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove(java.lang.String, java.lang.String, boolean, java.lang.String, java.util.Vector)
	 */
	public boolean can_remove(String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector ranges) {
		// TODO 従来のもの
		return _ruleManager.can_remove( kind, name, otherSpotsHaveThisObjectName, headName, ranges, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.util.Vector)
	 */
	public boolean can_remove(String player, String kind, String objectName, boolean otherPlayersHaveThisObjectName, String headName, Vector ranges) {
		// TODO これからはこちらに移行してゆく
		return _ruleManager.can_remove( player, kind, objectName, otherPlayersHaveThisObjectName, headName, ranges, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#is_number_object_type_correct(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_number_object_type_correct(String player, String numberObjectName, String newType) {
		return _ruleManager.is_number_object_type_correct( player, numberObjectName, newType, this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove_role_name(java.lang.String)
	 */
	public boolean can_remove_role_name(String roleName) {
		return _ruleManager.can_remove_role_name( _name, roleName);
	}

	/**
	 * @param function
	 * @return
	 */
	public boolean can_remove_expression(String function) {
		return _ruleManager.can_remove_expression( _name, function);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		return _ruleManager.update_agent_name_and_number(newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector ranges, String newHeadName, Vector newRanges) {
		return _ruleManager.update_spot_name_and_number(newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_role_name(java.lang.String, java.lang.String)
	 */
	public boolean update_role_name(String originalName, String name) {
		return _ruleManager.update_role_name( originalName, name);
	}

	/**
	 * @param kind
	 * @param originalName
	 * @param newName
	 * @param player
	 * @return
	 */
	public boolean update_object_name(String kind, String originalName, String newName, String player) {
		return _ruleManager.update_object_name( kind, originalName, newName, player, this);
	}

	/**
	 * @param visualShellExpressionManager
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		return _ruleManager.update_expression( visualShellExpressionManager);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_agent_name_and_number(java.lang.String, java.util.Vector)
	 */
	public void on_remove_agent_name_and_number(String headName, Vector ranges) {
		_ruleManager.on_remove_agent_name_and_number(headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_spot_name_and_number(java.lang.String, java.util.Vector)
	 */
	public void on_remove_spot_name_and_number(String headName, Vector ranges) {
		_ruleManager.on_remove_spot_name_and_number(headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_role_name(java.util.Vector)
	 */
	public void on_remove_role_name(Vector<String> roleNames) {
		_ruleManager.on_remove_role_name( roleNames);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_stage_name(java.util.Vector)
	 */
	public void on_remove_stage_name(Vector<String> stageNames) {
		_ruleManager.on_remove_stage_name( stageNames);
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String newName, String originalName) {
		return _ruleManager.update_stage_name( _name, newName, originalName);
	}

	/**
	 * @param stageName
	 * @return
	 */
	public boolean can_remove_stage_name(String stageName) {
		return _ruleManager.can_remove_stage_name( _name, stageName);
	}

	/**
	 * @param stageNames
	 * @return
	 */
	public boolean can_adjust_stage_name(Vector<String> stageNames) {
		return _ruleManager.can_adjust_stage_name( _name, stageNames);
	}

	/**
	 * @return
	 */
	public boolean update_stage_manager() {
		return _ruleManager.update_stage_manager();
	}

	/**
	 * @param expressionMap
	 * @param usedExpressionMap
	 */
	public void get_used_expressions(TreeMap expressionMap, TreeMap usedExpressionMap) {
		_ruleManager.get_used_expressions( expressionMap, usedExpressionMap);
	}

	/**
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		return _ruleManager.update_function( originalFunctionName, newFunctionName);
	}

	/**
	 * @param initialValues
	 * @param suffixes
	 */
	public void get_initial_values(Vector<String> initialValues, String[] suffixes) {
		_ruleManager.get_initial_values( initialValues, suffixes);
	}

	/**
	 * @return
	 */
	public boolean transform_time_conditions_and_commands() {
		return _ruleManager.transform_time_conditions_and_commands( this);
	}

	/**
	 * @return
	 */
	public boolean transform_keyword_conditions_and_commands() {
		return _ruleManager.transform_keyword_conditions_and_commands( this);
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean has_same_agent_name(String name, String number) {
		return _ruleManager.has_same_agent_name( name, number);
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		return _ruleManager.contains_this_alias( alias);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#edit(javax.swing.JComponent, java.awt.Frame)
	 */
	public void edit(JComponent component, Frame frame) {
		if ( null != _editRoleFrame)
			return;

		String key;
		if ( this instanceof AgentRole)
			key = "edit.agent.role.dialog.title";
		else if ( this instanceof SpotRole)
			key = "edit.spot.role.dialog.title";
		else
			return;

		_editRoleFrame = new EditRoleFrame( ResourceManager.get_instance().get( key) + " - " + _name, this);
		if ( !_editRoleFrame.create()) {
			_editRoleFrame = null;
			return;
		}
	}

	/**
	 * @param ruleCount
	 */
	public void how_many_rules(IntBuffer ruleCount) {
		_ruleManager.how_many_rules( ruleCount);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_initial_data()
	 */
	public String get_initial_data() {
		String script = _ruleManager.get_initial_data( get_name(), this);

		if ( !script.equals( ""))
			script += Constant._lineSeparator;

		return script;
	}

	/**
	 * @param ruleCount
	 * @param initialValueMap
	 * @param demo
	 * @return
	 */
	public String get_script(int ruleCount, InitialValueMap initialValueMap, boolean demo) {
		return _ruleManager.get_script( get_name(), ruleCount, initialValueMap, demo, this);
	}

	/**
	 * @param role 
	 * @param stageType 
	 * @return
	 */
	public boolean has_same_this_stage_rules(Role role, String stageType) {
		// TODO Auto-generated method stub
		return _ruleManager.has_this_stage_rules( role, stageType);
	}

	/**
	 * @param rulesArray
	 * @param stageType 
	 * @return
	 */
	public boolean has_same_this_stage_rules(List<Rules> rulesArray, String stageType) {
		// TODO Auto-generated method stub
		return _ruleManager.has_same_this_stage_rules( rulesArray, stageType);
	}

	/**
	 * @param roleDataSet 
	 * @param programRootFolder
	 * @param packagePrefix
	 * @param owners 
	 * @return
	 */
	public boolean get_role_class_java_program(RoleDataSet roleDataSet, File programRootFolder, String packagePrefix, List<PlayerBase> owners) {
		// TODO Auto-generated method stub
		String type = AgentRole.class.isInstance( this) ? "agent" : "spot";

		RoleClassCode roleClassCode = new RoleClassCode();

		roleClassCode._className = JavaProgramExporter.get_role_class_name( _name, roleDataSet._roleClassNameMap);

		roleClassCode._package = "package " + packagePrefix + ( packagePrefix.equals( "") ? "" : ".") +  type + ";\n";

		roleClassCode._imports.add( "soars2.core.TRole");
		roleClassCode._imports.add( type.equals( "agent") ? "soars2.core.TAgent" : "soars2.core.TSpot");
		roleClassCode._imports.add( "soars2.utils.random2013.ICRandom");
//		if ( roleClassCode._className.equals( "TAgentRole"))
//			roleClassCode._imports.add( "soars2.core.TTime");

		roleClassCode._imports.addAll( ( type.equals( "agent") && _name.equals( "")) ? PlayerBase.get_imports( roleDataSet._agentCommonVariableMap, null) : owners.get( 0).get_imports( roleDataSet._agentCommonVariableMap));

		// ここにimport文が入る

		roleClassCode._body1 = "\n/**\n";
		roleClassCode._body1 += " * " + roleClassCode._className + "\n"; 
		roleClassCode._body1 += " */\n";
		roleClassCode._body1 += "public class " + roleClassCode._className + " extends TRole {\n\n";

		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + "/** 役割名 */\n";
		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + "public static final String ROLE_NAME = \"" + roleClassCode._className.substring( 1) + "\";\n";

		roleClassCode._body1 += ( type.equals( "agent") && _name.equals( ""))
			? PlayerBase.get_variable_definitions( roleClassCode._className, roleDataSet._agentCommonVariableMap, null, roleDataSet._variableMap, roleClassCode._variables, type)
				: owners.get( 0).get_variable_definitions( roleClassCode._className, roleDataSet._agentCommonVariableMap, roleDataSet._variableMap, roleClassCode._variables, type);

//		if ( roleClassCode._className.equals( "TAgentRole")) {
//			roleClassCode._body1 += JavaProgramExporter._indents[ 1] + "/**  */\n";
//			roleClassCode._body1 += JavaProgramExporter._indents[ 1] + "private TTime fTimer;\n";
//		}

		for ( Variable variable:roleClassCode._variables) {
			String imprt = PlayerBase.get_import2( variable._kind);
			if ( imprt.equals( ""))
				continue;
			if ( roleClassCode._imports.contains( imprt))
				continue;
			roleClassCode._imports.add( imprt);
		}

		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + "\n\t/**\n";
		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + " * コンストラクタ\n";
		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + " * \n";
		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + " * @param " + ( type.equals( "agent") ? "ownerAgent この役割を持つエージェント" : "ownerSpot この役割を持つスポット") + "\n";
		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + " * @param rand 乱数発生器\n";
		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + " */\n";
		roleClassCode._body1 += JavaProgramExporter._indents[ 1] + "public " + roleClassCode._className + "(" + ( type.equals( "agent") ? "TAgent ownerAgent" : "TSpot ownerSpot") + ", ICRandom rand";
		String parameters = "";
		String text = "";
		for ( int i = 0; i < roleClassCode._variables.size(); ++i) {
			parameters += ( 0 == i) ? "," : "";
			text += ( text.equals( "") ? "\n" + JavaProgramExporter._indents[ 2] : ", ") + roleClassCode._variables.get( i)._kind + " " + roleClassCode._variables.get( i)._name.substring( 1);
			if ( 100 < text.length()) {
				parameters += text + ( ( roleClassCode._variables.size() - 1) == i ? "" : ",");
				text = "";
			}
		}
		parameters += text + ") {\n";
		roleClassCode._body1 += ( parameters.startsWith( ",\n") && 100 > parameters.length()) ? ( ", " + parameters.substring( ( ",\n" + JavaProgramExporter._indents[ 2]).length())) : parameters;
	//	System.out.println( parameters.length());
//		String text = "";
//		for ( int i = 0; i < roleClassCode._variables.size(); ++i) {
//			roleClassCode._body1 += ( 0 == i) ? "," : "";
//			text += ( text.equals( "") ? "\n" + JavaProgramExporter._indents[ 2] : ", ") + roleClassCode._variables.get( i)._type + " " + roleClassCode._variables.get( i)._name.substring( 1);
//			if ( 100 < text.length()) {
//				roleClassCode._body1 += text + ( ( roleClassCode._variables.size() - 1) == i ? "" : ",");
//				text = "";
//			}
//		}
//		roleClassCode._body1 += text + ") {\n";
		roleClassCode._body1 += JavaProgramExporter._indents[ 2] + "super(ROLE_NAME, " + ( type.equals( "agent") ? "ownerAgent" : "ownerSpot") + ", rand); // 親クラスのコンストラクタを呼び出す\n";
		for ( Variable variable:roleClassCode._variables)
			roleClassCode._body1 += JavaProgramExporter._indents[ 2] + variable._name + "=" + variable._name.substring( 1) + ";\n";
//		roleClassCode._body1 += ( roleClassCode._className.equals( "TAgentRole") ? ( JavaProgramExporter._indents[ 2] + "fTimer = new TTime(\"0/0:00\");\n") : "");

		// ここでルールクラス生成

		roleClassCode._body2 = JavaProgramExporter._indents[ 1] + "}\n\n";

		if ( !roleClassCode._variables.isEmpty() || roleClassCode._className.equals( "TAgentRole")) {
			roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "//\n";
			roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "// Getter and Setter\n";
			roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "//\n\n";
			for ( Variable variable:roleClassCode._variables) {
				roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "public " + variable._kind + " get" + variable._name.substring( 1) + "() {\n";
				roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "return this." + variable._name + ";\n";
				roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "}\n\n";
				roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "public void set" + variable._name.substring( 1) + "(" + variable._kind + " " + variable._name + ") {\n";
				roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "this." + variable._name + "=" + variable._name + ";\n";
				roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "}\n\n";
			}
//			if ( roleClassCode._className.equals( "TAgentRole")) {
//				roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "public TTime getTimer() {\n";
//				roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "return this.fTimer;\n";
//				roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "}\n\n";
//				roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "public void setTimer(TTime fTimer) {\n";
//				roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "this.fTimer = fTimer;\n";
//				roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "}\n\n";
//			}
		}

		roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "@Override\n";
		roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "public boolean equals(Object o) {\n";
		roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "if (o == this)\n";
		roleClassCode._body2 += JavaProgramExporter._indents[ 3] + "return true;\n";
		if ( roleClassCode._variables.isEmpty()) {
			roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "return (o instanceof " + roleClassCode._className + ");\n";
			roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "}\n";
		} else {
			roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "if (!(o instanceof " + roleClassCode._className + "))\n";
			roleClassCode._body2 += JavaProgramExporter._indents[ 3] + "return false;\n";
			String localVariableName = "t" + roleClassCode._className.substring( 1);
			roleClassCode._body2 += JavaProgramExporter._indents[ 2] + roleClassCode._className + " " + localVariableName + " = (" + roleClassCode._className + ") o;\n";
			roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "return ";
			for ( int i = 0; i < roleClassCode._variables.size(); ++i) {
				roleClassCode._body2 += ( 0 == i) ? "" : JavaProgramExporter._indents[ 3] + "&& ";
				if ( roleClassCode._variables.get( i)._kind.equals( "String")
					|| roleClassCode._variables.get( i)._kind.equals( "double")
					|| roleClassCode._variables.get( i)._kind.equals( "int"))
					roleClassCode._body2 += roleClassCode._variables.get( i)._name + "==" + localVariableName + "." + roleClassCode._variables.get( i)._name;
				else {
					roleClassCode._body2 += "Objects.equals(" + roleClassCode._variables.get( i)._name + ", " + localVariableName + "." + roleClassCode._variables.get( i)._name + ")";
					JavaProgramExporter.append_import( "java.util.Objects", roleClassCode._imports);
				}
				roleClassCode._body2 += ( ( i == roleClassCode._variables.size() - 1) ? ";\n" : "\n");
			}
			roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "}\n\n";
			roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "@Override\n";
			roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "public String toString() {\n";
			roleClassCode._body2 += JavaProgramExporter._indents[ 2] + "return \"{\" ";
			if ( 1 == roleClassCode._variables.size())
				roleClassCode._body2 += "+ \" " + roleClassCode._variables.get( 0)._name + "='\" + " + "get" +  roleClassCode._variables.get( 0)._name.substring( 1) + "() + \"'\" + \"}\";\n";
			else {
				for ( int i = 0; i < roleClassCode._variables.size(); ++i) {
					roleClassCode._body2 += ( 0 == i) ? "+ \" " : JavaProgramExporter._indents[ 3] + "+ \", ";
					roleClassCode._body2 += roleClassCode._variables.get( i)._name + "='\" + " + "get" +  roleClassCode._variables.get( i)._name.substring( 1) + "() + \"'\"\n";
				}
				roleClassCode._body2 += JavaProgramExporter._indents[ 3] + "+ \"}\";\n";
			}
			roleClassCode._body2 += JavaProgramExporter._indents[ 1] + "}\n";
		}
		roleClassCode._body2 += "}\n";

		roleDataSet._roleClassNameMap.put( _name, roleClassCode._className);

		// 無名ロールが存在する場合は予め各エージェントに共通の変数を取得しておく必要がある！
		roleDataSet._roleOwnersMap.put( roleClassCode._className, !owners.isEmpty() ? owners : null);

		roleDataSet._roleClassCodeMap.put( roleClassCode._className, roleClassCode);

		return true;
	}

	/**
	 * @param roleDataSet 
	 * @param programRootFolder
	 * @param packagePrefix
	 * @param stageNameMap
	 * @return
	 */
	public boolean export_RoleClass(RoleDataSet roleDataSet, File programRootFolder, String packagePrefix, Map<String, String> stageNameMap) {
		// TODO Auto-generated method stub
		//System.out.println( _name + " -> " + ( !owners.isEmpty() ? owners.get( 0)._name : "No owner!"));
		String roleClassName = roleDataSet._roleClassNameMap.get( _name);
		if ( null == roleClassName)
			return false;

		String type = AgentRole.class.isInstance( this) ? "agent" : "spot";
		File folder = new File( programRootFolder, type);
		if ( !folder.exists() && !folder.mkdirs())
			return false;

		File file = new File( folder, roleClassName + ".java");
		if ( file.exists())
			return true;

		RoleClassCode roleClassCode = roleDataSet._roleClassCodeMap.get( roleClassName);
		if ( null == roleClassCode)
			return false;

		// package
		String text = roleClassCode._package;

		// importの追加及びルールクラス生成コード作成
		List<String> codes = new ArrayList<>();
		for ( int i = 0; i < StageManager.get_instance()._main_stages.size(); ++i) {
			Stage stage = ( Stage)StageManager.get_instance()._main_stages.get( i);
			List<RuleData> roleDataList = roleDataSet._ruleDataMap.get( stage._name);
			if ( null == roleDataList)
				continue;

			for ( RuleData ruleData:roleDataList) {
				if ( !ruleData._roleName.equals( _name))
					continue;

				JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "TStages", roleClassCode._imports);
				JavaProgramExporter.append_import( "soars2.core.TTime", roleClassCode._imports);

				roleClassCode._imports.add( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + ( AgentRole.class.isInstance( this) ? "agent" : "spot") + ".rules." + ruleData._ruleClassName);

				// ルールクラス生成コード作成
				codes.add( JavaProgramExporter._indents[ 2] + "// " + stageNameMap.get( stage._name) + "クラス");
				codes.add( ruleData._time.equals( "")
					? JavaProgramExporter._indents[ 2] + "new " + ruleData._ruleClassName + "(this).setTimeAndStage(new TTime(\"0:00\"), new TTime(\"23:30\"), new TTime(\"0:30\"), " + stageNameMap.get( stage._name) + ");"
					: JavaProgramExporter._indents[ 2] + "new " + ruleData._ruleClassName + "(this).setTimeAndStage(true, new TTime(\"" + ruleData._time + "\"), " + stageNameMap.get( stage._name) + ");");
			}
		}

		Collections.sort( roleClassCode._imports);

		for ( String imprt:roleClassCode._imports)
			text += "\nimport " + imprt + ";";

		text += roleClassCode._imports.isEmpty() ? "" : "\n";

		text += roleClassCode._body1;

		// ルールクラス生成コードを挿入
		for ( String code:codes)
			text += code + "\n";

		text += roleClassCode._body2;

		return FileUtility.write_text_to_file( file, text, "UTF8");
	}

	/**
	 * @param roleDataSet
	 * @param roleClassNameMap 
	 * @param stageType
	 * @return
	 */
	public boolean get_ruleDataMap(RoleDataSet roleDataSet, Map<String, String> roleClassNameMap, String stageType) {
		// TODO Auto-generated method stub
		return _ruleManager.get_ruleDataMap( roleDataSet, roleClassNameMap, stageType, this);
	}

	/**
	 * @param roleDataSet
	 * @param agentNameMap 
	 * @param spotNameMap 
	 * @param initialValueMap 
	 * @param programRootFolder
	 * @return
	 */
	public boolean export_RuleClasses(RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, InitialValueMap initialValueMap, File programRootFolder, String packagePrefix) {
		// TODO Auto-generated method stub
		//System.out.println( "Role = " + _name);
		for ( int i = 0; i < StageManager.get_instance()._main_stages.size(); ++i) {
			Stage stage = ( Stage)StageManager.get_instance()._main_stages.get( i);
			List<RuleData> roleDataList = roleDataSet._ruleDataMap.get( stage._name);
			if ( null == roleDataList)
				continue;

			for ( RuleData ruleData:roleDataList) {
				if ( !ruleData._roleName.equals( _name))
					continue;

				//System.out.println( JavaProgramExporter._indents[ 1] + ruleData._roleClassName);

				File parentFolder = new File( programRootFolder, ruleData._type);
				if ( !parentFolder.exists() && !parentFolder.mkdirs())
					return false;

				File folder = new File( parentFolder, "rules");
				if ( !folder.exists() && !folder.mkdirs())
					return false;

				if ( !export_RuleClass( ruleData, roleDataSet, agentNameMap, spotNameMap, initialValueMap, folder, packagePrefix))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param ruleData
	 * @param roleDataSet
	 * @param agentNameMap
	 * @param spotNameMap
	 * @param initialValueMap
	 * @param folder
	 * @param packagePrefix
	 * @return
	 */
	private boolean export_RuleClass(RuleData ruleData, RoleDataSet roleDataSet, Map<String, String> agentNameMap, Map<String, String> spotNameMap, InitialValueMap initialValueMap, File folder, String packagePrefix) {
		// TODO Auto-generated method stub
		//System.out.println( ruleData._ruleClassName + " -> " + roleDataSet._roleClassNameMap.get( ruleData._roleName));
		String type = AgentRole.class.isInstance( this) ? "agent" : "spot";

		if ( type.equals( "spot") && !export_TSpotRuleClass( folder, packagePrefix))
			return false;

		List<String> imports = new ArrayList<>();
		if ( type.equals( "agent")) 
			imports.add( "soars2.core.TAgentRule");
		imports.add( "soars2.core.TAgent");
		imports.add( "soars2.core.TRole");
		imports.add( "soars2.core.TSpot");
		imports.add( "soars2.core.TTime");
		imports.add( "java.util.HashMap");

		List<List<String>> methods = new ArrayList<>();
		List<List<String>> commandslist = new ArrayList<>();
//		List<String> commentedRoleClassNames = new ArrayList<>();
		List<String> comments = new ArrayList<>();
		for ( int i = 0; i < ruleData._rulesList.size(); ++i) {
			List<String> commands = new ArrayList<>();
			List<String> lines = ruleData._rulesList.get( i).get_rule_class_java_program_body( ruleData, roleDataSet, agentNameMap, spotNameMap, imports, commands, initialValueMap, folder, packagePrefix, this, false);
			if ( lines.isEmpty())
				continue;

			methods.add( lines);
//			if ( duplicate( commands, "ownerRole"))
//				update( commands, "ownerRole", ruleData, roleDataSet._roleClassNameMap, commentedRoleClassNames);
//
//			if ( duplicate( commands, "currentSpotRole"))
//				update( commands, "currentSpotRole", ruleData, roleDataSet._roleClassNameMap, commentedRoleClassNames);

			commandslist.add( commands);
			comments.add( ruleData._rulesList.get( i)._comment);
		}

//		List<String> commentedImports = get_commented_imports( imports, commentedRoleClassNames, methods);

		File file = new File( folder, ruleData._ruleClassName + ".java");
		if ( file.exists())
			return true;

		String text1 = "package " + packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + type + ".rules;\n";

		String text2 = "\n/**\n";
		text2 += " * \n";
		text2 += " */\n";
		text2 += "public class " + ruleData._ruleClassName + " extends " + ( type.equals( "agent") ? "TAgentRule" : "TSpotRule") + " {\n";

		text2 += "\n" + JavaProgramExporter._indents[ 1] + "/** ルール名 */\n";
		text2 += JavaProgramExporter._indents[ 1] + "public static String RULE_NAME = \"" + ruleData._ruleClassName.substring( 1, ruleData._ruleClassName.length() - 4) + "\";\n";

		text2 += "\n" + JavaProgramExporter._indents[ 1] + "/**\n";
		text2 += JavaProgramExporter._indents[ 1] + " * コンストラクタ\n";
		text2 += JavaProgramExporter._indents[ 1] + " * \n";
		text2 += JavaProgramExporter._indents[ 1] + " * @param ownerRole\n";
		text2 += JavaProgramExporter._indents[ 1] + " */\n";
		//text += JavaProgramExporter._indents[ 1] + "public " + ruleData._ruleClassName + "(" + roleDataSet._roleClassNameMap.get( _name) + " ownerRole) {\n";
		text2 += JavaProgramExporter._indents[ 1] + "public " + ruleData._ruleClassName + "(TRole ownerRole) {\n";
		text2 += type.equals( "agent")
			? JavaProgramExporter._indents[ 2] + "super(RULE_NAME, ownerRole, null); // スポット条件なし\n"
			: JavaProgramExporter._indents[ 2] + "super(RULE_NAME, ownerRole);\n";
		text2 += JavaProgramExporter._indents[ 1] + "}\n";

		boolean executed = false;

		// For agent role
		if ( null != roleDataSet._sameVariableRoleClassNamesMap.get( type)) {
			if ( null != roleDataSet._sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName)) {
				if ( !executed) {
					text2 += "\n" + JavaProgramExporter._indents[ 1] + "//\n";
					text2 += JavaProgramExporter._indents[ 1] + "// Getter and Setter\n";
					text2 += JavaProgramExporter._indents[ 1] + "//\n";
					executed = true;
				}
				Set<String> kinds = roleDataSet._sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName).keySet();
				for ( String kind:kinds) {
					PlayerBase.get_import( kind, imports);
					Set<String> names = roleDataSet._sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName).get( kind).keySet();
					for ( String name:names) {
						text2 += "\n" + JavaProgramExporter._indents[ 1] + "/**\n";
						text2 += JavaProgramExporter._indents[ 1] + " * \n";
						text2 += JavaProgramExporter._indents[ 1] + " * return\n";
						text2 += JavaProgramExporter._indents[ 1] + " */\n";
						text2 += JavaProgramExporter._indents[ 1] + "private " + PlayerBase.get_variable_type( kind) + " get" + name.substring( 1) + "() {\n";
						if ( roleDataSet.is_agent_common_variable( kind, name.substring( 1))) {
							for ( String roleClassName:roleDataSet._sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName).get( kind).get( name))
								JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + type + "." + roleClassName, imports);
							text2 += JavaProgramExporter._indents[ 2] + "if (TAgentRole.class.isInstance(getOwnerRole()))\n";
							text2 += JavaProgramExporter._indents[ 3] + "return ((TAgentRole) getOwnerRole()).get" + name.substring( 1) + "();\n";
							text2 += JavaProgramExporter._indents[ 2] + "else\n";
							text2 += JavaProgramExporter._indents[ 3] + "return ((TAgentRole) getOwnerRole().getMergedRole(\"AgentRole\")).get" + name.substring( 1) + "();\n";
						} else {
							int index = 0;
							for ( String roleClassName:roleDataSet._sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName).get( kind).get( name)) {
								JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + type + "." + roleClassName, imports);
								text2 += JavaProgramExporter._indents[ 2] + ( ( 0 == index++) ? "" : "else ") + "if (" + roleClassName + ".class.isInstance( getOwnerRole()))\n";
								text2 += JavaProgramExporter._indents[ 3] + "return ((" + roleClassName + ")getOwnerRole()).get" + name.substring( 1) + "();\n";
							}
							text2 += JavaProgramExporter._indents[ 2] + "else\n";
							text2 += JavaProgramExporter._indents[ 3] + "return " + PlayerBase.get_default_return_value( kind) + ";\n";
						}
						text2 += JavaProgramExporter._indents[ 1] + "}\n";
						text2 += "\n" + JavaProgramExporter._indents[ 1] + "/**\n";
						text2 += JavaProgramExporter._indents[ 1] + " * @ param " + name + "\n";
						text2 += JavaProgramExporter._indents[ 1] + " */\n";
						text2 += JavaProgramExporter._indents[ 1] + "private void set" + name.substring( 1) + "(" +  PlayerBase.get_variable_type( kind) + " " + name + ") {\n";
						if ( roleDataSet.is_agent_common_variable( kind, name.substring( 1))) {
							text2 += JavaProgramExporter._indents[ 2] + "if (TAgentRole.class.isInstance(getOwnerRole()))\n";
							text2 += JavaProgramExporter._indents[ 3] + "((TAgentRole) getOwnerRole()).set" + name.substring( 1) + "(" + name + ");\n";
							text2 += JavaProgramExporter._indents[ 2] + "else\n";
							text2 += JavaProgramExporter._indents[ 3] + "((TAgentRole) getOwnerRole().getMergedRole(\"AgentRole\")).set" + name.substring( 1) + "(" + name + ");\n";
						} else {
							int index = 0;
							for ( String roleClassName:roleDataSet._sameVariableRoleClassNamesMap.get( type).get( ruleData._ruleClassName).get( kind).get( name)) {
								text2 += JavaProgramExporter._indents[ 2] + ( ( 0 == index++) ? "" : "else ") + "if (" + roleClassName + ".class.isInstance( getOwnerRole()))\n";
								text2 += JavaProgramExporter._indents[ 3] + "((" + roleClassName + ")getOwnerRole()).set" + name.substring( 1) + "(" + name + ");\n";
							}
						}
						text2 += JavaProgramExporter._indents[ 1] + "}\n";
					}
				}
			}
		}

		// For current spot role
		Map<String, Map<String, List<String>>> map1 = roleDataSet._currentSporRoleClassNamesMap.get( ruleData._ruleClassName);
		if ( null != map1) {
			Set<String> kinds = map1.keySet();
			if ( !executed) {
				text2 += "\n" + JavaProgramExporter._indents[ 1] + "//\n";
				text2 += JavaProgramExporter._indents[ 1] + "// Getter and Setter\n";
				text2 += JavaProgramExporter._indents[ 1] + "//\n";
				executed = true;
			}
			for ( String kind:kinds) {
				PlayerBase.get_import( kind, imports);
				Map<String, List<String>> map2 = map1.get( kind);
				Set<String> names = map2.keySet();
				for ( String name:names) {
					List<String> roleClassNames = map2.get( name);
					text2 += "\n" + JavaProgramExporter._indents[ 1] + "/**\n";
					text2 += JavaProgramExporter._indents[ 1] + " * \n";
					text2 += JavaProgramExporter._indents[ 1] + " * @ param spotSet\n";
					text2 += JavaProgramExporter._indents[ 1] + " * return\n";
					text2 += JavaProgramExporter._indents[ 1] + " */\n";
					text2 += JavaProgramExporter._indents[ 1] + "private " + PlayerBase.get_variable_type( kind) + " get" + name + "(HashMap<String, TSpot> spotSet) {\n";
					text2 += JavaProgramExporter._indents[ 2] + "TSpot currentSpot = spotSet.get(getAgent().getCurrentSpotName()); // 現在いるスポット\n";
					int index = 0;
					for ( String roleClassName:roleClassNames) {
						JavaProgramExporter.append_import( packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot." + roleClassName, imports);
						text2 += JavaProgramExporter._indents[ 2] + ( ( 0 == index++) ? "" : "else ") + "if (" + roleClassName + ".class.isInstance( getOwnerRole()))\n";
						text2 += JavaProgramExporter._indents[ 3] + "return ((" + roleClassName + ") currentSpot.getRole()).get" + name + "();\n";
					}
					text2 += JavaProgramExporter._indents[ 2] + "else\n";
					text2 += JavaProgramExporter._indents[ 3] + "return " + PlayerBase.get_default_return_value( kind) + ";\n";
					text2 += JavaProgramExporter._indents[ 1] + "}\n";
					text2 += "\n" + JavaProgramExporter._indents[ 1] + "/**\n";
					text2 += JavaProgramExporter._indents[ 1] + " * @ param spotSet\n";
					text2 += JavaProgramExporter._indents[ 1] + " * @ param " + name + "\n";
					text2 += JavaProgramExporter._indents[ 1] + " */\n";
					text2 += JavaProgramExporter._indents[ 1] + "private void set" + name + "(HashMap<String, TSpot> spotSet, " +  PlayerBase.get_variable_type( kind) + " " + name + ") {\n";
					text2 += JavaProgramExporter._indents[ 2] + "TSpot currentSpot = spotSet.get(getAgent().getCurrentSpotName()); // 現在いるスポット\n";
					index = 0;
					for ( String roleClassName:roleClassNames) {
						text2 += JavaProgramExporter._indents[ 2] + ( ( 0 == index++) ? "" : "else ") + "if (" + roleClassName + ".class.isInstance( getOwnerRole()))\n";
						text2 += JavaProgramExporter._indents[ 3] + "((" + roleClassName + ") currentSpot.getRole()).set" + name + "(" + name + ");\n";
					}
					text2 += JavaProgramExporter._indents[ 1] + "}\n";
				}
			}
		}

		text2 += "\n" + JavaProgramExporter._indents[ 1] + "@Override\n";
		text2 += JavaProgramExporter._indents[ 1] + "public boolean doIt(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,\n";
		text2 += JavaProgramExporter._indents[ 2] + "HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {\n";

		if ( type.equals( "agent")) {
			text2 += JavaProgramExporter._indents[ 2] + "if (!meetSpotCondition())\n";
			//text2 += JavaProgramExporter._indents[ 3] + "return true;\n";
			text2 += JavaProgramExporter._indents[ 3] + "return false;\n";
		}

		for ( int i = 0; i < methods.size(); ++i) {
			text2 += JavaProgramExporter._indents[ 2] + "// " + comments.get( i) + "\n";
			text2 += JavaProgramExporter._indents[ 2] + ( ( 0 == i) ? "" : "else ") + "if (method" + String.valueOf( i + 1) + "(currentTime, currentStage, spotSet, agentSet, globalSharedVariables))\n";
			text2 += JavaProgramExporter._indents[ 3] + "return true;\n";
		}

		text2 += JavaProgramExporter._indents[ 2] + "return false;\n";
		//text2 += JavaProgramExporter._indents[ 2] + "return true;\n";
		text2 += JavaProgramExporter._indents[ 1] + "}\n";

		for ( int i = 0; i < methods.size(); ++i) {
			text2 += "\n" + JavaProgramExporter._indents[ 1] + "/**\n";
			text2 += JavaProgramExporter._indents[ 1] + " * " +  comments.get( i) + "\n";
			text2 += JavaProgramExporter._indents[ 1] + " * \n";
			text2 += JavaProgramExporter._indents[ 1] + " * @param currentTime\n";
			text2 += JavaProgramExporter._indents[ 1] + " * @param currentStage\n";
			text2 += JavaProgramExporter._indents[ 1] + " * @param spotSet\n";
			text2 += JavaProgramExporter._indents[ 1] + " * @param agentSet\n";
			text2 += JavaProgramExporter._indents[ 1] + " * @param globalSharedVariables\n";
			text2 += JavaProgramExporter._indents[ 1] + " * return\n";
			text2 += JavaProgramExporter._indents[ 1] + " */\n";
			text2 += JavaProgramExporter._indents[ 1] + "private boolean method" + String.valueOf( i + 1) + "(TTime currentTime, String currentStage, HashMap<String, TSpot> spotSet,\n";
			text2 += JavaProgramExporter._indents[ 2] + "HashMap<String, TAgent> agentSet, HashMap<String, Object> globalSharedVariables) {\n";
			if ( duplicate( commandslist.get( i), "ownerRole") || duplicate( commandslist.get( i), "currentSpotRole"))
				text2 += JavaProgramExporter._indents[ 2] + "// 適宜キャスト願います" + ";\n";	// TODO リソース
			for ( String command:commandslist.get( i))
				text2 += JavaProgramExporter._indents[ 2] + command + ";\n";
			text2 += get_code( methods.get( i));
			text2 += JavaProgramExporter._indents[ 1] + "}\n";
		}

		text2 += "}\n";

		// import文を挿入
		Collections.sort( imports);
		for ( String imprt:imports)
			text1 += "\nimport " + imprt + ";";
//			text1 += "\n" + ( commentedImports.contains( imprt) ? "//" : "") + "import " + imprt + ";";
		
		text1 += imports.isEmpty() ? "" : "\n";

		return FileUtility.write_text_to_file( file, text1 + text2, "UTF8");
	}

	/**
	 * @param commands
	 * @param localVariableName
	 * @return
	 */
	private boolean duplicate(List<String> commands, String localVariableName) {
		// TODO Auto-generated method stub
		int counter = 0;
		for ( String command:commands) {
			if ( 0 <= command.indexOf( localVariableName))
				++counter;

			if ( 1 < counter)
				return true;
		}
		return false;
	}

//	/**
//	 * @param commands
//	 * @param localVariableName
//	 * @param ruleData
//	 * @param roleClassNameMap 
//	 * @param commentedRoleClassNames 
//	 */
//	private void update(List<String> commands, String localVariableName, RuleData ruleData, Map<String, String> roleClassNameMap, List<String> commentedRoleClassNames) {
//		// TODO Auto-generated method stub
//		boolean found = false;
//		for ( String command:commands) {
//			if ( 0 > command.indexOf( localVariableName))
//				continue;
//
//			if ( command.startsWith( roleClassNameMap.get( ruleData._roleName))) {
//				found = true;
//				break;
//			}
//		}
//
//		if ( found) {
//			for ( int i = 0; i < commands.size(); ++i) {
//				if ( 0 > commands.get( i).indexOf( localVariableName))
//					continue;
//
//				if ( !commands.get( i).startsWith( roleClassNameMap.get( ruleData._roleName))) {
//					String roleClassName = commands.get( i).split( " ")[ 0];
//					if ( !commentedRoleClassNames.contains( roleClassName))
//						commentedRoleClassNames.add( roleClassName);
//
//					commands.set( i, "//" + commands.get( i));
//				}
//			}
//		} else {
//			boolean already = false;
//			for ( int i = 0; i < commands.size(); ++i) {
//				if ( 0 > commands.get( i).indexOf( localVariableName))
//					continue;
//
//				if ( !already) {
//					already = true;
//					continue;
//				}
//
//				String roleClassName = commands.get( i).split( " ")[ 0];
//				if ( !commentedRoleClassNames.contains( roleClassName))
//					commentedRoleClassNames.add( roleClassName);
//
//				commands.set( i, "//" + commands.get( i));
//			}
//		}
//	}
//
//	/**
//	 * @param imports
//	 * @param commentedRoleClassNames
//	 * @param methods
//	 * @return
//	 */
//	private List<String> get_commented_imports(List<String> imports, List<String> commentedRoleClassNames, List<List<String>> methods) {
//		// TODO Auto-generated method stub
//		List<String> commentedImports = new ArrayList<>();
//		for ( String commentedRoleClassName:commentedRoleClassNames) {
//			boolean use = false;
//			for ( List<String> method:methods) {
//				for ( String code:method) {
//					if ( 0 <= code.indexOf( commentedRoleClassName)) {
//						use = true;
//						break;
//					}
//				}
//				if ( use)
//					break;
//			}
//			if ( !use) {
//				for ( int i = 0; i < imports.size(); ++i) {
//					if ( 0 <= imports.get( i).indexOf( commentedRoleClassName))
//						commentedImports.add( imports.get( i));
//						//imports.set( i, "//" + imports.get( i));
//				}
//			}
//		}
//		return commentedImports;
//	}

	/**
	 * @param lines
	 * @return
	 */
	private String get_code(List<String> lines) {
		// TODO Auto-generated method stub
		String code = "";
		for ( String line:lines)
			code += line + "\n";
		return code;
	}

	/**
	 * @param folder
	 * @param packagePrefix
	 * @return
	 */
	private boolean export_TSpotRuleClass(File folder, String packagePrefix) {
		// TODO Auto-generated method stub
		File file = new File( folder, "TSpotRule.java");
		if ( file.exists())
			return true;

		String text = "package " + packagePrefix + ( packagePrefix.equals( "") ? "" : ".") + "spot.rules;\n\n";
		text += "import soars2.core.TRole;\n";
		text += "import soars2.core.TRule;\n";
		text += "import soars2.core.TSpot;\n";
		text += "import soars2.core.TTime;\n\n";

		text += "abstract public class TSpotRule extends TRule {\n\n";

		text += JavaProgramExporter._indents[ 1] + "/**\n";
		text += JavaProgramExporter._indents[ 1] + " * コンストラクタ\n";
		text += JavaProgramExporter._indents[ 1] + " * @param ruleName ルール名\n";
		text += JavaProgramExporter._indents[ 1] + " * @param ownerRole このルールを持つロール\n";
		text += JavaProgramExporter._indents[ 1] + " */\n";
		text += JavaProgramExporter._indents[ 1] + "public TSpotRule(String ruleName, TRole ownerRole) {\n";
		text += JavaProgramExporter._indents[ 2] + "super(ruleName, ownerRole);\n";
		text += JavaProgramExporter._indents[ 2] + "// TODO TAgentRuleの発火するスポットのようなメンバ変数が必要か検討\n";
		text += JavaProgramExporter._indents[ 1] + "}\n";

		text += JavaProgramExporter._indents[ 1] + "@Override\n";
		text += JavaProgramExporter._indents[ 1] + "public TSpotRule setTimeAndStage(boolean isRegular, TTime time, String stage) {\n";
		text += JavaProgramExporter._indents[ 2] + "return (TSpotRule)super.setTimeAndStage(isRegular, time, stage);\n";
		text += JavaProgramExporter._indents[ 1] + "}\n\n";

		text += JavaProgramExporter._indents[ 1] + "@Override\n";
		text += JavaProgramExporter._indents[ 1] + "public TSpotRule setRelativeTimeAndStage(TRule precedentRule, TTime relativeTime, String stage) {\n";
		text += JavaProgramExporter._indents[ 2] + "return (TSpotRule)super.setRelativeTimeAndStage(precedentRule, relativeTime, stage);\n";
		text += JavaProgramExporter._indents[ 1] + "}\n\n";

		text += JavaProgramExporter._indents[ 1] + "/**\n";
		text += JavaProgramExporter._indents[ 1] + " * このルールを持つスポットを返す\n";
		text += JavaProgramExporter._indents[ 1] + " * @return スポット\n";
		text += JavaProgramExporter._indents[ 1] + " */\n";
		text += JavaProgramExporter._indents[ 1] + "public TSpot getSpot() {\n";
		text += JavaProgramExporter._indents[ 2] + "return (TSpot)getOwnerRole().getOwner();\n";
		text += JavaProgramExporter._indents[ 1] + "}\n";
		    
		text += "}\n";

		return FileUtility.write_text_to_file( file, text, "UTF8");
	}

	/**
	 * @param stageName
	 * @return
	 */
	public boolean has_this_stage(String stageName) {
		// TODO Auto-generated method stub
		return _ruleManager.has_this_stage( stageName);
	}

	/**
	 * @param stageName
	 * @return
	 */
	public RuleData get_ruleData(String stageName) {
		// TODO Auto-generated method stub
		RuleData ruleData = _ruleManager.get_ruleData( stageName);
		if ( null == ruleData)
			return null;

		ruleData._type = AgentRole.class.isInstance( this) ? "agent" : "spot";
		ruleData._roleName = _name;
		return ruleData;
	}

	/**
	 * @param name
	 * @param writer
	 * @return
	 */
	public boolean write(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		write( get_name(), attributesImpl);

		writer.startElement( null, null, name, attributesImpl);

		write_comment( writer);

		if ( !_ruleManager.write( writer))
			return false;

		writer.endElement( null, null, name);
		return true;
	}
}
