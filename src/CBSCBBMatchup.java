import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CBSCBBMatchup
{
	public String teamPick;
	public String overUnderPick;

	// default constructor
	public CBSCBBMatchup()
	{

	}

	public String getTeamPick()
	{
		return this.teamPick;
	}

	public String getOverUnderPick()
	{
		return this.overUnderPick;
	}

	public String toString()
	{
		return "\n" + teamPick + " " + overUnderPick;
	}
	
	// goes to CBS website and gathers data
	public static ArrayList<CBSCBBMatchup> getCBSMatchups(LocalDate matchupDate)
	{
		LocalDate date = matchupDate;
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyyMMdd");
		String cbsDate = date.format(myFormatObj);

		ArrayList<CBSCBBMatchup> cbsMatchups = new ArrayList<CBSCBBMatchup>();

		String cbsUrl = "https://www.cbssports.com/college-basketball/expert-picks/" + cbsDate + "/";
		try
		{
			Connection con = Jsoup.connect(cbsUrl);
			Document doc = con.get();

			if (con.response().statusCode() == 200)
			{
				System.out.println("Link:  " + cbsUrl);
				System.out.println(doc.title() + "\n");

				Elements picks = doc.select("div.expert-picks-col");
				for (Element pick : picks)
				{
					CBSCBBMatchup cbsMatchup = new CBSCBBMatchup();
					// accounting for pick given as "TBD"
					if (pick.text().indexOf(" ") != -1)
					{
						cbsMatchup.teamPick = pick.text().substring(0, pick.text().indexOf(" "));
					}
					else
					{
						cbsMatchup.teamPick = "N/A";
					}
					if (pick.select("span.pick-over").hasText() != false)
					{
						cbsMatchup.overUnderPick = "OVER";
					}
					else if (pick.select("span.pick-under").hasText() != false)
					{
						cbsMatchup.overUnderPick = "UNDER";
					}
					else
					{
						cbsMatchup.overUnderPick = "N/A";
					}
					cbsMatchups.add(cbsMatchup);
					System.out.println(cbsMatchup.teamPick + " " + cbsMatchup.overUnderPick);
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("IOException in try/catch block!");
		}
		return cbsMatchups;
	}
}
