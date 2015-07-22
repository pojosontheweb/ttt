parser grammar TttParser;

options { tokenVocab=TttLexer; }

r
	: signature part*
	;

signature
	: SIGNATURE_START args SIGNATURE_CLOSE
	;

args
	: arg (COMMA arg)*
	;

arg
	: ID COLON TYPE
	;

part
	: TEXT+ | SCRIPTLET | EXPRESSION
	;