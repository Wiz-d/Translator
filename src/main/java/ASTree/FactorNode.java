package ASTree;

public class FactorNode extends ASTNode{

    String operator;
    ASTNode node;

    public FactorNode(ASTNode node) {
        this.node = node;
    }

    public FactorNode(String operator, ASTNode node) {
        this.operator = operator;
        this.node = node;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getNode() {
        return node;
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
