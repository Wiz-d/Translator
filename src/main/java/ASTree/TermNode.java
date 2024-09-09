package ASTree;

import java.util.ArrayList;
import java.util.List;

public class TermNode extends ASTNode {

    List<FactorNode> factorNodes;
    List<String> operators;

    public TermNode(List<FactorNode> factorNodes, List<String> operators) {
        this.factorNodes = factorNodes;
        this.operators = operators;
    }

    public List<FactorNode> getFactorNodes() {
        return factorNodes;
    }

    public List<String> getOperators() {
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
