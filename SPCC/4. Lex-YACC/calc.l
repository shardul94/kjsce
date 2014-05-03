%{
	#include "y.tab.h"
%}

%%

[0-9]+			{yylval = atoi(yytext); return number;}
\+				{return plus;}
\-				{return minus;}
\*				{return multiply;}
\/				{return divide;}
[ \t]			;
\n				{return 0;}
.				{yyerror("Unexpected Character");}

%%
int yywrap (void){
	return 1;
}
