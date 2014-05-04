import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
class macro_pass2{
	static ArrayList<MNT> mnt_list;
	static ArrayList<MDT> mdt_list;
	static ArrayList<ALA> ala_list;
	static ArrayList<DPT> dpt_list;
	static ArrayList<ALA> ala2_list;
	public static void main(String args[])throws Exception{
		mnt_list = new ArrayList<MNT>();
		mdt_list = new ArrayList<MDT>();
		ala_list = new ArrayList<ALA>();
		dpt_list = new ArrayList<DPT>();
		ala2_list = new ArrayList<ALA>();
		File input_file = new File("pass1_output.txt");
		Scanner in = new Scanner(input_file);
		File mnt_file = new File("mnt.txt");
		Scanner mnt_in = new Scanner(mnt_file);
		File mdt_file = new File("mdt.txt");
		Scanner mdt_in = new Scanner(mdt_file);
		File ala_file = new File("ala.txt");
		Scanner ala_in = new Scanner(ala_file);
		File dpt_file = new File("dpt.txt");
		Scanner dpt_in = new Scanner(dpt_file);
		while(mnt_in.hasNextLine()){
			String temp = mnt_in.nextLine();
			String s[] = temp.split("\t");
			mnt_list.add(new MNT(Integer.parseInt(s[0]),s[1],Integer.parseInt(s[2])));
		}
		while(mdt_in.hasNextLine()){
			String temp = mdt_in.nextLine();
			int i = temp.indexOf("\t");
			mdt_list.add(new MDT(Integer.parseInt(temp.substring(0,i)),temp.substring(i+1)));
		}
		while(ala_in.hasNextLine()){
			String temp = ala_in.nextLine();
			int i = temp.indexOf("\t");
			ala_list.add(new ALA(Integer.parseInt(temp.substring(0,i)),temp.substring(i+1)));
		}
		while(dpt_in.hasNextLine()){
			String temp = dpt_in.nextLine();
			int i = temp.indexOf("\t");
			dpt_list.add(new DPT(Integer.parseInt(temp.substring(0,i)),temp.substring(i+1)));
		}
		File output = new File("pass2_output.txt");
		BufferedWriter out = new BufferedWriter(new FileWriter(output));
		while(in.hasNextLine()){
			String input = in.nextLine();
			String s[] = split(input);
			int mnt_index = searchMNT(s[0]);
			if(mnt_index!=-1){
				MNT macro = mnt_list.get(mnt_index-1);
				MDT macro_def = mdt_list.get(macro.mdt_index-1);
				String s_def[] = split_name(macro_def.line);
				s = split_name(input);
				for(int i=1;i<s.length;i++){
					if(s[i].charAt(0)=='&'){
						String temp[] = s[i].split("=");
						int arg_index = searchALA(temp[0]);
						ala2_list.add(new ALA(arg_index,temp[1]));
					}else{
						String temp[] = s_def[i].split("=");
						int arg_index = searchALA(temp[0]);
						ala2_list.add(new ALA(arg_index,s[i]));
					}
				}
				int mdt_counter = macro_def.index+1;
				while(!mdt_list.get(mdt_counter-1).line.equals("MEND")){
					input = mdt_list.get(mdt_counter-1).line;
					String temp[] = split(input);
					if(temp[2]!=null){
						if(temp[2].charAt(0)=='#'){
							int arg_index = Integer.parseInt(temp[2].substring(1));
							String param = getParam(arg_index);
							if(param!="")
								temp[2] = param;
							else
								temp[2] = getDefaultParam(arg_index);
						}
					}
					if(temp[3]!=null){
						if(temp[3].charAt(0)=='#'){
							int arg_index = Integer.parseInt(temp[3].substring(1));
							String param = getParam(arg_index);
							if(param!="")
								temp[3] = param;
							else
								temp[3] = getDefaultParam(arg_index);
						}
					}
					out.write(temp[0]+"\t"+temp[1]+"\t"+temp[2]+","+temp[3]+"\n");
					out.flush();
					mdt_counter++;
				}
			}else{
				out.write(input+"\n");
				out.flush();
			}
		}
		out.close();
	}
	static String[] split(String s){
		String toReturn[] = new String[4];
		int start=0,k=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\t'||s.charAt(i)==','){
				toReturn[k++] = s.substring(start,i);
				start = i+1;
			}else if(i==s.length()-1){
				toReturn[k++] = s.substring(start,i+1);
			}
		}
		return toReturn;
	}
	static String[] split_name(String s){
		String toReturn[] = new String[100];
		int start=0,k=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\t'||s.charAt(i)==','){
				toReturn[k++] = s.substring(start,i);
				start = i+1;
			}else if(i==s.length()-1){
				toReturn[k++] = s.substring(start,i+1);
			}
		}
		String temp[] = new String[k];
		for(int i=0;i<k;i++)
			temp[i] = toReturn[i];
		return temp;
	}
	static int searchMNT(String s){
		for(MNT m : mnt_list)
			if(m.name.equals(s))
				return m.index;
		return -1;
	}
	static int searchALA(String s){
		for(ALA a : ala_list)
			if(a.argument.equals(s))
				return a.index;
		return -1;
	}
	static int searchALA2(String s){
		for(ALA a : ala2_list)
			if(a.argument.equals(s))
				return a.index;
		return -1;
	}
	static String getParam(int i){
		for(ALA a : ala2_list)
			if(a.index==i)
				return a.argument;
		return "";
	}
	static String getDefaultParam(int i){
		for(DPT d : dpt_list)
			if(d.index==i)
				return d.parameter;
		return "";
	}
}
class MNT{
	int index;
	String name;
	int mdt_index;
	MNT(int i, String n, int mi){
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
	ALA(int i, String a){
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