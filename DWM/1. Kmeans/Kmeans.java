import java.util.*;
import java.sql.*;
class Kmeans{
	public static void main(String args[]) throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter no. of clusters: ");
		int k = sc.nextInt();
		float m[] = new float[k]; //To store means
		float old_m[] = new float[k]; //Copy of means
		
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		ResultSet rs;
		rs = st.executeQuery("SELECT count(*) FROM kmeans");
		int n=0;
		if(rs.next()) n = rs.getInt(1);
		float data[] = new float[n]; //TO store data
		int cluster[] = new int[n]; //cluster[i] indicates the cluster in which data[i] is present
		boolean mean_selected[] = new boolean[n];//to check if the items is already selected while choosing means randomly
		rs = st.executeQuery("SELECT * FROM kmeans");
		int x=0;
		while(rs.next())
			data[x++] = rs.getFloat(1);//Storing of data
		
		for(int i=0;i<k;i++){//Choosing means randomly
			int random = (int)(Math.random()*n)%n;
			if(mean_selected[random]==false){//Check if the items has already been selected as a mean
				m[i] = data[random];
				mean_selected[random] = true;
			}else i--;
		}
		System.out.print("Assumed Means: ");
		for(int i=0;i<k;i++)
			System.out.print((i+1)+"=>"+m[i]+" ");
		System.out.println("\n");
		
		while(!isSame(m,old_m)){//Do until both old means n new are same
			for(int i=0;i<k;i++)//Copy means to old_m
				old_m[i] = m[i];
			
			for(int i=0;i<n;i++){
				float min_diff=32767;//Check which is the closest mean
				for(int j=0;j<k;j++)
					if(Math.abs(m[j]-data[i])<min_diff){
						min_diff = Math.abs(m[j]-data[i]);
						cluster[i] = j;//Assign the cluster j to data[i]
					}
			}
			
			for(int i=0;i<k;i++){//Recalculation of means and also printing of clusters
				float sum=0;
				int num_of_ele = 0;
				System.out.print("Cluster "+(i+1)+": ");
				for(int j=0;j<n;j++)
					if(cluster[j]==i){
						System.out.print(data[j]+" ");
						sum += data[j];
						num_of_ele++;
					}
				System.out.println();
				m[i] = sum/num_of_ele;
			}
			System.out.print("Means: ");//Printing of means
			for(int i=0;i<k;i++)
				System.out.print((i+1)+"=>"+m[i]+" ");
			System.out.println("\n");
		}
	}
	static boolean isSame(float a[],float b[]){//returns true if both arrays are exactly same
		boolean valid = true;
		for(int i=0;i<a.length;i++)
			if(a[i]!=b[i]){
				valid=false;
				break;
			}
		return valid;
	}
}