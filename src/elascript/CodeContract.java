package elascript;

public final class CodeContract {
	public static void requiresArgumentNotNull(Object argValue, String argName) {
		if (argValue == null)
			throw new IllegalArgumentException(argName + " cannot be null.");
	}

	public static void requiresArgumentNotNull(String argValue, String argName) {
		if (argValue == null || argValue.isEmpty())
			throw new IllegalArgumentException(argName + " cannot be null or empty.");
	}

	public static void requires(boolean condition, String message) {
		if (!condition)
			throw new IllegalArgumentException(message);
	}
}
