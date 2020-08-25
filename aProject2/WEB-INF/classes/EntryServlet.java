
import java.io.*;
import java.sql.*;
import java.util.logging.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
//import javax.sql.DataSource;
@WebServlet("/start")
public class EntryServlet extends HttpServlet {
 
   
 
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();
 
      Connection conn = null;
      Statement stmt = null;
      try {
         conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/emobileshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         stmt = conn.createStatement();
         String sqlStr = "SELECT DISTINCT brand FROM mobiles WHERE qty > 0";
         ResultSet rset = stmt.executeQuery(sqlStr);
 
         out.println("<html><head><title>Welcome to Yuzhen and Joy's Eshop</title></head><body style='background:linear-gradient(#fff1eb,#ace0f9);opacity:0.8;'>");
         out.println("<br/>");
         
         out.println(" <h2><center>Welcome to Yuzhen and Joy's E-MobileShop   </center></h2>");
         out.println("<h4><center><a href='http://localhost:9999/aProject2/form.html'>Log Out</a></center></h4>");
         

        
         out.println("<br/>");
         out.println("<br/>");
         out.println("<br/>");
         out.println(" <form method='get' action='search'><center>");
 
         // A pull-down menu of all the authors with a no-selection option
         out.println("Choose a Brand: <select name='brand' size='1'>");
         out.println("<option value=''>Select...</option>");  // no-selection
         while (rset.next()) {  // list all the authors
            String brand = rset.getString("brand");
            out.println("<option value='" + brand + "'>" + brand + "</option>");
         }
         out.println("</select><br />");
         out.println("<p>OR</p>");
 
         // A text field for entering search word for pattern matching
         out.println("Search \"Product Name\" or \"Brand\": <input type='text' name='search' />");
 
         // Submit and reset buttons
         out.println("<br /><br />");
         out.println("<input type='submit' value='SEARCH' />");
         out.println("<input type='reset' value='CLEAR' />");
         out.println("</center></form>");
 
         // Show "View Shopping Cart" if the cart is not empty
         HttpSession session = request.getSession(false); // check if session exists
         if (session != null) {
            Cart cart;
            synchronized (session) {
               // Retrieve the shopping cart for this session, if any. Otherwise, create one.
               cart = (Cart) session.getAttribute("cart");
               if (cart != null && !cart.isEmpty()) {
                  out.println("<P><a href='cart?todo=view'>View Shopping Cart</a></p>");
               }
            }
         }
 
         out.println("</body></html>");
      } catch (SQLException ex) {
         out.println("<h3>Service not available. Please try again later!</h3></body></html>");
         Logger.getLogger(EntryServlet.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         out.close();
         try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();  // Return the connection to the pool
         } catch (SQLException ex) {
            Logger.getLogger(EntryServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
 
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      doGet(request, response);
   }
}
