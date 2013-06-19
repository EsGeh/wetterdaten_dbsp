import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class WeatherForCity {
	public WeatherForCity(SQLConnection connection) {
		this.connection = connection;
	}
	
	public void exec()
	{
		PrintStream out = System.out;
		String stadt_name = askUserForCity();
		Date date = askUserForDate();
		CityInfo cityInfo = null;
		try {
			cityInfo = lookUpCity(stadt_name);
			if( cityInfo == null)
			{
				out.println("city not found!");
				return;
			}
			int station_id = lookupNextStation(cityInfo);
			MessungInfo messung = lookupMessung(station_id,date);
		}
		catch(SQLException e) {
			out.println("ERROR: looking up city failed: " + e.getMessage());
		}
	}
	private String askUserForCity() {
		PrintStream out = System.out;
		Scanner in = new Scanner(System.in).useDelimiter("\n");
		out.print("please enter a city:\n\t");
		return in.next().trim();
	}
	private Date askUserForDate() {
		DateFormat format = new SimpleDateFormat("yy/mm/yyyy");
		PrintStream out = System.out;
		Scanner in = new Scanner(System.in).useDelimiter("\n");
		out.print("please enter a date (format: yy/mm/yyyy):\n\t");
		while(true)
		{
			try {
				String userInput = in.next().trim();
				return format.parse(userInput);
			}
			catch(Exception e) {
				out.println("wrong syntax!");
			}
		}
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
			result.id = townSet.getInt("stadt_id");
			result.name = townSet.getString("name");
			result.laenge = townSet.getDouble("laenge");
			result.breite = townSet.getDouble("breite");
			return result;
		} catch( SQLException e) {
			throw e;
			//out.println("ERROR: unable to look up city: " + e.getMessage());
		}
	}
	private int lookupNextStation(CityInfo cityInfo) throws SQLException
	{
		try
		{
			ResultSet set = connection.query(
				"select *" +
				"from dbsp_relevantefor\n" +
				"where stadt_id = " + cityInfo.id + "\n" +
				"order by distance ASC\n" +
				"limit 1\n" +
				";",
				false
			);
			set.next();
			return set.getInt("station_id");
		}
		catch(SQLException e)
		{
			throw e;
		}
	}
	// returns null, if no data found
	// throws an exception, if something if there is an sql error
	private MessungInfo lookupMessung(int station_id, Date date) throws SQLException
	{
		try
		{
			MessungInfo messung = new MessungInfo();
			ResultSet setMinDate = connection.query(
				"select *" +
				"from dbsp_wettermessung\n" +
				"where station_id = " + station_id + "\n" +
				"order by distance ASC\n" +
				"limit 1\n" +
				";",
				false
			);
			if( !setMinDate.next())
			{
				return null;
			}
			ResultSet setMaxDate = connection.query(
				"select *" +
				"from dbsp_wettermessung\n" +
				"where station_id = " + station_id + "\n" +
				"order by distance DESC\n" +
				"limit 1\n" +
				";",
				false
			);
			setMaxDate.next();
			ResultSet set = connection.query(
				"select *" +
				"from dbsp_wettermessung\n" +
				"where station_id = " + station_id + "\n" +
				"and datum >= " + date + "\n" +
				"order by distance ASC\n" +
				"limit 10\n" +
				";",
				false
			);
			if( !set.next())
			{
				System.out.println("no entries found! the date must be between " +
						setMinDate.getDate("date") + " and " + 
						setMaxDate.getDate("date")
					);
			}
			
			messung.station_id = set.getInt("station_id");
			messung.datum = set.getDate("datum");
			messung.qualitaet = set.getInt("qualitaet");
			messung.min_5cm = set.getDouble("min_5cm");
			messung.min_2m = set.getDouble("min2m");
			messung.mittel_2m = set.getDouble("mittel_2m");
			messung.max_2m = set.getDouble("max_2m");
			messung.relative_feuchte = set.getDouble("relative_feuchte");
			messung.mittel_windstaerke = set.getDouble("mittel_windstaerke");
			messung.max_windgeschwindigkeit = set.getDouble("max_windgeschwindigkeit");
			messung.sonnenscheindauer = set.getDouble("sonnenscheindauer");
			messung.mittel_bedeckungsgrad = set.getDouble("mittel_bedeckungsgrad");
			messung.niederschlagshoehe = set.getDouble("niederschlagshoehe");
			messung.mittel_luftdruck = set.getDouble("mittel_luftdruck");
			return messung;
		}
		catch(SQLException e)
		{
			throw e;
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
			this.id = 0;
			this.name = "";
			this.laenge = -1;
			this.breite = -1;
		}
		public CityInfo(int id, String name, double laenge , double breite ) {
			this.id = id;
			this.name = name;
			this.laenge = laenge;
			this.breite = breite;
		}
		public int id;
		public String name;
		public double laenge;
		public double breite;
	}
	private class MessungInfo
	{
		public int station_id;
		public Date datum;
		public int qualitaet;
		public double min_5cm;
		public double min_2m;
		public double mittel_2m;
		public double max_2m;
		public double relative_feuchte;
		public double mittel_windstaerke;
		public double max_windgeschwindigkeit;
		public double sonnenscheindauer;
		public double mittel_bedeckungsgrad;
		public double niederschlagshoehe;
		public double mittel_luftdruck;
	}
	private SQLConnection connection;
	private static final String queryPossibleTowns =
		"SELECT count(*)\n" +
		"FROM dbsp_stadt\n" +
		"WHERE ? LIKE name\n" +
		";"
	;
	private static final String queryToGetTown =
		"SELECT *\n" + 
		"FROM dbsp_stadt\n" +
		"WHERE ? LIKE name\n" +
		";"
	;
}
