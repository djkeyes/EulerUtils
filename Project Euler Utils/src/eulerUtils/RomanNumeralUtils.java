package eulerUtils;

import java.util.HashMap;
import java.util.Map;

public class RomanNumeralUtils {

	public static Map<Character, Integer> values;
	public static Map<Integer, Character> numerals;
	static {
		values = new HashMap<Character, Integer>();
		numerals = new HashMap<Integer, Character>();
		values.put('I', 1);
		values.put('i', 1);
		numerals.put(1, 'I');
		values.put('V', 5);
		values.put('v', 5);
		numerals.put(5, 'V');
		values.put('X', 10);
		values.put('x', 10);
		numerals.put(10, 'X');
		values.put('L', 50);
		values.put('l', 50);
		numerals.put(50, 'L');
		values.put('C', 100);
		values.put('c', 100);
		numerals.put(100, 'C');
		values.put('D', 500);
		values.put('d', 500);
		numerals.put(500, 'D');
		values.put('M', 1000);
		values.put('m', 1000);
		numerals.put(1000, 'M');
	}

	// this assume the string is a valid roman numeral. if it's not, we're
	// fucked.
	public static int getValue(String numeralString) {
		int value = 0;
		int curIndex = numeralString.length() - 1;
		char highestCharacter = 'I';
		while (curIndex >= 0) {
			char curCharacter = numeralString.charAt(curIndex);
			if (values.get(curCharacter) >= values.get(highestCharacter)) {
				value += values.get(curCharacter);
				highestCharacter = curCharacter;
			} else {
				value -= values.get(curCharacter);
			}

			curIndex--;
		}

		return value;
	}

	public static String getMinimalForm(int value) {
		
		StringBuilder result = new StringBuilder();
		// roman numerals are dumb, so let's just go one value at a time
		int pow10 = 1;
		for (int i = 0; i < 4; i++, pow10 *= 10) {
			int digit = value % 10;
			value /= 10;

			// wtf
			boolean normal = false;
			boolean hasFive = false;
			switch (digit) {
			case 0:
			case 1:
			case 2:
			case 3:
				normal = true;
				break;
			case 4:
				break;
			case 5:
			case 6:
			case 7:
			case 8:
				normal = true;
				hasFive = true;
				break;
			case 9:
				hasFive = true;
				break;
			}

			if (normal) {
				int numberOfOnes = digit % 5;
				char one = numerals.get(pow10);
				for (int j = 0; j < numberOfOnes; j++) {
					result.insert(0, one);
				}
				// if it's bigger than 4000, we're fucked
				if (hasFive && pow10 != 1000) {
					char five = numerals.get(5 * pow10);
					result.insert(0, five);
				}
			} else {
				// swaparoo
				// if it's bigger than like 9000, we're fucked (again)
				if (hasFive) {
					if (pow10 != 1000) {
						char ten = numerals.get(10 * pow10);
						char one = numerals.get(pow10);
						result.insert(0, ten);
						result.insert(0, one);
					}
				} else {
					if (pow10 != 1000) {
						char five = numerals.get(5 * pow10);
						char one = numerals.get(pow10);
						result.insert(0, five);
						result.insert(0, one);
					} else {
						// 4 Ms
						char one = numerals.get(pow10);
						result.insert(0, one);
						result.insert(0, one);
						result.insert(0, one);
						result.insert(0, one);
					}
				}
			}
		}
		return result.toString();
	}

	public static void main(String[] args) {
//		for (int i = 0; i < 4999; i++) {
//			System.out.println(i + ": " + getMinimalForm(i));
//		}
		
		String[] strings = {
				"XXXXVIIII",
				"XXXXIX",
				"XLVIIII",
				"XLIX",
				"IL"
		};
		for(String s : strings){
			System.out.println(getValue(s));
		}
	}
}
