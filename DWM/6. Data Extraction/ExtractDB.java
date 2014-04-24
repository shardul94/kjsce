import java.util.*;
import java.sql.*;
class ExtractDB{
	public static void main(String args[]) throws Exception{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:extractdb");
		Connection con1= DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		Statement st1= con1.createStatement();
		ResultSet rs;
		
		rs = st.executeQuery("SELECT * FROM student");
		while(rs.next()){
			st1.executeUpdate("INSERT INTO extraction(name,age,branch,year,city) VALUES ('"+rs.getString(1)+"',"+rs.getInt(2)+",'"+rs.getString(3)+"','"+rs.getString(4)+"','"+rs.getString(5)+"')");
		}
	}
}