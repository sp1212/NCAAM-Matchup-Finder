import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ESPNCBBMatchup {
	public String awayTeam;
	public String homeTeam;

	public double awayWinPercentage;
	public double homeWinPercentage;

	public String awayHexColor;
	public String homeHexColor;

	public int awayRank;
	public int homeRank;

	public String matchupUrl;

	public ESPNCBBMatchup() {
		this.awayTeam = "-";
		this.homeTeam = "-";
		this.awayWinPercentage = 0;
		this.homeWinPercentage = 0;
		this.awayHexColor = "000000";
		this.homeHexColor = "000000";
		this.awayRank = -1;
		this.homeRank = -1;
		this.matchupUrl = "#";
	}

	public double getAwayWinPercentage() {
		return awayWinPercentage;
	}

	public double getHomeWinPercentage() {
		return homeWinPercentage;
	}

	public String toString() {
		return "\n" + "(R" + awayRank + ")" + awayTeam + " " + awayWinPercentage + "% <--> " + "(R" + homeRank + ")"
				+ homeTeam + " " + homeWinPercentage + "%";
	}

	// gets all CBB matchups from ESPN
	public static ArrayList<ESPNCBBMatchup> getESPNMatchups(LocalDate matchupDate) {
		LocalDate date = matchupDate;
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMdd");
		String espnDate = date.format(myFormatObj);

		ArrayList<ESPNCBBMatchup> espnMatchups = new ArrayList<ESPNCBBMatchup>();

		String espnUrl = "https://www.espn.com/mens-college-basketball/schedule/_/date/" + espnDate + "/group/50";
		try {
			Connection con = Jsoup.connect(espnUrl);
			Document doc = con.get();

			if (con.response().statusCode() == 200) {
				System.out.println("Link:  " + espnUrl);
				System.out.println(doc.title() + "\n");

				Elements games = doc.select("a[href*=/mens-college-basketball/game]");
				System.out.println("Size of games selected:  " + games.size());
				for (Element game : games) {
					try {
						String matchupUrl = "https://www.espn.com" + game.attr("href");
						Connection conESPNMatchup = Jsoup.connect(matchupUrl);
						Document docESPNMatchup = conESPNMatchup.get();

						ESPNCBBMatchup espnMatchup = new ESPNCBBMatchup();
						espnMatchup.matchupUrl = matchupUrl;
						espnMatchup.awayTeam = docESPNMatchup.select("h2.ScoreCell__TeamName").get(0).text();
						espnMatchup.homeTeam = docESPNMatchup.select("h2.ScoreCell__TeamName").get(1).text();

						if (docESPNMatchup.select("div.matchupPredictor__innerContent").hasText()) {
							espnMatchup.awayWinPercentage = Double.parseDouble(docESPNMatchup
									.select("div.matchupPredictor__teamValue").get(0).text().replaceAll("%", ""));
							espnMatchup.homeWinPercentage = Double.parseDouble(docESPNMatchup
									.select("div.matchupPredictor__teamValue").get(1).text().replaceAll("%", ""));

							espnMatchup.awayHexColor = docESPNMatchup
									.select("div.matchupPredictor__wrap > div > svg > g > path").get(1).attr("stroke")
									.replaceAll("#", "");

							espnMatchup.homeHexColor = docESPNMatchup
									.select("div.matchupPredictor__wrap > div > svg > g > path").get(0).attr("stroke")
									.replaceAll("#", "");
						}

						if (docESPNMatchup
								.select("div.Gamestrip__Team--away > div > div > div > div > div.ScoreCell__Rank")
								.hasText()) {
							espnMatchup.awayRank = Integer.parseInt(docESPNMatchup
									.select("div.Gamestrip__Team--away > div > div > div > div > div.ScoreCell__Rank")
									.text());
						}
						if (docESPNMatchup
								.select("div.Gamestrip__Team--home > div > div > div > div > div.ScoreCell__Rank")
								.hasText()) {
							espnMatchup.homeRank = Integer.parseInt(docESPNMatchup
									.select("div.Gamestrip__Team--home > div > div > div > div > div.ScoreCell__Rank")
									.text());
						}
						
						System.out.println(espnMatchup.awayTeam + " vs. " + espnMatchup.homeTeam);

						espnMatchups.add(espnMatchup);
					} catch (IOException e) {
						System.out.println("IOException in try/catch block!");
					}

				}
			}
		} catch (IOException e) {
			System.out.println("IOException in try/catch block!");
		}

		return espnMatchups;
	}
}
