package ASTree;

import java.util.List;

public class ReadOpNode extends ASTNode{

    String identName;

    public ReadOpNode(String identName) {
        this.identName = identName;
    }

    public String getIdentName() {
        return this.identName;
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
