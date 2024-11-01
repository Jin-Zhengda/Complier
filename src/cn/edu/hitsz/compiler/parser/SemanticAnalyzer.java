package cn.edu.hitsz.compiler.parser;

import cn.edu.hitsz.compiler.lexer.Token;
import cn.edu.hitsz.compiler.parser.table.Production;
import cn.edu.hitsz.compiler.parser.table.Status;
import cn.edu.hitsz.compiler.parser.table.Symbol;
import cn.edu.hitsz.compiler.symtab.SourceCodeType;
import cn.edu.hitsz.compiler.symtab.SymbolTable;

import java.util.Stack;

// TODO: 实验三: 实现语义分析
public class SemanticAnalyzer implements ActionObserver {
    private SymbolTable symbolTable;
    private final Stack<Symbol> symbolStack = new Stack<>();

    @Override
    public void whenAccept(Status currentStatus) {
        // TODO: 该过程在遇到 Accept 时要采取的代码动作
    }

    @Override
    public void whenReduce(Status currentStatus, Production production) {
        // TODO: 该过程在遇到 reduce production 时要采取的代码动作
        var index = production.index();
        switch (index) {
            case 4 -> {
                // S -> D id
                var id = symbolStack.pop();
                var D = symbolStack.pop();
                if (id.token != null) {
                    symbolTable.get(id.token.getText()).setType(D.type);
                }
                symbolStack.push(new Symbol(production.head()));
            }
            case 5 -> {
                // D -> int
                var nonTerminal = new Symbol(production.head());
                nonTerminal.type = symbolStack.pop().type;
                symbolStack.push(nonTerminal);
            }
            default -> {
                for (var i = 0; i < production.body().size(); i++) {
                    symbolStack.pop();
                }
                var nonTerminal = new Symbol(production.head());
                symbolStack.push(nonTerminal);
            }
        }
    }

    @Override
    public void whenShift(Status currentStatus, Token currentToken) {
        // TODO: 该过程在遇到 shift 时要采取的代码动作
        var symbol = new Symbol(currentToken);
        if (currentToken.getKindId().equals("int")) {
            symbol.type = SourceCodeType.Int;
        }
        symbolStack.push(symbol);
    }

    @Override
    public void setSymbolTable(SymbolTable table) {
        // TODO: 设计你可能需要的符号表存储结构
        // 如果需要使用符号表的话, 可以将它或者它的一部分信息存起来, 比如使用一个成员变量存储
        this.symbolTable = table;
    }
}

