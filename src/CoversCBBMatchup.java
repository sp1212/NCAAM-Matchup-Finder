import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CoversCBBMatchup
{
	public String awayTeam;
	public int awayConsensus;
	public double awaySpread;
	public String homeTeam;
	public int homeConsensus;
	public double homeSpread;
	public double overUnder;
	public int overConsensus;
	public int underConsensus;
	public boolean isNeutral;
	public String matchupUrlIndex;
	public LocalTime gameTime;

	// default constructor
	public CoversCBBMatchup()
	{

	}

	public int getAwayConsensus()
	{
		return this.awayConsensus;
	}

	public double getAwaySpread()
	{
		return this.awaySpread;
	}

	public int getHomeConsensus()
	{
		return this.homeConsensus;
	}

	public double getHomeSpread()
	{
		return this.homeSpread;
	}

	public double getOverUnder()
	{
		return this.overUnder;
	}

	public int getOverConsensus()
	{
		return this.overConsensus;
	}

	public int getUnderConsensus()
	{
		return this.underConsensus;
	}

	public boolean getIsNeutral()
	{
		return this.isNeutral;
	}

	public String toString()
	{
		return "\n" + " " + awayTeam + " " + awaySpread + " " + awayConsensus + "% " + homeTeam + " " + homeSpread + " "
				+ homeConsensus + "% " + overUnder + " OVER:" + overConsensus + "% UNDER:" + underConsensus + "% "
				+ isNeutral + " " + gameTime;
	}
	
	// gathers matchup data from Covers
	public static ArrayList<CoversCBBMatchup> getCoversMatchups(LocalDate matchupDate)
	{
		LocalDate date = matchupDate;

		ArrayList<CoversCBBMatchup> coversCBBMatchups = new ArrayList<CoversCBBMatchup>();
		String coversUrl = "https://www.covers.com/sports/ncaab/matchups?selectedDate=" + date;
		try
		{
			Connection conCovers = Jsoup.connect(coversUrl);
			Document docCovers = conCovers.get();

			if (conCovers.response().statusCode() == 200)
			{
				System.out.println("Link:  " + coversUrl);
				System.out.println(docCovers.title() + "\n");

				Elements picksCovers = docCovers.select("div.cmg_game_container");
				for (Element pickCovers : picksCovers)
				{
					// setting a flag for if a matchup from the site is valid / should be used
					// disregards matchups that are in progress, canceled, postponed, or are unbettable
					boolean shouldAddMatchup = true;
					if (pickCovers.select("div.cmg_game_time").text().compareTo("Postponed") == 0
							|| pickCovers.select("div.cmg_team_live_odds").text().contains("Off")
							|| pickCovers.select("div.cmg_matchup_list_gamebox").text().contains("Boxscore")
							|| pickCovers.select("div.cmg_matchup_list_status").hasText() != false
							|| pickCovers.select("div.cmg_matchup_list_column_1")
									.select("div.cmg_matchup_list_odds_value").text().contains("-"))
					{
						shouldAddMatchup = false;
					}
					if (shouldAddMatchup)
					{
						CoversCBBMatchup matchup = new CoversCBBMatchup();
						matchup.matchupUrlIndex = pickCovers.select("a[href^=/sport/basketball/ncaab/matchup/preview/]").attr("href").replaceAll("/sport/basketball/ncaab/matchup/preview/", "");
						matchup.awayTeam = pickCovers.select("div.cmg_matchup_list_column_1")
								.select("div.cmg_team_name").text().replaceAll("[^a-zA-Z-]", "");
						// System.out.println(matchup.awayTeam);
						matchup.awayConsensus = Integer.parseInt(pickCovers.select("div.cmg_matchup_list_column_1")
								.select("div.cmg_matchup_list_odds_value").text().replaceAll("[^0-9]", ""));
						matchup.homeTeam = pickCovers.select("div.cmg_matchup_list_column_3")
								.select("div.cmg_team_name").text().replaceAll("[^a-zA-Z-]", "");
						System.out.println(matchup.awayTeam + " vs. " + matchup.homeTeam);
						String homeSpreadString = pickCovers.select("div.cmg_matchup_list_home_odds").text()
								.substring(0, pickCovers.select("div.cmg_matchup_list_home_odds").text().indexOf(" "));
						// accounting for even spread listed as PK ("pick em") instead of +/-0.0
						if (homeSpreadString.compareTo("PK") == 0)
						{
							homeSpreadString = "0";
						}
						matchup.homeSpread = Double.parseDouble(homeSpreadString);
						if (matchup.homeSpread == 0)
						{
							matchup.awaySpread = 0;
						}
						else
						{
							matchup.awaySpread = matchup.homeSpread * -1;
						}
						matchup.homeConsensus = Integer.parseInt(pickCovers.select("div.cmg_matchup_list_column_3")
								.select("div.cmg_matchup_list_odds_value").text().replaceAll("[^0-9]", ""));
						matchup.overUnder = Double.parseDouble(
								pickCovers.select("div.cmg_team_live_odds").text().substring(11, 11 + pickCovers
										.select("div.cmg_team_live_odds").text().substring(11).indexOf(" ")));

						try
						{
							// opening associated consensus page to get over/under consensus data
							String coversConsensusUrl = pickCovers.select("a[href^=https://contests.covers.com/consensus/]").get(0).attr("href");
							// System.out.println(coversConsensusUrl);
							Connection conCoversConsensus = Jsoup.connect(coversConsensusUrl);
							Document docCoversConsensus = conCoversConsensus.get();

							if (conCoversConsensus.response().statusCode() == 200)
							{
								matchup.overConsensus = Integer.parseInt(
										docCoversConsensus.select("div.covers-CoversConsensusDetailsTable-sideHeadLeft")
												.get(1).text().replaceAll("[^0-9]", ""));
								matchup.underConsensus = Integer.parseInt(docCoversConsensus
										.select("div.covers-CoversConsensusDetailsTable-sideHeadRight").get(1).text()
										.replaceAll("[^0-9]", ""));
							}
						}
						catch (IOException e)
						{
							System.out.println("IOException in try/catch block! #Covers1");
							System.out.println();
						}

						if (pickCovers.select("span.covers-CoversScoreboards-neutralSiteMarker").text()
								.compareTo("NEUTRAL") == 0)
						{
							matchup.isNeutral = true;
						}
						else
						{
							matchup.isNeutral = false;
						}
						matchup.gameTime = TimeConversion.formalizeTime(pickCovers.select("div.cmg_game_time").text().replaceAll("[ ET]", ""));
						coversCBBMatchups.add(matchup);
					}
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("IOException in try/catch block! #Covers2");
		}

		return coversCBBMatchups;
	}
}
