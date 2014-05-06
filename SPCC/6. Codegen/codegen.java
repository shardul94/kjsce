import java.util.Scanner;
class codegen{
	static char register[];
	static int register_count=0;
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the no. of lines: ");
		int n = sc.nextInt();
		sc.nextLine();
		register = new char[100];
		String input[] = new String[n];
		System.out.println("Enter the lines: ");
		for(int i=0;i<n;i++) input[i] = sc.nextLine();
		for(int i=0;i<input.length;i++){
			char temp[] = input[i].toCharArray();
			int register1 = find(temp[2]);
			int register2 = find(temp[4]);
			if(register1==-1){
				System.out.println("MOV R"+register_count+","+temp[2]);
				register[register_count] = temp[2];
			}
			if(temp[3]=='+') System.out.print("ADD ");
			else if(temp[3]=='-') System.out.print("SUB ");
			else if(temp[3]=='*') System.out.print("MUL ");
			else if(temp[3]=='/') System.out.print("DIV ");
			if(register2==-1){
				System.out.println("R"+register_count+","+temp[4]);
				register[register_count++]=temp[0];
			}else if(register1==-1){
				System.out.println("R"+register_count+","+"R"+register2);
				register[register_count++]=temp[0];
			}
			if(register1!=-1&&register2!=-1){
				System.out.println("R"+register1+",R"+register2);
				register[register1] = temp[0];
			}
			if(i==input.length-1)
				System.out.println("MOV "+temp[0]+",R"+find(temp[0]));
			System.out.println();
		}
	}
	static int find(char c){
		for(int i=0;i<register.length;i++)
			if(register[i]==c)
				return i;
		return -1;
	}
}

/************************
 * OUTPUT:
 * 
Enter the no. of lines: 4
Enter the lines:
t=a-b
u=a-c
v=t+u
d=v+u

MOV R0,a
SUB R0,b

MOV R1,a
SUB R1,c

ADD R0,R1

ADD R0,R1
MOV d,R0

Enter the no. of lines: 4
Enter the lines:
t=a*b
u=a-c
v=a*t
w=v+u

MOV R0,a
MUL R0,b

MOV R1,a
SUB R1,c

MOV R2,a
MUL R2,R0

ADD R2,R1
MOV w,R2
******************************/
