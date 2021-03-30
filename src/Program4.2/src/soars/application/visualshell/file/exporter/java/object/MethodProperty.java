/**
 * 
 */
package soars.application.visualshell.file.exporter.java.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soars.application.visualshell.object.player.base.PlayerBase;

/**
 * @author kurata
 *
 */
public class MethodProperty {

	/**
	 * ロールクラス名
	 */
	public String _roleClassName = "";

	/**
	 * メソッド名
	 */
	public List<String> _methods = new ArrayList<>();

	/**
	 * エンティティ数が２つ以上の場合
	 */
	public boolean _number = false;

	/**
	 * @param roleClassName 
	 * @param method 
	 * @param number 
	 */
	public MethodProperty(String roleClassName, List<String> methods, boolean number) {
		_roleClassName = roleClassName;
		_methods = methods;
		_number = number;
	}

	/**
	 * @param ruleData
	 * @param number
	 * @return
	 */
	private boolean equals(RuleData ruleData, boolean number) {
		// TODO Auto-generated method stub
		if ( _methods.size() != ruleData._methods.size())
			return false;

		for ( int i = 0; i < _methods.size(); ++i) {
			if ( !_methods.get( i).equals( ruleData._methods.get( i)))
				return false;
		}

		return _roleClassName.equals( ruleData._roleClassName) && _number == number;
	}

	/**
	 * @param ruleData
	 * @param entity 
	 * @param usedMethodPropertyMap
	 * @return
	 */
	public static int get(RuleData ruleData, PlayerBase entity, Map<Integer, MethodProperty> usedMethodPropertyMap) {
		// TODO Auto-generated method stub
		Set<Integer> integers = usedMethodPropertyMap.keySet();
		for ( Integer integer:integers) {
			if ( usedMethodPropertyMap.get( integer).equals( ruleData, !entity._number.equals( "")))
				return integer;
		}
		return 0;
	}
}
