lexer grammar TttLexer;

WS
	: ' ' | '\t' | '\r'? '\n' | '\r'
	;

TEXT
	: .+?
	;

JSP_COMMENT_START
	: '<%--' -> pushMode(JSP_COMMENT)
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

mode JSP_COMMENT;

JSP_COMMENT_END
	: '--%>' -> popMode
	;

JSP_COMMENT_WS
	: [ \t\r\n]
	;

JSP_COMMENT_TEXT
	: .+?
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

CONTENT_TYPE
	: 'contentType'
	;

SLASH
	: '/'
	;

DOT_STAR
	: '.*'
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

DEC_GENERIC_SUPER
	: 'super'
	;

DEC_GENERIC_EXTENDS
	: 'extends'
	;

DEC_LETTER
	: [a-zA-Z]
	;

DEC_DIGIT
	: [0-9]
	;

DEC_ARRAY_OPEN
	: '['
	;

DEC_ARRAY_CLOSE
	: ']'
	;

DEC_ID
	: DEC_LETTER ( DEC_LETTER | DEC_DIGIT )*
	;

DEC_FQN
	: DEC_ID ('.' DEC_ID)+
	;

DEC_GENERIC_START
	: '<'
	;

DEC_GENERIC_WILD
	: '?'
	;

DEC_GENERIC_COMMA
	: ','
	;

DEC_GENERIC_END
	: '>'
	;


DEC_EOL
	: ';'
	;

DECLARATION_END
	: '%>' -> popMode
	;

DEC_LINE_COMMENT_START
	: '//' -> pushMode(LINE_COMMENT_START)
	;

DEC_MULTI_LINE_COMMENT_START
	: '/*' ~'*' -> pushMode(MULTI_LINE_COMMENT_START)
	;

ARG_JDOC_START
	: '/**' WS* -> pushMode(ARG_JDOC)
	;

mode LINE_COMMENT_START;

LINE_COMMENT_TEXT
	: ~('\r' | '\n')+
	;

LINE_COMMENT_END
	: '\n' -> popMode
	;

mode MULTI_LINE_COMMENT_START;

MULTI_LINE_COMMENT_TEXT
	: .+?
	;

MULTI_LINE_COMMENT_END
	: '*/' -> popMode
	;

mode ARG_JDOC;

JDOC_TEXT
	: .+?
	;

JDOC_END
	: '*/' -> popMode
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
