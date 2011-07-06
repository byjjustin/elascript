package elascript.interpreter;

import java.lang.reflect.*;

import elascript.*;

public class FieldResult extends LValueResult {
	private Field field;
	private Object instance;

	public FieldResult(Field field, Object instance) {
		CodeContract.requiresArgumentNotNull(field, "field");
		this.field = field;
		this.instance = instance;
	}

	@Override
	public void setValue(Object value) throws AccessException {
		try {
			field.set(instance, value);
		} catch (Exception e) {
			throw new AccessException(e.getMessage());
		}
	}

	@Override
	public Object getValue() throws AccessException {
		try {
			return field.get(instance);
		} catch (Exception e) {
			throw new AccessException(e.getMessage());
		}
	}
}
