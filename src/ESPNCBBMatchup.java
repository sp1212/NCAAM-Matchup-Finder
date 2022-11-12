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

	public String matchupUrl;

	public ESPNCBBMatchup() {
		this.awayTeam = "-";
		this.homeTeam = "-";
		this.awayWinPercentage = 0;
		this.homeWinPercentage = 0;
		this.awayHexColor = "000000";
		this.homeHexColor = "000000";
		this.matchupUrl = "#";
	}

	public double getAwayWinPercentage() {
		return awayWinPercentage;
	}

	public double getHomeWinPercentage() {
		return homeWinPercentage;
	}

	public String toString() {
		return "\n" + awayTeam + " " + awayWinPercentage + "% <--> " + homeTeam + " " + homeWinPercentage + "%";
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
				for (Element game : games) {
					try {
						String matchupUrl = "https://www.espn.com" + game.attr("href");
						Connection conESPNMatchup = Jsoup.connect(matchupUrl);
						Document docESPNMatchup = conESPNMatchup.get();

						ESPNCBBMatchup espnMatchup = new ESPNCBBMatchup();
						espnMatchup.matchupUrl = matchupUrl;
						espnMatchup.awayTeam = docESPNMatchup.select("span.long-name").get(0).text() + " "
								+ docESPNMatchup.select("span.short-name").get(0).text();
						espnMatchup.homeTeam = docESPNMatchup.select("span.long-name").get(1).text() + " "
								+ docESPNMatchup.select("span.short-name").get(1).text();

						System.out.println(espnMatchup.awayTeam + " vs. " + espnMatchup.homeTeam);

						if (docESPNMatchup.select("span.value-home").hasText() != false) {
							espnMatchup.awayWinPercentage = Double
									.parseDouble(docESPNMatchup.select("span.value-away").text().replaceAll("%", ""));
							espnMatchup.homeWinPercentage = Double
									.parseDouble(docESPNMatchup.select("span.value-home").text().replaceAll("%", ""));

							String awayRawColor = docESPNMatchup.select("div.wedge").get(1).attr("style");
							String awayHexColor = awayRawColor.substring(awayRawColor.indexOf("#")).replaceAll("[;#]",
									"");

							String homeRawColor = docESPNMatchup.select("div.wedge").get(0).attr("style");
							String homeHexColor = homeRawColor.substring(homeRawColor.indexOf("#")).replaceAll("[;#]",
									"");

							espnMatchup.awayHexColor = awayHexColor;
							espnMatchup.homeHexColor = homeHexColor;
						}
						espnMatchups.add(espnMatchup);
					}
					catch (IOException e) {
						System.out.println("IOException in try/catch block!");
					}

				}
			}
		}
		catch (IOException e) {
			System.out.println("IOException in try/catch block!");
		}

		return espnMatchups;
	}
}
