package com.zzt;

import com.zzt.gen.java.JavaBaseListener;
import com.zzt.gen.java.JavaParser;
import org.antlr.v4.runtime.TokenStream;


public class ExtractInterfaceListener extends JavaBaseListener {

    JavaParser parser;

    public ExtractInterfaceListener(JavaParser parser){this.parser=parser;}

    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        System.out.println("interface I"+ctx.Identifier()+"{");
    }

    @Override
    public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        System.out.println("}");
    }

    @Override
    public void enterMethodDeclaration(JavaParser.MethodDeclarationContext ctx) {
        TokenStream tokens = parser.getTokenStream();
        String type = "void";
        if (ctx.typeSpec()!=null){
            type = tokens.getText(ctx.typeSpec());
        }
        String args = tokens.getText(ctx.formalParameters());
        System.out.println("\t" + type +" "+ctx.Identifier()+args+";");
    }
}
