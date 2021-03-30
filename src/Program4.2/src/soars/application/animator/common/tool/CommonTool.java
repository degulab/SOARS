/*
 * 2005/03/10
 */
package soars.application.animator.common.tool;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import soars.common.utility.swing.tool.ExampleFileFilter;
import soars.common.utility.tool.sort.QuickSort;
import soars.application.animator.main.AnimatorView;
import soars.application.animator.main.Environment;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.object.player.ObjectManager;
import soars.application.animator.object.player.agent.AgentObjectManager;
import soars.application.animator.object.player.base.ObjectBase;
import soars.application.animator.object.player.base.edit.objects.ObjectComparator;
import soars.application.animator.object.player.spot.ISpotObjectManipulator;

/**
 * The common tool for Animator.
 * @author kurata / SOARS project
 */
public class CommonTool {

	/**
	 * Returns the combo box to select the font name.
	 * @return the combo box to select the font name
	 */
	public static JComboBox get_font_family_combo_box() {
		GraphicsEnvironment graphicsEnvironment
			= GraphicsEnvironment.getLocalGraphicsEnvironment();

		Font[] fonts = graphicsEnvironment.getAllFonts();
		Vector familyNames = new Vector();

		for ( int i = 0; i < fonts.length; ++i) {
			if ( !familyNames.contains( fonts[ i].getFamily( Locale.getDefault())))
				familyNames.add( fonts[ i].getFamily( Locale.getDefault()));
		}

		JComboBox comboBox = new JComboBox( familyNames);
		comboBox.setPreferredSize(
			new Dimension( comboBox.getPreferredSize().width + 15,
				comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * Returns the combo box to select the font style.
	 * @return the combo box to select the font style
	 */
	public static JComboBox get_font_style_combo_box() {
		String[] styles = {
			ResourceManager.get( "common.font.style.plain"),
			ResourceManager.get( "common.font.style.bold"),
			ResourceManager.get( "common.font.style.italic"),
			ResourceManager.get( "common.font.style.bold&italic")
		};

		JComboBox comboBox = new JComboBox( styles);
		comboBox.setPreferredSize(
			new Dimension( comboBox.getPreferredSize().width + 15,
				comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * Returns the combo box to select the font size.
	 * @return the combo box to select the font size
	 */
	public static JComboBox get_font_size_combo_box() {
		String[] sizes = {
			"8", "9", "10", "11", "12", "13", "14", "16", "18", "20", "24", "28", "32", "36", "48", "72"
		};

		JComboBox comboBox = new JComboBox( sizes);
		comboBox.setPreferredSize(
			new Dimension( comboBox.getPreferredSize().width + 15,
				comboBox.getPreferredSize().height));

		return comboBox;
	}

	/**
	 * Returns the font style string from the specified font style constant.
	 * @param style the specified font style constant
	 * @return the font style string from the specified font style constant
	 */
	public static String get_font_style(int style) {
		if ( Font.PLAIN == style)
			return ResourceManager.get(
				"common.font.style.plain");
		else if ( ( Font.BOLD + Font.ITALIC) == style)
			return ResourceManager.get(
				"common.font.style.bold&italic");
		else if ( Font.BOLD == style)
			return ResourceManager.get(
				"common.font.style.bold");
		else if ( Font.ITALIC == style)
			return ResourceManager.get(
				"common.font.style.italic");
		return "";
	}

	/**
	 * Returns the font style constant from the font style string.
	 * @param style the specified font style string
	 * @return the font style constant from the font style string
	 */
	public static int get_font_style(String style) {
		if ( style.equals( ResourceManager.get(
			"common.font.style.plain")))
			return Font.PLAIN;
		else if ( style.equals( ResourceManager.get(
			"common.font.style.bold&italic")))
			return ( Font.BOLD + Font.ITALIC);
		else if ( style.equals( ResourceManager.get(
			"common.font.style.bold")))
			return Font.BOLD;
		else if ( style.equals( ResourceManager.get(
			"common.font.style.italic")))
			return Font.ITALIC;
		return Font.PLAIN;
	}

	/**
	 * Returns the image file selected by the user.
	 * @param open_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the image file selected by the user
	 */
	public static File get_imagefile(String open_directory_key, Component component) {
		return get_open_file(
			open_directory_key,
			ResourceManager.get( "select.imagefile.dialog.title"),
			new String[] { "gif", "jpg", "jpeg", "png"},
			"imagefile",
			component);
	}

	/**
	 * Returns the image file selected by the user.
	 * @param open_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the image file selected by the user
	 */
	public static File get_imagefile(String open_directory_key, String title, Component component) {
		return get_open_file( open_directory_key, title,
			new String[] { "gif", "jpg", "jpeg", "png"},
			"imagefile",
			component);
	}

	/**
	 * Returns the file selected by the user.
	 * @param open_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param extensions the specified file extensions
	 * @param description the human readable description of file types
	 * @param component the parent component of the file chooser dialog
	 * @return the file selected by the user
	 */
	public static File get_open_file(String open_directory_key, String title, String[] extensions, String description, Component component) {
		String open_directory = "";
	
		String value = Environment.get_instance().get( open_directory_key, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				open_directory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}
	
		if ( !open_directory.equals( ""))
			fileChooser.setCurrentDirectory( new File( open_directory));
	
		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			File file = fileChooser.getSelectedFile();
	
			File directory = fileChooser.getCurrentDirectory();
			open_directory = directory.getAbsolutePath();
	
			Environment.get_instance().set( open_directory_key, open_directory);

			return file;
		}

		return null;
	}

	/**
	 * Returns the file saved by the user.
	 * @param save_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param extensions the specified file extensions
	 * @param description the human readable description of file types
	 * @param component the parent component of the file chooser dialog
	 * @return the file saved by the user
	 */
	public static File get_save_file(String save_directory_key, String title, String[] extensions, String description, Component component) {
		String save_directory = "";
	
		String value = Environment.get_instance().get( save_directory_key, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				save_directory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}
	
		if ( !save_directory.equals( ""))
			fileChooser.setCurrentDirectory( new File( save_directory));
	
		if ( JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog( component)) {
			File file = fileChooser.getSelectedFile();
	
			File directory = fileChooser.getCurrentDirectory();
			save_directory = directory.getAbsolutePath();
	
			Environment.get_instance().set( save_directory_key, save_directory);

			return file;
		}

		return null;
	}

	/**
	 * Returns the directory selected by the user.
	 * @param import_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the directory selected by the user
	 */
	public static File get_import_directory(String import_directory_key, String title, Component component) {
		String import_directory = "";
		File directory = null;
		
		String value = Environment.get_instance().get( import_directory_key, "");
		if ( null != value && !value.equals( "")) {
			directory = new File( value);
			if ( directory.exists())
				import_directory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( !import_directory.equals( "")) {
			fileChooser.setCurrentDirectory( new File( import_directory + "/../"));
			fileChooser.setSelectedFile( directory);
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			directory = fileChooser.getSelectedFile();
			import_directory = directory.getAbsolutePath();
			Environment.get_instance().set( import_directory_key, import_directory);
			return directory;
		}

		return null;
	}

	/**
	 * Returns the image files array selected by the user.
	 * @param open_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the image files array selected by the user
	 */
	public static File[] get_imagefiles(String open_directory_key, Component component) {
		return get_open_files(
			open_directory_key,
			ResourceManager.get( "select.imagefile.dialog.title"),
			new String[] { "gif", "jpg", "jpeg", "png"},
			"imagefile",
			component);
	}

	/**
	 * Returns the files array selected by the user.
	 * @param open_directory_key the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param extensions the specified file extensions
	 * @param description the human readable description of file types
	 * @param component the parent component of the file chooser dialog
	 * @return the files array selected by the user
	 */
	public static File[] get_open_files(String open_directory_key, String title, String[] extensions, String description, Component component) {
		String open_directory = "";
		
		String value = Environment.get_instance().get( open_directory_key, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				open_directory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		fileChooser.setMultiSelectionEnabled( true);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}
	
		if ( !open_directory.equals( ""))
			fileChooser.setCurrentDirectory( new File( open_directory));
	
		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			File[] files = fileChooser.getSelectedFiles();
	
			File directory = fileChooser.getCurrentDirectory();
			open_directory = directory.getAbsolutePath();
	
			Environment.get_instance().set( open_directory_key, open_directory);

			return files;
		}

		return null;
	}

	/**
	 * @param time
	 * @return
	 */
	public static double time_to_double(String time) {
		String[] words = time.split( "/");
		double day = Double.parseDouble( words[ 0]);
		words = words[ 1].split( ":");
		double hour = Double.parseDouble( words[ 0]);
		double min = Double.parseDouble( words[ 1]);
		return ( 24.0f * 60.0f * day + 60.0f * hour + min);
	}

	/**
	 * @param list
	 */
	public static void arrange_spots(List list) {
		ObjectBase[] objectBases = ( ObjectBase[])list.toArray( new ObjectBase[ 0]);
		QuickSort.sort( objectBases, new ObjectComparator( true, false));

		Rectangle rectangle = new Rectangle();
		for ( int i = 0; i < objectBases.length; ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objectBases[ i];
			if ( 0 == i || rectangle.x > spotObjectManipulator.get_position().x)
				rectangle.x = spotObjectManipulator.get_position().x;
			if ( 0 == i || rectangle.y > spotObjectManipulator.get_position().y)
				rectangle.y = spotObjectManipulator.get_position().y;
			if ( 0 == i || rectangle.width < spotObjectManipulator.get_dimension().width)
				rectangle.width = spotObjectManipulator.get_dimension().width;
			if ( 0 == i || rectangle.height < spotObjectManipulator.get_dimension().height)
				rectangle.height = spotObjectManipulator.get_dimension().height;
		}

		int side = ( int)Math.ceil( Math.sqrt( objectBases.length));
		for ( int i = 0; i < objectBases.length; ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objectBases[ i];
			int line = i / side;
			int row = i % side;
			spotObjectManipulator.move_to( rectangle.x + rectangle.width * row, rectangle.y + rectangle.height * line, false);
		}

		AgentObjectManager.get_instance().arrange();
		ObjectManager.get_instance().update_preferred_size( AnimatorView.get_instance());
		AnimatorView.get_instance().repaint();
	}
}
