package ASTree;

public class AssignOpNode extends ASTNode{

    ASTNode expression;
    ASTNode Ident;

    public AssignOpNode(ASTNode expression, ASTNode Ident) {
        this.expression = expression;
        this.Ident = Ident;
    }

    public ASTNode getExpression() {
        return expression;
    }

    public ASTNode getIdent() {
        return Ident;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return "";
    }
}
