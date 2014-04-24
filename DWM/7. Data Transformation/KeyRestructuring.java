import java.util.*;
import java.sql.*;
class KeyRestructuring{
	public static void main(String args[]) throws Exception{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		Vector<Integer> id = new Vector<Integer>();
		ResultSet rs;
		
		rs = st.executeQuery("SELECT id FROM keyrestructuring");
		while(rs.next()){
			id.add(rs.getInt(1));
		}
		int count=1;
		for(int i:id){
			st.executeUpdate("UPDATE keyrestructuring SET new_key="+count+" WHERE id="+i);
			count++;
		}
	}
}