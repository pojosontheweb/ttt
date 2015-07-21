grammar Ttt;

r 		: args? part* ;
args 	: '<%(' text ')%>' ;
id		: LETTER (LETTER | DIGIT)* ;

part	: expr | code | text;
expr 	: '<%=' escText '%>' ;
code 	: '<%' escText '%>' ;
text	: CHAR+ ;
escText	: text ( ESCAPE text)* ;

CHAR 	: . ;
DIGIT 	: '0'..'9' ;
LETTER 	: 'A'..'Z'
		| 'a'..'z'
		| '_'
		;
WS : [ \t\r\n]+ ;
ESCAPE	:  '\\%>' ;
