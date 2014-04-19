import java.util.*;
import java.sql.*;
class Apriori{
	static ArrayList<ItemSet> l; //To store the itemsets
	static ArrayList<ItemSet> old_l; //To store a copy of the itemsets
	static ArrayList<Transaction> transactions; //To store transactions
	public static void main(String args[]) throws Exception{
		l = new ArrayList<ItemSet>();
		old_l = new ArrayList<ItemSet>();
		transactions = new ArrayList<Transaction>();
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the minimum support count: ");
		int minsup = sc.nextInt();
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		ResultSet rs;
		//Define the items in the Database
		Item all_items[] = {new Item("Milk"),new Item("Bread"),new Item("Butter"),new Item("Beer"),new Item("Jam")};
		rs = st.executeQuery("SELECT * FROM apriori");
		while(rs.next()){
			int tid = Integer.parseInt(rs.getString(1));
			Transaction t = new Transaction();
			for(int i=0;i<all_items.length;i++){//If value of item in DB is 1, then it is present, so add in t
				if(rs.getInt(i+2)==1)//we take i+2 cz 1 is ID of transaction so items start from 2
					t.items.add(all_items[i]);
			}
			transactions.add(t);
		}
		//Generate the C1
		for(int i=0;i<all_items.length;i++){
			ItemSet is = new ItemSet();
			is.items.add(all_items[i]); // Add single item in 1 itemset for C1
			is.count = countTrans(is.items); //Count the no of occurences of that item
			l.add(is);
		}
		
		int n=1;
		while(l.size()>0){ //Do until no more candidate elements are present
			old_l = l;
			System.out.println("C"+n+"=>");
			print(old_l);
			//Remove all elements without minimum support
			for(int i=old_l.size()-1;i>=0;i--){//Compulsory to go in reverse order, or error will occur
				if(old_l.get(i).count<minsup)
					old_l.remove(i);
			}
			old_l = l;
			System.out.println("L"+n+"=>");
			print(old_l);
			l = new ArrayList<ItemSet>();
			n++;
			for(ItemSet is:old_l){ // Add new items sets which can be paired
				addCandidates(is,n-2); //we pass n-2 as the no of matching element you want: for C3 1st element should match, hence 3-2=1
			}
		}
		System.out.println("The items most frequently bought together are: ");
		for(ItemSet is:old_l){
			for(Item i:is.items)
				System.out.print(i.name+" ");
			System.out.println();
		}
	}
	static void print(ArrayList<ItemSet> a){ //To print Ci and Li
		System.out.println("Count\tItems");
		for(ItemSet is:a){
			System.out.print(is.count+"\t");
			for(Item i:is.items){
				System.out.print(i.name+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	//Most imp function
	static void addCandidates(ItemSet is,int k){
		int i = old_l.indexOf(is);//While searching, we only want to search elements after the particular one
		i++;//Therfore i start with index of that element + 1
		for(;i<old_l.size();i++){
			ItemSet is1 = old_l.get(i);
			boolean valid=true;//Checks if the starting k elements of both itemsets match
			for(int j=0;j<k;j++){
				if(is1.items.get(j)!=is.items.get(j))
					valid=false;
			}
			if(valid){//If they match, generate the new itemset and add to L
				ItemSet is2 = new ItemSet(); //Suppose itemsets are {1,2,3} {1,2,5}
				for(int j=0;j<k;j++){
					is2.items.add(is.items.get(j)); //First add the k common items eg. {1,2}
				}
				is2.items.add(is.items.get(k)); //add last item from 1st itemset eg. 3
				is2.items.add(is1.items.get(k)); //add last item form 2nd itemset eg. 5
				is2.count = countTrans(is2.items); //So we get itemset {1,2,3,5}, and we calculate count
				l.add(is2);
			}
		}
	}
	static int countTrans(ArrayList<Item> items){
		int count=0;
		for(Transaction t:transactions){
			boolean found=true;//Check if all items are present in this transaction
			for(Item i:items){
				if(!t.items.contains(i)){
					found=false;
					break;
				}
			}
			if(found) count++;//Increment if all items are present
		}
		return count; //return the no of trans in which items were found
	}
}
class Transaction{
	ArrayList<Item> items;
	Transaction(){
		items = new ArrayList<Item>();
	}
}
class ItemSet{
	ArrayList<Item> items;
	int count; 
	ItemSet(){
		items = new ArrayList<Item>();
	}
}
class Item{
	String name;
	Item(String s){
		name = s;
	}
}