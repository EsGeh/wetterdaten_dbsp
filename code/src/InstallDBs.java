import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Scanner;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: andre
 * Date: 18.06.13
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
public class InstallDBs {
    public void exec(SQLConnection connection,
        String path,
        String serverURL,
        String serverPort,
        String databaseName,
        String userName,
        String password
    )
    {
		PrintStream out = System.out;
		Scanner in = new Scanner(System.in).useDelimiter("\n");

        if (!connection.tableExist("WETTERSTATION") || !connection.tableExist("WETTERMESSUNG"))
        {
        	out.println("Die Wetterdaten sind nicht eingelesen jetzt einlesen ( = 1 Tasse Kaffee)? (y/n)");
        	int answer = -1;
        	do {
        		String userInput = in.nextLine();
        		if( userInput.trim().equals("y"))
        			answer = 1;
        		else if( userInput.trim().equals("n"))
        			answer = 0;
        		else
        			out.println("invalid input!");
        	}
        	while(answer == -1);
        	if( answer == 0)
        		System.exit(0);
        	
        	System.out.println("INFO: Lese Wetterdaten ein!");
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"wetterdaten/dd.sql");
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"wetterdaten/dm.sql");
        }

        if (   !connection.tableExist("geodb_type_names") || !connection.tableExist("geodb_locations")
            || !connection.tableExist("geodb_hierarchies") || !connection.tableExist("geodb_coordinates")
            || !connection.tableExist("geodb_textdata") || !connection.tableExist("geodb_intdata")
            || !connection.tableExist("geodb_floatdata") || !connection.tableExist("geodb_changelog"))
        {
        	out.println("Die Geodaten sind nicht eingelesen. Jetzt einlesen ( = sehr viele Tasse Kaffee)? (y/n)");
        	int answer = -1;
        	do {
        		String userInput = in.nextLine();
        		if( userInput.trim().equals("y"))
        			answer = 1;
        		else if( userInput.trim().equals("n"))
        			answer = 0;
        		else
        			out.println("invalid input!");
        	}
        	while( answer == -1);
        	if( answer == 0)
        		System.exit(0);
        	
            System.out.println("INFO: Lese Geodaten ein!");
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"opengeodb/opengeodb-begin2.sql");
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"opengeodb/DE2.sql");
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"opengeodb/opengeodb-end.sql");
        }
    }

    public void dynamicimport(SQLConnection connection,
                              String path,
                              String serverURL,
                              String serverPort,
                              String databaseName,
                              String userName,
                              String password,
                              String fileName
    )
    {
        try {
            String line;
            Process p = Runtime.getRuntime().exec
                    ("psql -U "+userName+" -d "+databaseName+" -h "+serverURL+" -f "+path+fileName);
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getErrorStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }
}