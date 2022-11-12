import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeConversion {
	public static void main(String[] args) {
		String test1 = "8:00PM";
		String test2 = "11:15PM";
		String test3 = "9:30AM";
		String test4 = "10:45AM";
		String test5 = "12:15AM";
		String test6 = "12:15PM";
		String test7 = "12:00PM";

		System.out.println(formalizeTime(test1));
		System.out.println(formalizeTime(test2));
		System.out.println(formalizeTime(test3));
		System.out.println(formalizeTime(test4));
		System.out.println(formalizeTime(test5));
		System.out.println(formalizeTime(test6));
		System.out.println(formalizeTime(test7));

		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm a");
		System.out.println(timeFormat.format(formalizeTime(test7)));

		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("LLL d, u");
		System.out.println(dateFormat.format(LocalDate.now()));
	}

	public static LocalTime formalizeTime(String inputTime) {
		boolean isPm = inputTime.contains("PM");
		int hours = Integer.parseInt(inputTime.substring(0, inputTime.indexOf(":")));
		int minutes = Integer.parseInt(inputTime.substring(inputTime.indexOf(":") + 1).replaceAll("[^0-9]", ""));
		if (isPm && hours != 12) {
			hours += 12;
		}
		if (!isPm && hours == 12) {
			hours = 0;
		}
		LocalTime output = LocalTime.of(hours, minutes);
		return output;
	}
}
