lexer grammar TttLexer;

SIGNATURE_START
	: '<%(' -> pushMode(SIGNATURE)
	;

TEXT
	: .+?
	;

//EXPRESSION
//	: '<%=' .*? '%>'
//	;
//
//SCRIPTLET
//	: '<%' ~[=(] .*? '%>'
//	;

mode SIGNATURE;

WS
	: [ \t\r\n] -> skip
	;

SIGNATURE_CLOSE
	: ')%>' -> popMode
	;

ID
	: LETTER ( LETTER | DIGIT )*
	;

TYPE
	: ID ('.' ID)*
	;

LETTER
	: [a-zA-Z]
	;

DIGIT
	: [0-9]
	;

COMMA
	: ','
	;

COLON
	: ':'
	;
