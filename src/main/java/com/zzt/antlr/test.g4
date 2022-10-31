grammar test;

@lexer::members {
    int counter = 0;
}

WS : [ \r\t]+ -> skip;
COMMENT : '/*' .*? '*/' NEWLINE? -> skip;
//COMMENT2: '//' .*? NEWLINE? -> skip;
TEXT : [a-zA-Z0-9]+;
NEWLINE : '\r'? '\n' { {System.out.println("Newlines so far: " + (++counter)); } };

content: (TEXT | COMMENT | NEWLINE )* EOF;