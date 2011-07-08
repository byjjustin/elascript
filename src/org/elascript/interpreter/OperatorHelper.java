package org.elascript.interpreter;

import org.elascript.*;
import org.elascript.ast.*;

final class OperatorHelper {
	public static Number add(Number left, Number right) {
		if (left instanceof Double || right instanceof Double)
			return left.doubleValue() + right.doubleValue();
		if (left instanceof Float || right instanceof Float)
			return left.floatValue() + right.floatValue();
		if (left instanceof Long || right instanceof Long)
			return left.longValue() + right.longValue();
		return left.intValue() + right.intValue();
	}

	public static Number subtract(Number left, Number right) {
		if (left instanceof Double || right instanceof Double)
			return left.doubleValue() - right.doubleValue();
		if (left instanceof Float || right instanceof Float)
			return left.floatValue() - right.floatValue();
		if (left instanceof Long || right instanceof Long)
			return left.longValue() - right.longValue();
		return left.intValue() - right.intValue();
	}

	public static Number multiply(Number left, Number right) {
		if (left instanceof Double || right instanceof Double)
			return left.doubleValue() * right.doubleValue();
		if (left instanceof Float || right instanceof Float)
			return left.floatValue() * right.floatValue();
		if (left instanceof Long || right instanceof Long)
			return left.longValue() * right.longValue();
		return left.intValue() * right.intValue();
	}

	public static Number divide(Number left, Number right) {
		if (left instanceof Double || right instanceof Double)
			return left.doubleValue() / right.doubleValue();
		if (left instanceof Float || right instanceof Float)
			return left.floatValue() / right.floatValue();
		if (left instanceof Long || right instanceof Long)
			return left.longValue() / right.longValue();
		return left.intValue() / right.intValue();
	}

	public static Number modulo(Number left, Number right)
			throws TypeDismatchException {
		checkInteger(left);
		checkInteger(right);
		if (left instanceof Long || right instanceof Long)
			return left.longValue() % right.longValue();
		return left.intValue() % right.intValue();
	}

	public static Number bitwiseAnd(Number left, Number right)
			throws TypeDismatchException {
		checkInteger(left);
		checkInteger(right);
		if (left instanceof Long || right instanceof Long)
			return left.longValue() & right.longValue();
		return left.intValue() & right.intValue();
	}

	public static Number bitwiseOr(Number left, Number right)
			throws TypeDismatchException {
		checkInteger(left);
		checkInteger(right);
		if (left instanceof Long || right instanceof Long)
			return left.longValue() | right.longValue();
		return left.intValue() | right.intValue();
	}

	public static Number exclusiveOr(Number left, Number right)
			throws TypeDismatchException {
		checkInteger(left);
		checkInteger(right);
		if (left instanceof Long || right instanceof Long)
			return left.longValue() ^ right.longValue();
		return left.intValue() ^ right.intValue();
	}

	public static Number bitwiseComplement(Number operand)
			throws TypeDismatchException {
		checkInteger(operand);
		if (operand instanceof Long)
			return ~operand.longValue();
		return ~operand.intValue();
	}

	public static Number leftShift(Number left, Number right)
			throws TypeDismatchException {
		checkInteger(left);
		checkInteger(right);
		if (left instanceof Long || right instanceof Long)
			return left.longValue() << right.longValue();
		return left.intValue() << right.intValue();
	}

	public static Number rightShift(Number left, Number right)
			throws TypeDismatchException {
		checkInteger(left);
		checkInteger(right);
		if (left instanceof Long || right instanceof Long)
			return left.longValue() >> right.longValue();
		return left.intValue() >> right.intValue();
	}

	public static Number unsignedRightShift(Number left, Number right)
			throws TypeDismatchException {
		checkInteger(left);
		checkInteger(right);
		if (left instanceof Long || right instanceof Long)
			return left.longValue() >>> right.longValue();
		return left.intValue() >>> right.intValue();
	}

	public static boolean equal(Object left, Object right) {
		Number leftNumber = TypeHelper.tryToNumber(left);
		Number rightNumber = TypeHelper.tryToNumber(right);
		if (leftNumber != null && rightNumber != null) {
			if (leftNumber instanceof Double || rightNumber instanceof Double)
				return leftNumber.doubleValue() == rightNumber.doubleValue();
			if (leftNumber instanceof Float || rightNumber instanceof Float)
				return leftNumber.floatValue() == rightNumber.floatValue();
			if (leftNumber instanceof Long || rightNumber instanceof Long)
				return leftNumber.longValue() == rightNumber.longValue();
			return leftNumber.intValue() == rightNumber.intValue();
		}
		return left == right;
	}

