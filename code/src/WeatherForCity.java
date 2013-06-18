import java.io.PrintStream;
import java.util.Scanner;


public class WeatherForCity {
	public WeatherForCity(SQLConnection connection) {
		this.connection = connection;
	}
	
	public void exec()
	{
		String cityName = getCityName();
		lookUpCity(cityName);
		/* ... to do */
	}
	private String getCityName() {
		PrintStream out = System.out;
		Scanner in = new Scanner(System.in).useDelimiter("\n");
		out.print("please enter a city:\n\t");
		return in.next();
	}
	private void lookUpCity(String cityName) {
		/* todo */
	}
	private SQLConnection connection;
}
