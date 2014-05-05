%{
	#include "y.tab.h"
%}

%%

a		{return char_a;}
b		{return char_b;}
\n		{return 0;}
.		{yyerror("Syntax Error\n");}

%%

int yywrap(void){
	return 1;
}