	public static boolean greater(Number left, Number right) {
		if (left instanceof Double || right instanceof Double)
			return left.doubleValue() > right.doubleValue();
		if (left instanceof Float || right instanceof Float)
			return left.floatValue() > right.floatValue();
		if (left instanceof Long || right instanceof Long)
			return left.longValue() > right.longValue();
		return left.intValue() > right.intValue();
	}

	public static boolean less(Number left, Number right)
			throws TypeDismatchException {
		if (left instanceof Double || right instanceof Double)
			return left.doubleValue() < right.doubleValue();
		if (left instanceof Float || right instanceof Float)
			return left.floatValue() < right.floatValue();
		if (left instanceof Long || right instanceof Long)
			return left.longValue() < right.longValue();
		return left.intValue() < right.intValue();
	}

	public static LValueResult assign(LValueResult left, ValueResult right)
			throws AccessException, TypeDismatchException {
		left.setValue(right.asValue());
		return left;
	}

	public static LValueResult addAssign(LValueResult left, ValueResult right)
			throws AccessException, TypeDismatchException {
		left.setValue(add(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult subtractAssign(LValueResult left,
			ValueResult right) throws AccessException, TypeDismatchException {
		left.setValue(subtract(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult multiplyAssign(LValueResult left,
			ValueResult right) throws AccessException, TypeDismatchException {
		left.setValue(multiply(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult divideAssign(LValueResult left, ValueResult right)
			throws AccessException, TypeDismatchException {
		left.setValue(divide(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult moduloAssign(LValueResult left, ValueResult right)
			throws TypeDismatchException, AccessException {
		left.setValue(modulo(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult bitwiseAndAssign(LValueResult left,
			ValueResult right) throws TypeDismatchException, AccessException {
		left.setValue(bitwiseAnd(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult bitwiseOrAssign(LValueResult left,
			ValueResult right) throws TypeDismatchException, AccessException {
		left.setValue(bitwiseOr(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult exclusiveOrAssign(LValueResult left,
			ValueResult right) throws TypeDismatchException, AccessException {
		left.setValue(exclusiveOr(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult leftShiftAssign(LValueResult left,
			ValueResult right) throws TypeDismatchException, AccessException {
		left.setValue(leftShift(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult rightShiftAssign(LValueResult left,
			ValueResult right) throws TypeDismatchException, AccessException {
		left.setValue(rightShift(left.asNumber(), right.asNumber()));
		return left;
	}

	public static LValueResult unsignedRightShiftAssign(LValueResult left,
			ValueResult right) throws TypeDismatchException, AccessException {
		left.setValue(unsignedRightShift(left.asNumber(), right.asNumber()));
		return left;
	}

	public static ValueResult binary(Binary ast, ESInterpreter inte)
			throws TypeDismatchException, AccessException,
			InterpretingException {
		Expression left = ast.getLeft();
		Expression right = ast.getRight();
		switch (ast.getOperator()) {
		case ADD:
			return new RValueResult(add(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case SUBTRACT:
			return new RValueResult(subtract(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case MULTIPLY:
			return new RValueResult(multiply(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case DIVIDE:
			return new RValueResult(divide(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case MODULO:
			return new RValueResult(modulo(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case BITWISE_AND:
			return new RValueResult(bitwiseAnd(inte.visit(left).asNumber(),
					inte.visit(right).asNumber()));
		case BITWISE_OR:
			return new RValueResult(bitwiseOr(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case EXCLUSIVE_OR:
			return new RValueResult(exclusiveOr(inte.visit(left).asNumber(),
					inte.visit(right).asNumber()));
		case LEFT_SHIFT:
			return new RValueResult(leftShift(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case RIGHT_SHIFT:
			return new RValueResult(rightShift(inte.visit(left).asNumber(),
					inte.visit(right).asNumber()));
		case UNSIGNED_RIGHT_SHIFT:
			return new RValueResult(unsignedRightShift(inte.visit(left)
					.asNumber(), inte.visit(right).asNumber()));
		case AND:
			return new RValueResult(inte.visit(left).asBoolean()
					&& inte.visit(right).asBoolean());
		case OR:
			return new RValueResult(inte.visit(left).asBoolean()
					|| inte.visit(right).asBoolean());
		case EQUAL:
			return new RValueResult(equal(inte.visit(left).asValue(), inte
					.visit(right).asValue()));
		case NOT_EQUAL:
			return new RValueResult(!equal(inte.visit(left).asValue(), inte
					.visit(right).asValue()));
		case GREATER:
			return new RValueResult(greater(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case LESS:
			return new RValueResult(less(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case GREATER_OR_EQUAL:
			return new RValueResult(!less(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case LESS_OR_EQUAL:
			return new RValueResult(!greater(inte.visit(left).asNumber(), inte
					.visit(right).asNumber()));
		case ARRAY_INDEX:
			return new ArrayElementResult(inte.visit(left).asValue(), inte
					.visit(right).asNumber().intValue());
		case ASSIGN:
			return assign(inte.visit(left).asLValueResult(), inte.visit(right)
					.asValueResult());
		case ADD_ASSIGN:
			return addAssign(inte.visit(left).asLValueResult(),
					inte.visit(right).asValueResult());
		case SUBTRACT_ASSIGN:
			return subtractAssign(inte.visit(left).asLValueResult(), inte
					.visit(right).asValueResult());
		case MULTIPLY_ASSIGN:
			return multiplyAssign(inte.visit(left).asLValueResult(), inte
					.visit(right).asValueResult());
		case DIVIDE_ASSIGN:
			return divideAssign(inte.visit(left).asLValueResult(),
					inte.visit(right).asValueResult());
		case MODULO_ASSIGN:
			return moduloAssign(inte.visit(left).asLValueResult(),
					inte.visit(right).asValueResult());
		case BITWISE_AND_ASSIGN:
			return bitwiseAndAssign(inte.visit(left).asLValueResult(), inte
					.visit(right).asValueResult());
		case BITWISE_OR_ASSIGN:
			return bitwiseOrAssign(inte.visit(left).asLValueResult(), inte
					.visit(right).asValueResult());
		case EXCLUSIVE_OR_ASSIGN:
			return exclusiveOrAssign(inte.visit(left).asLValueResult(), inte
					.visit(right).asValueResult());
		case LEFT_SHIFT_ASSIGN:
			return leftShiftAssign(inte.visit(left).asLValueResult(), inte
					.visit(right).asValueResult());
		case RIGHT_SHIFT_ASSIGN:
			return rightShiftAssign(inte.visit(left).asLValueResult(), inte
					.visit(right).asValueResult());
		case UNSIGNED_RIGHT_SHIFT_ASSIGN:
			return unsignedRightShiftAssign(inte.visit(left).asLValueResult(),
					inte.visit(right).asValueResult());
		default:
			throw new UnsupportedOperationException("Unsupported operator.");
		}
	}

	public static ValueResult unary(Unary ast, ESInterpreter inte)
			throws TypeDismatchException, AccessException,
			InterpretingException {
		Result res = inte.visit(ast.getOperand());
		switch (ast.getOperator()) {
		case PLUS:
			return new RValueResult(res.asNumber());
		case MINUS:
			return new RValueResult(subtract(0, res.asNumber()));
		case PRE_INC:
		case PRE_DEC:
		case POST_INC:
		case POST_DEC:
			LValueResult lres = res.asLValueResult();
			RValueResult prev = null;
			if (ast.getOperator() == UnaryOperator.POST_INC
					|| ast.getOperator() == UnaryOperator.POST_DEC)
				prev = new RValueResult(lres.getValue());
			if (ast.getOperator() == UnaryOperator.PRE_INC
					|| ast.getOperator() == UnaryOperator.POST_INC)
				lres.setValue(add(lres.asNumber(), 1));
			else
				lres.setValue(subtract(lres.asNumber(), 1));
			if (ast.getOperator() == UnaryOperator.POST_INC
					|| ast.getOperator() == UnaryOperator.POST_DEC)
				return prev;
			else
				return lres;
		case NOT:
			return new RValueResult(!res.asBoolean());
		case BITWISE_COMPLEMENT:
			return new RValueResult(bitwiseComplement(res.asNumber()));
		default:
			throw new UnsupportedOperationException("Unsupported operator.");
		}
	}

	private static void checkInteger(Number operand)
			throws TypeDismatchException {
		if (operand instanceof Double || operand instanceof Float)
			throw new TypeDismatchException("integer");
	}
}
