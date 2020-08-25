//跳转页面
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
@WebServlet("/toregist")
public class toregist extends HttpServlet {
    	
    	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		response.setContentType("text/html");
    		response.sendRedirect("/aProject2/regist.html");
    	}
    
    	
    	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    		// TODO Auto-generated method stub
    		doGet(request, response);
    	}
    
}