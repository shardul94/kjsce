import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.StringTokenizer;
import java.io.*;
class Lexical{
	static char special_syms[] = {';',',','\'','\"','{','}'};
	static char operators[] = {'+','-','*','/','='};
	static String keywords[] ={"auto","break","case","char","const","continue","default","do","double","else","enum","extern","float","for","goto","if","int","long","register","return","short","signed","sizeof","static","struct","switch","typedef","union","unsigned","void","volatile","while"};
	public static void main(String args[]) throws Exception{
		File input = new File("lexical_input.txt");
        Scanner in = new Scanner(input);
        while(in.hasNextLine()){        	
        	String s = in.nextLine();
        	String delimiters = " "+new String(special_syms)+ new String(operators); 
        	StringTokenizer st = new StringTokenizer(s,delimiters,true);
        	while(st.hasMoreTokens()){
        		String token = st.nextToken().trim();
        		if(token.length()>0){
        			if(isSpecialSym(token))
        				write("specialSym",token);
        			else if(isOperator(token))
        				write("operator",token);
        			else if(isKeyword(token))
        				write("keyword",token);
        			else if(isFunction(token)){
        				token = token.split("\\(")[0];
        				write("function",token+"()");
        			}else if(isIdentifier(token))
        				write("identifier",token);
        			else if(isConstant(token))
        				write("constant",token);
        		}
        	}
        }
	}
	static boolean isSpecialSym(String s){
		for(char c : special_syms)
			if(Character.toString(c).equals(s))
				return true;
		return false;
	}	
	static boolean isOperator(String s){
		for(char c : operators)
			if(Character.toString(c).equals(s))
				return true;
		return false;
	}
	static boolean isKeyword(String s){
		for(String temp : keywords)
			if(temp.equals(s))
				return true;
		return false;
	}
	static boolean isFunction(String s){
		if(Pattern.matches("^[a-zA-Z_]\\w*\\(.*",s))
			return true;
		return false;
	}
	static boolean isIdentifier(String s){
		if(Pattern.matches("^[a-zA-Z_]\\w*",s))
			return true;
		return false;
	}
	static boolean isConstant(String s){
		if(Pattern.matches("\\d+[\\.\\d*]?",s))
			return true;
		return false;
	}
	static void write(String filename,String token) throws Exception{
		File f = new File(filename+".txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(f,true));
		out.write(token+"\r\n");
		out.flush();
		out.close();
	}
}
/***************PATTERN EXPLANATION****************/
/* Function Pattern: ^[a-zA-Z_]\\w*\\(.*
 * ^[a-zA-Z_] => ^ means starting with, [a-zA-Z_] means valid characters except 0-9 cz functions cannot start with numbers
 * \\w means any valid letter i.e. [a-zA-Z0-9_] Also \\w* means any no. of letters
 * \\( means it should have opening bracket after function name
 * .* means after function, any no of chars
 *
 * Identifier Pattern: ^[a-zA-Z_]\\w*
 * Same as the function name pattern excluding bracket and afterwards
 * ^[a-zA-Z_] => ^ means starting with, [a-zA-Z_] means valid characters except 0-9 cz identifiers cannot start with numbers
 * \\w means any valid letter i.e. [a-zA-Z0-9_] Also \\w* means any no. of letters
 *
 * Constant Pattern: \\d+[\\.\\d*]?
 * \\d means digit and \\d+ means 1 or more digits
 * \\. means a dot eg 15.137
 * \\d* means 0 or more digits after dot
 * [\\.\\d*]? means the thing inside [ ] is optional
 *
 ***************************************************/