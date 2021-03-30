/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.common.java_method_call;

import java.util.ArrayList;
import java.util.List;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.generic.element.EntityVariableRule;

/**
 * @author kurata
 *
 */
public class MethodObject {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public String _returnType = "";

	/**
	 * 
	 */
	public EntityVariableRule _returnVariableRule = null;

	/**
	 * 
	 */
	public String[] _argumentTypes = null;

	/**
	 * 
	 */
	public List<EntityVariableRule> _argumentVariableRules = new ArrayList<EntityVariableRule>();

	/**
	 * 
	 */
	public MethodObject() {
		super();
	}

	/**
	 * @param name
	 * @param returnType
	 * @param argumentTypes
	 * @param role
	 */
	public MethodObject(String name, String returnType, List<String> argumentTypes, Role role) {
		super();
		_name = name;
		_returnType = returnType;
		_returnVariableRule = new EntityVariableRule();
		_argumentTypes = argumentTypes.toArray( new String[ 0]);
		for ( int i = 0; i < _argumentTypes.length; ++i)
			_argumentVariableRules.add( new EntityVariableRule());
	}

	/**
	 * @param methodObject
	 */
	public MethodObject(MethodObject methodObject) {
		super();
		_name = methodObject._name;
		_returnType = methodObject._returnType;
		_returnVariableRule = new EntityVariableRule( methodObject._returnVariableRule);
		_argumentTypes = new String[ methodObject._argumentTypes.length];
		System.arraycopy( methodObject._argumentTypes, 0, _argumentTypes, 0, methodObject._argumentTypes.length);
		for ( EntityVariableRule argumentVariableRule:methodObject._argumentVariableRules)
			_argumentVariableRules.add( new EntityVariableRule( argumentVariableRule));
	}

	/**
	 * @return
	 */
	public String get_full_method_name() {
		String name = ( _name + "(");
		for ( int i = 0; i < _argumentTypes.length; ++i)
			name += ( ( ( 0 == i) ? "" : ", ") + _argumentTypes[ i]);
		name += ")";
		return name;
	}

	/**
	 * @param methodName
	 * @param functionalObject
	 * @return
	 */
//	public boolean equals(String methodName, FunctionalObject functionalObject) {
//		String name = ( functionalObject._method + "(");
//		for ( int i = 0; i < functionalObject._parameters.length; ++i)
//			name += ( ( ( 0 == i) ? "" : ", ") + functionalObject._parameters[ i][ 1]);
//		name += ")";
//		return name.equals( methodName);
//	}
}
