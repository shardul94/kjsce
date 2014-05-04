import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
class MNT{
	int index;
	String name;
	int mdt_index;
	MNT(int i, String n, int mdt_i){
		index=i;
		name=n;
		mdt_index=mdt_i;
	}
}
class MDT{
	int index;
	String line;
	MDT(int i, String l){
		index=i;
		line=l;
	}
}
class ALA{
	int index;
	String argument;
	ALA(int i, String arg){
		index=i;
		argument=arg;
	}
}
class DPT{
	int index;
	String parameter;
	DPT(int i, String param){
		index=i;
		parameter=param;
	}
}
class macro{
	static ArrayList<MNT> mnt_list = new ArrayList<MNT>();
	static ArrayList<MDT> mdt_list = new ArrayList<MDT>();
	static ArrayList<ALA> ala_list = new ArrayList<ALA>();
	static ArrayList<DPT> dpt_list = new ArrayList<DPT>();
	static int mnt_counter=1;
	static int mdt_counter=1;
	static int ala_counter=1;
	public static void main(String args[]) throws Exception{
		File input = new File("macro_input.txt");
		Scanner in = new Scanner(input);
		File file_mdt = new File("mdt.txt");
		BufferedWriter out_mdt = new BufferedWriter(new FileWriter(file_mdt));
		File file_mnt = new File("mnt.txt");
		BufferedWriter out_mnt = new BufferedWriter(new FileWriter(file_mnt));
		File file_ala = new File("ala.txt");
		BufferedWriter out_ala = new BufferedWriter(new FileWriter(file_ala));
		File file_dpt = new File("dpt.txt");
		BufferedWriter out_dpt = new BufferedWriter(new FileWriter(file_dpt));
		File file_pass1 = new File("pass1_output.txt");
		BufferedWriter out_pass1 = new BufferedWriter(new FileWriter(file_pass1));
		String str;
		String s[];
		
		
		/************PASS ONE****************/
		while(in.hasNextLine()){
			str = in.nextLine();
			s = split(str);
			if(s[0].equals("MACRO")){
				str = in.nextLine();
				s = split_name(str);
				mnt_list.add(new MNT(mnt_counter++,s[0],mdt_counter));
				mdt_list.add(new MDT(mdt_counter++,str));
				for(int i=1;i<s.length;i++){
					String temp[] = s[i].split("=");
					ala_list.add(new ALA(ala_counter++,temp[0]));
					if(temp.length>1) dpt_list.add(new DPT(ala_counter-1,temp[1]));
				}
				while(true){
					str = in.nextLine();
					s = split(str);
					if(s[0].equals("MEND")){
						mdt_list.add(new MDT(mdt_counter++,s[0]));
						break;
					}else{
						if(s[2].charAt(0)=='&'){
							int index = searchALA(s[2]);
							s[2] = "#"+index;
						}
						if(s[3].charAt(0)=='&'){
							int index = searchALA(s[3]);
							s[3] = "#"+index;
						}
						String line = s[0]+"\t"+s[1]+"\t"+s[2]+","+s[3];
						mdt_list.add(new MDT(mdt_counter++,line));
					}
				}
			}
			else out_pass1.write(str+"\n");
		}
		for(int i=0;i<mnt_list.size();i++){
			MNT temp = mnt_list.get(i);
			out_mnt.write(temp.index+"\t"+temp.name+"\t"+temp.mdt_index+"\n");
			out_mnt.flush();
		}
		for(int i=0;i<mdt_list.size();i++){
			MDT temp = mdt_list.get(i);
			out_mdt.write(temp.index+"\t"+temp.line+"\n");
			out_mdt.flush();
		}
		for(int i=0;i<ala_list.size();i++){
			ALA temp = ala_list.get(i);
			out_ala.write(temp.index+"\t"+temp.argument+"\n");
			out_ala.flush();
		}
		for(int i=0;i<dpt_list.size();i++){
			DPT temp = dpt_list.get(i);
			out_dpt.write(temp.index+"\t"+temp.parameter+"\n");
			out_dpt.flush();
		}
		out_pass1.close();
		
		
		/**************PASS TWO****************/
		input = new File("pass1_output.txt");
		in = new Scanner(input);
		File file_pass2 = new File("pass2_output.txt");
		BufferedWriter out_pass2 = new BufferedWriter(new FileWriter(file_pass2));
		while(in.hasNextLine()){
			str = in.nextLine();
			s = split(str);
			int macro_index = searchMNT(s[0]);
			if(macro_index!=-1){
				s = split_name(str);
				String macro_def = mdt_list.get(macro_index-1).line;
				String s_def[] = split_name(macro_def);
				ArrayList<ALA> ala2_list = new ArrayList<ALA>();
				for(int i=1;i<s.length;i++){
					if(s[i].charAt(0)=='&'){
						String temp[] = s[i].split("=");
						int ala_index = searchALA(temp[0]);
						ala2_list.add(new ALA(ala_index,temp[1]));
					}else{
						String temp[] = s_def[i].split("=");
						int ala_index = searchALA(temp[0]);
						ala2_list.add(new ALA(ala_index,s[i]));
					}
				}
				while(!mdt_list.get(macro_index).line.equals("MEND")){
					String temp = mdt_list.get(macro_index).line;
					for(int i=0;i<temp.length();i++){
						if(temp.charAt(i)=='#'){
							int start = i;
							while(i<temp.length()){
								if(temp.charAt(i)!=',')
									i++;
							}
							int param_index = Integer.parseInt(temp.substring(start+1,i));
							String param_name = "";
							boolean ala_found = false;
							for(int j=0;j<ala2_list.size();j++){
								ALA temp1 = ala2_list.get(j);
								if(temp1.index==param_index){
									param_name = temp1.argument;
									ala_found = true;
									break;
								}
							}
							if(!ala_found){
								param_name = searchDPT(param_index);
							}
							temp = temp.substring(0,start)+param_name+temp.substring(i,temp.length());
							i++;
						}
					}
					macro_index++;
					out_pass2.write(temp+"\n");
					out_pass2.flush();
				}
			}else out_pass2.write(str+"\n");
		}
		out_mnt.close();
		out_mdt.close();
		out_ala.close();
		out_dpt.close();
		out_pass2.close();
	}
	static String[] split(String s){
		int start=0;
		String tokens[] = new String[4];
		int k=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\t'){
				tokens[k++] = s.substring(start,i).trim();
				start=i; 
			}else if(s.charAt(i)==','){
				tokens[k++] = s.substring(start,i).trim();
				start=i+1;
			}else if(i==s.length()-1)
				tokens[k++] = s.substring(start,i+1).trim();
		}
		return tokens;
	}
	static String[] split_name(String s){
		int start=0;
		String tokens[] = new String[100];
		int k=0;
		for(int i=0;i<s.length();i++){
			if(s.charAt(i)=='\t'){
				tokens[k++] = s.substring(start,i).trim();
				start=i; 
			}else if(s.charAt(i)==','){
				tokens[k++] = s.substring(start,i).trim();
				start=i+1;
			}else if(i==s.length()-1)
				tokens[k++] = s.substring(start,i+1).trim();
		}
		String toReturn[] = new String[k];
		for(int i=0;i<k;i++)
			toReturn[i] = tokens[i];
		return toReturn;
	}
	static int searchALA(String s){
		for(int i=0;i<ala_list.size();i++){
			ALA temp = ala_list.get(i);
			if(temp.argument.equals(s))
				return temp.index;
		}
		return -1;
	}
	static String searchDPT(int index){
		for(int i=0;i<dpt_list.size();i++){
			DPT temp = dpt_list.get(i);
			if(temp.index==index)
				return temp.parameter;
		}
		return null;
	}
	static int searchMNT(String s){
		for(int i=0;i<mnt_list.size();i++){
			MNT temp = mnt_list.get(i);
			if(temp.name.equals(s))
				return temp.mdt_index;
		}
		return -1;
	}
}
