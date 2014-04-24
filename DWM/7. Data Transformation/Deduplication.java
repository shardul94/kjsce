import java.util.*;
import java.sql.*;
class Deduplication{
	public static void main(String args[]) throws Exception{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		Vector<Integer> id = new Vector<Integer>();
		Vector<String> data = new Vector<String>();
		ResultSet rs;
		
		rs = st.executeQuery("SELECT * FROM deduplication");
		while(rs.next()){
			id.add(rs.getInt(1));
			data.add(rs.getString(2)+","+rs.getString(3));
		}
		for(int i=0;i<data.size();i++){
			String tempData = data.get(i);
			boolean duplicate = false;
			for(int j=0;j<i;j++)
				if(tempData.equals(data.get(j))){
					duplicate = true;
					break;
				}
			if(duplicate)
				st.executeUpdate("DELETE FROM deduplication WHERE id="+id.get(i));
		}
	}
}