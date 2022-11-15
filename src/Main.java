import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) {
		try {
			long startTime = System.currentTimeMillis();
			LocalDate matchupDate = LocalDate.now().plusDays(0);
			System.out.println(matchupDate + "\n");

			ArrayList<CBBMatchup> cbbMatchups = CBBMatchup.getCBBMatchups(matchupDate);
			int numMatchups = cbbMatchups.size();
			LocalTime currentTime = LocalTime.now();

			System.out.println(numMatchups + " matchup(s) found @ " + currentTime);
			// System.out.println(cbbMatchups);
			System.out.println();

			HTMLFormat.generateMatchupsHtml(cbbMatchups);
			
			System.out.println("\nTook " + ((System.currentTimeMillis() - startTime) / 1000F) + " seconds");
		}
		catch (Exception | Error e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			JOptionPane.showMessageDialog(null, "An error has occurred.\n" + sw.toString(), "NCAAM Matchups", 0);
		}
	}
}
