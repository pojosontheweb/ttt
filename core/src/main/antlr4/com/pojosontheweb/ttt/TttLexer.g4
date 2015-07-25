lexer grammar TttLexer;

WS
	: [ \t\r\n]
	;

TEXT
	: .+?
	;

DIRECTIVE_START
	: '<%@' -> pushMode(DIRECTIVE)
	;

DECLARATION_START
	: '<%!' -> pushMode(DECLARATION)
	;

EXPRESSION_START
	: '<%=' -> pushMode(EXPRESSION)
	;

SCRIPTLET_START
	: '<%' ~[=(!] -> pushMode(SCRIPTLET)
	;


mode DIRECTIVE;

DIR_WS
	: [ \t\r\n] -> skip
	;

PAGE
	: 'page'
	;

IMPORT
	: 'import'
	;

EXTENDS
	: 'extends'
	;

EQ
	: '='
	;

DBL_QUOTE
	: '"'
	;

DIRECTIVE_END
	: '%>' -> popMode
	;

LETTER
	: [a-zA-Z]
	;

DIGIT
	: [0-9]
	;

ID
	: LETTER ( LETTER | DIGIT )*
	;

TYPE
	: (ID '.')* ID
	;

mode DECLARATION;

DEC_WS
	: [ \t\r\n] -> skip
	;

DEC_LETTER
	: [a-zA-Z]
	;

DEC_DIGIT
	: [0-9]
	;

DEC_ID
	: DEC_LETTER ( DEC_LETTER | DEC_DIGIT )*
	;

DEC_GENERIC
	: '<' DEC_ID '>'
	;

DEC_TYPE
	: (DEC_ID '.')* DEC_ID DEC_GENERIC?
	;

DEC_EOL
	: ';'
	;

DECLARATION_END
	: '%>' -> popMode
	;

mode EXPRESSION;

EXPRESSION_TEXT
	: .+?
	;

EXPRESSION_END
	: '%>' -> popMode
	;

mode SCRIPTLET;

SCRIPTLET_TEXT
	: .+?
	;

SCRIPTLET_END
	: '%>' -> popMode
	;


//TEXT
//	: .+?
//	;
//
//EXPRESSION_START
//	: '<%='
//	;
//
//SCRIPTLET_START
//	: '<%' ~[=(]
//	;
//
//CLOSER
//	: '%>'
//	;
//
//mode SIGNATURE;
//
//WS
//	: [ \t\r\n] -> skip
//	;
//
//SIGNATURE_CLOSE
//	: ')%>' WS* -> popMode
//	;
//
//ID
//	: LETTER ( LETTER | DIGIT )*
//	;
//
//GENERIC
//	: '<' ID '>'
//	;
//
//TYPE
//	: (ID '.')* ID GENERIC?
//	;
//
//LETTER
//	: [a-zA-Z]
//	;
//
//DIGIT
//	: [0-9]
//	;
//
//COMMA
//	: ','
//	;
