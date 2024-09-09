import ASTree.*;
import SymbTable.SymbolTable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Emitter implements ASTVisitor{

    private SymbolTable symbolTable;
    private ClassWriter cw;
    private MethodVisitor mv;
    private PrintWriter pw;
    private TraceClassVisitor tcv;
    private List<String> name;

    public Emitter(SymbolTable symbolTable) {

        this.symbolTable = symbolTable;
        this.symbolTable.printTable();
        this.fillVariableTable();
        this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.pw = new PrintWriter(System.out,true);
        this.tcv = new TraceClassVisitor(this.cw,this.pw);
        this.tcv.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "translator", null, "java/lang/Object", null);
        this.mv = tcv.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V",null,null);
        this.mv.visitCode();

        for (int i = 0; i <= name.size(); i++){
            this.mv.visitLdcInsn(0);
            this.mv.visitVarInsn(Opcodes.ISTORE, i+1);
        }

        // Create Scanner object
        mv.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
        mv.visitInsn(Opcodes.DUP);
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
        mv.visitVarInsn(Opcodes.ASTORE, 0);
    }

    @Override
    public void visit(ProgramNode node) {

        System.out.println("Visiting ProgramNode");
        for(OperatorNode operatorNode: node.getOperators()){
            operatorNode.accept(this);
        }

        mv.visitInsn(Opcodes.RETURN);
        mv.visitEnd();
        mv.visitMaxs(2, 4);
        tcv.visitEnd();

        System.out.println("Inputs:");
        byte[] bytecode = cw.toByteArray();
        ByteAClassWriter loader = new ByteAClassWriter();
        Class<?> test = loader.defineClass("translator", bytecode);
        try {
            test.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void visit(OperatorNode node) {
        node.getNode().accept(this);
        System.out.println("Visiting OperatorNode");
    }

    @Override
    public void visit(AssignOpNode node) {
        System.out.println("Visiting AssignOpNode");
        node.getExpression().accept(this);
        mv.visitVarInsn(Opcodes.ISTORE, getVariableIndex(node.getIdent().getName()));
    }

    @Override
    public void visit(ReadOpNode node) {
        System.out.println("Visiting ReadOpNode");

        // Read integer
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()I", false);
        mv.visitVarInsn(Opcodes.ISTORE, getVariableIndex(node.getIdentName()));
    }

    @Override
    public void visit(WriteOpNode node) {
        System.out.println("Visiting WriteOpNode");
        node.getNode().accept(this);
        // Print integer
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitInsn(Opcodes.SWAP);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    }

    @Override
    public void visit(ExperssionNode node) {
        List<TermNode> terms = node.getTermNodes();
        List<String> operators = node.getOperators();

        if (!terms.isEmpty()){
            terms.get(0).accept(this);
        }

        for(int i = 1; i < terms.size(); i++){
            terms.get(i).accept(this);
            switch(operators.get(i-1)) {
                case "+":
                    mv.visitInsn(Opcodes.IADD);
                    break;
                case "-":
                    mv.visitInsn(Opcodes.ISUB);
                    break;
                case "OR":
                    mv.visitInsn(Opcodes.IOR);
                    break;
            }
        }
    }

    @Override
    public void visit(TermNode node) {
        List<FactorNode> factors = node.getFactorNodes();
        List<String> operators = node.getOperators();

        if (!factors.isEmpty()){
            factors.get(0).accept(this);
        }

        for(int i = 1; i < factors.size(); i++){
            factors.get(i).accept(this);
            switch(operators.get(i-1)) {
                case "*":
                    mv.visitInsn(Opcodes.IMUL);
                    break;
                case "DIV":
                    mv.visitInsn(Opcodes.IDIV);
                    break;
                case "MOD":
                    mv.visitInsn(Opcodes.IREM);
                    break;
                case "AND":
                    mv.visitInsn(Opcodes.IAND);
                    break;
            }
        }
    }

    @Override
    public void visit(FactorNode node) {
        System.out.println("Visiting FactorNode");

        node.getNode().accept(this);
        if(node.getOperator() != null){
            if(node.getOperator().equals("NOT")){
                mv.visitInsn(Opcodes.IXOR);
            }
        }
    }

    @Override
    public void visit(IdentNode node) {
        System.out.println("Visiting IdentNode");
        mv.visitVarInsn(Opcodes.ILOAD, getVariableIndex(node.getName()));
    }

    @Override
    public void visit(NumberNode node) {
        System.out.println("Visiting NumberNode");
        mv.visitLdcInsn(Integer.parseInt(node.getValue()));
    }

    private void fillVariableTable(){
        this.name = this.symbolTable.getAllNames();
    }

    private int getVariableIndex(String varName) {
        for (int i = 0; i < this.name.size(); i++) {
            if (varName.equals(this.name.get(i))){
                return i + 1;
            }
        }
        return 0;
    }

}
