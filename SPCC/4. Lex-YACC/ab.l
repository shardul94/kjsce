%{
	#include "y.tab.h"
%}

%%

a		{return char_b;}
b		{return char_a;}
\n		{return 0;}
.		{yyerror("Syntax Error\n");}

%%

int yywrap(void){
	return 1;
}
