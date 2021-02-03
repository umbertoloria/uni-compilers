package app.gen;
import java_cup.runtime.Symbol; //This is how we pass tokens to the parser
%%
// Declarations for JFlex
%unicode // We wish to read text files
%public
%cup // Declare that we expect to use Java CUP
%line

%{
StringBuffer string = new StringBuffer();
private Symbol makeSymbol(int type) {
    return new Symbol(type, yyline);
}
private Symbol makeSymbol(int type, Object value) {
    return new Symbol(type, yyline, 0, value);
}
%}

// Abbreviations for regular expressions
whitespace = [ \r\n\t\f]


letter = [a-zA-Z]
digit = [0-9]
id = ({letter}|_)({letter}|{digit}|_)*
integer = 0|[1-9][0-9]*
real = {integer}\.([0-9]*[1-9]|0)
string = \"([^\\\"]|\\.)*\"

%state COMMENT
%state STRING

%%
<YYINITIAL> {
// Now for the actual tokens and assocated actions
{whitespace} { /* ignore */ }

// plain symbols
"(" {return makeSymbol(sym.LPAR); }
"," {return makeSymbol(sym.COMMA); }
")" {return makeSymbol(sym.RPAR); }
";" {return makeSymbol(sym.SEMI); }
":" {return makeSymbol(sym.COLON); }

"!" {return makeSymbol(sym.NOT); }
"&&" {return makeSymbol(sym.AND); }
"||" {return makeSymbol(sym.OR); }

"+" {return makeSymbol(sym.PLUS); }
"-" {return makeSymbol(sym.MINUS); }
"*" {return makeSymbol(sym.TIMES); }
"/" {return makeSymbol(sym.DIV); }

"<" {return makeSymbol(sym.LT); }
"<=" {return makeSymbol(sym.LE); }
">" {return makeSymbol(sym.GT); }
">=" {return makeSymbol(sym.GE); }

":=" {return makeSymbol(sym.ASSIGN); }
"->" {return makeSymbol(sym.RETURN); }
"=" {return makeSymbol(sym.EQ); }
"<>" {return makeSymbol(sym.NE); }

// type/statment symbols
"int" {return makeSymbol(sym.INT); }
"bool" {return makeSymbol(sym.BOOL); }
"float" {return makeSymbol(sym.FLOAT); }
"string" {return makeSymbol(sym.STRING); }
"void" {return makeSymbol(sym.VOID); }
"proc" {return makeSymbol(sym.PROC); }
"corp" {return makeSymbol(sym.CORP); }
"if" {return makeSymbol(sym.IF); }
"then" {return makeSymbol(sym.THEN); }
"elif" {return makeSymbol(sym.ELIF); }
"else" {return makeSymbol(sym.ELSE); }
"fi" {return makeSymbol(sym.FI); }
"while" {return makeSymbol(sym.WHILE); }
"do" {return makeSymbol(sym.DO); }
"od" {return makeSymbol(sym.OD); }
"readln" {return makeSymbol(sym.READLN); }
"write" {return makeSymbol(sym.WRITE); }

// value/id symbols
"true" {return makeSymbol(sym.TRUE); }
"false" {return makeSymbol(sym.FALSE); }
{integer} {return makeSymbol(sym.INT_CONST, Integer.parseInt(yytext())); }
{real} {return makeSymbol(sym.FLOAT_CONST, Float.parseFloat(yytext())); }
      \"                             { string = new StringBuffer(); yybegin(STRING); }
{id} {return makeSymbol(sym.ID, yytext()); }

"/*"         { yybegin(COMMENT); }

}

<COMMENT> {
"*/"     { yybegin(YYINITIAL); }
<<EOF>>  { throw new Error("Commento non chiuso in linea " + yyline); }
[^]      { /* Ignore */ }
}

<STRING> {
\"                             { yybegin(YYINITIAL); return makeSymbol(sym.STRING_CONST, string.toString()); }
[^\n\r\"\\]+                   { string.append( yytext() ); }
\\t                            { string.append("\\t"); }
\\n                            { string.append("\\n"); }
\\r                            { string.append("\\r"); }
\\\"                           { string.append("\\\""); }
\\                             { string.append("\\"); }
<<EOF>> { throw new Error("Stringa non chiusa in linea " + yyline); }
}
