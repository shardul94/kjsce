import java.util.Scanner;
import java.util.ArrayList;
class invariant{
	static ArrayList<String> variants;
	static ArrayList<String> old_variants;
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter no. of lines: ");
		int n = sc.nextInt();
		sc.nextLine();
		Expression lines[] = new Expression[n];
		variants = new ArrayList<String>();
		old_variants = new ArrayList<String>();
		System.out.println("Enter lines: ");
		for(int i=0;i<n;i++){
			lines[i] = new Expression();
			String line = sc.nextLine();
			lines[i].full = line;
			if(line.startsWith("for(")){
				variants.add(line.charAt(4)+"");
				lines[i].lhs=line;
			}else{
				String s1[] = line.split("=");
				lines[i].lhs = s1[0].trim();
				if(s1.length>1){
					String s2[] = s1[1].trim().toCharArray();
					lines[i].rhs = s2;
				}
			}
		}
		while(!old_variants.equals(variants)){
			old_variants.clear();
			old_variants.addAll(variants);
			for(Expression e:lines){
				if(hasVariant(e.rhs))
					if(!variants.contains(e.lhs))
						variants.add(e.lhs);
				if(hasSelf(e.rhs,e.lhs))
					if(!variants.contains(e.lhs))
						variants.add(e.lhs);	
			}
		}
		System.out.println("\nOutput:");
		for(Expression e:lines){
			if(!variants.contains(e.lhs)&&e.rhs.length!=0)
				System.out.println(e.full);
		}
		for(Expression e:lines){
			if(variants.contains(e.lhs)||e.rhs.length==0)
				System.out.println(e.full);
		}
	}
	static boolean hasVariant(String rhs[]){
		for(String s:rhs)
			if(variants.contains(s))
				return true;
		return false;
	}
	static boolean hasSelf(String rhs[], String lhs){
		for(String s:rhs)
			if(s.equals(lhs))
				return true;
		return false;
	}
}
class Expression{
	String full;
	String lhs;
	String rhs[];
	Expression(){
		full="";
		lhs="";
		rhs = new String[0];
	}
}

/***************************
Enter no. of lines: 7
Enter lines: 
for(i=0;i<n;i++){
t = a[i] ;
x = j + k ;
z = j + y ;
y = j + t ;
b = b + k ;
}

Output:
x = j + k ;
for(i=0;i<n;i++){
t = a[i] ;
z = j + y ;
y = j + t ;
b = b + k ;
}
****************************/