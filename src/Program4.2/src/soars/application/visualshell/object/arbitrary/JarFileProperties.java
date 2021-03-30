/**
 * 
 */
package soars.application.visualshell.object.arbitrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.stream.StreamPumper;
import soars.common.utility.xml.dom.DomUtility;
import soars.common.utility.xml.dom.XmlTool;

/**
 * Manages the jar files for the functional object.
 * @author kurata / SOARS project
 */
public class JarFileProperties {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private JarFileProperties _jarFileProperties = null;

	/**
	 * 
	 */
	private String _directory = System.getProperty( Constant._soarsProperty) + File.separator
		+ "program" + File.separator
		+ "visualshell" + File.separator
		+ "environment" + File.separator;

	/**
	 * 
	 */
	private String _qualifiedName = "jarfile_properties";

	/**
	 * 
	 */
	private Document _document = null;

	/**
	 * 
	 */
	private Element _root_element = null;

	/**
	 * 
	 */
	private Element _module_element = null;

	/**
	 * 
	 */
	private Element _jarfile_element = null;

	/**
	 * 
	 */
	private Element _class_element = null;

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
			if ( !JavaClasses.initialize())
				System.exit( 1);

			if ( null == _jarFileProperties) {
				_jarFileProperties = new JarFileProperties();
				if ( !_jarFileProperties.initialize())
					System.exit( 1);
			}
		}
	}

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static JarFileProperties get_instance() {
		if ( null == _jarFileProperties)
			System.exit( 1);

		return _jarFileProperties;
	}

	/**
	 * @return
	 */
	private boolean initialize() {
		_document = XmlTool.create_document( null, _qualifiedName, null);
		if ( null == _document)
			return false;

		return parse();
	}

	/**
	 * @return
	 */
	private boolean parse() {
		String[] cmdarray = get_cmdarray();

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray);
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		Element root = _document.getDocumentElement();
		if ( null == root)
			return false;

		boolean result = true;
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( process.getInputStream()));
		Map<File, Element> map = new HashMap<File, Element>();
		try {
			String line = null;
			while ( true) {
				line = bufferedReader.readLine();
				if ( null == line)
					break;

				if ( !analyze( line, root, map)) {
					result = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @return
	 */
	private String[] get_cmdarray() {
		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant._macJava);
			list.add( "-Dfile.encoding=UTF-8");
		} else {
			list.add( Tool.get_java_command());
		}

//		list.add( "-Djava.endorsed.dirs=" + Constant._xerces_directory);
		if ( !memory_size.equals( "0"))
			list.add( "-Xmx" + memory_size + "m");
		list.add( "-jar");
		list.add( Constant._jarfilesAnalyzerJarFilename);

		for ( int i = 0; i < Constant._functionalObjectDirectories.length; ++i)
			list.add( Constant._functionalObjectDirectories[ i]);

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param line
	 * @param root
	 * @param map
	 * @return
	 */
	private boolean analyze(String line, Element root, Map<File, Element> map) {
		//System.out.println( line);
		if ( line.startsWith( "root:") && !line.matches( "^root:+[ ]*")) {
			String name = line.substring( "root:".length());
			_root_element = XmlTool.create_and_append_node( "root", _document, root);
			if ( null == _root_element)
				return false;

			XmlTool.set_attribute( _document, _root_element, "name", name);
		} else if ( line.startsWith( "module:") && !line.matches( "^module:+[ ]*")) {
			if ( null == _root_element)
				return false;

			String name = line.substring( "module:".length());
			_module_element = XmlTool.create_and_append_node( "module", _document, _root_element);
			if ( null == _module_element)
				return false;

			XmlTool.set_attribute( _document, _module_element, "name", name);

			map.clear();

			if ( !name.equals( Constant._moduleSpringFilename))
				map.put( new File( name), _module_element);

		} else if ( line.startsWith( "folder:") && !line.matches( "^folder:+[ ]*")) {
			if ( null == _module_element)
				return false;

			String name = line.substring( "folder:".length());
			File folder = new File( name);
			Element parent = map.get( folder.getParentFile());
			Element element = XmlTool.create_and_append_node( "folder", _document, ( ( null != parent) ? parent : _module_element));
			if ( null == element)
				return false;

			XmlTool.set_attribute( _document, element, "name", name);

			map.put( folder, element);

		} else if ( line.startsWith( "jarfile:") && !line.matches( "^jarfile:+[ ]*")) {
			if ( null == _module_element)
				return false;

			String name = line.substring( "jarfile:".length());
			File file = new File( name);
			Element parent = map.get( file.getParentFile());
			_jarfile_element = XmlTool.create_and_append_node( "jarfile", _document, ( ( null != parent) ? parent : _module_element));
			if ( null == _jarfile_element)
				return false;

			XmlTool.set_attribute( _document, _jarfile_element, "name", name);
		} else if ( line.startsWith( "class:") && !line.matches( "^class:+[ ]*")) {
			if ( null == _jarfile_element)
				return false;

			String name = line.substring( "class:".length());
			_class_element = XmlTool.create_and_append_node( "class", _document, _jarfile_element);
			if ( null == _class_element)
				return false;

			XmlTool.set_attribute( _document, _class_element, "name", name);
		} else if ( line.startsWith( "method:") && !line.matches( "^method:+[ ]*")) {
			if ( null == _class_element)
				return false;

			String name = line.substring( "method:".length(), line.indexOf( "return_type:"));
			Element element = XmlTool.create_and_append_node( "method", _document, _class_element);
			if ( null == element)
				return false;

			XmlTool.set_attribute( _document, element, "name", name);

			String return_type = line.substring( line.indexOf( "return_type:") + "return_type:".length(), line.indexOf( "parameter_type:"));
			XmlTool.set_attribute( _document, element, "return_type", return_type);

			String parameter_type = line.substring( line.indexOf( "parameter_type:") + "parameter_type:".length());
			if ( !parameter_type.equals( "")) {
				String[] parameter_types = parameter_type.split( "\\\t");
				for ( int i = 0; i < parameter_types.length; ++i) {
					if ( parameter_types[ i].equals( ""))
						return false;

					XmlTool.set_attribute( _document, element, "parameter_type" + i, parameter_types[ i]);
				}
			}
		} else
			return false;

		return true;
	}

	/**
	 * Returns true for merging the document tree of the java classes into one of this class successfully.
	 * @return true for merging the document tree of the java classes into one of this class successfully
	 */
	public boolean merge() {
		Node root = get_root_node();
		if ( null == root)
			return false;

		NodeList nodeList = JavaClasses.get_instance().get_nodes();
		if ( null == nodeList)
			return false;

		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node node = nodeList.item( i);
			if ( null == node)
				continue;

			root.appendChild( _document.importNode( node, true));
		}

		//serialize();

		return true;
	}

	/**
	 * Returns the root node of the document tree(Document Object Model).
	 * @return the root node of the document tree(Document Object Model)
	 */
	public Node get_root_node() {
		if ( null == _document)
			return null;

		return _document.getDocumentElement();
	}

	/**
	 * Returns true if the document tree contains the specified data.
	 * @param jar_filename the specified jar file name
	 * @param classname the specified class name
	 * @param method the specified method name
	 * @param parameters the specified array of the argument type names
	 * @param return_value the specified array of the return value type names
	 * @return true if the document tree contains the specified data
	 */
	public boolean contains(String jarfilename, String classname, String method, String[][] parameters, String[] return_value) {
		Node node = get_jarfile_node( jarfilename);
		if ( null == node)
			return false;

		node = get_class_node( node, classname);
		if ( null == node)
			return false;

		node = get_method_node( node,
			( ( !method.equals( classname) && classname.endsWith( "." + method)) ? classname : method),
			parameters, return_value);
		if ( null == node)
			return false;

		return true;
	}

	/**
	 * Returns true if the document tree contains the specified data.
	 * @param jar_filename the specified jar file name
	 * @param classname the specified class name
	 * @return true if the document tree contains the specified data
	 */
	public boolean contains(String jarfilename, String classname) {
		Node node = get_jarfile_node( jarfilename);
		if ( null == node)
			return false;

		node = get_class_node( node, classname);
		if ( null == node)
			return false;

		return true;
	}

	/**
	 * Returns the node which contains the specified jar file name in the document tree(Document Object Model).
	 * @param jar_filename the specified jar file name
	 * @return the node which contains the specified jar file name in the document tree(Document Object Model)
	 */
	public Node get_jarfile_node(String jarfilename) {
		Element root = ( Element)get_root_node();
		if ( null == root)
			return null;

		if ( jarfilename.equals( Constant._javaClasses))
			return XmlTool.get_node( root, "root[@name=\"" + Constant._javaClasses + "\"]");
		else {
			File jarfile = new File( jarfilename);
			if ( null == jarfile || !jarfile.exists() || !jarfile.isFile())
				return null;

			NodeList nodeList = XmlTool.get_node_list( root, "root");
			if ( null == nodeList)
				return null;

			for ( int i = 0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item( i);
				if ( null == node)
					continue;

				String name = XmlTool.get_attribute( node, "name");
				if ( null == name || name.equals( Constant._javaClasses))
					continue;

				File directory = new File( name);
				if ( null == directory || !directory.exists() || !directory.isDirectory())
					continue;

				if ( !FileUtility.is_parent( jarfile, directory))
					continue;

				return get_jarfile_node( node, jarfilename, jarfile);
			}
			return null;
		}
	}

	/**
	 * @param root
	 * @param jarfilename
	 * @param jarfile
	 * @return
	 */
	private Node get_jarfile_node(Node root, String jarfilename, File jarfile) {
		NodeList nodeList = XmlTool.get_node_list( root, "module");
		if ( null == nodeList)
			return null;

		Node default_node = null;
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node node = nodeList.item( i);
			if ( null == node)
				continue;

			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				continue;

			if ( name.equals( Constant._noDefinedModule)) {
				default_node = node;
				continue;
			}

			File modulefile = new File( name);
			if ( null == modulefile || !modulefile.exists() || !modulefile.isFile())
				continue;

			if ( !FileUtility.is_parent( jarfile, modulefile.getParentFile()))
				continue;

			return get_jarfile_node_recursively( node, jarfilename, jarfile);
		}

		if ( null == default_node)
			return null;

		return get_jarfile_node( default_node, jarfilename);
	}

	/**
	 * @param parent
	 * @param jarfilename
	 * @param jarfile
	 * @return
	 */
	private Node get_jarfile_node_recursively(Node parent, String jarfilename, File jarfile) {
		Node node = get_jarfile_node( parent, jarfilename);
		if ( null != node)
			return node;

		NodeList nodeList = parent.getChildNodes();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			node = nodeList.item( i);
			if ( null == node || !node.getNodeName().equals( "folder"))
				continue;

			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				continue;

			File folder = new File( name);
			if ( null == folder || !folder.exists() || !folder.isDirectory())
				continue;

			if ( !FileUtility.is_parent( jarfile, folder))
				continue;

			return get_jarfile_node_recursively( node, jarfilename, jarfile);
		}
		return null;
	}

	/**
	 * @param node
	 * @param jarfilename
	 * @return
	 */
	private Node get_jarfile_node(Node node, String jarfilename) {
		return XmlTool.get_node( node, "jarfile[@name=\"" + jarfilename + "\"]");
	}

	/**
	 * @param jarfile_node
	 * @param classname
	 * @return
	 */
	public Node get_class_node(Node jarfile_node, String classname) {
		return XmlTool.get_node( jarfile_node, "class[@name=\"" + classname + "\"]");
	}

	/**
	 * Returns true if the document tree contains the specified data.
	 * @param class_node the specified class name node
	 * @param methodname the specified method name
	 * @param parameters the specified array of the argument type names
	 * @param return_value the specified array of the return value type names
	 * @return true if the document tree contains the specified data
	 */
	public Node get_method_node(Node class_node, String methodname, String[][] parameters, String[] return_value) {
		NodeList nodeList = XmlTool.get_node_list( class_node, "method[@name=\"" + methodname + "\"]");
		if ( null == nodeList)
			return null;

		for ( int i = 0; i < nodeList.getLength(); ++i) {

			Node node = nodeList.item( i);
			if ( null == node)
				continue;

			String[] parameter_types = get_parameter_types( node);
			if ( !same_parameter_types( parameter_types, parameters))
				continue;

			String return_type = XmlTool.get_attribute( node, "return_type");
			if ( !same_return_type( return_type, return_value))
				continue;

			return node;
		}

		return null;
	}

	/**
	 * @param node
	 * @return
	 */
	private String[] get_parameter_types(Node node) {
		List parameter_types = new ArrayList();
		int index = 0;
		while ( true) {
			String parameter_type = XmlTool.get_attribute( node, "parameter_type" + index);
			if ( null == parameter_type)
				break;

			parameter_types.add( parameter_type);
			++index;
		}

		return parameter_types.isEmpty() ? null : ( String[])parameter_types.toArray( new String[ 0]);
	}

	/**
	 * @param parameter_types
	 * @param parameters
	 * @return
	 */
	private boolean same_parameter_types(String[] parameter_types, String[][] parameters) {
		if ( null == parameter_types)
			return ( null == parameters || 0 == parameters.length);

		if ( null == parameters)
			return ( null == parameter_types || 0 == parameter_types.length);

		if ( parameter_types.length != parameters.length)
			return false;

		for ( int i = 0; i < parameter_types.length; ++i) {
			if ( !parameter_types[ i].equals( parameters[ i][ 1]))
				return false;
		}

		return true;
	}

	/**
	 * @param return_type
	 * @param return_value
	 * @return
	 */
	private boolean same_return_type(String return_type, String[] return_value) {
		if ( null == return_type)
			return ( null == return_value || return_value[ 1].equals( "") || return_value[ 1].equals( "void"));

		if ( null == return_value)
			return ( null == return_type || return_type.equals( "") || return_type.equals( "void"));

		return return_type.equals( return_value[ 1]);
	}

	/**
	 * Returns true for updating the java classes for the functional object.
	 * @return true for updating the java classes for the functional object
	 */
	public boolean update_java_classes() {
		if ( !remove_java_classes_node())
			return false;

		if ( !JavaClasses.update())
			return false;

		if ( !merge())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	private boolean remove_java_classes_node() {
		Node root = get_root_node();
		if ( null == root)
			return false;

		Node node = XmlTool.get_node( root, "root[@name=\"" + Constant._javaClasses + "\"]");
		if ( null == node)
			return false;

		root.removeChild( node);

		return true;
	}

	/**
	 * Returns true if the document tree contains the specified data.
	 * @param jar_filename the specified jar file name
	 * @param classname the specified class name
	 * @param appendedJavaClassList the array of the java classes
	 * @return true if the document tree contains the specified data
	 */
	public boolean exist(String jar_filename, String classname, List<String> appendedJavaClassList) {
		if ( contains( jar_filename, classname))
			return true;

		if ( !jar_filename.equals( Constant._javaClasses))
			return false;
		else {
			// Javaのクラスが存在しているなら追加する
			// そうでなければ読み込みを続けるかどうか？確認する
			// 読み込み中止ならfalseを返す
			Node node = get_jarfile_node( Constant._javaClasses);
			if ( null == node)
				return ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
					MainFrame.get_instance(),
					"Java class : " + classname + "\n"
						+ ResourceManager.get_instance().get( "file.open.java.class.not.exist.message") + "\n"
						+ ResourceManager.get_instance().get( "file.open.confirm.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.YES_NO_OPTION));

			if ( !JavaClasses.get_instance().append( classname, node, _document))
				return ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
					MainFrame.get_instance(),
					"Java class : " + classname + "\n"
						+ ResourceManager.get_instance().get( "file.open.java.class.not.exist.message") + "\n"
						+ ResourceManager.get_instance().get( "file.open.confirm.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.YES_NO_OPTION));

			appendedJavaClassList.add( classname);

			return true;
		}
	}

	/**
	 * @return
	 */
	public boolean serialize() {
		if ( null == _document)
			return false;

		String current_directory_name = System.getProperty( Constant._soarsHome);
		if ( null == current_directory_name)
			return false;

		String temporary_directory_name = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._tmpKey, "");
		if ( temporary_directory_name.equals( ""))
			return false;

		File temporary_directory = new File( current_directory_name + "/" + temporary_directory_name /*+ "/soars/program/visualshell/test"*/);
		if ( temporary_directory.exists() && !temporary_directory.isDirectory())
			return false;

		if ( !temporary_directory.exists() && !temporary_directory.mkdirs())
			return false;

		return DomUtility.write( _document, new File( temporary_directory, "jarfile_properties.xml"), "UTF-8", null);
	}

	// TODO 以下要修正

	/**
	 * Returns true for appending the array of the jar file informations to the document tree(Document Object Model).
	 * @param jar_file_list the array of the jar files
	 * @return true for appending the array of the jar file informations to the document tree(Document Object Model)
	 */
	public boolean append(List jar_file_list) {
		Element root = _document.getDocumentElement();
		if ( null == root)
			return false;

		List new_jar_file_list = new ArrayList();
		for ( int i = 0; i < jar_file_list.size(); ++i) {
			File path = ( File)jar_file_list.get( i);
			Node node = XmlTool.get_node( root, "jarfile[@name=\"" + path.getPath().replaceAll( "\\\\", "/") + "\"]");
			if ( null == node) {
				new_jar_file_list.add( path);
				continue;
			}

			root.removeChild( node);
			new_jar_file_list.add( path);
		}


		URLClassLoader urlClassLoader = load( new_jar_file_list);
		if ( null == urlClassLoader)
			return false;


		for ( int i = 0; i < new_jar_file_list.size(); ++i) {
			File path = ( File)new_jar_file_list.get( i);

			Node node = XmlTool.create_and_append_node( "jarfile", _document, root);
			if ( null == node)
				return false;

			XmlTool.set_attribute( _document, ( Element)node, "name", path.getPath().replaceAll( "\\\\", "/"));

			if ( !parse( ( Element)node, path, urlClassLoader))
				continue;
		}

		return true;
	}

	/**
	 * @param jar_file_list
	 * @return
	 */
	private URLClassLoader load(List jar_file_list) {
		List urlList = new ArrayList();

		for ( int i = 0; i < jar_file_list.size(); ++i) {
			File path = ( File)jar_file_list.get( i);
			try {
				URL url = new URL( "jar:file:" + path.getPath() + "!/");
				urlList.add( url);
			} catch (MalformedURLException e) {
				//e.printStackTrace();
				return null;
			}
		}

		URL[] urls = ( URL[])urlList.toArray( new URL[ 0]);

		return new URLClassLoader( urls, ClassLoader.getSystemClassLoader().getParent());
	}

	/**
	 * @param parent
	 * @param path
	 * @param urlClassLoader
	 * @return
	 */
	private boolean parse(Element parent, File path, URLClassLoader urlClassLoader) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile( path.getPath());
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		Enumeration enumeration = jarFile.entries();
		if ( null == enumeration)
			return false;

		while ( enumeration.hasMoreElements()) {
			ZipEntry zipEntry = ( ZipEntry)enumeration.nextElement();
			String name = zipEntry.getName();
			if ( !name.endsWith( ".class") || 0 <= name.indexOf( '$'))
				continue;

			name = name.substring( 0, name.length() - ".class".length());
			name = name.replaceAll( "/", ".");
			if ( !parse( name, parent, urlClassLoader))
				continue;
		}
		return true;
	}

	/**
	 * @param name
	 * @param parent
	 * @param urlClassLoader
	 * @return
	 */
	private boolean parse(String name, Element parent, URLClassLoader urlClassLoader) {
		Class cls = null;
		try {
			cls = urlClassLoader.loadClass( name);
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			//System.out.println( "\t- ClassNotFoundException!");
			//System.out.println( "");
			return false;
		} catch (Throwable e) {
			//e.printStackTrace();
			//System.out.println( "\t- Class error!");
			//System.out.println( "");
			return false;
		}

		if ( null == cls) {
			//System.out.println( "\t- Class error!");
			//System.out.println( "");
			return false;
		}

		if ( cls.isInterface())
			return false;

		Member[] constructors = null;
		try {
			constructors = cls.getConstructors();
		} catch (Throwable ex) {
			//System.out.println( "\t- Method error!");
			//System.out.println( "");
			constructors = null;
		}

		Member[] methods = null;
		try {
			methods = cls.getMethods();
		} catch (Throwable ex) {
			//System.out.println( "\t- Method error!");
			//System.out.println( "");
			methods = null;
		}

		List member_list = new ArrayList();

		if ( null != constructors && 0 < constructors.length) {
			for ( int i = 0; i < constructors.length; ++i) {
				if ( 0 <= constructors[ i].getName().indexOf( '$'))
					continue;

				member_list.add( constructors[ i]);
			}
		}

		if ( null != methods && 0 < methods.length) {
			for ( int i = 0; i < methods.length; ++i) {
				if ( 0 <= methods[ i].getName().indexOf( '$'))
					continue;

				member_list.add( methods[ i]);
			}
		}

		if ( member_list.isEmpty()) {
			//System.out.println( "\t- No method!");
			//System.out.println( "");
			return false;
		}

		Element element = XmlTool.create_and_append_node( "class", _document, parent);
		if ( null == element)
			return false;

		XmlTool.set_attribute( _document, element, "name", name);

		for ( int i = 0; i < member_list.size(); ++i) {
			if ( member_list.get( i) instanceof Constructor) {
				Constructor constructor = ( Constructor)member_list.get( i);
				parse( constructor.getName(), null, constructor.getParameterTypes(), element);
			} else if ( member_list.get( i) instanceof Method) {
				Method method = ( Method)member_list.get( i);
				parse( method.getName(), method.getReturnType(), method.getParameterTypes(), element);
			}
		}

		return true;
	}

	/**
	 * @param name
	 * @param return_type
	 * @param parameter_types
	 * @param parent
	 * @return
	 */
	private boolean parse(String name, Class return_type, Class[] parameter_types, Element parent) {
		Element element = XmlTool.create_and_append_node( "method", _document, parent);
		if ( null == element)
			return false;

		XmlTool.set_attribute( _document, element, "name", name);

		XmlTool.set_attribute( _document, element, "return_type", ( ( null == return_type) ? "" : return_type.getName()));

		for ( int i = 0; i < parameter_types.length; ++i)
			XmlTool.set_attribute( _document, element, "parameter_type" + i, parameter_types[ i].getName());

		return true;
	}
}
