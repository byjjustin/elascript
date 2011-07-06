package elascript.parser;

import elascript.*;

public class Token {
	private String value;

	public Token(String value) {
		CodeContract.requiresArgumentNotNull(value, "value");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Token))
			return false;
		Token that = (Token) obj;
		return value.equals(that.value);
	}
}
