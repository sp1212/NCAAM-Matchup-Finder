import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class CBBMatchup {
	public String awayTeam;
	public String homeTeam;
	public boolean isNeutralSite;
	public int awayConsensus;
	public int homeConsensus;
	public double awaySpread;
	public double homeSpread;
	public double overUnder;
	public int overConsensus;
	public int underConsensus;
	public String coversMatchupUrlIndex;
	public ArrayList<PastGame> awayPastGames;
	public ArrayList<PastGame> homePastGames;

	public double awayWinChance;
	public double homeWinChance;
	public int awayRank;
	public int homeRank;

	public boolean cbsPickedAwayTeam;
	public boolean cbsPickedHomeTeam;
	public boolean cbsPickedOver;
	public boolean cbsPickedUnder;

	public double oddsSharkAwayScore;
	public double oddsSharkHomeScore;
	public double oddsSharkTotalScore;
	public boolean oddsSharkLikesOver;
	public boolean oddsSharkLikesUnder;
	public double oddsSharkOverUnderMargin;
	public double oddsSharkAwayCoverBy;
	public double oddsSharkHomeCoverBy;

	// index 1-358 based on team data, unique index for each team
	public int awayTeamIndex;
	public int homeTeamIndex;

	// 6-digit hex color from ESPN in format 123456
	public String awayHexColor;
	public String homeHexColor;

	public CoversCBBMatchup coversMatchup;
	public ESPNCBBMatchup espnMatchup;

	public LocalTime gameTime;

	public CBBMatchup() {
		this.espnMatchup = new ESPNCBBMatchup();
		this.awayHexColor = "000000";
		this.homeHexColor = "000000";
	}

	public String toString() {
		return "awayTeam " + awayTeam + "\n" + "homeTeam " + homeTeam + "\n" + "espnAwayTeam " + espnMatchup.awayTeam
				+ "\n" + "espnHomeTeam " + espnMatchup.homeTeam + "\n" + "awayRank " + awayRank + "\n"
				+ "homeRank " + homeRank + "\n" + "isNeutralSite " + isNeutralSite + "\n" + "awayConsensus " + awayConsensus
				+ "\n" + "homeConsensus " + homeConsensus + "\n" + "awaySpread " + awaySpread + "\n" + "homeSpread "
				+ homeSpread + "\n" + "overUnder " + overUnder + "\n" + "overConsensus " + overConsensus + "\n"
				+ "underConsensus " + underConsensus + "\n" + "awayWinChance " + awayWinChance + "\n" + "homeWinChance "
				+ homeWinChance + "\n" + "cbsPickedAwayTeam " + cbsPickedAwayTeam + "\n" + "cbsPickedHomeTeam "
				+ cbsPickedHomeTeam + "\n" + "cbsPickedOver " + cbsPickedOver + "\n" + "cbsPickedUnder "
				+ cbsPickedUnder + "\n" + "oddsSharkAwayScore " + oddsSharkAwayScore + "\n" + "oddsSharkHomeScore "
				+ oddsSharkHomeScore + "\n" + "oddsSharkAwayCoverBy " + oddsSharkAwayCoverBy + "\n"
				+ "oddsSharkHomeCoverBy " + oddsSharkHomeCoverBy + "\n" + "oddsSharkTotalScore " + oddsSharkTotalScore
				+ "\n" + "oddsSharkLikesOver " + oddsSharkLikesOver + "\n" + "oddsSharkLikesUnder "
				+ oddsSharkLikesUnder + "\n" + "oddsSharkOverUnderMargin " + oddsSharkOverUnderMargin + "\n"
				+ "coversMatchupUrlIndex " + coversMatchupUrlIndex + "\n" + "gameTime " + gameTime + "\n" + "\n";
	}

	// gathers and outputs matchup objects from scratch
	public static ArrayList<CBBMatchup> getCBBMatchups(LocalDate date) {
		LocalDate matchupDate = date;
		DecimalFormat numFormatter = new DecimalFormat("0.0");

		// read team names from file into arraylists
		ArrayList<ArrayList<String>> teamNames = ingestTeamNames();
		ArrayList<String> coversTeams = teamNames.get(0);
		ArrayList<String> cbsTeams = teamNames.get(1);
		ArrayList<String> oddsSharkTeams = teamNames.get(2);
		ArrayList<String> espnTeams = teamNames.get(3);

		// gathering data from various sources
		ArrayList<CoversCBBMatchup> coversCBBMatchups = CoversCBBMatchup.getCoversMatchups(matchupDate);
		ArrayList<OddsSharkCBBMatchup> oddsSharkCBBMatchups = OddsSharkCBBMatchup.getOddsSharkMatchups();
		ArrayList<ESPNCBBMatchup> espnCBBMatchups = ESPNCBBMatchup.getESPNMatchups(matchupDate);
		ArrayList<CBSCBBMatchup> cbsCBBMatchups = CBSCBBMatchup.getCBSMatchups(matchupDate);

		System.out.println("\n\nConsolidating matchups~~~~~~");

		// combining all separate matchup objects from various sources into singular
		// CBBMatchup objects
		// all valid matchups from Covers are used and then data from other sources is
		// matched to Covers matchups
		ArrayList<CBBMatchup> cbbMatchups = new ArrayList<CBBMatchup>(coversCBBMatchups.size());
		for (CoversCBBMatchup coversMatchup : coversCBBMatchups) {
			CBBMatchup matchup = new CBBMatchup();

			matchup.coversMatchup = coversMatchup;
			matchup.coversMatchupUrlIndex = coversMatchup.matchupUrlIndex;
			matchup.awayTeam = coversMatchup.awayTeam;
			matchup.homeTeam = coversMatchup.homeTeam;
			matchup.awayConsensus = coversMatchup.awayConsensus;
			matchup.homeConsensus = coversMatchup.homeConsensus;
			matchup.awaySpread = coversMatchup.awaySpread;
			matchup.homeSpread = coversMatchup.homeSpread;
			matchup.overUnder = coversMatchup.overUnder;
			matchup.overConsensus = coversMatchup.overConsensus;
			matchup.underConsensus = coversMatchup.underConsensus;
			matchup.isNeutralSite = coversMatchup.isNeutral;
			matchup.gameTime = coversMatchup.gameTime;
			
			matchup.awayPastGames = coversMatchup.awayPastGames;
			matchup.homePastGames = coversMatchup.homePastGames;

			matchup.awayTeamIndex = coversTeams.indexOf(matchup.awayTeam);
			matchup.homeTeamIndex = coversTeams.indexOf(matchup.homeTeam);

			String cbsAwayTeam = cbsTeams.get(matchup.awayTeamIndex);
			String cbsHomeTeam = cbsTeams.get(matchup.homeTeamIndex);
			String oddsSharkAwayTeam = oddsSharkTeams.get(matchup.awayTeamIndex);
			String oddsSharkHomeTeam = oddsSharkTeams.get(matchup.homeTeamIndex);
			String espnAwayName = espnTeams.get(matchup.awayTeamIndex);
			String espnHomeName = espnTeams.get(matchup.homeTeamIndex);

			// adding ESPN matchup data
			for (int i = 0; i < espnCBBMatchups.size(); i++) {
				ESPNCBBMatchup temp = espnCBBMatchups.get(i);
				if (temp.awayTeam.compareTo(espnAwayName) == 0) {
					matchup.espnMatchup = temp;
					matchup.awayWinChance = temp.awayWinPercentage;
					matchup.homeWinChance = temp.homeWinPercentage;
					matchup.awayRank = temp.awayRank;
					matchup.homeRank = temp.homeRank;
					if (temp.awayHexColor.length() < 6) {
						matchup.awayHexColor = "#000000";
					} else {
						matchup.awayHexColor = temp.awayHexColor;
					}
					if (temp.homeHexColor.length() < 6) {
						matchup.homeHexColor = "#000000";
					} else {
						matchup.homeHexColor = temp.homeHexColor;
					}
					espnCBBMatchups.remove(i);
					break;
				}
				// accounting for neutral site "home/away" variation between different sites
				else if (temp.awayTeam.compareTo(espnHomeName) == 0) {
					matchup.espnMatchup = temp;
					matchup.awayWinChance = temp.homeWinPercentage;
					matchup.homeWinChance = temp.awayWinPercentage;
					matchup.awayRank = temp.homeRank;
					matchup.homeRank = temp.awayRank;
					if (temp.awayHexColor.length() < 6) {
						matchup.homeHexColor = "#000000";
					} else {
						matchup.homeHexColor = temp.awayHexColor;
					}
					if (temp.homeHexColor.length() < 6) {
						matchup.awayHexColor = "#000000";
					} else {
						matchup.awayHexColor = temp.homeHexColor;
					}
					espnCBBMatchups.remove(i);
					break;
				}
			}

			// adding CBS matchup data
			for (int i = 0; i < cbsCBBMatchups.size(); i++) {
				CBSCBBMatchup temp = cbsCBBMatchups.get(i);
				if (cbsAwayTeam.compareTo(temp.teamPick) == 0) {
					matchup.cbsPickedAwayTeam = true;
					if (temp.overUnderPick.compareTo("OVER") == 0) {
						matchup.cbsPickedOver = true;
					} else if (temp.overUnderPick.compareTo("UNDER") == 0) {
						matchup.cbsPickedUnder = true;
					} else {
						matchup.cbsPickedOver = false;
						matchup.cbsPickedUnder = false;
					}
					cbsCBBMatchups.remove(i);
					break;
				} else if (cbsHomeTeam.compareTo(temp.teamPick) == 0) {
					matchup.cbsPickedHomeTeam = true;
					if (temp.overUnderPick.compareTo("OVER") == 0) {
						matchup.cbsPickedOver = true;
					} else if (temp.overUnderPick.compareTo("UNDER") == 0) {
						matchup.cbsPickedUnder = true;
					} else {
						matchup.cbsPickedOver = false;
						matchup.cbsPickedUnder = false;
					}
					cbsCBBMatchups.remove(i);
					break;
				}
			}

			// adding Odds Shark data to matchups
			for (int i = 0; i < oddsSharkCBBMatchups.size(); i++) {
				OddsSharkCBBMatchup temp = oddsSharkCBBMatchups.get(i);
				if (temp.awayTeam.compareTo(oddsSharkAwayTeam) == 0) {
					matchup.oddsSharkAwayScore = temp.awayScore;
					matchup.oddsSharkHomeScore = temp.homeScore;
					// casting and other stuff to help fix floating point addition error
					matchup.oddsSharkTotalScore = Double
							.parseDouble(numFormatter.format(temp.awayScore + temp.homeScore));
					matchup.oddsSharkOverUnderMargin = ((double) (((int) (matchup.oddsSharkTotalScore * 10))
							- ((int) (matchup.overUnder * 10)))) / 10;
					matchup.oddsSharkAwayCoverBy = ((double) (((int) (matchup.oddsSharkAwayScore * 10))
							- ((int) (matchup.oddsSharkHomeScore * 10)) + ((int) (matchup.awaySpread * 10)))) / 10;
					matchup.oddsSharkHomeCoverBy = ((double) (((int) (matchup.oddsSharkHomeScore * 10))
							- ((int) (matchup.oddsSharkAwayScore * 10)) + ((int) (matchup.homeSpread * 10)))) / 10;
					if (matchup.oddsSharkTotalScore > matchup.overUnder) {
						matchup.oddsSharkLikesOver = true;
					} else if (matchup.oddsSharkTotalScore < matchup.overUnder) {
						matchup.oddsSharkLikesUnder = true;
					}
					oddsSharkCBBMatchups.remove(i);
					break;
				}
				// accounting for neutral site "away/home" team variation between sites
				else if (temp.awayTeam.compareTo(oddsSharkHomeTeam) == 0) {
					matchup.oddsSharkAwayScore = temp.homeScore;
					matchup.oddsSharkHomeScore = temp.awayScore;
					matchup.oddsSharkTotalScore = Double
							.parseDouble(numFormatter.format(temp.awayScore + temp.homeScore));
					matchup.oddsSharkOverUnderMargin = ((double) (((int) (matchup.oddsSharkTotalScore * 10))
							- ((int) (matchup.overUnder * 10)))) / 10;
					matchup.oddsSharkAwayCoverBy = ((double) (((int) (matchup.oddsSharkAwayScore * 10))
							- ((int) (matchup.oddsSharkHomeScore * 10)) + ((int) (matchup.awaySpread * 10)))) / 10;
					matchup.oddsSharkHomeCoverBy = ((double) (((int) (matchup.oddsSharkHomeScore * 10))
							- ((int) (matchup.oddsSharkAwayScore * 10)) + ((int) (matchup.homeSpread * 10)))) / 10;
					if (matchup.oddsSharkTotalScore > matchup.overUnder) {
						matchup.oddsSharkLikesOver = true;
					} else if (matchup.oddsSharkTotalScore < matchup.overUnder) {
						matchup.oddsSharkLikesUnder = true;
					}
					oddsSharkCBBMatchups.remove(i);
					break;
				}
			}
			cbbMatchups.add(matchup);
		}
		return cbbMatchups;
	}

	// reading in team names from csv file of all team names/abbreviations from
	// various sources
	public static ArrayList<ArrayList<String>> ingestTeamNames() {
		ArrayList<ArrayList<String>> sources = new ArrayList<ArrayList<String>>(4);
		ArrayList<String> coversTeams = new ArrayList<String>(363);
		ArrayList<String> cbsTeams = new ArrayList<String>(363);
		ArrayList<String> oddsSharkTeams = new ArrayList<String>(363);
		ArrayList<String> espnTeams = new ArrayList<String>(363);

		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("rawteamnames.csv"));
			// accounting for first line metadata (source name column headings)
			scanner.nextLine();
			while (scanner.hasNextLine()) {
				String current = scanner.nextLine();
				String[] teamArr = current.split(",");
				coversTeams.add(teamArr[0]);
				cbsTeams.add(teamArr[1]);
				oddsSharkTeams.add(teamArr[2]);
				espnTeams.add(teamArr[3]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error reading team names from file.");
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		sources.add(coversTeams);
		sources.add(cbsTeams);
		sources.add(oddsSharkTeams);
		sources.add(espnTeams);

		return sources;
	}
}
