import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetTeamNames {
	// used for testing and periodically gathering team names used by various sites
	public static void main(String[] args) {

		ArrayList<String> cbsTeams = getCBSTeams();
		// System.out.println(cbsTeams.size() + " Teams from CBS");
		Collections.sort(cbsTeams); // System.out.println(cbsTeams);

		// System.out.println();

		ArrayList<String> coversTeams = getCoversTeams();
		// System.out.println(coversTeams.size() + " Teams from Covers");
		Collections.sort(coversTeams); // System.out.println(coversTeams);

		// System.out.println();

		ArrayList<String> ESPNTeams = getESPNTeams();
		// System.out.println(ESPNTeams.size() + " Teams from ESPN");
		Collections.sort(ESPNTeams); // System.out.println(ESPNTeams);

		System.out.println();

		ArrayList<String> oddsSharkTeams = getOddsSharkTeams();
		// System.out.println(oddsSharkTeams.size() + " Teams from Odds Shark");
		Collections.sort(oddsSharkTeams); // System.out.println(oddsSharkTeams);

		for (int i = 0; i < 358; i++) {
			System.out.println(coversTeams.get(i) + ",," + cbsTeams.get(i) + ",," + oddsSharkTeams.get(i) + ",,"
					+ ESPNTeams.get(i));
		}

		ArrayList<ArrayList<String>> srTeamsArrArr = getSrTeams();
		ArrayList<String> srFullTeamNames = srTeamsArrArr.get(0);
		ArrayList<String> srUrlTeamNames = srTeamsArrArr.get(1);
		for (int i = 0; i < srFullTeamNames.size(); i++) {
			System.out.println(srUrlTeamNames.get(i) + "," + srFullTeamNames.get(i));
		}
	}

	public static ArrayList<ArrayList<String>> getSrTeams() {
		ArrayList<String> srFullTeamNames = new ArrayList<String>(358);
		ArrayList<String> srUrlTeamNames = new ArrayList<String>(358);

		String url = "https://www.sports-reference.com/cbb/schools/";
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();

			if (con.response().statusCode() == 200) {
				Elements teamRows = doc.select("tr");
				for (Element teamRow : teamRows) {
					if (teamRow.text().contains("2022") == true) {
						String fullTeamName = teamRow.select("a[href]").get(0).text();
						String teamUrl = teamRow.select("a[href]").get(0).attr("href");
						teamUrl = teamUrl.replaceAll("/cbb/schools/", "");
						teamUrl = teamUrl.replaceAll("/", "");
						srFullTeamNames.add(fullTeamName);
						srUrlTeamNames.add(teamUrl);
					}
				}
			}
		}
		catch (IOException e) {
			System.out.println("IOException in try/catch block!");
		}

		ArrayList<ArrayList<String>> teamsArrArr = new ArrayList<ArrayList<String>>(2);
		teamsArrArr.add(srFullTeamNames);
		teamsArrArr.add(srUrlTeamNames);
		return teamsArrArr;
	}

	public static ArrayList<String> getCBSTeams() {
		ArrayList<String> teams = new ArrayList<String>();
		String url = "https://www.cbssports.com/college-basketball/teams/";
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();

			if (con.response().statusCode() == 200) {
				// System.out.println("Link: " + url);
				// System.out.println(doc.title() + "\n");

				Elements teamLinks = doc.select("span.TeamName > a[href]");
				for (Element teamLink : teamLinks) {
					String team = teamLink.attr("href").substring(26);
					team = team.substring(0, team.indexOf("/"));
					teams.add(team);
				}
			}
		}
		catch (IOException e) {
			System.out.println("IOException in try/catch block!");
		}
		return teams;
	}

	public static ArrayList<String> getCoversTeams() {
		ArrayList<String> teams = new ArrayList<String>();
		String url = "https://www.covers.com/sport/basketball/ncaab/statistics/team-betting/2021-2022";
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();

			if (con.response().statusCode() == 200) {
				// System.out.println("Link: " + url);
				// System.out.println(doc.title() + "\n");

				Elements conferences = doc.select("span.covers-CoversMatchups-desktopHide");
				for (Element conference : conferences) {
					String team = conference.text();
					teams.add(team);
				}
			}
		}
		catch (IOException e) {
			System.out.println("IOException in try/catch block!");
		}
		return teams;
	}

	public static ArrayList<String> getESPNTeams() {
		ArrayList<String> teams = new ArrayList<String>();
		String url = "https://www.espn.com/mens-college-basketball/standings";
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();

			if (con.response().statusCode() == 200) {
				// System.out.println("Link: " + url);
				// System.out.println(doc.title() + "\n");

				Elements conferences = doc.select("span.hide-mobile > a.AnchorLink");
				for (Element conference : conferences) {
					String team = conference.text();
					teams.add(team);
				}
			}
		}
		catch (IOException e) {
			System.out.println("IOException in try/catch block!");
		}
		return teams;
	}

	public static ArrayList<String> getOddsSharkTeams() {
		ArrayList<String> teams = new ArrayList<String>();
		String url = "https://www.oddsshark.com/ncaab/extended-standings";
		try {
			Connection con = Jsoup.connect(url);
			Document doc = con.get();

			if (con.response().statusCode() == 200) {
				// System.out.println("Link: " + url);
				// System.out.println(doc.title() + "\n");

				Elements conferences = doc.select("div.table-wrapper");
				Elements teamNames = conferences.select("td > a[href]");
				for (Element name : teamNames) {
					String team = name.text();
					teams.add(team);
				}
			}
		}
		catch (IOException e) {
			System.out.println("IOException in try/catch block!");
		}
		return teams;
	}

}
