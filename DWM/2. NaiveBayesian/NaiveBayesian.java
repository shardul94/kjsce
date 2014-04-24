import java.util.*;
import java.sql.*;
class NaiveBayesian{
	public static void main(String args[]) throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter outlook: ");
		String outlook = sc.nextLine();
		System.out.print("Enter temperature: ");
		String temperature = sc.nextLine();
		System.out.print("Enter humidity: ");
		String humidity = sc.nextLine();
		System.out.print("Is windy?: ");
		String windy = sc.nextLine();
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection con = DriverManager.getConnection("jdbc:odbc:dwm");
		Statement st = con.createStatement();
		ResultSet rs;
		
		rs = st.executeQuery("SELECT count(*) FROM naivebayes");
		rs.next();
		int c = rs.getInt(1);
		//For class play = yes
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='yes'");
		rs.next();
		int c1 = rs.getInt(1);
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='yes' AND outlook='"+outlook+"'");
		rs.next();
		int c1_o = rs.getInt(1);
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='yes' AND temperature='"+temperature+"'");
		rs.next();
		int c1_t = rs.getInt(1);
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='yes' AND humidity='"+humidity+"'");
		rs.next();
		int c1_h = rs.getInt(1);
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='yes' AND windy='"+windy+"'");
		rs.next();
		int c1_w = rs.getInt(1);
		double pc1 = (double)(c1_o*c1_t*c1_h*c1_w*c1)/(c1*c1*c1*c1*c);
		//For class play = no
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='no'");
		rs.next();
		int c2 = rs.getInt(1);
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='no' AND outlook='"+outlook+"'");
		rs.next();
		int c2_o = rs.getInt(1);
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='no' AND temperature='"+temperature+"'");
		rs.next();
		int c2_t = rs.getInt(1);
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='no' AND humidity='"+humidity+"'");
		rs.next();
		int c2_h = rs.getInt(1);
		rs = st.executeQuery("SELECT count(*) FROM naivebayes where play='no' AND windy='"+windy+"'");
		rs.next();
		int c2_w = rs.getInt(1);
		double pc2 = (double)(c2_o*c2_t*c2_h*c2_w*c2)/(c2*c2*c2*c2*c);
		
		if(pc1>pc2) System.out.println("Tennis should be played today");
		else System.out.println("Tennis should not be played today");
	}
}