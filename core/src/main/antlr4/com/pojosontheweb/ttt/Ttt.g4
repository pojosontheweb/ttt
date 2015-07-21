// Define a grammar called Hello
grammar Ttt;

r : args WS* ( expr | .(.)*? )* WS*;

arg : WS* identifier WS* ':' WS* className WS*;

className : identifier ( '.' identifier )* ;

args : '<%(' arg (',' arg)* ')%>' ;

identifier : LETTER (LETTER | DIGIT)* ;

LETTER : 'A'..'Z' | 'a'..'z' ;
DIGIT : '0'..'9' ;
WS : [ \t\r\n]+ ;

expr : '<%=' .*? '%>' ;