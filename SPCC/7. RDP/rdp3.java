import java.util.Scanner;
class rdp3{
	static int i=0;
	static String in;
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the input string: ");
		in = sc.nextLine();
		try{
			S();
		}catch(Exception e){
			error();
		}
		if(i==in.length()) System.out.println("Input sentence is valid");
		else System.out.println("Input sentence is invalid");
	}
	static void S(){
		if(in.charAt(i)=='c'){
			i++;
			W();
			if(in.charAt(i)=='d'){
				i++;
			}else error();
		}else error();
	}
	static void W(){
		if(in.charAt(i)=='a'){
			i++;
			if(in.charAt(i)=='b'){
				i++;
			}else return;
		}else return;
	}
	static void error(){
		System.out.println("Input Sentence is invalid");
		System.exit(0);
	}
}
/***************
 * For Grammar *
 * S -> cWd    *
 * W -> ab|a|epsilon   *
 ***************/ 