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
    {
    	List<String> tablelist = connection.getExistingTables();
    	if (tablelist.contains("")){
            try {
                String line;
                Process p = Runtime.getRuntime().exec
                        ("psql -U "+userName+"-d "+databaseName+" -h "+serverURL+" -f "+path+"dd.sql");
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
}
