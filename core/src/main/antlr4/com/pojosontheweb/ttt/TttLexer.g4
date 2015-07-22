lexer grammar TttLexer;

SIGNATURE_START
	: '<%(' -> pushMode(SIGNATURE)
	;

TEXT
	: .+?
	;

EXPRESSION_START
	: '<%='
	;

SCRIPTLET_START
	: '<%' ~[=(]
	;

CLOSER
	: '%>'
	;

mode SIGNATURE;

WS
	: [ \t\r\n] -> skip
	;

SIGNATURE_CLOSE
	: ')%>' WS* -> popMode
	;

ID
	: LETTER ( LETTER | DIGIT )*
	;

GENERIC
	: '<' ID '>'
	;

TYPE
	: (ID '.')* ID GENERIC?
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
