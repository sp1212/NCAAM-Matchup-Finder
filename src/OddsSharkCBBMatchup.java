import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OddsSharkCBBMatchup
{
	public String awayTeam;
	public String homeTeam;
	public double awayScore;
	public double homeScore;

	public OddsSharkCBBMatchup()
	{

	}

	public double getAwayScore()
	{
		return this.awayScore;
	}

	public double getHomeScore()
	{
		return this.homeScore;
	}

	public String toString()
	{
		return "\n" + awayTeam + " " + awayScore + " <--> " + homeTeam + " " + homeScore;
	}
	
	public static ArrayList<OddsSharkCBBMatchup> getOddsSharkMatchups()
	{
		ArrayList<OddsSharkCBBMatchup> oddsSharkMatchups = new ArrayList<OddsSharkCBBMatchup>();

		String oddsSharkUrl = "https://www.oddsshark.com/ncaab/computer-picks";
		try
		{
			Connection con = Jsoup.connect(oddsSharkUrl);
			Document doc = con.get();

			if (con.response().statusCode() == 200)
			{
				System.out.println("Link:  " + oddsSharkUrl);
				System.out.println(doc.title() + "\n");

				Elements games = doc.select("table.pick-table");
				for (Element game : games)
				{
					OddsSharkCBBMatchup oddsSharkMatchup = new OddsSharkCBBMatchup();

					oddsSharkMatchup.awayTeam = game.select("span.soccer-college").get(0).text();
					oddsSharkMatchup.awayScore = Double
							.parseDouble(game.select("td.pick-predicted-score-away").text().replaceAll("[^0-9.]", ""));
					oddsSharkMatchup.homeTeam = game.select("span.soccer-college").get(1).text();
					oddsSharkMatchup.homeScore = Double
							.parseDouble(game.select("td.pick-predicted-score-home").text().replaceAll("[^0-9.]", ""));

					oddsSharkMatchups.add(oddsSharkMatchup);
					System.out.println(oddsSharkMatchup.awayTeam + " vs. " + oddsSharkMatchup.homeTeam);
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("IOException in try/catch block!");
		}
		return oddsSharkMatchups;
	}
}
