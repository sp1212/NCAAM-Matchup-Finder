
public class ColorConversion
{
	public static void main(String[] args)
	{
		int r1 = 116;
		int g1 = 35;
		int b1 = 45;
		
		int r2 = 248;
		int g2 = 76;
		int b2 = 30;
		
		int r3 = 116;
		int g3 = 1;
		int b3 = 45;
		
		System.out.println(rgbToHex(r1, g1, b1));
		System.out.println(rgbToHex(r2, g2, b2));
		System.out.println(rgbToHex(r3, g3, b3));
	}
	
	// converts separate RGB values into a single 6-digit hex value
	public static String rgbToHex(int red, int green, int blue)
	{
		String mostSigTwo = String.format("%1$02X", red);
		String middleTwo = String.format("%1$02X", green);
		String leastSigTwo = String.format("%1$02X", blue);
		return mostSigTwo + middleTwo + leastSigTwo;
	}
}
