
 
import java.io.*;
import java.sql.*;
import java.util.logging.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.*;
@WebServlet("/search")
public class QueryServlet extends HttpServlet {
 
   
 
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();
 
      Connection conn = null;
      Statement stmt = null;
 
      try {
         // Retrieve and process request parameters: "" and "search"
         String brand = request.getParameter("brand");
         boolean hasAuthorParam = brand != null && !brand.equals("Select...");
         String searchWord = request.getParameter("search").trim();
         boolean hasSearchParam = searchWord != null && (searchWord.length() > 0);
 
         out.println("<html><head><title>Query Results</title></head><body style='background:linear-gradient(#fff1eb,#ace0f9);opacity:0.9;'>");
         out.println("<br/>");
         out.println("<br/>");
         out.println("<br/>");
         out.println("<br/>");
         out.println("<br/>");
         out.println("<h2><center>Query Results</center></h2>");
 
         if (!hasAuthorParam && !hasSearchParam) {  // No params present
            out.println("<h3>Please select an brand or enter a search term!</h3>");
            out.println("<p><center><a href='start'>Back to Select Menu</a></center></p>");
         } else {
            conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/emobileshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

            // Step 2: Allocate a 'Statement' object in the Connection
            stmt = conn.createStatement();
 
            // Form a SQL command based on the param(s) present
            StringBuilder sqlStr = new StringBuilder();  // more efficient than String
            sqlStr.append("SELECT * FROM mobiles WHERE qty > 0 AND (");
            if (hasAuthorParam) {
               sqlStr.append("brand = '").append(brand).append("'");
            }
            if (hasSearchParam) {
               if (hasAuthorParam) {
                  sqlStr.append(" OR ");
               }
               sqlStr.append("brand LIKE '%").append(searchWord)
                     .append("%' OR name LIKE '%").append(searchWord).append("%'");
            }
            sqlStr.append(") ORDER BY brand, name");
            //System.out.println(sqlStr);  // for debugging
            ResultSet rset = stmt.executeQuery(sqlStr.toString());
 
            if (!rset.next()) {  // Check for empty ResultSet (no book found)
               out.println("<h3>No phones found. Please try again!</h3>");
               out.println("<p><a href='start'>Back to Select Menu</a></p>");
            } else {
               // Print the result in an HTML form inside a table
               out.println("<form method='get' action='cart'><center>");
               out.println("<input type='hidden' name='todo' value='add' />");
               out.println("<table border='1' cellpadding='6'>");
               out.println("<tr>");
               out.println("<th>&nbsp;</th>");
               out.println("<th>BRAND</th>");
               out.println("<th>PRODUCT NAME</th>");
               out.println("<th>PRICE</th>");
               out.println("<th>QTY</th>");
               out.println("</tr>");
 
               // ResultSet's cursor now pointing at first row
               do {
                  // Print each row with a checkbox identified by book's id
                  String id = rset.getString("id");
                  out.println("<tr>");
                  out.println("<td><input type='checkbox' name='id' value='" + id + "' /></td>");
                  out.println("<td>" + rset.getString("brand") + "</td>");
                  out.println("<td>" + rset.getString("name") + "</td>");
                  out.println("<td>$" + rset.getString("price") + "</td>");
                  out.println("<td><input type='text' size='3' value='1' name='qty" + id + "' /></td>");
                  out.println("</tr>");
               } while (rset.next());
               out.println("</table><br />");
 
               // Submit and reset buttons
               out.println("<input type='submit' value='Add to My Shopping Cart' />");
               out.println("<input type='reset' value='CLEAR' /></center></form>");
 
               // Hyperlink to go back to search menu
               out.println("<p><center><a href='start'>Back to Select Menu</a><center></p>");
 
               // Show "View Shopping Cart" if cart is not empty
               HttpSession session = request.getSession(false); // check if session exists
               if (session != null) {
                  Cart cart;
                  synchronized (session) {
                     // Retrieve the shopping cart for this session, if any. Otherwise, create one.
                     cart = (Cart) session.getAttribute("cart");
                     if (cart != null && !cart.isEmpty()) {
                        out.println("<p><a href='cart?todo=view'>View Shopping Cart</a></p>");
                     }
                  }
               }
 
               out.println("</body></html>");
            }
         }
      } catch (SQLException ex) {
         out.println("<h3>Service not available. Please try again later!</h3></body></html>");
         Logger.getLogger(QueryServlet.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         out.close();
         try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();  // Return the connection to the pool
         } catch (SQLException ex) {
            Logger.getLogger(QueryServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
 
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      doGet(request, response);
   }
}
