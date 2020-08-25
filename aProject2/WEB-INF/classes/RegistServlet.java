import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
@WebServlet("/RegistServlet")
public class RegistServlet extends HttpServlet {
    	
    	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		
    		String username=request.getParameter("username");
    		String password=request.getParameter("password");
    		user u=new user();
    		u.setName(username);
    		u.setPassword(password);
    		userdao us=new userdao();
    		boolean t = false;
    	    try {
    			t=us.insert(u);
    		} catch (ClassNotFoundException | SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		if(t==true){
    			response.sendRedirect("/aProject2/form.html");
    		}else{
    			response.sendRedirect("/aProject2/regist.html");
    		}
    		
    		
    }
    
    	
    	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		// TODO Auto-generated method stub
    		doGet(request, response);
    	}
    
    }