/*
 * Created on 2006/06/06
 */
package soars.application.visualshell.plugin;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.swing.window.Frame;
import soars.common.utility.tool.reflection.Reflection;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;

/**
 * @author kurata
 */
public class Plugin {


	/**
	 * 
	 */
	private String _name;

	/**
	 * 
	 */
	private String _version;

	/**
	 * 
	 */
	private Map _titleMap;

	/**
	 * 
	 */
	private Map _commentMap;

	/**
	 * 
	 */
	private List _fileModuleList;

	/**
	 * 
	 */
	private List _urlModuleList;

	/**
	 * 
	 */
	private Map _menuMap;

	/**
	 * 
	 */
	private String _className;

	/**
	 * 
	 */
	private String _methodName;

	/**
	 * 
	 */
	private List _argumentList;


	/**
	 * 
	 */
	public boolean _enable = false;


	/**
	 * 
	 */
	public String _directory = "";


	/**
	 * 
	 */
	private JMenuItem _menuItem = null;


	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private HashMap _parameterTypeMap = null;


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
			if ( null == _parameterTypeMap) {
				_parameterTypeMap = new HashMap();
				_parameterTypeMap.put( "Name", String.class);
				_parameterTypeMap.put( "Version", String.class);
				_parameterTypeMap.put( "Title", String.class);
				_parameterTypeMap.put( "Comment", String.class);
				_parameterTypeMap.put( "File", File.class);
				_parameterTypeMap.put( "MainFrame", JFrame.class);
			}
		}
	}

	/**
	 * @return
	 */
	public String getName() {
		if ( null == _name)
			_name = "";

		return _name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		if ( null == _version)
			_version = "";

		return _version;
	}

	/**
	 * @param version
	 */
	public void setVersion(String version) {
		_version = version;
	}

	/**
	 * @return
	 */
	public Map getTitleMap() {
		if ( null == _titleMap)
			_titleMap = new HashMap();

		return _titleMap;
	}

	/**
	 * @param titleMap
	 */
	public void setTitleMap(Map titleMap) {
		_titleMap = titleMap;
	}

	/**
	 * @return
	 */
	public Map getCommentMap() {
		if ( null == _commentMap)
			_commentMap = new HashMap();

		return _commentMap;
	}

	/**
	 * @param commentMap
	 */
	public void setCommentMap(Map commentMap) {
		_commentMap = commentMap;
	}

	/**
	 * @return
	 */
	public List getFileModuleList() {
		if ( null == _fileModuleList)
			_fileModuleList = new ArrayList();

		return _fileModuleList;
	}

	/**
	 * @param fileModuleList
	 */
	public void setFileModuleList(List fileModuleList) {
		_fileModuleList = fileModuleList;
	}

	/**
	 * @return
	 */
	public List getUrlModuleList() {
		if ( null == _urlModuleList)
			_urlModuleList = new ArrayList();

		return _urlModuleList;
	}

	/**
	 * @param urlModuleList
	 */
	public void setUrlModuleList(List urlModuleList) {
		_urlModuleList = urlModuleList;
	}

	/**
	 * @return
	 */
	public Map getMenuMap() {
		if ( null == _menuMap)
			_menuMap = new HashMap();

		return _menuMap;
	}

	/**
	 * @param menuMap
	 */
	public void setMenuMap(Map menuMap) {
		_menuMap = menuMap;
	}

	/**
	 * @return
	 */
	public String getClassName() {
		if ( null == _className)
			_className = "";

		return _className;
	}

	/**
	 * @param className
	 */
	public void setClassName(String className) {
		_className = className;
	}

	/**
	 * @return
	 */
	public String getMethodName() {
		if ( null == _methodName)
			_methodName = "";

		return _methodName;
	}

	/**
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		_methodName = methodName;
	}

	/**
	 * @return
	 */
	public List getArgumentList() {
		if ( null == _argumentList)
			_argumentList = new ArrayList();

		return _argumentList;
	}

	/**
	 * @param argumentList
	 */
	public void setArgumentList(List argumentList) {
		_argumentList = argumentList;
	}

	/**
	 * @return
	 */
	public boolean is_correct() {
		if ( null == getName() || getName().equals( ""))
			return false;

		if ( 0 <= getName().indexOf( ';'))
			return false;

		return true;
	}

	/**
	 * @return
	 */
	public String get_title() {
		String title = ( String)getTitleMap().get( Locale.getDefault().getLanguage());
		if ( null != title)
			return title;

		title = ( String)getTitleMap().get( "default");
		return ( ( null == title) ? "" : title);
	}

	/**
	 * @return
	 */
	public String get_comment() {
		String comment = ( String)getCommentMap().get( Locale.getDefault().getLanguage());
		if ( null != comment)
			return comment;

		comment = ( String)getCommentMap().get( "default");
		return ( ( null == comment) ? "" : comment);
	}

	/**
	 * @param urlList
	 */
	public void get_file_modules(List urlList) {
		String directory = _directory + ( ( _directory.endsWith( "/") || _directory.endsWith( "\\")) ? "" : "/");
		for ( int i = 0; i < getFileModuleList().size(); ++i) {
			String file_module_name = "jar:file:" + directory + ( String)getFileModuleList().get( i) + "!/";
			//System.out.println( file_module_name);

			URL url;
			try {
				url = new URL( file_module_name);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				continue;
			}

			urlList.add( url);
		}
	}

	/**
	 * @param urlList
	 */
	public void get_url_modules(List urlList) {
		for ( int i = 0; i < getUrlModuleList().size(); ++i) {
			String url_module_name = "jar:" + ( String)getUrlModuleList().get( i) + "!/";
			//System.out.println( url_module_name);

			URL url;
			try {
				url = new URL( url_module_name);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				continue;
			}

			urlList.add( url);
		}
	}

	/**
	 * @param userInterface
	 * @param message_label
	 * @param frame
	 * @param menuTextMap
	 * @return
	 */
	public boolean setup_menu(UserInterface userInterface, JLabel message_label, Frame frame, Map menuTextMap) {
		List menuList = ( List)getMenuMap().get( Locale.getDefault().getLanguage());
		if ( null == menuList)
			menuList = ( List)getMenuMap().get( "default");

		if ( null == menuList)
			return false;

		if ( null == menuList || 2 > menuList.size())
			return false;

		for ( int i = 0; i < menuList.size(); ++i) {
			List list = ( List)menuList.get( i);
			if ( null == list || list.isEmpty())
				return false;
		}

		if ( userInterface.this_menuItem_exists( menuList, frame.getJMenuBar()))
			return false;

		PluginMenuAction pluginMenuAction = new PluginMenuAction( getName(), frame, this);

		JMenu menu = userInterface.create_menu( menuList, frame.getJMenuBar(), message_label);
		if ( null == menu)
			return false;

		add_separator( menu, menuTextMap);

		List list = ( List)menuList.get( menuList.size() - 1);
		String[] menu_elements = ( String[])list.toArray( new String[ 0]);
		_menuItem = userInterface.append_menuitem(
			menu,
			menu_elements[ 0],
			pluginMenuAction,
			( ( 1 < menu_elements.length && null != menu_elements[ 1]) ? menu_elements[ 1] : ""),
			( ( 2 < menu_elements.length && null != menu_elements[ 2]) ? menu_elements[ 2] : ""),
			message_label,
			( ( 3 < menu_elements.length && null != menu_elements[ 3]) ? menu_elements[ 3] : ""));

		return true;
	}

	/**
	 * @param menu
	 * @param menuTextMap
	 */
	private void add_separator(JMenu menu, Map menuTextMap) {
		if ( 0 == menu.getItemCount())
			return;

		JMenuItem menuItem = menu.getItem( menu.getItemCount() - 1);
		if ( null == menuItem)
			return;

		if ( null == menuTextMap.get( menuItem.getText()))
			return;

		menu.addSeparator();
	}

	/**
	 * @param frame
	 * @param actionEvent
	 */
	public void on_selected(Frame frame, ActionEvent actionEvent) {
		if ( null == getClassName() || getClassName().equals( "")
			|| null == getMethodName() || getMethodName().equals( "")
			|| null == PluginManager._urlClassLoader)
			return;

		Class[] parameterTypes = get_parameterTypes();

		Object[] args = get_args();

		Class cls;
		try {
			cls = PluginManager._urlClassLoader.loadClass( getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		List resultList = new ArrayList();
		if ( !Reflection.execute_static_method( cls, getMethodName(), parameterTypes, args, resultList)) {
			JOptionPane.showMessageDialog( frame,
				"Plugin error : Reflection.execute_static_method( ... )" + Constant._lineSeparator
				+ " Plugin name : " + getName() + Constant._lineSeparator
				+ " Class name : " + getClassName() + Constant._lineSeparator
				+ " Method name : " + getMethodName() + Constant._lineSeparator,
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		if ( !resultList.isEmpty() && null != resultList.get( 0)
			&& resultList.get( 0) instanceof Boolean) {
			Boolean result = ( Boolean)resultList.get( 0);
			if ( !result.booleanValue()) {
				JOptionPane.showMessageDialog( frame,
					"Plugin false : Reflection.execute_static_method( ... )" + Constant._lineSeparator
					+ " Plugin name : " + getName() + Constant._lineSeparator
					+ " Class name : " + getClassName() + Constant._lineSeparator
					+ " Method name : " + getMethodName() + Constant._lineSeparator,
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	/**
	 * @return
	 */
	private Class[] get_parameterTypes() {
		if ( getArgumentList().isEmpty())
			return null;

		Class[] parameterTypes = new Class[ getArgumentList().size()];
		for ( int i = 0; i < getArgumentList().size(); ++i) {
			String argument = ( String)getArgumentList().get( i);
			Class cls = ( Class)_parameterTypeMap.get( argument);
			parameterTypes[ i] = ( ( null != cls) ? cls : String.class);
		}
		return parameterTypes;
	}

	/**
	 * @return
	 */
	private Object[] get_args() {
		if ( getArgumentList().isEmpty())
			return null;

		Object[] args = new Object[ getArgumentList().size()];
		for ( int i = 0; i < getArgumentList().size(); ++i) {
			String argument = ( String)getArgumentList().get( i);
			if ( argument.equals( "Name"))
				args[ i] = getName();
			else if ( argument.equals( "Version"))
				args[ i] = getVersion();
			else if ( argument.equals( "Title"))
				args[ i] = get_title();
			else if ( argument.equals( "Comment"))
				args[ i] = get_comment();
			else if ( argument.equals( "File"))
				args[ i] = LayerManager.get_instance().get_current_file();
			else if ( argument.equals( "MainFrame"))
				args[ i] = MainFrame.get_instance();
			else
				args[ i] = argument;
		}
		return args;
	}
}
