import java.util.*;
import java.sql.*;
class Decoding{
	public static void main(String args[]) throws Exception{
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		Vector<Integer> id = new Vector<Integer>();
		Vector<String> day = new Vector<String>();
		ResultSet rs;
		
		rs = st.executeQuery("SELECT id,day FROM decoding");
		while(rs.next()){
			id.add(rs.getInt(1));
			day.add(rs.getString(2));
		}
		for(int i=0;i<id.size();i++){
			String temp = convert(day.get(i));
			st.executeUpdate("UPDATE decoding SET day='"+temp+"' WHERE id="+id.get(i));
		}
	}
	static String convert(String s){
		if(s.equalsIgnoreCase("Mon"))
			return "MONDAY";
		else if(s.equalsIgnoreCase("Tue"))
			return "TUESDAY";
		else if(s.equalsIgnoreCase("Wed"))
			return "WEDNESDAY";
		else if(s.equalsIgnoreCase("Thu"))
			return "THURSDAY";
		else if(s.equalsIgnoreCase("Fri"))
			return "FRIDAY";
		else if(s.equalsIgnoreCase("Sat"))
			return "SATURDAY";
		else if(s.equalsIgnoreCase("Sun"))
			return "SUNDAY";
		else return s;
	}
}