import ASTree.*;
import SymbTable.SymbolTable;
import Tokens.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private int tokenIndex = 0;
    private List<Token> tokenList;
    private SymbolTable symbolTable = new SymbolTable();

    public Parser(List<Token> tokenList) throws Exception {
        this.tokenList = tokenList;
        ProgramNode node = parseProgram();
        ASTVisitor visitor = new Emitter(this.symbolTable);
        node.accept(visitor);
        symbolTable.exitScope();
    }

    private void expect(Token tok) throws Exception {
        if(!this.tokenList.get(tokenIndex).getClass().equals(tok.getClass())){
            throw new Exception("Unexpected token: " + this.tokenList.get(tokenIndex).getValue());
        }
        tokenIndex++;
    }

    public ProgramNode parseProgram() throws Exception {
        symbolTable.enterScope();
        List<OperatorNode> opNodes = new ArrayList<>();
        while(!(this.tokenList.get(tokenIndex) instanceof EOFToken)){
//            System.out.println("entering program");

           OperatorNode opNode = parseOperator();

           if(opNode != null){
               opNodes.add(opNode);
           }
           if(this.tokenList.get(this.tokenIndex).getClass().equals(EOFToken.class)){
//               System.out.println("exiting program");

               return new ProgramNode(opNodes);
           }
            expect(new SpecSymbolToken(";"));

        }

        return new ProgramNode(opNodes);
    }

    private OperatorNode parseOperator() throws Exception {
//        System.out.println("entering parse operator");

        if(this.tokenList.get(tokenIndex) instanceof IdentToken){
            return new OperatorNode(parseAssignOp());
        } else if(this.tokenList.get(tokenIndex) instanceof KeywordToken && this.tokenList.get(tokenIndex).getValue().equals("Read")){
            return new OperatorNode(parseReadOp());
        } else if(this.tokenList.get(tokenIndex) instanceof KeywordToken && this.tokenList.get(tokenIndex).getValue().equals("Write")){
            return new OperatorNode(parseWriteOp());
        } else {
            throw new Exception("Unexpected token: " + this.tokenList.get(tokenIndex).getValue());
        }

    }

    private AssignOpNode parseAssignOp() throws Exception {
//        System.out.println("entering assign operator" );
        expect(new IdentToken(""));
        String name = this.tokenList.get(tokenIndex-1).getValue();
        symbolTable.insert(tokenList.get(tokenIndex-1).getValue(),"Integer",null);
        expect(new SpecSymbolToken(":="));
        return new AssignOpNode(parseExpression(),new IdentNode(name));
    }

    private ReadOpNode parseReadOp() throws Exception {
//        System.out.println("entering read operator");
        expect(new KeywordToken(""));
        expect(new IdentToken(""));
        symbolTable.insert(tokenList.get(tokenIndex-1).getValue(),"Integer",null);
        return new ReadOpNode(tokenList.get(tokenIndex-1).getValue());
    }

    private WriteOpNode parseWriteOp() throws Exception {
//        System.out.println("entering write operator");
        expect(new KeywordToken("Write"));
        return new WriteOpNode(parseExpression());
    }

    private ExperssionNode parseExpression() throws Exception {
//        System.out.println("entering parse expression");
        List<TermNode> termNodes = new ArrayList<>();
        List<String> operators = new ArrayList<>();
        if (this.tokenList.get(tokenIndex) instanceof SpecSymbolToken && this.tokenList.get(tokenIndex).getValue().equals("+") || this.tokenList.get(tokenIndex).getValue().equals("-")) {
            operators.add(this.tokenList.get(tokenIndex).getValue());
            tokenIndex++;
        }
        termNodes.add(parseTerm());
        while (this.tokenList.get(tokenIndex) instanceof SpecSymbolToken && this.tokenList.get(tokenIndex).getValue().equals("+") || this.tokenList.get(tokenIndex).getValue().equals("-") || this.tokenList.get(tokenIndex).getValue().equals("OR")){
            operators.add(this.tokenList.get(tokenIndex).getValue());
            tokenIndex++;
            termNodes.add(parseTerm());
        }
        return new ExperssionNode(termNodes,operators);
    }

    private TermNode parseTerm() throws Exception {
//        System.out.println("entering parse term");
        List<FactorNode> factorNodes = new ArrayList<>();
        List<String> operators = new ArrayList<>();
        factorNodes.add(parseFactor());
        while (this.tokenList.get(tokenIndex) instanceof SpecSymbolToken && this.tokenList.get(tokenIndex).getValue().equals("*") || this.tokenList.get(tokenIndex).getValue().equals("DIV") || this.tokenList.get(tokenIndex).getValue().equals("MOD") || this.tokenList.get(tokenIndex).getValue().equals("AND")) {
            operators.add(this.tokenList.get(tokenIndex).getValue());
            tokenIndex++;
            factorNodes.add(parseFactor());
        }
        return new TermNode(factorNodes,operators);
    }

    private FactorNode parseFactor() throws Exception {
//        System.out.println("entering parse factor");
        if(this.tokenList.get(tokenIndex) instanceof KeywordToken && this.tokenList.get(tokenIndex).getValue().equals("NOT")){
            tokenIndex++;
            return new FactorNode("NOT", parseFactor());
        } else if(this.tokenList.get(tokenIndex) instanceof NumberToken){
            String value = tokenList.get(tokenIndex).getValue();
            tokenIndex++;
            return new FactorNode(new NumberNode(value));
        } else if (this.tokenList.get(tokenIndex) instanceof IdentToken){
            symbolTable.insert(this.tokenList.get(tokenIndex).getValue(),"Integer",null);
            String value = tokenList.get(tokenIndex).getValue();
            tokenIndex++;
            return new FactorNode(new IdentNode(value));
        } else if (this.tokenList.get(tokenIndex) instanceof SpecSymbolToken && this.tokenList.get(tokenIndex).getValue().equals("(")){
            tokenIndex++;
            ExperssionNode experssionNode = parseExpression();
            expect(new SpecSymbolToken(")"));
            return new FactorNode(experssionNode);
        } else {
            throw new Exception("Unexpected token: " + this.tokenList.get(tokenIndex).getValue());
        }
    }
}
