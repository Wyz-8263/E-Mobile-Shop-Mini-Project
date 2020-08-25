import java.io.*;
import java.sql.*;


public class jdbc {
	public static Connection getConnection() throws SQLException,
	ClassNotFoundException {
	
    String url="jdbc:mysql://localhost:3306/emobileshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    String username="myuser";
    String password="xxxx";
    Connection con=DriverManager.getConnection(url,username,password);
	return con;
	}
}