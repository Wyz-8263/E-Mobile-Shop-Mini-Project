import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    	private static final long serialVersionUID = 1L;
    
    	
    	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	   user u=new user();
    		String username=request.getParameter("username");
    		String password=request.getParameter("password");
    		u.setName(username);
    		u.setPassword(password);
    		
    		userdao us=new userdao();
    	      
    	       boolean x=false;
    	       try {
    			x=us.select(u);
    		} catch (ClassNotFoundException | SQLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		
    		if(x==true){
    			response.sendRedirect("http://localhost:9999/aProject2/start");
    		}else{
    			response.sendRedirect("/aProject2/form.html");
    		}
    		
    	}
    
    	
    	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		// TODO Auto-generated method stub
    		doGet(request, response);
    	}
    
    }