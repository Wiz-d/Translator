package SymbTable;

import java.util.*;

public class SymbolTable {

    private Stack<HashMap<String, Symbol>> scopes;

    public SymbolTable() {
        this.scopes = new Stack<>();
        enterScope();
        addPredefinedSymbol();
    }

    public void enterScope() {
        this.scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
        }
    }

    public void insert(String name, String type, Object value) {
        Map<String, Symbol> currentScope = scopes.peek();
        Symbol symbol = new Symbol(name, type, value);
        currentScope.put(name, symbol);
    }

    public Symbol lookup(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Map<String, Symbol> scope = scopes.get(i);
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return null;
    }

    public void printTable(){
        for (int i = 0; i < scopes.size(); i++) {
            System.out.println("Scope Level " + i + ":");
            for (HashMap.Entry<String, Symbol> entry : scopes.get(i).entrySet()) {
                System.out.println(entry.getValue().getName());
            }
        }
    }

    private void addPredefinedSymbol(){
        insert("Integer", "type",null);
    }

    public List<String> getAllNames() {
        ArrayList<String> symbolNames = new ArrayList<>();
        for (int i = 1; i < this.scopes.size(); i++) {
            HashMap<String, Symbol> scope = this.scopes.get(i);
            symbolNames.addAll(scope.keySet());
        }
        return symbolNames;
    }

}
