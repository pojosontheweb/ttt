parser grammar TttParser;

options { tokenVocab=TttLexer; }

r
	: signature parts
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

parts
	: part*
	;

part
	: TEXT+ | SCRIPTLET | EXPRESSION
	;