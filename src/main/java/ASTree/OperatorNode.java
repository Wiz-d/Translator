package ASTree;

public class OperatorNode extends ASTNode {

    ASTNode node;

    public OperatorNode(ASTNode node) {
        this.node = node;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public ASTNode getNode() {
        return node;
    }

    @Override
    public String getName() {
        return "";
    }
}
