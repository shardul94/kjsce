%{
	#include <stdio.h>
	#include <stdlib.h>
	void yyerror(char *s);

%}

%start line
%token select1 from where and star comma equals quote identifier constant

%%

line		:	select1 fields from tables							{printf("Syntax Correct\n");}
			|	select1 fields from tables where conditions			{printf("Syntax Correct\n");}
			;
fields		:	star
			|	identifier
			|	identifier comma fields
			;
tables		:	identifier
			|	identifier comma tables
			;
conditions	:	condition
			|	condition and conditions
			;
condition	:	identifier equals constant
			|	identifier equals quote identifier quote
			;
			
%%

int main(void){
	yyparse();
	return 0;
}
void yyerror (char *s){
	printf("Syntax error\n");
	exit(0);
}
