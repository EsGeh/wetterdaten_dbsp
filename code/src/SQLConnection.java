import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SQLConnection {
	public SQLConnection(
		String serverURL,
		String serverPort,
		String databaseName,
		String userName,
		String password
	) throws CouldNotLoadDriverException, DatabaseConnectionException
	{
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch(Exception e)
		{
			throw new CouldNotLoadDriverException(e.getMessage());
		}
		connection = null;
		try
		{
			Properties props = new Properties();
			props.setProperty("user", userName ); 
			props.setProperty("password", password );
			//props.setProperty("ssl", "true");
			connection = DriverManager.getConnection(
					//"jdbc:postgresql://" + serverURL + ":" + serverPort + "/" + databaseName,
					"jdbc:postgresql://" + serverURL + "/" + databaseName,
					props
				);
		}
		catch( SQLException e)
		{
			throw new DatabaseConnectionException(e.getMessage());
		}
	}
	public void close() throws CouldNotCloseException
	{
		if( connection!=null )
		{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				throw new CouldNotCloseException("connection could not be closed: " + e.getMessage());
			}
		}
	}
	// test if a table exists in the schema, by trying to read from it (hacky, but quiet reliable):
	public boolean tableExist(String tableName)
	{
		try
		{
			query(
				"select count(*)\n" +
				"from " + tableName + "\n" +
				"limit 1\n" +
				";"
			);
			/*PreparedStatement stmt = prepareStmt(
			);
			stmt.setString(1, tableName);
			stmt.executeQuery();*/
			return true;
		}
		catch(Exception e)
		{
			System.out.println("Scheiß-Exception: " + e.getMessage());
			return false;
		}
	}
	/*public List<String> getExistingTables()
	{
		try
		{
			List<String> result = new ArrayList<String>();
	        DatabaseMetaData meta = connection.getMetaData();
	        ResultSet tables = meta.getTables(null, null, null, new String[] { "TABLES" } );
	        while( tables.next() )
	        {
	        	result.add( tables.getString("TABLE_NAME") );
	        }
	        tables.close();
	        return result;
		}
		catch( Exception e)
		{
			System.out.println("ERROR: exception: " + e.getMessage());
		}
		return null;
	}*/
	// use non-prepared statements:
	// if your query does not have a result set (e.g. CREATE ...), use SQLConnection.update(...) instead
	public ResultSet query(String sqlQuery) throws SQLException
	{
		try
		{
			Statement stmt = connection.createStatement();
			return stmt.executeQuery(sqlQuery);
		}
		catch(SQLException e)
		{
			throw e;
		}
	}
	// executes an update query (one that has no result set)
	// returns the number of updates
	public int update(String sqlQuery) throws SQLException
	{
		try
		{
			Statement stmt = connection.createStatement();
			return stmt.executeUpdate(sqlQuery);
		}
		catch(SQLException e)
		{
			throw e;
		}
	}
	/* prepare a prepared statment.
	 * a PreparedStatement can contain variables, set them using PreparedStatement.set<Typ>()
	 * execute them using PreparedStatement.executeQuery()
	 */
	public PreparedStatement prepareStmt(String sqlQueryWithVariables) throws SQLException
	{
		try
		{
			return connection.prepareStatement(sqlQueryWithVariables);
		}
		catch(SQLException e)
		{
			throw e;
		}
	}
	private Connection connection;
	private static final String queryTestIfTableExists =
			"select count(*)\n" +
			"from ?\n" +
			"limit 1\n" +
			";"
		;
	public class CouldNotLoadDriverException extends Exception {
		public CouldNotLoadDriverException(String msg)
		{
			super(msg);
		}
	};
	public class DatabaseConnectionException extends Exception {
		public DatabaseConnectionException(String msg) {
			super(msg);
		}
	};
	public class CouldNotCloseException extends Exception {
		public CouldNotCloseException(String msg)
		{
			super(msg);
		}
	};
}