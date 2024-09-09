package ASTree;

import org.w3c.dom.Node;

import java.util.List;

public class ProgramNode extends ASTNode {

    List<OperatorNode> operators;

    public ProgramNode(List<OperatorNode> operators) {
        this.operators = operators;
    }

    public List<OperatorNode> getOperators() {
        return operators;
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
