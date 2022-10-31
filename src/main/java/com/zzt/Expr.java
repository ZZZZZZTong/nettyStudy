package com.zzt;

import com.zzt.gen.com.ExprLexer;
import com.zzt.gen.com.ExprParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


public class Expr {
    public static void main(String[] args) {
        CharStream input = CharStreams.fromString("a=25*5+3\n");
        ExprLexer lexer  = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree = parser.prog(); //启动语法分析器
//        int c = tree.getChild(0).getChildCount();

        System.out.println(tree.toStringTree(parser));



    }
}
