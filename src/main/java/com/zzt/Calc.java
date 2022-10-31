package com.zzt;

import com.zzt.gen.label.LabeledExprLexer;
import com.zzt.gen.label.LabeledExprParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Calc {
    public static void main(String[] args) {

        CharStream input = CharStreams.fromString("a=25*5+3\n");
        LabeledExprLexer lexer = new LabeledExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LabeledExprParser parser  = new LabeledExprParser(tokens);
        ParseTree tree = parser.prog();

        //实例化自定义访问器
        EvalVisitor eval = new EvalVisitor();
        System.out.println(eval.visit(tree));


    }
}
