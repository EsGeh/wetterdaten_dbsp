import java.sql.DatabaseMetaData;
import java.util.List;
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
    {   List<String> tablelist = connection.getExistingTables();

        if (! tablelist.contains("Wetterstation") && ! tablelist.contains("Wettermessung"))
        {
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"dd.sql");
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"dm.sql");
        }

        if (   ! tablelist.contains("geodb_type_names") && ! tablelist.contains("geodb_locations")
            && ! tablelist.contains("geodb_hierarchies") && ! tablelist.contains("geodb_coordinates")
            && ! tablelist.contains("geodb_textdata") && ! tablelist.contains("geodb_intdata")
            && ! tablelist.contains("geodb_floatdata") && ! tablelist.contains("geodb_changelog"))
        {
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"opengeodb-begin2.sql");
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"DE2.sql");
            dynamicimport(connection,path, serverURL,serverPort,databaseName,userName,password,"opengeodb-end.sql");
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
                        ("psql -U "+userName+"-d "+databaseName+" -h "+serverURL+" -f "+path+fileName);
                BufferedReader input =
                        new BufferedReader
                                (new InputStreamReader(p.getInputStream()));
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
