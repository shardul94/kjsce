%{
	#include "y.tab.h"
%}

%%

SELECT				{return select1;}
FROM				{return from;}
WHERE				{return where;}
AND				{return and;}
\*				{return star;}
\,				{return comma;}
\=				{return equals;}
\'				{return quote;}
[a-zA-Z][a-zA-Z0-9_]*		{return identifier;}
[0-9]+				{return constant;}
[ \t]				;
\n				{return 0;}
.				{yyerror("Unexpected Character");}

%%

int yywrap(void){
	return 1;
}
