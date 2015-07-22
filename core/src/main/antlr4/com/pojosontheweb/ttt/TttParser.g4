parser grammar TttParser;

options { tokenVocab=TttLexer; }

r
	: signature rest
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

rest
	: TEXT*
	;