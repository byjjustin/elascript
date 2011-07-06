package elascript.interpreter;

public abstract class Result {
	public ValueResult asValueResult() throws TypeDismatchException {
		if (this instanceof ValueResult)
			return (ValueResult) this;
		throw new TypeDismatchException("value");
	}

	public LValueResult asLValueResult() throws TypeDismatchException {
		if (this instanceof LValueResult)
			return (LValueResult) this;
		throw new TypeDismatchException("lvalue");
	}

	public Class<?> asType() throws TypeDismatchException {
		if (this instanceof TypeResult)
			return ((TypeResult) this).getType();
		throw new TypeDismatchException("type");
	}

	public Object asValue() throws TypeDismatchException, AccessException {
		return asValueResult().getValue();
	}

	public Number asNumber() throws TypeDismatchException, AccessException {
		Number value = TypeHelper.tryToNumber(asValue());
		if (value == null)
			throw new TypeDismatchException("Numeric value");
		return value;
	}

	public boolean asBoolean() throws TypeDismatchException, AccessException {
		return TypeHelper.numberToBoolean(asNumber());
	}
}
