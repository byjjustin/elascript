package elascript.ast;

import elascript.*;

public class MemberOf extends Expression {
	private Expression left;
	private String memberName;

	public MemberOf(Expression left, String memberName, int line) {
		super(line);
		CodeContract.requiresArgumentNotNull(left, "left");
		CodeContract.requiresArgumentNotNull(memberName, "memberName");
		this.left = left;
		this.memberName = memberName;
	}

	public Expression getLeft() {
		return left;
	}

	public String getMemberName() {
		return memberName;
	}

	@Override
	public <T, E extends Throwable> T accept(ASTVisitor<T, E> visitor) throws E {
		CodeContract.requiresArgumentNotNull(visitor, "visitor");
		return visitor.visit(this);
	}
}
