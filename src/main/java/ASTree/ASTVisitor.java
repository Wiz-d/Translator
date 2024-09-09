package ASTree;

public interface ASTVisitor {
    void visit(ProgramNode node);
    void visit(AssignOpNode node);
    void visit(ReadOpNode node);
    void visit(WriteOpNode node);
    void visit(ExperssionNode node);
    void visit(TermNode node);
    void visit(FactorNode node);
    void visit(OperatorNode node);
    void visit(IdentNode node);
    void visit(NumberNode node);
}
