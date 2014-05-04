import java.util.Scanner;
import java.util.ArrayList;
class commonsub{
	static ArrayList<Expression> lines;
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		lines = new ArrayList<Expression>();
		System.out.println("Enter lines: ");
		while(sc.hasNextLine()){
			Expression e = new Expression();
			String line = sc.nextLine();
			if(line.equals("END")) break;
			String s1[] = line.split("=");
			e.lhs = s1[0].trim();
			e.rhs = s1[1].trim();
			lines.add(e);
		}
		for(Expression e : lines){
			findCommon(e);
		}
		System.out.println("\nOutput: ");
		for(Expression e : lines){
			if(!e.lhs.equals("")||!e.rhs.equals(""))
				System.out.println(e.lhs+"="+e.rhs);
		}
	}
	static void findCommon(Expression e){
		for(Expression e1 : lines){
			if(e1!=e){
				if(e1.rhs.equals(e.rhs))
					eliminate(e1,e);
				else if(e1.rhs.contains(e.rhs))
					e1.rhs = e1.rhs.replace(e.rhs,e.lhs);
			}
		}
	}
	static void eliminate(Expression e1,Expression e){
		for(Expression e2 : lines){
			if(e2!=e){
				if(e2.rhs.contains(e1.lhs))
					e2.rhs = e2.rhs.replace(e1.lhs,e.lhs);
			}
		}
		e1.lhs = "";
		e1.rhs = "";
	}
}
class Expression{
	String lhs;
	String rhs;
}
/***********************
Enter lines: 
t0=-c
t1=b*t0
t2=-c
t3=b*t2
t4=t1+t3
t5=a*-c
a=t4
END

Output: 
t0=-c
t1=b*t0
t4=t1+t1
t5=a*t0
a=t4
*/
