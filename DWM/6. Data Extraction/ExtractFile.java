import java.util.*;
import java.sql.*;
import java.io.File;
class ExtractFile{
	public static void main(String args[]) throws Exception{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Scanner in = new Scanner(new File("Data.txt"));
		Connection con1= DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st1= con1.createStatement();
	
		while(in.hasNextLine()){
			String line = in.nextLine();
			String s[] = line.split("\t");
			st1.executeUpdate("INSERT INTO extraction(name,age,branch,year,city) VALUES ('"+s[0]+"',"+Integer.parseInt(s[1])+",'"+s[2]+"','"+s[3]+"','"+s[4]+"')");
		}
	}
}