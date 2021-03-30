/**
 * 
 */
package soars.library.adapter.java_method_call;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import env.EquippedObject;
import soars.common.soars.constant.CommonConstant;
import soars.common.soars.tool.SoarsReflection;
import soars.library.adapter.userrules.Unit;
import util.DoubleValue;
import util.FloatValue;
import util.IntValue;
import util.LongValue;

/**
 * @author kurata
 *
 */
public class JavaMethodCall {

	/**
	 * @param linkedList
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public static boolean execute_condition(LinkedList<Object> linkedList) throws ClassNotFoundException {
		return execute( linkedList, true);
	}

	/**
	 * @param linkedList
	 * @throws ClassNotFoundException 
	 */
	public static void execute_command(LinkedList<Object> linkedList) throws ClassNotFoundException {
		execute( linkedList, false);
	}

	/**
	 * @param linkedList
	 * @param condition
	 * @return
	 * @throws ClassNotFoundException 
	 */
	private static boolean execute(LinkedList<Object> linkedList, boolean condition) throws ClassNotFoundException {
		if ( null == linkedList || 2 > linkedList.size()) {
			JOptionPane.showMessageDialog( null,
				"Java method call error : Invalid LinkedList",
				get_title( condition),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		EquippedObject equippedObject = ( EquippedObject)linkedList.get( 0);
		if ( null == equippedObject) {
			JOptionPane.showMessageDialog( null,
				"Java method call error : EquippedObject",
				get_title( condition),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String classVariableClassName = ( String)linkedList.get( 3);
		Unit classVariableUnit = Unit.create( equippedObject, ( String)linkedList.get( 4));
		if ( null == classVariableUnit) {
			JOptionPane.showMessageDialog( null,
				"Java method call error : Invalid class variable",
				get_title( condition),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Object classVariable = classVariableUnit.getInstance();

		String returnVariableType = ( String)linkedList.get( 5);

		Unit returnVariableUnit = null;
		if ( !( ( String)linkedList.get( 5)).equals( "") && !( ( String)linkedList.get( 5)).equals( "void")) {
			returnVariableUnit = Unit.create( equippedObject, ( String)linkedList.get( 6));
			if ( null == returnVariableUnit) {
				JOptionPane.showMessageDialog( null,
					"Java method call error : Invalid return class variable",
					get_title( condition),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String returnVariableClassName = ( String)linkedList.get( 7);

		String method = ( String)linkedList.get( 8);

		List<Class> classes = new ArrayList<Class>();
		List<Object> arguments = new ArrayList<Object>();
		for ( int i = 9; i < linkedList.size(); i += 2) {
			String className = ( String)linkedList.get( i);
			Class cls = null;
			if ( className.equals( "int"))
				cls = int.class;
			else if ( className.equals( "long"))
				cls = long.class;
			else if ( className.equals( "float"))
				cls = float.class;
			else if ( className.equals( "double"))
				cls = double.class;
			else
				cls = Class.forName( className);

			if ( null == cls) {
				JOptionPane.showMessageDialog( null,
					"Java method call error : Could not get class - " + className,
					get_title( condition),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			classes.add( cls);

			String argument = ( String)linkedList.get( i + 1);
			if ( argument.contains( "\"")) {
				String value = argument.replaceAll( "\"", "");
				if ( cls.equals( String.class))
					arguments.add( value);
				else if ( cls.equals( int.class))
					arguments.add( Integer.valueOf( value).intValue());
				else if ( cls.equals( long.class))
					arguments.add( Long.valueOf( value).longValue());
				else if ( cls.equals( float.class))
					arguments.add( Float.valueOf( value).floatValue());
				else if ( cls.equals( double.class))
					arguments.add( Double.valueOf( value).doubleValue());
				else {
					JOptionPane.showMessageDialog( null,
						"Java method call error : Unknown object - " + argument,
						get_title( condition),
						JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else {
				Unit unit = Unit.create( equippedObject, argument);
				if ( null == unit) {
					JOptionPane.showMessageDialog( null,
						"Java method call error : Could not create unit - " + argument,
						get_title( condition),
						JOptionPane.ERROR_MESSAGE);
					return false;
				}
				Object object = unit.getInstance();
				if ( null != object) {
					if ( cls.equals( int.class) && ( object instanceof IntValue))
						object = ( ( IntValue)object).intValue();
					else if ( cls.equals( long.class) && ( object instanceof IntValue))
						object = ( ( IntValue)object).longValue();
					else if ( cls.equals( long.class) && ( object instanceof LongValue))
						object = ( ( LongValue)object).longValue();
					else if ( cls.equals( float.class) && ( object instanceof FloatValue))
						object = ( ( FloatValue)object).floatValue();
					else if ( cls.equals( float.class) && ( object instanceof DoubleValue))
						object = ( ( DoubleValue)object).floatValue();
					else if ( cls.equals( double.class) && ( object instanceof DoubleValue))
						object = ( ( DoubleValue)object).doubleValue();
				}
				arguments.add( ( null != object) ? object : unit._elements[ 2]);
			}
		}

//		if ( !arguments.isEmpty()) {
//			String text = "";
//			for ( int i = 0; i < arguments.size(); ++i)
//				text += classes.get( i).toString() + " : " + arguments.get( i).getClass().toString() + CommonConstant._lineSeparator;
//			JOptionPane.showMessageDialog( null,
//				"Java method call success : arguments" + CommonConstant._lineSeparator
//					+ text,
//				get_title( condition),
//				JOptionPane.INFORMATION_MESSAGE);
//		}

		List<Object> resultList = new ArrayList<Object>();

		if ( null == classVariable) {
			// Constructor or static method
			if ( classVariableClassName.endsWith( method)) {
				// Constructor
				Object object = arguments.isEmpty()
					? SoarsReflection.get_class_instance( classVariableClassName)
					: SoarsReflection.get_class_instance( classVariableClassName, classes.toArray( new Class[ 0]), arguments.toArray( new Object[ 0]));
				if ( null == object) {
					JOptionPane.showMessageDialog( null,
						"Java method call error : SoarsReflection.get_class_instance( ... )" + CommonConstant._lineSeparator
							+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
							+ " Method name : " + method + CommonConstant._lineSeparator,
						get_title( condition),
						JOptionPane.ERROR_MESSAGE);
					return false;
				}
				classVariableUnit.setInstance( object);
//				JOptionPane.showMessageDialog( null,
//					"Java method call success : unit.setInstance( ... )" + CommonConstant._lineSeparator
//						+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
//						+ " Method name : " + method + CommonConstant._lineSeparator,
//					get_title( condition),
//					JOptionPane.INFORMATION_MESSAGE);
				return true;
			} else {
				// Static method
				if ( !SoarsReflection.execute_static_method( Class.forName( classVariableClassName), method, classes.toArray( new Class[ 0]), arguments.toArray( new Object[ 0]), resultList)) {
					JOptionPane.showMessageDialog( null,
						"Java method call error : SoarsReflection.execute_static_method( ... )" + CommonConstant._lineSeparator
							+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
							+ " Method name : " + method + CommonConstant._lineSeparator,
						get_title( condition),
						JOptionPane.ERROR_MESSAGE);
					return false;
//				} else {
//					JOptionPane.showMessageDialog( null,
//						"Java method call success : SoarsReflection.execute_static_method( ... )" + CommonConstant._lineSeparator
//							+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
//							+ " Method name : " + method + CommonConstant._lineSeparator,
//						get_title( condition),
//						JOptionPane.INFORMATION_MESSAGE);
				}
			}
		} else {
			// Member method
			if ( !SoarsReflection.execute_class_method( classVariable, method, classes.toArray( new Class[ 0]), arguments.toArray( new Object[ 0]), resultList)) {
				JOptionPane.showMessageDialog( null,
					"Java method call error : SoarsReflection.execute_class_method( ... )" + CommonConstant._lineSeparator
						+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
						+ " Method name : " + method + CommonConstant._lineSeparator,
					get_title( condition),
					JOptionPane.ERROR_MESSAGE);
				return false;
//			} else {
//				JOptionPane.showMessageDialog( null,
//					"Java method call success : SoarsReflection.execute_class_method( ... )" + CommonConstant._lineSeparator
//						+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
//						+ " Method name : " + method + CommonConstant._lineSeparator,
//					get_title( condition),
//					JOptionPane.INFORMATION_MESSAGE);
			}
		}

		if ( condition) {
			if ( resultList.isEmpty() || null == resultList.get( 0)) {
				JOptionPane.showMessageDialog( null,
					"Java method call error : Could not get return value" + CommonConstant._lineSeparator
						+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
						+ " Method name : " + method + CommonConstant._lineSeparator,
					get_title( condition),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			return ( resultList.get( 0) instanceof Boolean) ? ( ( Boolean)resultList.get( 0)).booleanValue() : true;
		} else {
			if ( null == returnVariableUnit)
				return true;

			if ( resultList.isEmpty() || null == resultList.get( 0)) {
				JOptionPane.showMessageDialog( null,
					"Java method call error : Could not get return value" + CommonConstant._lineSeparator
						+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
						+ " Method name : " + method + CommonConstant._lineSeparator,
					get_title( condition),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if ( returnVariableType.equals( "class variable")) {
				returnVariableUnit.setInstance( resultList.get( 0));
//				JOptionPane.showMessageDialog( null,
//					"Java method call success : Could get return value" + CommonConstant._lineSeparator
//						+ " Return variable type : " + returnVariableType + CommonConstant._lineSeparator
//						+ " Return variable : " + ( String)linkedList.get( 6) + CommonConstant._lineSeparator
//						+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
//						+ " Method name : " + method + CommonConstant._lineSeparator
//						+ " Retrun value : " + resultList.get( 0).getClass().toString(),
//					get_title( condition),
//					JOptionPane.INFORMATION_MESSAGE);
			} else {
				if ( resultList.get( 0) instanceof Integer)
					returnVariableUnit.set( ( ( Integer)resultList.get( 0)).intValue());
				else if ( resultList.get( 0) instanceof Double)
					returnVariableUnit.set( ( ( Double)resultList.get( 0)).doubleValue());
				else if ( resultList.get( 0) instanceof Boolean)
					returnVariableUnit.set( String.valueOf( ( ( Boolean)resultList.get( 0)).booleanValue()));
				else
					returnVariableUnit.set( resultList.get( 0));

//				JOptionPane.showMessageDialog( null,
//					"Java method call success : Could get return value" + CommonConstant._lineSeparator
//						+ " Return variable type : " + returnVariableType + CommonConstant._lineSeparator
//						+ " Return variable : " + ( String)linkedList.get( 6) + CommonConstant._lineSeparator
//						+ " Class name : " + classVariableClassName + CommonConstant._lineSeparator
//						+ " Method name : " + method + CommonConstant._lineSeparator
//						+ " Retrun value : " + resultList.get( 0).getClass().toString(),
//					get_title( condition),
//					JOptionPane.INFORMATION_MESSAGE);
			}
		}

		return true;
	}

	/**
	 * @param condition
	 * @return
	 */
	private static String get_title(boolean condition) {
		return condition ? "execute_condition" : "execute_command";
	}
}
