import java.io.*;
import java.sql.*;


//import user;
//import jdbc;

public class userdao{
	public boolean select(user u) throws ClassNotFoundException, SQLException{
		int x=0;
		jdbc j=new jdbc();
        Connection con=j.getConnection();
        Statement stmt=(Statement) con.createStatement();
		String sql="select Name,Password from userpassword";
    	ResultSet rs=stmt.executeQuery(sql);

    	while(rs.next()){
    		System.out.println(u.getName()+rs.getString(1));
    		if(u.getName().equals(rs.getString(1))){
    			System.out.println("can compare now");
    			if(u.getPassword().equals(rs.getString(2))){
    				System.out.println("match successfully");
    				x=1;
    				break;
    			}
    	   }
    	}

    	if(x==1)
    		return true;
    	else
    		return false;
	}
    public boolean insert(user u) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		jdbc j=new jdbc();
        Connection con=j.getConnection();
        Statement stmt=(Statement) con.createStatement();
		System.out.println("get connection");
        
    	String sql="insert into userpassword values('"+u.getName()+"','"+u.getPassword()+"')";
    	   int s=stmt.executeUpdate(sql);
    	if(s>0)
    		return true;
    	return false;
	}


}