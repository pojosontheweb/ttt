parser grammar TttParser;

options { tokenVocab=TttLexer; }

r
	: directives declaration? parts
	;

directives
	: WS* (jspComment | directiveImport | directiveExtends)*
	;

jspComment
	: JSP_COMMENT_START jspCommentValue JSP_COMMENT_END WS*
	;

jspCommentValue
	: (JSP_COMMENT_WS | JSP_COMMENT_TEXT)*
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
	: (decLineComment | decMultilineComment | arg) *
	;

decLineComment
	: DEC_LINE_COMMENT_START decLineCommentText LINE_COMMENT_END
	;

decLineCommentText
	: LINE_COMMENT_TEXT*
	;

decMultilineComment
	: DEC_MULTI_LINE_COMMENT_START decMultilineCommentText MULTI_LINE_COMMENT_END
	;

decMultilineCommentText
	: MULTI_LINE_COMMENT_TEXT*
	;

arg
	: argJavaDoc? argType argName DEC_EOL
	;

argJavaDoc
	: ARG_JDOC_START jdocText JDOC_END
	;

jdocText
	: JDOC_TEXT*
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
	: text | expression | scriptlet | jspComment
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
