parser grammar TttParser;

options { tokenVocab=TttLexer; }

r
	: directives declaration? parts
	;

directives
	: WS* directiveImport* directiveExtends? directiveImport*
	;

directiveImport
	: DIRECTIVE_START PAGE IMPORT EQ DBL_QUOTE directiveValue DBL_QUOTE DIRECTIVE_END WS*
	;

directiveExtends
	: DIRECTIVE_START PAGE EXTENDS EQ DBL_QUOTE directiveValue DBL_QUOTE DIRECTIVE_END WS*
	;

directiveValue
	: ( ID | TYPE ) DOT_STAR?
	;

declaration
	: DECLARATION_START args DECLARATION_END WS?
	;

args
	: arg*
	;

arg
	: argType argName DEC_EOL
	;

argType
	: DEC_TYPE | DEC_ID
	;

argName
	: DEC_ID | DEC_LETTER
	;

parts
	: part*
	;

part
	: text | expression | scriptlet
	;

text
	: (TEXT | WS)+
	;

scriptlet
	: SCRIPTLET_START script SCRIPTLET_END
	;

script
	: SCRIPTLET_TEXT*
	;

expression
	: EXPRESSION_START expr EXPRESSION_END
	;

expr
	: EXPRESSION_TEXT*
	;


//
//signature
//	: SIGNATURE_START args SIGNATURE_CLOSE
//	;
//
//args
//	: arg (COMMA arg)*
//	;
//
//arg
//	: argType argName
//	;
//
//argName
//	: ID
//	;
//
//argType
//	: ID
//	| TYPE
//	;
//
//parts
//	: part*
//	;
//
//part
//	: text | scriptlet | expression
//	;
//
//text
//	: TEXT+
//	;
//
//scriptlet
//	: SCRIPTLET_START script CLOSER
//	;
//
//script
//	: .*?
//	;
//
//expression
//	: EXPRESSION_START expr CLOSER;
//
//expr
//	: .*?
//	;
