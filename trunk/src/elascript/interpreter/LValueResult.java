package elascript.interpreter;

public abstract class LValueResult extends ValueResult {
	public abstract void setValue(Object value) throws AccessException;
}
