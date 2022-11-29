import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

public class HTMLFormat {
	public static void generateMatchupsHtml(ArrayList<CBBMatchup> cbbMatchups) {
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("LLL d, u");
		int numMatchups = cbbMatchups.size();
		LocalTime currentTime = LocalTime.now();
		LocalDate currentDate = LocalDate.now();

		try {
			File htmlTemplateFile = new File("outputTemplate.html");
			String htmlString = FileUtils.readFileToString(htmlTemplateFile, StandardCharsets.UTF_8);
			if (numMatchups == 1) {
				htmlString = htmlString.replace("$numMatchups", Integer.toString(numMatchups) + " matchup");
			} else {
				htmlString = htmlString.replace("$numMatchups", Integer.toString(numMatchups) + " matchups");
			}
			htmlString = htmlString.replace("$currentDate", dateFormat.format(currentDate));
			htmlString = htmlString.replace("$currentTime", timeFormat.format(currentTime));

			int middleStart = htmlString.indexOf("<!-- START -->") + 14;
			String topHtml = htmlString.substring(0, middleStart);
			int middleEnd = htmlString.indexOf("<!-- END -->");
			String bottomHtml = htmlString.substring(middleEnd);
			String middleHtml = htmlString.substring(middleStart, middleEnd);

			htmlString = topHtml;

			for (CBBMatchup matchup : cbbMatchups) {
				String matchupCard = middleHtml;
				matchupCard = matchupCard.replace("$aTeamAbbr", matchup.coversMatchup.awayTeam);
				matchupCard = matchupCard.replace("$hTeamAbbr", matchup.coversMatchup.homeTeam);
				matchupCard = matchupCard.replace("$awayTeamFull", matchup.espnMatchup.awayTeam);
				matchupCard = matchupCard.replace("$homeTeamFull", matchup.espnMatchup.homeTeam);
				matchupCard = matchupCard.replace("$awayWinChance", Double.toString(matchup.awayWinChance));
				matchupCard = matchupCard.replace("$homeWinChance", Double.toString(matchup.homeWinChance));
				matchupCard = matchupCard.replace("$awayConsensus", Integer.toString(matchup.awayConsensus));
				matchupCard = matchupCard.replace("$homeConsensus", Integer.toString(matchup.homeConsensus));
				matchupCard = matchupCard.replace("$ovUn", Double.toString(matchup.overUnder));
				matchupCard = matchupCard.replace("$predictedAwayScore", Double.toString(matchup.oddsSharkAwayScore));
				matchupCard = matchupCard.replace("$predictedHomeScore", Double.toString(matchup.oddsSharkHomeScore));
				matchupCard = matchupCard.replace("$predictedAwayCover", Double.toString(matchup.oddsSharkAwayCoverBy));
				matchupCard = matchupCard.replace("$predictedHomeCover", Double.toString(matchup.oddsSharkHomeCoverBy));
				matchupCard = matchupCard.replace("$overConsensus", Integer.toString(matchup.overConsensus));
				matchupCard = matchupCard.replace("$underConsensus", Integer.toString(matchup.underConsensus));
				matchupCard = matchupCard.replace("$cbsPickedOver", Boolean.toString(matchup.cbsPickedOver));
				matchupCard = matchupCard.replace("$cbsPickedUnder", Boolean.toString(matchup.cbsPickedUnder));
				matchupCard = matchupCard.replace("$gameTime", timeFormat.format(matchup.gameTime));
				matchupCard = matchupCard.replace("$awayTeamUrlAbbr", matchup.coversMatchup.awayTeam.toLowerCase());
				matchupCard = matchupCard.replace("$homeTeamUrlAbbr", matchup.coversMatchup.homeTeam.toLowerCase());
				matchupCard = matchupCard.replace("$awayHexColor", matchup.awayHexColor);
				matchupCard = matchupCard.replace("$homeHexColor", matchup.homeHexColor);
				matchupCard = matchupCard.replace("$matchupUrlIndex", matchup.coversMatchupUrlIndex);
				matchupCard = matchupCard.replace("$espnMatchupUrl", matchup.espnMatchup.matchupUrl);
				
				matchupCard = matchupCard.replace("$ad1", matchup.awayPastGames.get(0).date);
				matchupCard = matchupCard.replace("$ad2", matchup.awayPastGames.get(1).date);
				matchupCard = matchupCard.replace("$ad3", matchup.awayPastGames.get(2).date);
				matchupCard = matchupCard.replace("$ad4", matchup.awayPastGames.get(3).date);
				matchupCard = matchupCard.replace("$ad5", matchup.awayPastGames.get(4).date);
				
				matchupCard = matchupCard.replace("$ao1", matchup.awayPastGames.get(0).team);
				matchupCard = matchupCard.replace("$ao2", matchup.awayPastGames.get(1).team);
				matchupCard = matchupCard.replace("$ao3", matchup.awayPastGames.get(2).team);
				matchupCard = matchupCard.replace("$ao4", matchup.awayPastGames.get(3).team);
				matchupCard = matchupCard.replace("$ao5", matchup.awayPastGames.get(4).team);
				
				matchupCard = matchupCard.replace("$ar1", matchup.awayPastGames.get(0).scoreString);
				matchupCard = matchupCard.replace("$ar2", matchup.awayPastGames.get(1).scoreString);
				matchupCard = matchupCard.replace("$ar3", matchup.awayPastGames.get(2).scoreString);
				matchupCard = matchupCard.replace("$ar4", matchup.awayPastGames.get(3).scoreString);
				matchupCard = matchupCard.replace("$ar5", matchup.awayPastGames.get(4).scoreString);
				
				matchupCard = matchupCard.replace("$aats1", matchup.awayPastGames.get(0).atsMargin);
				matchupCard = matchupCard.replace("$aats2", matchup.awayPastGames.get(1).atsMargin);
				matchupCard = matchupCard.replace("$aats3", matchup.awayPastGames.get(2).atsMargin);
				matchupCard = matchupCard.replace("$aats4", matchup.awayPastGames.get(3).atsMargin);
				matchupCard = matchupCard.replace("$aats5", matchup.awayPastGames.get(4).atsMargin);
				
				matchupCard = matchupCard.replace("$aou1", matchup.awayPastGames.get(0).ouMargin);
				matchupCard = matchupCard.replace("$aou2", matchup.awayPastGames.get(1).ouMargin);
				matchupCard = matchupCard.replace("$aou3", matchup.awayPastGames.get(2).ouMargin);
				matchupCard = matchupCard.replace("$aou4", matchup.awayPastGames.get(3).ouMargin);
				matchupCard = matchupCard.replace("$aou5", matchup.awayPastGames.get(4).ouMargin);
				
				matchupCard = matchupCard.replace("$ap1", Integer.toString(matchup.awayPastGames.get(0).totalPoints));
				matchupCard = matchupCard.replace("$ap2", Integer.toString(matchup.awayPastGames.get(1).totalPoints));
				matchupCard = matchupCard.replace("$ap3", Integer.toString(matchup.awayPastGames.get(2).totalPoints));
				matchupCard = matchupCard.replace("$ap4", Integer.toString(matchup.awayPastGames.get(3).totalPoints));
				matchupCard = matchupCard.replace("$ap5", Integer.toString(matchup.awayPastGames.get(4).totalPoints));
				
				matchupCard = matchupCard.replace("$hd1", matchup.homePastGames.get(0).date);
				matchupCard = matchupCard.replace("$hd2", matchup.homePastGames.get(1).date);
				matchupCard = matchupCard.replace("$hd3", matchup.homePastGames.get(2).date);
				matchupCard = matchupCard.replace("$hd4", matchup.homePastGames.get(3).date);
				matchupCard = matchupCard.replace("$hd5", matchup.homePastGames.get(4).date);
				
				matchupCard = matchupCard.replace("$ho1", matchup.homePastGames.get(0).team);
				matchupCard = matchupCard.replace("$ho2", matchup.homePastGames.get(1).team);
				matchupCard = matchupCard.replace("$ho3", matchup.homePastGames.get(2).team);
				matchupCard = matchupCard.replace("$ho4", matchup.homePastGames.get(3).team);
				matchupCard = matchupCard.replace("$ho5", matchup.homePastGames.get(4).team);
				
				matchupCard = matchupCard.replace("$hr1", matchup.homePastGames.get(0).scoreString);
				matchupCard = matchupCard.replace("$hr2", matchup.homePastGames.get(1).scoreString);
				matchupCard = matchupCard.replace("$hr3", matchup.homePastGames.get(2).scoreString);
				matchupCard = matchupCard.replace("$hr4", matchup.homePastGames.get(3).scoreString);
				matchupCard = matchupCard.replace("$hr5", matchup.homePastGames.get(4).scoreString);
				
				matchupCard = matchupCard.replace("$hats1", matchup.homePastGames.get(0).atsMargin);
				matchupCard = matchupCard.replace("$hats2", matchup.homePastGames.get(1).atsMargin);
				matchupCard = matchupCard.replace("$hats3", matchup.homePastGames.get(2).atsMargin);
				matchupCard = matchupCard.replace("$hats4", matchup.homePastGames.get(3).atsMargin);
				matchupCard = matchupCard.replace("$hats5", matchup.homePastGames.get(4).atsMargin);
				
				matchupCard = matchupCard.replace("$hou1", matchup.homePastGames.get(0).ouMargin);
				matchupCard = matchupCard.replace("$hou2", matchup.homePastGames.get(1).ouMargin);
				matchupCard = matchupCard.replace("$hou3", matchup.homePastGames.get(2).ouMargin);
				matchupCard = matchupCard.replace("$hou4", matchup.homePastGames.get(3).ouMargin);
				matchupCard = matchupCard.replace("$hou5", matchup.homePastGames.get(4).ouMargin);
				
				matchupCard = matchupCard.replace("$hp1", Integer.toString(matchup.homePastGames.get(0).totalPoints));
				matchupCard = matchupCard.replace("$hp2", Integer.toString(matchup.homePastGames.get(1).totalPoints));
				matchupCard = matchupCard.replace("$hp3", Integer.toString(matchup.homePastGames.get(2).totalPoints));
				matchupCard = matchupCard.replace("$hp4", Integer.toString(matchup.homePastGames.get(3).totalPoints));
				matchupCard = matchupCard.replace("$hp5", Integer.toString(matchup.homePastGames.get(4).totalPoints));

				if (matchup.oddsSharkLikesOver == true) {
					matchupCard = matchupCard.replace(" $oddsSharkUnderAnimated", "");
					matchupCard = matchupCard.replace("$oddsSharkOverAnimated",
							"progress-bar-striped progress-bar-animated");
					matchupCard = matchupCard.replace("$oddsSharkUnderWidth", "0");
					matchupCard = matchupCard.replace("$oddsSharkOverWidth", "100");
					matchupCard = matchupCard.replace("$oddsSharkUnderLabel", "");
					matchupCard = matchupCard.replace("$oddsSharkOverLabel",
							"Over by " + matchup.oddsSharkOverUnderMargin);
				}
				else if (matchup.oddsSharkLikesUnder == true) {
					matchupCard = matchupCard.replace("$oddsSharkUnderAnimated",
							"progress-bar-striped progress-bar-animated");
					matchupCard = matchupCard.replace(" $oddsSharkOverAnimated", "");
					matchupCard = matchupCard.replace("$oddsSharkUnderWidth", "100");
					matchupCard = matchupCard.replace("$oddsSharkOverWidth", "0");
					matchupCard = matchupCard.replace("$oddsSharkUnderLabel",
							"Under by " + matchup.oddsSharkOverUnderMargin);
					matchupCard = matchupCard.replace("$oddsSharkOverLabel", "");
				}
				else {
					matchupCard = matchupCard.replace(" $oddsSharkUnderAnimated", "");
					matchupCard = matchupCard.replace(" $oddsSharkOverAnimated", "");
					matchupCard = matchupCard.replace("$oddsSharkUnderWidth", "0");
					matchupCard = matchupCard.replace("$oddsSharkOverWidth", "0");
					matchupCard = matchupCard.replace("$oddsSharkUnderLabel", "");
					matchupCard = matchupCard.replace("$oddsSharkOverLabel", "");
				}

				if (matchup.cbsPickedOver == true) {
					matchupCard = matchupCard.replace(" $cbsUnderAnimated", "");
					matchupCard = matchupCard.replace("$cbsOverAnimated", "progress-bar-striped progress-bar-animated");
					matchupCard = matchupCard.replace("$cbsUnderWidth", "0");
					matchupCard = matchupCard.replace("$cbsOverWidth", "100");
					matchupCard = matchupCard.replace("$cbsUnderLabel", "");
					matchupCard = matchupCard.replace("$cbsOverLabel", "CBS Picked");
				}
				else if (matchup.cbsPickedUnder == true) {
					matchupCard = matchupCard.replace("$cbsUnderAnimated",
							"progress-bar-striped progress-bar-animated");
					matchupCard = matchupCard.replace(" $cbsOverAnimated", "");
					matchupCard = matchupCard.replace("$cbsUnderWidth", "100");
					matchupCard = matchupCard.replace("$cbsOverWidth", "0");
					matchupCard = matchupCard.replace("$cbsUnderLabel", "CBS Picked");
					matchupCard = matchupCard.replace("$cbsOverLabel", "");
				}
				else {
					matchupCard = matchupCard.replace(" $cbsUnderAnimated", "");
					matchupCard = matchupCard.replace(" $cbsOverAnimated", "");
					matchupCard = matchupCard.replace("$cbsUnderWidth", "0");
					matchupCard = matchupCard.replace("$cbsOverWidth", "0");
					matchupCard = matchupCard.replace("$cbsUnderLabel", "");
					matchupCard = matchupCard.replace("$cbsOverLabel", "");
				}

				if (matchup.overConsensus > matchup.underConsensus) {
					matchupCard = matchupCard.replace("$coversOverAnimated",
							"progress-bar-striped progress-bar-animated");
					matchupCard = matchupCard.replace(" $coversUnderAnimated", "");
				}
				else if (matchup.underConsensus > matchup.overConsensus) {
					matchupCard = matchupCard.replace("$coversUnderAnimated",
							"progress-bar-striped progress-bar-animated");
					matchupCard = matchupCard.replace(" $coversOverAnimated", "");
				}
				else {
					matchupCard = matchupCard.replace(" $coversOverAnimated", "");
					matchupCard = matchupCard.replace(" $coversUnderAnimated", "");
				}

				if (matchup.oddsSharkAwayCoverBy > 0) {
					matchupCard = matchupCard.replace("$oddsSharkAwayCoverWidth", "100");
					matchupCard = matchupCard.replace("$oddsSharkHomeCoverWidth", "0");
				}
				else if (matchup.oddsSharkHomeCoverBy > 0) {
					matchupCard = matchupCard.replace("$oddsSharkAwayCoverWidth", "0");
					matchupCard = matchupCard.replace("$oddsSharkHomeCoverWidth", "100");
				}
				else {
					matchupCard = matchupCard.replace("$oddsSharkAwayCoverWidth", "0");
					matchupCard = matchupCard.replace("$oddsSharkHomeCoverWidth", "0");
				}

				if (matchup.cbsPickedAwayTeam == true) {
					matchupCard = matchupCard.replace("$cbsAwayPickWidth", "100");
					matchupCard = matchupCard.replace("$cbsHomePickWidth", "0");
				}
				else if (matchup.cbsPickedHomeTeam == true) {
					matchupCard = matchupCard.replace("$cbsAwayPickWidth", "0");
					matchupCard = matchupCard.replace("$cbsHomePickWidth", "100");
				}
				else {
					matchupCard = matchupCard.replace("$cbsAwayPickWidth", "0");
					matchupCard = matchupCard.replace("$cbsHomePickWidth", "0");
				}

				if (matchup.homeConsensus >= 65) {
					matchupCard = matchupCard.replace("$homeCovBarAnimated",
							" progress-bar-striped progress-bar-animated");
					matchupCard = matchupCard.replace("$awayCovBarAnimated", "");
				}
				else if (matchup.awayConsensus >= 65) {
					matchupCard = matchupCard.replace("$awayCovBarAnimated",
							" progress-bar-striped progress-bar-animated");
					matchupCard = matchupCard.replace("$homeCovBarAnimated", "");
				}
				else {
					matchupCard = matchupCard.replace("$awayCovBarAnimated", "");
					matchupCard = matchupCard.replace("$homeCovBarAnimated", "");
				}

				if (matchup.awaySpread > 0) {
					matchupCard = matchupCard.replace("$aSpread", "+" + Double.toString(matchup.awaySpread));
				}
				else {
					matchupCard = matchupCard.replace("$aSpread", Double.toString(matchup.awaySpread));
				}

				if (matchup.homeSpread > 0) {
					matchupCard = matchupCard.replace("$hSpread", "+" + Double.toString(matchup.homeSpread));
				}
				else {
					matchupCard = matchupCard.replace("$hSpread", Double.toString(matchup.homeSpread));
				}

				if (matchup.isNeutralSite == true) {
					matchupCard = matchupCard.replace("$atVs", "NEUTRAL");
					matchupCard = matchupCard.replace("$neutralColor", "#FFFFE0");
				}
				else {
					matchupCard = matchupCard.replace("$atVs", "at");
					matchupCard = matchupCard.replace("$neutralColor", "#FFFFFF");
				}
				
				if (matchup.awayRank > 0) {
					matchupCard = matchupCard.replace("$aRank", "<small>" + matchup.awayRank + " </small>");
				}
				else {
					matchupCard = matchupCard.replace("$aRank", "");
				}
				if (matchup.homeRank > 0) {
					matchupCard = matchupCard.replace("$hRank", "<small>" + matchup.homeRank + " </small>");
				}
				else {
					matchupCard = matchupCard.replace("$hRank", "");
				}
				
				htmlString += matchupCard;
			}

			htmlString += bottomHtml;
			
			htmlString = htmlString.replace("<!-- START -->", "");
			htmlString = htmlString.replace("<!-- END -->", "");

			File newHtmlFile = new File("outputfile.html");
			FileUtils.writeStringToFile(newHtmlFile, htmlString, StandardCharsets.UTF_8);

			System.out.println("HTML output completed.");

			URI fileUri = newHtmlFile.toURI();
			System.out.println(fileUri);

			class OpenListener implements ActionListener {
				public void actionPerformed(ActionEvent event) {
					System.out.println("Opening file. . .");
					Desktop desktop = Desktop.getDesktop();
					try {
						desktop.browse(fileUri);
						System.exit(0);
					}
					catch (IOException e) {
						System.out.println("Error opening file.");
						e.printStackTrace();
					}
				}
			}

			class CloseListener implements ActionListener {
				public void actionPerformed(ActionEvent event) {
					System.out.println("Closing window. . .");
					System.exit(0);
				}
			}

			final int FRAME_WIDTH = 400;
			final int FRAME_HEIGHT = 140;

			JFrame frame = new JFrame();
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setVgap(22);
			borderLayout.setHgap(12);
			JPanel panel = new JPanel(borderLayout);
			panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
			panel.setOpaque(true);
			panel.setBackground(Color.LIGHT_GRAY);

			JLabel label = new JLabel();
			label.setText("Completed.  Found " + numMatchups + " matchups.");
			label.setFont(new Font("SansSerif", Font.PLAIN, 18));
			label.setHorizontalAlignment(JLabel.CENTER);
			panel.add(label, BorderLayout.PAGE_START);

			JButton viewButton = new JButton("View Matchups");
			viewButton.setPreferredSize(new Dimension(160, 40));
			viewButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
			viewButton.setFocusPainted(false);
			viewButton.setContentAreaFilled(false);
			viewButton.setOpaque(true);
			viewButton.setBackground(Color.WHITE);
			panel.add(viewButton, BorderLayout.LINE_START);
			JButton okButton = new JButton("OK");
			okButton.setPreferredSize(new Dimension(160, 40));
			okButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
			okButton.setFocusPainted(false);
			okButton.setContentAreaFilled(false);
			okButton.setOpaque(true);
			okButton.setBackground(Color.WHITE);
			panel.add(okButton, BorderLayout.LINE_END);

			frame.add(panel);

			viewButton.addActionListener(new OpenListener());
			okButton.addActionListener(new CloseListener());

			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setAlwaysOnTop(true);
			frame.setTitle("NCAAM Matchups");

			URL frameIconUrl = new URL(
					"https://files.softicons.com/download/sport-icons/sports-icons-by-martin-berube/png/16x16/Basketball.png");
			BufferedImage frameIcon = ImageIO.read(frameIconUrl);
			frame.setIconImage(frameIcon);

			frame.setVisible(true);

			return;
		}
		catch (IOException e) {
			System.out.println("Exception occurred when writing to HTML.");
		}
	}
}
