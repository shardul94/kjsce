import java.util.*;
import java.sql.*;
class AnnualSalary{
	public static void main(String args[]) throws Exception{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		Vector<Integer> id = new Vector<Integer>();
		Vector<Integer> salary = new Vector<Integer>();
		ResultSet rs;
		
		rs = st.executeQuery("SELECT id,salary FROM annualsalary");
		while(rs.next()){
			id.add(rs.getInt(1));
			salary.add(rs.getInt(2));
		}
		for(int i=0;i<id.size();i++){
			st.executeUpdate("UPDATE annualsalary SET annual_salary="+(salary.get(i)*12)+" WHERE id="+id.get(i));
		}
	}
}