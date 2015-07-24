package com.pojosontheweb.ttt;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.pojosontheweb.ttt.psi.TttTypes;
import com.intellij.psi.TokenType;

%%

%class TttLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

SIG_START="<%("

%state SIGNATURE
SIG_END=")%>"
ID=[:jletter:] [:jletterdigit:]*
TYPE={ID} ("." {ID})*
EOL="\r"|"\n"|"\r\n"
LINE_WS=[\ \t\f]
WHITE_SPACE=({LINE_WS}|{EOL})+
ARG_SEP=","
%%

<YYINITIAL> {SIG_START}	{ yybegin(SIGNATURE); return TttTypes.SIG_START; }
<SIGNATURE> {
	{SIG_END} 		{ yybegin(YYINITIAL); return TttTypes.SIG_END; }
	{ID}			{ yybegin(SIGNATURE); return TttTypes.ID; }
	{TYPE}			{ yybegin(SIGNATURE); return TttTypes.TYPE; }
	{WHITE_SPACE}	{ yybegin(SIGNATURE); return com.intellij.psi.TokenType.WHITE_SPACE; }
	{ARG_SEP}		{ yybegin(SIGNATURE); return TttTypes.ARG_SEP; }
	[^] 			{ yybegin(SIGNATURE); return com.intellij.psi.TokenType.BAD_CHARACTER; }
}

//CRLF= \n|\r|\r\n
//WHITE_SPACE=[\ \t\f]
//FIRST_VALUE_CHARACTER=[^ \n\r\f\\] | "\\"{CRLF} | "\\".
//VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".
//END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
//SEPARATOR=[:=]
//KEY_CHARACTER=[^:=\ \n\r\t\f\\] | "\\"{CRLF} | "\\".
//
//%state WAITING_VALUE
//
//%%
//
//<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return TttTypes.COMMENT; }
//
//<YYINITIAL> {KEY_CHARACTER}+                                { yybegin(YYINITIAL); return TttTypes.KEY; }
//
//<YYINITIAL> {SEPARATOR}                                     { yybegin(WAITING_VALUE); return TttTypes.SEPARATOR; }
//
//<WAITING_VALUE> {CRLF}                                     { yybegin(YYINITIAL); return TttTypes.CRLF; }
//
//<WAITING_VALUE> {WHITE_SPACE}+                              { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }
//
//<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(YYINITIAL); return TttTypes.VALUE; }
//
//{CRLF}                                                     { yybegin(YYINITIAL); return TttTypes.CRLF; }
//
//{WHITE_SPACE}+                                              { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
//
//.                                                           { return TokenType.BAD_CHARACTER; }