import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
class pass2{
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
		fillSymbolTable();
		fillLiteralTable();
		File input_file = new File("twopass_input.txt");
		Scanner in = new Scanner(input_file);
		File output_file = new File("twopass_output.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(output_file));
		String input = in.nextLine();
		String s[] = split(input);
		int pc = (s[2]!=null)?Integer.parseInt(s[2]):0;
		out.write(pc+"\n");
		int base = 0;
		while(in.hasNextLine()){
			input = in.nextLine();
			s = split(input);
			if(s[1].equals("DC")){
				int length = 0;
				if(s[2].charAt(0)=='B') length=1;
				if(s[2].charAt(0)=='H') length=2;
				if(s[2].charAt(0)=='F') length=4;
				if(s[2].charAt(0)=='D') length=8;
				while(pc%length!=0) pc++;
				out.write(pc+" "+Integer.toBinaryString(Integer.parseInt(s[2].substring(2,s[2].length()-1)))+"\n");
				pc += length;
			}
			if(s[1].equals("DS")){
				int length = 0;
				if(s[2].charAt(s[2].length()-1)=='B') length=1;
				if(s[2].charAt(s[2].length()-1)=='H') length=2;
				if(s[2].charAt(s[2].length()-1)=='F') length=4;
				if(s[2].charAt(s[2].length()-1)=='D') length=8;
				while(pc%length!=0) pc++;
				out.write(pc+"\n");
				pc = pc+length*Integer.parseInt(s[2].substring(1,s[2].length()-2));
			}
			if(s[1].equals("EQU")){
				out.write(pc+"\n");
			}
			if(s[1].equals("USING")){
				base = Integer.parseInt(s[3]);
				out.write(pc+"\n");
			}
			if(s[1].equals("DROP")){
				base = 0;
				out.write(pc+"\n");
			}
			if(s[1].equals("END")){
				for(Literal l : literal_table){
					out.write(pc+" "+Integer.toBinaryString(Integer.parseInt(l.name.substring(3,l.name.length()-1)))+"\n");
					pc += l.length;
				}
				out.write(pc+"\n");
			}
			if(inMOT(s[1])){
				MOT m = findMOT(s[1]);
				out.write(pc+" "+s[1]+" ");
				if(isInteger(s[2])) out.write(s[2]);
				else out.write(findSymbol(s[2]).value+"");
				out.write(",");
				if(isInteger(s[3])) out.write(s[3]);
				else if(s[3].charAt(0)=='=') out.write(findLiteral(s[3]).value+"");
				else out.write(findSymbol(s[3]).value+"");
				if(m.rr.equals("RX")) out.write("(0,"+base+")");
				out.write("\n");
				pc += m.length;
			}
		}
		out.flush();
		out.close();
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
	static Symbol findSymbol(String str){
		for(Symbol s:symbol_table)
			if(s.name.equals(str))
				return s;
		return null;
	}
	static Literal findLiteral(String str){
		for(Literal l : literal_table)
			if(l.name.equals(str))
				return l;
		return null;
	}
	static String[] split(String s){
		String toReturn[] = new String[4];
		int start=0,k=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\t'||s.charAt(i)==','){
				toReturn[k++] = s.substring(start,i).trim();
				start = i+1;
			}else if(i==s.length()-1)
				toReturn[k++] = s.substring(start,i+1).trim();
		}
		return toReturn;
	}
	static void fillSymbolTable() throws Exception{
		File f = new File("symbol.txt");
		Scanner sc = new Scanner(f);
		while(sc.hasNextLine()){
			String s[] = sc.nextLine().split("\t");
			symbol_table.add(new Symbol(s[0],Integer.parseInt(s[1]),Integer.parseInt(s[2]),s[3]));
		}
	}
	static void fillLiteralTable() throws Exception{
		File f = new File("literal.txt");
		Scanner sc = new Scanner(f);
		while(sc.hasNextLine()){
			String s[] = sc.nextLine().split("\t");
			literal_table.add(new Literal(s[0],Integer.parseInt(s[1]),Integer.parseInt(s[2]),s[3]));
		}
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
