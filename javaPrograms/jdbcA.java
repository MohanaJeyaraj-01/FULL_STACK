import java.util.*;
import java.sql.*;
public class jdbcA
{
	public static void main(String[] args)
	{
		try{
		String url="jdbc:mysql://localhost:3306/Hello";
		String username="root";
		String password="root";
		String query="CREATE TABLE IF NOT EXISTS Hi(id INT AUTO_INCREMENT PRIMARY KEY , name VARCHAR(20),salary INT)";
		Connection con = DriverManager.getConnection(url,username,password);
		Statement stmt=con.createStatement();
		stmt.execute(query);
		query="INSERT INTO Hi(name,salary) VALUES('Karthik',10000)";
		stmt.execute(query);	
		stmt.close();
		con.close();
		}catch(SQLException e){
			e.printStackTrace();
			}
		
	}
}