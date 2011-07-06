package elascript;

import java.util.*;

public final class Utils {
	public static <T> List<T> makeReadOnlyCopy(List<T> source) {
		ArrayList<T> copy;
		if (source == null)
			copy = new ArrayList<T>();
		else
			copy = new ArrayList<T>(source);
		return Collections.unmodifiableList(copy);
	}
}
