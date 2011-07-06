package elascript.interpreter;

public class WhiteListChecker extends SecurityCheckerBase {
	@Override
	protected boolean checkInternal(Class<?> type) {
		return rules.contains(type);
	}
}
