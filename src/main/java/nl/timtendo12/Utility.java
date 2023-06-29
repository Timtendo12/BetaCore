package nl.timtendo12;

import java.util.Map;
import java.util.logging.Logger;

public class Utility {
	public static String replaceColorChar(String string, char colorChar) {
		return string.replace(colorChar, '§');
	}

	public static void printMap(Map mp) {

		Logger logger = Main.getCurrentServer().getLogger();

		for (Object o : mp.entrySet()) {
			Map.Entry pair = (Map.Entry) o;
			logger.info(pair.getKey() + " = " + pair.getValue());
		}
	}
}
