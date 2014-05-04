import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
class macro_pass1{
	static ArrayList<MNT> mnt_list;
	static ArrayList<MDT> mdt_list;
	static ArrayList<ALA> ala_list;
	static ArrayList<DPT> dpt_list;
	static int mnt_counter = 1;
	static int mdt_counter = 1;
	static int ala_counter = 1;
	public static void main(String args[]) throws Exception{
		mnt_list = new ArrayList<MNT>();
		mdt_list = new ArrayList<MDT>();
		ala_list = new ArrayList<ALA>();
		dpt_list = new ArrayList<DPT>();
		File input_file = new File("macro_input.txt");
		Scanner in = new Scanner(input_file);
		File output_file = new File("pass1_output.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(output_file));
		File mnt_file = new File("mnt.txt");
		BufferedWriter mnt_out = new BufferedWriter(new FileWriter(mnt_file));
		File mdt_file = new File("mdt.txt");
		BufferedWriter mdt_out = new BufferedWriter(new FileWriter(mdt_file));
		File ala_file = new File("ala.txt");
		BufferedWriter ala_out = new BufferedWriter(new FileWriter(ala_file));
		File dpt_file = new File("dpt.txt");
		BufferedWriter dpt_out = new BufferedWriter(new FileWriter(dpt_file));
		while(in.hasNextLine()){
			String input = in.nextLine();
			String s[] = split(input);
			if(s[0].equals("MACRO")){
				input = in.nextLine();
				s = split_name(input);
				mnt_list.add(new MNT(mnt_counter++,s[0],mdt_counter));
				mdt_list.add(new MDT(mdt_counter++,input));
				for(int i=1;i<s.length;i++){
					String temp[] = s[i].split("=");
					ala_list.add(new ALA(ala_counter++,temp[0]));
					if(temp.length>1) dpt_list.add(new DPT(ala_counter-1,temp[1]));
				}
				while(true){
					input = in.nextLine();
					s = split(input);
					if(s[0].equals("MEND")){
						mdt_list.add(new MDT(mdt_counter++,input));
						break;
					}else{
						if(s[2].charAt(0)=='&'){
							int temp = searchALA(s[2]);
							s[2] = "#"+temp;
						}
						if(s[3].charAt(0)=='&'){
							int temp = searchALA(s[3]);
							s[3] = "#"+temp;
						}
						String line = s[0]+"\t"+s[1]+"\t"+s[2]+","+s[3];
						mdt_list.add(new MDT(mdt_counter++,line));
					}
				}
			}else{
				out.write(input+"\n");
				out.flush();
			}
		}
		for(MNT m : mnt_list){
			mnt_out.write(m.index+"\t"+m.name+"\t"+m.mdt_index+"\n");
			mnt_out.flush();
		}
		for(MDT m : mdt_list){
			mdt_out.write(m.index+"\t"+m.line+"\n");
			mdt_out.flush();
		}
		for(ALA a : ala_list){
			ala_out.write(a.index+"\t"+a.argument+"\n");
			ala_out.flush();
		}
		for(DPT d : dpt_list){
			dpt_out.write(d.index+"\t"+d.parameter+"\n");
			dpt_out.flush();
		}
		out.close();
		mdt_out.close();
		mnt_out.close();
		ala_out.close();
		dpt_out.close();
	}
	static String[] split(String s){
		String toReturn[] = new String[4];
		int start=0,k=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\t'||s.charAt(i)==','){
				toReturn[k++] = s.substring(start,i).trim();
				start=i+1;
			}else if(i==s.length()-1)
				toReturn[k++] = s.substring(start,i+1).trim();
		}
		return toReturn;
	}
	static String[] split_name(String s){
		String toReturn[] = new String[100];
		int start=0,k=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\t'||s.charAt(i)==','){
				toReturn[k++] = s.substring(start,i).trim();
				start=i+1;
			}else if(i==s.length()-1)
				toReturn[k++] = s.substring(start,i+1).trim();
		}
		String temp[] = new String[k];
		for(int i=0;i<k;i++)
			temp[i] = toReturn[i];
		return temp;
	}
	static int searchALA(String s){
		for(ALA a : ala_list)
			if(a.argument.equals(s))
				return a.index;
		return -1;
	}
}
class MNT{
	int index;
	String name;
	int mdt_index;
	MNT(int i,String n,int mi){
		index = i;
		name = n;
		mdt_index = mi;
	}
}
class MDT{
	int index;
	String line;
	MDT(int i, String l){
		index = i;
		line = l;
	}
}
class ALA{
	int index;
	String argument;
	ALA(int i,String a){
		index = i;
		argument = a;
	}
}
class DPT{
	int index;
	String parameter;
	DPT(int i, String p){
		index = i;
		parameter = p;
	}
}