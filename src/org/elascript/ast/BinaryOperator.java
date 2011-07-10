package org.elascript.ast;

public enum BinaryOperator {
	ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO,

	BITWISE_AND, BITWISE_OR, EXCLUSIVE_OR,

	LEFT_SHIFT, RIGHT_SHIFT, UNSIGNED_RIGHT_SHIFT,

	AND, OR,

	EQUAL, NOT_EQUAL, GREATER, LESS, GREATER_OR_EQUAL, LESS_OR_EQUAL,

	ARRAY_INDEX,

	ASSIGN, ADD_ASSIGN, SUBTRACT_ASSIGN, MULTIPLY_ASSIGN, DIVIDE_ASSIGN, MODULO_ASSIGN,

	BITWISE_AND_ASSIGN, BITWISE_OR_ASSIGN, EXCLUSIVE_OR_ASSIGN,

	LEFT_SHIFT_ASSIGN, RIGHT_SHIFT_ASSIGN, UNSIGNED_RIGHT_SHIFT_ASSIGN,
}