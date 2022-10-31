package com.zzt;

import com.zzt.gen.label.LabeledExprBaseVisitor;
import com.zzt.gen.label.LabeledExprParser;

import java.util.HashMap;
import java.util.Map;

public class EvalVisitor extends LabeledExprBaseVisitor<Integer> {

    //计算器的"内存"，存放变量名和变量值的对应关系
    Map<String,Integer> memory = new HashMap<String,Integer>();

    // 'ID'
    @Override
    public Integer visitAssign(LabeledExprParser.AssignContext ctx){
        String id = ctx.ID().getText(); // id 在'='的左侧
        int value = visit(ctx.expr()); //计算右侧表达式的值
        memory.put(id,value);  //将这个映射关系存储在计算器的'内存'中
        return value;
    }

    @Override
    public Integer visitPrintExpr(LabeledExprParser.PrintExprContext ctx) {
        Integer value = visit(ctx.expr()); //计算expr子节点的值
        System.out.println(value); //打印结果
        return 0; //返回值
    }

    @Override
    public Integer visitInt(LabeledExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitId(LabeledExprParser.IdContext ctx) {
        String id =ctx.ID().getText();
        if (memory.containsKey(id))return memory.get(id);
        return 0;
    }

    @Override
    public Integer visitMulDiv(LabeledExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0)); //计算左侧子表达式的值
        int right = visit(ctx.expr(1)); //计算右侧子表达式的值
        if(ctx.op.getType() == LabeledExprParser.MUL)
            return left * right;
        return left/right;
    }

    @Override
    public Integer visitAddSub(LabeledExprParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0)); //计算左侧子表达式的值
        int right = visit(ctx.expr(1)); //计算右侧子表达式的值
        if(ctx.op.getType() == LabeledExprParser.ADD)
            return left + right;
        return left-right;
    }

    @Override
    public Integer visitParens(LabeledExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}
