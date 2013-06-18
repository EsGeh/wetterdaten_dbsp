import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
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
			DriverManager.getConnection(
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
	public List<String> getExistingTables()
	{
		List<String> result = new ArrayList<String>();
		try
		{
	        DatabaseMetaData meta = connection.getMetaData();
	        ResultSet tables = meta.getTables(null, null, null, new String[] { "TABLES" } );
	        while( tables.next() )
	        {
	        	result.add( tables.getString("TABLE_NAME") );
	        }
		}
		catch( Exception e)
		{
			System.out.println("ERROR: exception: " + e.getMessage());
		}
		return result;
	}
	public void createTable(String tableName, String sqlFileToFillTableFrom)
	{
		
	}
	public ResultSet query(String sqlQuery)
	{
		// to do
	}
	private Connection connection;
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