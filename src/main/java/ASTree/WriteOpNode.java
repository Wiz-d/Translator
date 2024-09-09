package ASTree;

public class WriteOpNode extends ASTNode {

    ASTNode node;

    public WriteOpNode(ASTNode node) {
        this.node = node;
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
