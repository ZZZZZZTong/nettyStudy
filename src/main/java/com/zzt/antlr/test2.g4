grammar test2;

WS : [ \r\t]+ -> skip;

stat: '//' .*? ;


NEWLINE : '\r'? '\n';