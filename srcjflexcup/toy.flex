import java_cup.runtime.Symbol; //This is how we pass tokens to the parser
%%
// Declarations for JFlex
%unicode // We wish to read text files

%cup // Declare that we expect to use Java CUP
// Abbreviations for regular expressions
whitespace = [ \r\n\t\f]


letter = [a-zA-Z]
id = {letter}({letter}|_)*

digit = [0-9]
integer = 0|[1-9][0-9]*
real = integer\.[0-9]*[1-9]
string = \".*\"

%%
// Now for the actual tokens and assocated actions
{whitespace} { /* ignore */ }

// plain symbols
"(" {return new Symbol(sym.LPAR); }
"," {return new Symbol(sym.COMMA); }
")" {return new Symbol(sym.RPAR); }
";" {return new Symbol(sym.SEMI); }
":" {return new Symbol(sym.COLON); }

"not" {return new Symbol(sym.NOT); }
"and" {return new Symbol(sym.AND); }
"or" {return new Symbol(sym.OR); }

"+" {return new Symbol(sym.PLUS); }
"-" {return new Symbol(sym.MINUS); }
"*" {return new Symbol(sym.TIMES); }
"/" {return new Symbol(sym.DIV); }

"<" {return new Symbol(sym.LT); }
"<=" {return new Symbol(sym.LE); }
">" {return new Symbol(sym.GT); }
">=" {return new Symbol(sym.GE); }

":=" {return new Symbol(sym.ASSIGN); }
"->" {return new Symbol(sym.RETURN); }
"=" {return new Symbol(sym.EQ); }
"<>" {return new Symbol(sym.NE); }

// type/statment symbols
"int" {return new Symbol(sym.INT); }
"bool" {return new Symbol(sym.BOOL); }
"float" {return new Symbol(sym.FLOAT); }
"string" {return new Symbol(sym.STRING); }
"void" {return new Symbol(sym.VOID); }
"proc" {return new Symbol(sym.PROC); }
"corp" {return new Symbol(sym.CORP); }
"if" {return new Symbol(sym.IF); }
"then" {return new Symbol(sym.THEN); }
"elif" {return new Symbol(sym.ELIF); }
"else" {return new Symbol(sym.ELSE); }
"fi" {return new Symbol(sym.FI); }
"while" {return new Symbol(sym.WHILE); }
"do" {return new Symbol(sym.DO); }
"od" {return new Symbol(sym.OD); }
"readln" {return new Symbol(sym.READLN); }
"write" {return new Symbol(sym.WRITE); }

// value/id symbols
"null" {return new Symbol(sym.NULL); }
"true" {return new Symbol(sym.TRUE); }
"false" {return new Symbol(sym.FALSE); }
{integer} {return new Symbol(sym.INT_CONST, Integer.parseInt(yytext())); }
{real} {return new Symbol(sym.FLOAT_CONST, Float.parseFloat(yytext())); }
{string} {return new Symbol(sym.STRING_CONST, yytext()); }
{id} {return new Symbol(sym.ID, yytext()); }

[^]           { throw new Error("\n\nIllegal character < "+ yytext()+" >\n"); }
// End of file
