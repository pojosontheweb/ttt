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
	: argType argName
	;

argName
	: ID
	;

argType
	: ID
	| TYPE
	;

parts
	: part*
	;

part
	: text | scriptlet | expression
	;

text
	: TEXT+
	;

scriptlet
	: SCRIPTLET_START script CLOSER
	;

script
	: .*?
	;

expression
	: EXPRESSION_START expr CLOSER;

expr
	: .*?
	;
