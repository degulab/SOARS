/**
 * 
 */
package soars.tool.initial_data_file_maker.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class InitialData {

	/**
	 * 
	 */
	public List<String> sql = null;

	/**
	 * 
	 */
	public Map<String, Object> entity = null;

	/**
	 * 
	 */
	public List<Map<String, String>> data = null;

	/**
	 * 
	 */
	public String encoding = null;

	/**
	 * 
	 */
	public InitialData() {
	}

	/**
	 * @param url
	 * @param index
	 * @param folder 
	 * @return
	 */
	public boolean write(String url, int index, File folder) {
		if ( null == sql || null == entity || null == entity.get( "name") || null == data || data.isEmpty())
			return false;

		Connection connection = null;

		try {
			String command = get();
			if ( null == command)
				return false;

			connection = DriverManager.getConnection( url);
			PreparedStatement preparedStatement = connection.prepareStatement( command);

			ResultSet resultSet = preparedStatement.executeQuery();

//			Map<String, String> typeMap = new HashMap<>();
//			for ( int i = 1; i <= resultSet.getMetaData().getColumnCount(); ++i)
//				typeMap.put( resultSet.getMetaData().getColumnName( i), resultSet.getMetaData().getColumnTypeName( i));

			String text = "";

			for ( Map<String, String> map:data)
				text += ( "\t" + map.get( "command"));

			text += "\n";

			for ( Map<String, String> map:data)
				text += ( "\t" + map.get( "name"));

			text += "\n";

			if ( null == entity.get( "to")) {
				text += ( String)entity.get( "name");

				String line = get( resultSet);
				if ( null == line)
					return false;

				text += line;
			} else {
				int entityFrom = ( null == ( Integer)entity.get( "from") ? 1 : ( Integer)entity.get( "from"));
				int entityTo = ( Integer)entity.get( "to");
				if ( entityFrom >= entityTo)
					return false;

				for ( int i = entityFrom; i <= entityTo; ++i) {
					text += ( ( String)entity.get( "name") + String.valueOf( i));

					String line = get( resultSet);
					if ( null == line)
						return false;

					text += line;
				}
			}

			System.out.println( text);

			File file = new File( folder, "initial" + String.valueOf( index) + ".csv");
			FileUtility.write_text_to_file( file, text, encoding);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * @return
	 */
	private String get() {
		String command = "";
		for ( String line:sql)
			command += ( line + "\n");
		return command;
	}

	/**
	 * @param resultSet
	 * @return
	 * @throws SQLException 
	 */
	private String get(ResultSet resultSet) throws SQLException {
		if ( !resultSet.next())
			return null;

		String text = "";
		for ( Map<String, String> map:data) {
			text += "\t";
			String[] columns = ( ( String)map.get( "column")).split( ",");
			for ( String column:columns)
				text += resultSet.getString( column);
		}

		text += "\n";

		return text;
	}
}
