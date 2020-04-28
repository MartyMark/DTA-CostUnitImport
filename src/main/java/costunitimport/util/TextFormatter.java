package costunitimport.util;

import java.io.IOException;
import java.util.Arrays;

public class TextFormatter {
	
	/**
	 * Füllt einen String mit Leerzeichen auf eine gewünschte Länge auf.
	 * 
	 * @param value -
	 *            Der Wert, der aufgefüllt werden soll
	 * @param length -
	 *            Gewünschte Länge, auf die aufgefüllt werden soll
	 * @param alignment -
	 *            Richtung aus der aufgefüllt werden soll
	 * @return String
	 * @throws IOException
	 */
	public static String convertString(String value, int length, boolean alignment, char filler) throws IOException {
		if (value == null) {
			return getFiller(filler, length);
		}
		if (value.length() > length) {
			throw new IOException("CobolUtil.convertString : Der Text '" + value + "' ist zu lang für die Spaltelänge von " + length);
		}
		return fill(value, length, filler, alignment);
	}
	
	public static String getFiller(char c, int length) {
		final char[] chars = new char[length];
		Arrays.fill(chars, c);

		return new String(chars);
	}
	
	/**
	 * Füllt einen String mit Leerzeichen auf eine gewünschte Länge auf.
	 * 
	 * @param value -
	 *            Der Wert, der aufgefüllt werden soll
	 * @param length -
	 *            Gewünschte Länge, auf die aufgefüllt werden soll
	 * @param filler
	 * @param alignment -
	 *            Richtung aus der aufgefüllt werden soll
	 * @return String
	 */
	static String fill(String value, int length, char filler, boolean alignment) {
		String result = value;
		if (result.length() < length) {
			String fillValue = getFiller(filler, length - value.length());

			if (alignment) {
				result = value + fillValue;
			} else {
				result = fillValue + value;
			}
		}
		return result;
	}
}
