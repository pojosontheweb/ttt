// Define a grammar called Hello
grammar Ttt;

r : args ;

arg : identifier ':' className ;

className : identifier ( '.' identifier )* ;

args : '<%(' arg (',' arg)* ')%>' ;

identifier : LETTER (LETTER | DIGIT)* ;

LETTER : 'A'..'Z' | 'a'..'z' ;
DIGIT : '0'..'9' ;
WS : [ \t\r\n]+ -> skip;


//r  : 'hello' ID ;         // match keyword hello followed by an identifier
//ID : [a-z]+ ;             // match lower-case identifiers
//WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines