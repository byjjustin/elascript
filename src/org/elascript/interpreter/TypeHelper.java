package org.elascript.interpreter;

import java.lang.reflect.*;

final class TypeHelper {
	public static Number tryToNumber(Object value) {
		if (value instanceof Number)
			return (Number) value;
		if (value instanceof Character)
			return (int) ((Character) value).charValue();
		if (value instanceof Boolean) {
			boolean boolValue = (Boolean) value;
			return boolValue ? 1 : 0;
		}
		return null;
	}

	public static boolean numberToBoolean(Number value) {
		if (value instanceof Double || value instanceof Float)
			return value.doubleValue() != 0;
		if (value instanceof Long)
			return value.longValue() != 0;
		return value.intValue() != 0;
	}

	public static Constructor<?> matchConstructor(Class<?> type, Object[] args)
			throws FunctionCallException {
		Constructor<?> matchedCtor = null;
		Class<?> argTypes[] = getTypes(args);
		try {
			matchedCtor = type.getConstructor(argTypes);
		} catch (NoSuchMethodException e) {
		}
		if (matchedCtor == null)
			for (Constructor<?> ctor : type.getConstructors())
				if (ctor.getParameterTypes().length == args.length
						&& checkArgumentTypes(ctor.getParameterTypes(),
								argTypes))
					if (matchedCtor != null)
						throw new FunctionCallException(
								"More than one matched constructors.");
					else
						matchedCtor = ctor;
		if (matchedCtor == null)
			throw new FunctionCallException("No matched constructors.");
		return matchedCtor;
	}

	public static Method matchMethod(Class<?> type, String target, Object[] args)
			throws FunctionCallException {
		Method matchedMethod = null;
		Class<?> argTypes[] = getTypes(args);
		try {
			matchedMethod = type.getMethod(target, argTypes);
		} catch (Exception e) {
		}
		if (matchedMethod == null)
			for (Method method : type.getMethods())
				if (method.getName().equals(target)
						&& method.getParameterTypes().length == args.length
						&& checkArgumentTypes(method.getParameterTypes(),
								argTypes))
					if (matchedMethod != null)
						throw new FunctionCallException(
								"More than one matched methods.");
					else
						matchedMethod = method;
		if (matchedMethod == null)
			throw new FunctionCallException("No matched methods.");
		return matchedMethod;
	}

	private static Class<?>[] getTypes(Object[] args) {
		Class<?>[] types = new Class<?>[args.length];
		for (int i = 0; i < args.length; ++i) {
			if (args[i] instanceof Integer)
				types[i] = int.class;
			else if (args[i] instanceof Long)
				types[i] = long.class;
			else if (args[i] instanceof Short)
				types[i] = short.class;
			else if (args[i] instanceof Byte)
				types[i] = byte.class;
			else if (args[i] instanceof Float)
				types[i] = float.class;
			else if (args[i] instanceof Double)
				types[i] = double.class;
			else if (args[i] instanceof Character)
				types[i] = char.class;
			else if (args[i] instanceof Boolean)
				types[i] = boolean.class;
			else if (args[i] instanceof Void)
				types[i] = void.class;
			else
				types[i] = args[i].getClass();
		}
		return types;
	}

	private static boolean checkArgumentTypes(Class<?>[] paramTypes,
			Class<?>[] argTypes) {
		for (int i = 0; i < paramTypes.length; ++i)
			if (!paramTypes[i].isAssignableFrom(argTypes[i]))
				return false;
		return true;
	}
}
