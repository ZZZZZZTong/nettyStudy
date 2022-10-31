grammar Expr;

prog:stat+;

stat: expr NEWLINE
| ID '=' expr NEWLINE
| NEWLINE
;

expr: expr ('*'|'/') expr //使用|分隔同一个语言规则的若干备选分支，使用圆括号把一些符号组合成子规则
| expr ('+'|'-') expr
| INT
| ID
| '(' expr ')'
;

ID: [a-zA-Z]+; //匹配标识符
INT: [0-9]+;  //匹配整数
NEWLINE:'\r'?'\n'; //新行开始
WS:[\t]+ -> skip;  //丢弃空白符