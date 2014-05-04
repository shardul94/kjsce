import java.util.Scanner;
class rdp1{
	static int i=0;
	static String in;
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the input string: ");
		in = sc.nextLine();
		try{
			E();
		}catch(Exception e){
			error();
		}
		if(i==in.length()) System.out.println("Input sentence is valid");
		else System.out.println("Input sentence is invalid");
	}
	static void E(){
		if(in.charAt(i)=='x'){
			i++;
			if(in.charAt(i)=='+'){
				i++;
				T();
			}else error();
		}else error();
	}
	static void T(){
		if(in.charAt(i)=='x')
			i++;
		else if(in.charAt(i)=='('){
			i++;
			E();
			if(in.charAt(i)==')'){
				i++;
			}else error();
		}else error();
	}
	static void error(){
		System.out.println("Input Sentence is invalid");
		System.exit(0);
	}
}
/***************
 * For Grammar *
 * E -> x+T    *
 * T -> (E)	   *
 * T -> x      *
 ***************/ 