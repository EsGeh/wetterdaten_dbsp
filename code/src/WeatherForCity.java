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
		String cityName = getCityName();
		try {
			ResultSet set = lookUpCity(cityName);
			if( set.next() )
				out.println("city lookup succeeded!");
		} catch( SQLException e) {
			out.println("ERROR: unable to look up city: " + e.getMessage());
		}
	}
	private String getCityName() {
		PrintStream out = System.out;
		Scanner in = new Scanner(System.in).useDelimiter("\n");
		out.print("please enter a city:\n\t");
		return in.next();
	}
	private ResultSet lookUpCity(String cityName) throws SQLException {
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStmt(
					queryPossibleTowns
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
		"SELECT stadt_id, laenge, breite\n" + "FROM dbsp_stadt \n" +
		"WHERE ? LIKE name\n" +
		";"
	;
}
