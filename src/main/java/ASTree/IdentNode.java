package ASTree;

public class IdentNode extends ASTNode{

    private String name;

    public IdentNode(String name) {
        this.name = name;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
