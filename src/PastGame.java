
public class PastGame {
	public String team;
	public String logoUrl;
	public String date;
	public String scoreString;
	public String atsMargin;
	public String ouMargin;
	public int totalPoints;

	public PastGame() {
		this.team = "---";
		this.logoUrl = "";
		this.date = "---";
		this.scoreString = "---";
		this.atsMargin = "---";
		this.ouMargin = "---";
		this.totalPoints = 0;
	}

	public String toString() {
		return this.date + " " + this.logoUrl + " " + this.team + " " + this.scoreString + " " + this.atsMargin + " "
				+ this.ouMargin + " " + this.totalPoints + "\n";
	}
}
