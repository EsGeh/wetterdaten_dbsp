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
		PrintStream out = System.out;
		String stadt_name = getCityName();
		try {
			ResultSet set = lookUpCity(stadt_name);
			if( !set.next() )
			{
				out.println("city not found!");
				return;
			}
			int stadt_id = set.getInt("stadt_id");
			//String stadt_name = set.getString("name");
			double stadt_laenge = set.getDouble("laenge");
			double stadt_breite = set.getDouble("breite");
			//out.println("city lookup succeeded: " + set.getInt("stadt_id"));
		} catch( SQLException e) {
			out.println("ERROR: unable to look up city: " + e.getMessage());
		}
	}
	private String getCityName() {
		PrintStream out = System.out;
		Scanner in = new Scanner(System.in).useDelimiter("\n");
		out.print("please enter a city:\n\t");
		return in.next().trim();
	}
	private ResultSet lookUpCity(String cityName) throws SQLException {
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStmt(
					queryToGetTown
			);
		}
		catch(SQLException e)
		{
			throw e;
		}
		try
		{
			stmt.setString( 1, cityName );
			ResultSet set = stmt.executeQuery();
			return set;
		}
		catch(SQLException e)
		{
			throw e;
		}
	}
	private SQLConnection connection;
	private static final String queryPossibleTowns =
		"SELECT count(*)\n" +
		"FROM dbsp_stadt\n" +
		"WHERE ? LIKE name\n" +
		";"
	;
	private static final String queryToGetTown =
		"SELECT *\n" + "FROM dbsp_stadt \n" +
		"WHERE ? LIKE name\n" +
		";"
	;
}
