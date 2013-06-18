import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class WeatherForCity {
	public WeatherForCity(SQLConnection connection) {
		this.connection = connection;
	}
	
	public void exec()
	{
		/*PrintStream out = System.out;
		String stadt_name = askUserForCity();
		try {
			CityInfo cityInfo = lookUpCity(stadt_name);
			if( cityInfo == null)
			{
				out.println("city not found!");
				return;
			}
		}
		catch(SQLException e) {
			out.println("ERROR: looking up city failed: " + e.getMessage());
		}*/
			
	}
	private String askUserForCity() {
		PrintStream out = System.out;
		Scanner in = new Scanner(System.in).useDelimiter("\n");
		out.print("please enter a city:\n\t");
		return in.next().trim();
	}
	// returns null, if city not found in database.
	// throws an exception, if the query failed.
	private CityInfo lookUpCity(String cityName) throws SQLException {
		CityInfo result = new CityInfo();
		try {
			ResultSet townSet = lookupWithOneVariable(queryToGetTown, cityName);
			if( !townSet.next() )
			{
				return null;
			}
			result = new CityInfo();
			result.name = townSet.getString("name");
			result.laenge = townSet.getDouble("laenge");
			result.breite = townSet.getDouble("breite");
			return result;
		} catch( SQLException e) {
			throw e;
			//out.println("ERROR: unable to look up city: " + e.getMessage());
		}
	}
	private ResultSet lookupWithOneVariable(String query, String var) throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStmt(
				query
			);
			
			stmt.setString( 1, var );
			ResultSet set = stmt.executeQuery();
			return set;
		}
		catch(SQLException e)
		{
			throw e;
		}
	}
	private class CityInfo
	{
		public CityInfo()
		{
			this.name = "";
			this.laenge = -1;
			this.breite = -1;
		}
		public CityInfo(String name, double laenge , double breite ) {
			this.name = name;
			this.laenge = laenge;
			this.breite = breite;
		}
		public String name;
		public double laenge;
		public double breite;
	}
	private SQLConnection connection;
	private static final String queryPossibleTowns =
		"SELECT count(*)\n" +
		"FROM dbsp_stadt\n" +
		"WHERE ? LIKE name\n" +
		";"
	;
	private static final String queryToGetTown =
		"SELECT *\n" + "FROM dbsp_stadt:w" +
				" \n" +
		"WHERE ? LIKE name\n" +
		";"
	;
}
