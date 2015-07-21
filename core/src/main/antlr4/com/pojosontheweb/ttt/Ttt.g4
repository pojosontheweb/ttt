// Define a grammar called Hello
grammar Ttt;

r : args WS* ( expr | .(.)*? )* WS*;

arg : WS* ID WS* ':' WS* className WS*;

className : ID ( '.' ID )* ;

args : '<%(' arg (',' arg)* ')%>' ;

expr : '<%=' ( ESCAPE | .*? ) '%>' ;

ID : LETTER (LETTER | DIGIT)* ;
LETTER : 'A'..'Z' | 'a'..'z' | '_' ;
DIGIT : '0'..'9' ;
WS : [ \t\r\n]+ ;

ESCAPE	:  '\\%>' ;
