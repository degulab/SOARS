/**
 * 
 */
package soars.application.visualshell.object.role.base.object.generic.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.base.Role;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class JavaMethodCallRule implements IObject {

	/**
	 * 
	 */
	public String _kind = "";

	/**
	 * 
	 */
	public EntityVariableRule _classVariableRule = new EntityVariableRule();

	/**
	 * 
	 */
	public String _method = "";

	/**
	 * 
	 */
	public EntityVariableRule _returnVariableRule = new EntityVariableRule();

	/**
	 * 
	 */
	public List<EntityVariableRule> _argumentVariableRules = new ArrayList<EntityVariableRule>();

	/**
	 * @param kind 
	 * 
	 */
	public JavaMethodCallRule(String kind) {
		_kind = kind;
	}

	/**
	 * @param kind
	 * @param method
	 */
	public JavaMethodCallRule(String kind, String method) {
		_kind = kind;
		_method = method;
	}

	/**
	 * @param javaMethodCallRule
	 */
	public JavaMethodCallRule(JavaMethodCallRule javaMethodCallRule) {
		_kind = javaMethodCallRule._kind;
		_classVariableRule = new EntityVariableRule( javaMethodCallRule._classVariableRule);
		_method = javaMethodCallRule._method;
		_returnVariableRule = new EntityVariableRule( javaMethodCallRule._returnVariableRule);
		for ( EntityVariableRule argumentVariableRule:javaMethodCallRule._argumentVariableRules)
			_argumentVariableRules.add( new EntityVariableRule( argumentVariableRule));
	}

	/**
	 * @param kind 
	 * @param entityVariableRule
	 */
	public void set(String kind, EntityVariableRule entityVariableRule) {
		if ( kind.equals( "classVariable"))
			_classVariableRule = entityVariableRule;
		else if ( kind.equals( "returnVariable"))
			_returnVariableRule = entityVariableRule;
		else if ( kind.equals( "argumentVariable"))
			_argumentVariableRules.add( entityVariableRule);
	}

	@Override
	public boolean same_as(IObject object) {
		if ( !( object instanceof JavaMethodCallRule))
			return false;

		JavaMethodCallRule javaMethodCallRule = ( JavaMethodCallRule)object;

		if ( !_kind.equals( javaMethodCallRule._kind))
			return false;

		if ( !_classVariableRule.same_as( javaMethodCallRule._classVariableRule))
			return false;

		if ( !_method.equals( javaMethodCallRule._method))
			return false;

		if ( !_returnVariableRule.same_as( javaMethodCallRule._returnVariableRule))
			return false;

		for ( int i = 0; i < _argumentVariableRules.size(); ++i) {
			if ( !_argumentVariableRules.get( i).same_as( javaMethodCallRule._argumentVariableRules.get( i)))
				return false;
		}

		return true;
	}

	@Override
	public void get_initial_values(Vector<String> initialValues) {
		_classVariableRule.get_initial_values( initialValues);
		_returnVariableRule.get_initial_values( initialValues);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_initial_values( initialValues);
	}

	@Override
	public boolean has_same_agent_name(String name, String number) {
		if ( _classVariableRule.has_same_agent_name( name, number))
			return true;

		if ( _returnVariableRule.has_same_agent_name( name, number))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.has_same_agent_name( name, number))
				return true;
		}

		return false;
	}

	@Override
	public boolean contains_this_alias(String alias) {
		if ( _classVariableRule.contains_this_alias( alias))
			return true;

		if ( _returnVariableRule.contains_this_alias( alias))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.contains_this_alias( alias))
				return true;
		}

		return false;
	}

	@Override
	public void get_used_agent_names(List<String> names) {
		_classVariableRule.get_used_agent_names( names);
		_returnVariableRule.get_used_agent_names( names);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_agent_names( names);
	}

	@Override
	public void get_used_spot_names(List<String> names) {
		_classVariableRule.get_used_spot_names( names);
		_returnVariableRule.get_used_spot_names( names);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_spot_names( names);
	}

	@Override
	public void get_used_independent_variable_names(String type, List<String> names) {
		_classVariableRule.get_used_independent_variable_names( type, names);
		_returnVariableRule.get_used_independent_variable_names( type, names);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_independent_variable_names( type, names);
	}

	@Override
	public void get_used_agent_variable_names(List<String> names) {
		_classVariableRule.get_used_agent_variable_names( names);
		_returnVariableRule.get_used_agent_variable_names( names);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_agent_variable_names( names);
	}

	@Override
	public void get_used_agent_variable_names(List<String> names, EntityVariableRule subject) {
		_classVariableRule.get_used_agent_variable_names( names, subject);
		_returnVariableRule.get_used_agent_variable_names( names, subject);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_agent_variable_names( names, subject);
	}

	@Override
	public void get_used_spot_variable_names(List<String> names) {
		_classVariableRule.get_used_spot_variable_names( names);
		_returnVariableRule.get_used_spot_variable_names( names);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_spot_variable_names( names);
	}

	@Override
	public void get_used_spot_variable_names(List<String> names, EntityVariableRule subject) {
		_classVariableRule.get_used_spot_variable_names( names, subject);
		_returnVariableRule.get_used_spot_variable_names( names, subject);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_spot_variable_names( names, subject);
	}

	@Override
	public void get_used_variable_names(String type, List<String> names) {
		_classVariableRule.get_used_variable_names( type, names);
		_returnVariableRule.get_used_variable_names( type, names);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_variable_names( type, names);
	}

	@Override
	public void get_used_variable_names(String type, List<String> names, EntityVariableRule subject) {
		_classVariableRule.get_used_variable_names( type, names, subject);
		_returnVariableRule.get_used_variable_names( type, names, subject);
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules)
			argumentVariableRule.get_used_variable_names( type, names, subject);
	}

	@Override
	public void get_used_expressions(List<String> names) {
	}

	@Override
	public boolean update_agent_or_spot_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		if ( _classVariableRule.update_agent_or_spot_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
			return true;

		if ( _returnVariableRule.update_agent_or_spot_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_agent_or_spot_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_role_name(String originalName, String newName) {
		if ( _classVariableRule.update_role_name( originalName, newName))
			return true;

		if ( _returnVariableRule.update_role_name( originalName, newName))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_role_name( originalName, newName))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_independent_variable_name(String type, String originalName, String newName) {
		if ( _classVariableRule.update_independent_variable_name( type, originalName, newName))
			return true;

		if ( _returnVariableRule.update_independent_variable_name( type, originalName, newName))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_independent_variable_name( type, originalName, newName))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_agent_variable_name(String originalName, String newName, String entityType) {
		if ( _classVariableRule.update_agent_variable_name( originalName, newName, entityType))
			return true;

		if ( _returnVariableRule.update_agent_variable_name( originalName, newName, entityType))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_agent_variable_name( originalName, newName, entityType))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_agent_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject) {
		if ( _classVariableRule.update_agent_variable_name( originalName, newName, entityType, subject))
			return true;

		if ( _returnVariableRule.update_agent_variable_name( originalName, newName, entityType, subject))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_agent_variable_name( originalName, newName, entityType, subject))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_spot_variable_name(String originalName, String newName, String entityType) {
		if ( _classVariableRule.update_spot_variable_name( originalName, newName, entityType))
			return true;

		if ( _returnVariableRule.update_spot_variable_name( originalName, newName, entityType))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_spot_variable_name( originalName, newName, entityType))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_spot_variable_name(String originalName, String newName, String entityType, EntityVariableRule subject) {
		if ( _classVariableRule.update_spot_variable_name( originalName, newName, entityType, subject))
			return true;

		if ( _returnVariableRule.update_spot_variable_name( originalName, newName, entityType, subject))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_spot_variable_name( originalName, newName, entityType, subject))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_variable_name(String type, String originalName, String newName, String entityType) {
		if ( _classVariableRule.update_variable_name( type, originalName, newName, entityType))
			return true;

		if ( _returnVariableRule.update_variable_name( type, originalName, newName, entityType))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_variable_name( type, originalName, newName, entityType))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_variable_name(String type, String originalName, String newName, String entityType, EntityVariableRule subject) {
		if ( _classVariableRule.update_variable_name( type, originalName, newName, entityType, subject))
			return true;

		if ( _returnVariableRule.update_variable_name( type, originalName, newName, entityType, subject))
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_variable_name( type, originalName, newName, entityType, subject))
				return true;
		}

		return false;
	}

	@Override
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		return false;
	}

	@Override
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		return false;
	}

	@Override
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		return false;
	}

	@Override
	public boolean can_paste(Layer drawObjects) {
		if ( !_classVariableRule.can_paste( drawObjects))
			return false;

		if ( !_returnVariableRule.can_paste( drawObjects))
			return false;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( !argumentVariableRule.can_paste( drawObjects))
				return false;
		}

		return true;
	}

	@Override
	public boolean can_paste(Layer drawObjects, EntityVariableRule subject) {
		if ( !_classVariableRule.can_paste( drawObjects, subject))
			return false;

		if ( !_returnVariableRule.can_paste( drawObjects, subject))
			return false;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( !argumentVariableRule.can_paste( drawObjects, subject))
				return false;
		}

		return true;
	}

	@Override
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType) {
		if ( !_classVariableRule.is_number_object_type_correct( entityType, numberObjectName, numberObjectNewType))
			return false;

		if ( !_returnVariableRule.is_number_object_type_correct( entityType, numberObjectName, numberObjectNewType))
			return false;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( !argumentVariableRule.is_number_object_type_correct( entityType, numberObjectName, numberObjectNewType))
				return false;
		}

		return true;
	}

	@Override
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String numberObjectNewType, EntityVariableRule subject) {
		if ( !_classVariableRule.is_number_object_type_correct( entityType, numberObjectName, numberObjectNewType, subject))
			return false;

		if ( !_returnVariableRule.is_number_object_type_correct( entityType, numberObjectName, numberObjectNewType, subject))
			return false;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( !argumentVariableRule.is_number_object_type_correct( entityType, numberObjectName, numberObjectNewType, subject))
				return false;
		}

		return true;
	}

	@Override
	public boolean update_stage_manager() {
		if ( _classVariableRule.update_stage_manager())
			return true;

		if ( _returnVariableRule.update_stage_manager())
			return true;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( argumentVariableRule.update_stage_manager())
				return true;
		}

		return false;
	}

	@Override
	public String get_script(InitialValueMap initialValueMap, Role role, boolean isSubject) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String get_script(InitialValueMap initialValueMap, EntityVariableRule subject, Role role) {
		// TODO 2020.1.11
		String text = "";

		ClassVariableObject classVariableObject = get_classVariableObject( _classVariableRule);
		if ( null == classVariableObject)
			return "";

		text += "<" + Constant._javaMethodCallSpotName + ">addLastString " + Constant._javaMethodCallParameterListName + "=" + classVariableObject._classname + get_separator();
		text += "<" + Constant._javaMethodCallSpotName + ">addLastString " + Constant._javaMethodCallParameterListName + "=" + _classVariableRule.get_script( initialValueMap, subject, role) + get_separator();

//		if ( _kind.equals( "command"))
		text += "<" + Constant._javaMethodCallSpotName + ">addLastString " + Constant._javaMethodCallParameterListName + "=" + _returnVariableRule._variableType + get_separator();
		text += "<" + Constant._javaMethodCallSpotName + ">addLastString " + Constant._javaMethodCallParameterListName + "=" + _returnVariableRule.get_script( initialValueMap, subject, role) + get_separator();
		classVariableObject = _returnVariableRule._variableType.equals( "class variable") ? get_classVariableObject( _returnVariableRule) : null;
		text += "<" + Constant._javaMethodCallSpotName + ">addLastString " + Constant._javaMethodCallParameterListName + "=" + ( ( null != classVariableObject) ? classVariableObject._classname : "") + get_separator();

		text += "<" + Constant._javaMethodCallSpotName + ">addLastString " + Constant._javaMethodCallParameterListName + "=" + get_method() + get_separator();

		List<String> argumentTypes = get_argument_types();
		if ( null == argumentTypes || argumentTypes.size() != _argumentVariableRules.size())
			return "";

		for ( int i = 0; i < argumentTypes.size(); ++i) {
			text += "<" + Constant._javaMethodCallSpotName + ">addLastString " + Constant._javaMethodCallParameterListName + "=" + argumentTypes.get( i) + get_separator();
			text += "<" + Constant._javaMethodCallSpotName + ">addLastString " + Constant._javaMethodCallParameterListName + "=" + _argumentVariableRules.get( i).get_script( initialValueMap, subject, role) + get_separator();
		}

		return text;
	}

	/**
	 * @param entityVariableRule
	 * @return
	 */
	private ClassVariableObject get_classVariableObject(EntityVariableRule entityVariableRule) {
		// TODO Auto-generated method stub
		String entityType = entityVariableRule._entity;
		String entityName = entityVariableRule._entityName;
		if ( !entityVariableRule._agentVariable.equals( "")) {
			entityType = "agent";
			entityName = "";
		} else if ( !entityVariableRule._spotVariable.equals( "")) {
			entityType = "spot";
			entityName = "";
		}

		return entityVariableRule._variableType.equals( "exchange algebra")
			? new ClassVariableObject( entityVariableRule._variableValue, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname)
			: LayerManager.get_instance().get_class_variable( entityType, entityName, entityVariableRule._variableValue);
	}

	/**
	 * @return
	 */
	private String get_separator() {
		// TODO 2015.7.29
		return _kind.equals( "condition") ? " , " : " ; ";
	}

	/**
	 * @return
	 */
	private String get_method() {
		// TODO 2020.1.11
		String[] words = Tool.split( _method, '(');
		return ( null != words && 0 < words.length) ? words[ 0] : "";
	}

	/**
	 * @return
	 */
	private List<String> get_argument_types() {
		// TODO Auto-generated method stub
		String[] words = Tool.split( _method, '(');
		if ( null == words || 2 > words.length)
			return null;

		words = Tool.split( words[ 1], ')');
		if ( null == words)
			return null;

		if ( words[ 0].equals( ""))
			return new ArrayList<String>();

		words = words[ 0].split( ", ");

		return Arrays.asList( words);
	}

	@Override
	public String get_script_to_set_variables_into_expression_spot(InitialValueMap initialValueMap, EntityVariableRule subject, Role role, String separator) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String get_cell_text(Role role, boolean isSubject) {
		String retunnVariable = _returnVariableRule.get_cell_text( role, isSubject);
		String prefix = retunnVariable.equals( "") ? "" : ( retunnVariable + "=");
		prefix += ( _classVariableRule.get_cell_text( role, isSubject) + "." + _method.substring( 0, _method.indexOf( "(")));
		String variables = "";
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			variables += ( variables.equals( "") ? "" : ", ");
			variables += argumentVariableRule.get_cell_text( role, isSubject);
		}
		return prefix + "(" + variables + ")";
	}

	@Override
	public String get_cell_text(EntityVariableRule subject, Role role) {
		String retunnVariable = _returnVariableRule.get_cell_text( subject, role);
		String prefix = retunnVariable.equals( "") ? "" : ( retunnVariable + "=");
		prefix += ( _classVariableRule.get_cell_text( subject, role) + "." + _method.substring( 0, _method.indexOf( "(")));
		String variables = "";
		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			variables += ( variables.equals( "") ? "" : ", ");
			variables += argumentVariableRule.get_cell_text( subject, role);
		}
		return prefix + "(" + variables + ")";
	}

	@Override
	public boolean write(Writer writer) throws SAXException {
		return write( "javamethodcall", writer);
	}

	@Override
	public boolean write(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "method", "", Writer.escapeAttributeCharData( _method));

		writer.startElement( null, null, name, attributesImpl);

		if ( !_classVariableRule.write( writer, "classVariable"))
			return false;

		if ( !_returnVariableRule.write( writer, "returnVariable"))
			return false;

		for ( EntityVariableRule argumentVariableRule:_argumentVariableRules) {
			if ( !argumentVariableRule.write( writer, "argumentVariable"))
				return false;
		}

		writer.endElement( null, null, name);

		return true;
	}

	@Override
	public void print() {
	}
}
