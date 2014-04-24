import java.util.*;
import java.sql.*;
class Kmeans2D{
	public static void main(String args[]) throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter no. of clusters: ");
		int k = sc.nextInt();
		float m[][] = new float[k][2]; //To store means 2D array since 2D data
		float old_m[][] = new float[k][2];//Copy of means
		
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		ResultSet rs;
		rs = st.executeQuery("SELECT count(*) FROM kmeans2d");
		int n=0;
		if(rs.next()) n = rs.getInt(1);
		String names[] = new String[n];//TO Store names of 2D objects
		float data[][] = new float[n][2];//TO store 2D data
		int cluster[] = new int[n]; //cluster[i] indicates the cluster in which data[i] is present
		boolean mean_selected[] = new boolean[n];//to check if the items is already selected while choosing means randomly
		rs = st.executeQuery("SELECT * FROM kmeans2d");
		int x=0;
		while(rs.next()){
			names[x] = rs.getString(1);//Storing of names
			data[x][0] = rs.getFloat(2);//Storing of data
			data[x][1] = rs.getFloat(3);
			x++;
		}
		
		for(int i=0;i<k;i++){//Choosing means randomly
			int random = (int)(Math.random()*n)%n;
			if(mean_selected[random]==false){//Check if the items has already been selected as a mean
				m[i][0] = data[random][0];
				m[i][1] = data[random][1];
				mean_selected[random] = true;
			}else i--;
		}
		System.out.print("Assumed Means: ");
		for(int i=0;i<k;i++)
			System.out.print((i+1)+"=>("+m[i][0]+","+m[i][1]+") ");
		System.out.println("\n");
		
		while(!isSame(m,old_m)){//Do until both old means n new are same
			for(int i=0;i<k;i++){//Copy means to old_m
				old_m[i][0] = m[i][0];
				old_m[i][1] = m[i][1];
			}
			
			for(int i=0;i<n;i++){
				double min_diff=32767;//Check which is the closest mean
				for(int j=0;j<k;j++){
					double diff = Math.sqrt((m[j][0]-data[i][0])*(m[j][0]-data[i][0])+(m[j][1]-data[i][1])*(m[j][1]-data[i][1]));//Euclidian Distance: sqrt(a^2-b^2)
					if(diff<min_diff){
						min_diff = diff;
						cluster[i] = j;//Assign the cluster j to data[i]
					}
				}
			}
			
			for(int i=0;i<k;i++){//Recalculation of means and also printing of clusters
				float sumx=0;
				float sum[] = new float[2];
				int num_of_ele = 0;
				System.out.print("Cluster "+(i+1)+": ");
				for(int j=0;j<n;j++)
					if(cluster[j]==i){
						System.out.print(names[j]+" ");
						sum[0] += data[j][0];
						sum[1] += data[j][1];
						num_of_ele++;
					}
				System.out.println();
				m[i][0] = sum[0]/num_of_ele;
				m[i][1] = sum[1]/num_of_ele;
			}
			System.out.print("Means: ");//Printing of means
			for(int i=0;i<k;i++)
				System.out.print((i+1)+"=>("+m[i][0]+","+m[i][1]+") ");
			System.out.println("\n");
		}
	}
	static boolean isSame(float a[][],float b[][]){//returns true if both arrays are exactly same
		boolean valid = true;
		for(int i=0;i<a.length;i++)
			if(a[i][0]!=b[i][0]||a[i][1]!=b[i][1]){
				valid=false;
				break;
			}
		return valid;
	}
}