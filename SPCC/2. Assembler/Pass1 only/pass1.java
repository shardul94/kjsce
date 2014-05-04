import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
class pass1{
	static ArrayList<MOT> mot;
	static ArrayList<POT> pot;
	static ArrayList<Symbol> symbol_table;
	static ArrayList<Literal> literal_table;
	public static void main(String args[]) throws Exception{
		mot = new ArrayList<MOT>();
		pot = new ArrayList<POT>();
		symbol_table = new ArrayList<Symbol>();
		literal_table = new ArrayList<Literal>();
		fillMOT();
		fillPOT();
		File input_file = new File("twopass_input.txt");
		Scanner in = new Scanner(input_file);
		File output_file = new File("twopass_output.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(output_file));
		String input = in.nextLine();
		String s[] = split(input);
		int pc = (s[2]!=null)?Integer.parseInt(s[2]):0;
		symbol_table.add(new Symbol(s[0],0,1,"R"));
		while(in.hasNextLine()){
			input = in.nextLine();
			s = split(input);
			if(s[0]!=null&&s[0].length()>0){
				if(!inSymbolTable(s[0]))
					symbol_table.add(new Symbol(s[0],pc,4,"R"));
			}
			if(s[1].equals("DC")){
				int length = 0;
				if(s[2].charAt(0)=='B') length=1;
				if(s[2].charAt(0)=='H') length=2;
				if(s[2].charAt(0)=='F') length=4;
				if(s[2].charAt(0)=='D') length=8;
				while(pc%length!=0) pc++;
				if(!inSymbolTable(s[0])){
					symbol_table.add(new Symbol(s[0],pc,length,"R"));
				}else{
					Symbol sym = findSymbol(s[0]);
					sym.value = pc;
					sym.length = length;
				}
				out.write(pc+" "+s[2].substring(2,s[2].length()-1)+"\n");
				pc = pc+length;
			}
			if(s[1].equals("DS")){
				int length = 0;
				if(s[2].charAt(s[2].length()-1)=='B') length=1;
				if(s[2].charAt(s[2].length()-1)=='H') length=2;
				if(s[2].charAt(s[2].length()-1)=='F') length=4;
				if(s[2].charAt(s[2].length()-1)=='D') length=8;
				while(pc%length!=0) pc++;
				if(!inSymbolTable(s[0])){
					symbol_table.add(new Symbol(s[0],pc,length,"R"));
				}else{
					Symbol sym = findSymbol(s[0]);
					sym.value = pc;
					sym.length = length;
				}
				out.write(pc+"\n");
				pc = pc+length*Integer.parseInt(s[2].substring(1,s[2].length()-2));
			}
			if(s[1].equals("EQU")){
				int val = (s[2].trim().equals("*"))?pc:Integer.parseInt(s[2]);
				if(!inSymbolTable(s[0])){
					symbol_table.add(new Symbol(s[0],val,1,"A"));
				}else{
					Symbol sym = findSymbol(s[0]);
					sym.value = val;
					sym.length = 1;
					sym.ra = "A";
				}
				out.write(pc+"\n");
			}
			if(s[1].equals("USING")||s[1].equals("DROP")){
				out.write(pc+"\n");
			}
			if(s[1].equals("END")){
				for(Literal l : literal_table){
					l.value = pc;
					out.write(pc+" "+l.name.substring(3,l.name.length()-1)+"\n");
					pc += 4;
				}
			}
			if(inMOT(s[1])){
				MOT m = findMOT(s[1]);
				out.write(pc+" "+s[1]+" ");
				if(isInteger(s[2])) out.write(s[2]);
				else out.write("_");
				out.write(",");
				if(isInteger(s[3])) out.write(s[3]);
				else out.write("_");
				out.write("\n");
				pc += m.length;
			}
			if(s[3]!=null&&s[3].length()>0){
				if(s[3].charAt(0)=='=')
					if(!inLiteralTable(s[3]))
						literal_table.add(new Literal(s[3],0,4,"R"));
			}
		}
		out.flush();
		out.close();
		File symbol_file = new File("symbol.txt");
		BufferedWriter symbol_out = new BufferedWriter(new FileWriter(symbol_file));
		for(Symbol sym : symbol_table)
			symbol_out.write(sym.name+"\t"+sym.value+"\t"+sym.length+"\t"+sym.ra+"\n");
		symbol_out.flush();
		symbol_out.close();
		File literal_file = new File("literal.txt");
		BufferedWriter literal_out = new BufferedWriter(new FileWriter(literal_file));
		for(Literal l : literal_table)
			literal_out.write(l.name+"\t"+l.value+"\t"+l.length+"\t"+l.ra+"\n");
		literal_out.flush();
		literal_out.close();
		
	}
	static boolean inLiteralTable(String str){
		for(Literal l : literal_table)
			if(l.name.equals(str))
				return true;
		return false;
	}
	static boolean inMOT(String str){
		for(MOT m:mot)
			if(m.inst.equals(str))
				return true;
		return false;
	}
	static MOT findMOT(String str){
		for(MOT m:mot)
			if(m.inst.equals(str))
				return m;
		return null;
	}
	static boolean inSymbolTable(String str){
		for(Symbol s:symbol_table)
			if(s.name.equals(str))
				return true;
		return false;
	}
	static Symbol findSymbol(String str){
		for(Symbol s:symbol_table)
			if(s.name.equals(str))
				return s;
		return null;
	}
	static String[] split(String s){
		String toReturn[] = new String[4];
		int start=0,k=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\t'||s.charAt(i)==','){
				toReturn[k++] = s.substring(start,i);
				start = i+1;
			}else if(i==s.length()-1)
				toReturn[k++] = s.substring(start,i+1);
		}
		return toReturn;
	}
	static void fillMOT() throws Exception{
		File f = new File("mot.txt");
		Scanner sc = new Scanner(f);
		while(sc.hasNextLine()){
			String s[] = sc.nextLine().split("\t");
			mot.add(new MOT(s[0],s[1],Integer.parseInt(s[2]),s[3]));
		}
	}
	static void fillPOT() throws Exception{
		File f = new File("pot.txt");
		Scanner sc = new Scanner(f);
		while(sc.hasNextLine()){
			String s[] = sc.nextLine().split("\t");
			pot.add(new POT(s[0],s[1]));
		}
	}
	static boolean isInteger(String s){
		try{
			Integer.parseInt(s);
			return true;
		}catch(Exception e){
			return false;
		}
	}
}
class Symbol{
	String name;
	int value;
	int length;
	String ra;
	Symbol(String n,int v,int l,String r){
		name = n;
		value = v;
		length = l;
		ra = r;
	}
}
class Literal{
	String name;
	int value;
	int length;
	String ra;
	Literal(String n,int v,int l,String r){
		name = n;
		value = v;
		length = l;
		ra = r;
	}
}
class MOT{
	String inst;
	String addr;
	int length;
	String rr;
	MOT(String i,String a,int l,String r){
		inst = i;
		addr = a;
		length = l;
		rr = r; 
	}
}
class POT{
	String inst;
	String addr;
	POT(String i,String a){
		inst = i;
		addr = a;
	}
}