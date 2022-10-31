lexer grammar CommonLexerRules;

ID: [a-zA-Z]+; //匹配标识符
INT: [0-9]+;  //匹配整数
NEWLINE:'\r'?'\n'; //新行开始
WS:[\t]+ -> skip;  //丢弃空白符