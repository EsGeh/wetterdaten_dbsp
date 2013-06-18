import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.omg.CORBA.portable.InputStream;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SQLConnection conn = createConnection();
		{
            InstallDBs installer = new InstallDBs();
            installer.exec(conn,args[0], serverURL,serverPort,databaseName,userName,password);

            WeatherForCity w = new WeatherForCity(conn);
            w.exec();
		}
		closeConnection(conn);
	}
	
	private static SQLConnection createConnection() {
		SQLConnection connection = null;
		try
		{
			connection = new SQLConnection(serverURL, serverPort, databaseName, userName, password);
		}
		catch( SQLConnection.CouldNotLoadDriverException e)
		{
			System.out.println("ERROR: could not load the jdbc - SQL - Driver!");
			System.exit(0);
		}
		catch( SQLConnection.DatabaseConnectionException e)
		{
			System.out.println("ERROR: connection to database via failed!: " + e.getMessage());
			System.exit(0);
		}
		System.out.println("INFO: connection to the database established");
		return connection;
	}
	private static void closeConnection(SQLConnection connection) {
		try {
			connection.close();
			System.out.println("INFO: disconnected from database");
		}
		catch(SQLConnection.CouldNotCloseException e) { 
			System.out.println("ERROR: could not disconnect from database: " + e.getMessage());
		}
	}
	private final static String serverURL = "localhost";
	private final static String serverPort = ""; // currently disabled
	private final static String databaseName = "dbs_project";
	private final static String userName = "dbs_project";
	private final static String password = "dbsp";
}
