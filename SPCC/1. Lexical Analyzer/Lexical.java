import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.StringTokenizer;
import java.io.*;
class Lexical{
	static char special_syms[] = {';',',','\'','\"','{','}','(',')'};
	static char operators[] = {'+','-','*','/','='};
	static String keywords[] ={"auto","break","case","char","const","continue","default","do","double","else","enum","extern","float","for","goto","if","int","long","register","return","short","signed","sizeof","static","struct","switch","typedef","union","unsigned","void","volatile","while"};
	public static void main(String args[]) throws Exception{
		File input = new File("lexical_input.txt");
        Scanner in = new Scanner(input);
        File output_file = new File("output.txt");
        BufferedWriter output = new BufferedWriter(new FileWriter(output_file));
        while(in.hasNextLine()){        	
        	String s = in.nextLine();
        	String delimiters = " "+new String(special_syms)+ new String(operators); 
        	StringTokenizer st = new StringTokenizer(s,delimiters,true);
        	while(st.hasMoreTokens()){
        		String token = st.nextToken().trim();
        		if(token.length()>0){
        			if(isSpecialSym(token)){
        				int temp = find("specialSym",token);
        				if(temp==-1)
        					write("specialSym",token);
        				temp = find("specialSym",token);
        				output.write("sp#"+temp+" ");
        			}else if(isOperator(token)){
        				int temp = find("operator",token);
        				if(temp==-1)
        					write("operator",token);
        				temp = find("operator",token);
        				output.write("op#"+temp+" ");
        			}else if(isKeyword(token)){
        				int temp = find("keyword",token);
        				if(temp==-1)
        					write("keyword",token);
        				temp = find("keyword",token);
        				output.write("key#"+temp+" ");
        			}else if(isIdentifier(token)){
						int index = s.indexOf(token)+token.length();
						if(s.charAt(index)=='('){
        					int temp = find("function",token+"()");
        					if(temp==-1)
        						write("function",token+"()");
        					temp = find("function",token+"()");
        					output.write("fn#"+temp+" ");
						}else{
        					int temp = find("identifier",token);
        					if(temp==-1)
        						write("identifier",token);
        					temp = find("identifier",token);
        					output.write("id#"+temp+" ");
						}
        			}else if(isConstant(token)){
        				int temp = find("constant",token);
        				if(temp==-1)
        					write("constant",token);
        				temp = find("constant",token);
        				output.write("cn#"+temp+" ");
        			}
        		}
        	}
        	output.write("\n");
        }
        output.flush();
        output.close();
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
	static int find(String filename,String token){
		try{
			File f = new File(filename+".txt");
			Scanner in = new Scanner(f);
			int i=1;
			while(in.hasNextLine()){
				if(in.nextLine().equals(token))
					return i;
				i++;
			}
			return -1;
		}catch(FileNotFoundException e){
			return -1;
		}
	}
}
/***************PATTERN EXPLANATION****************/
/* Identifier Pattern: ^[a-zA-Z_]\\w*
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