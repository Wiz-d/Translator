package ASTree;

import java.util.ArrayList;
import java.util.List;

public class ExperssionNode extends ASTNode{

    List<TermNode> termNodes;
    List<String> operators;

    public ExperssionNode(List<TermNode> termNodes, List<String> operators) {
        this.termNodes = termNodes;
        this.operators = operators;
    }

    public List<TermNode> getTermNodes() {
        return termNodes;
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
