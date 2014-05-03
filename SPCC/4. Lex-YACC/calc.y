%{
	#include <stdio.h>
	#include <stdlib.h>
	int result=0;
	void yyerror (char *s);
%}

%start line
%token number plus minus divide multiply
%left plus minus
%left multiply divide

%%

line	:	exp					{result=$1;}
		;
exp		:	number				{$$=$1;}
		|	exp plus exp		{$$=$1+$3;}
		|	exp minus exp		{$$=$1-$3;}
		|	exp multiply exp	{$$=$1*$3;}
		|	exp divide exp		{ if($3==0) yyerror("Divide by zero error"); else $$=$1/$3;}
		|	minus exp			{$$=-$2;}
		;

%%

int main(void){
	yyparse();
	printf("%d\n",result);
	return 0;
}

void yyerror (char *s){
	printf("%s\n",s);
	exit(0);
}
