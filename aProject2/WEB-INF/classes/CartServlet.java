
import java.io.*;
import java.sql.*;
import java.util.logging.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
//import javax.sql.DataSource;
@WebServlet("/cart")
public class CartServlet extends HttpServlet {
 
   
 
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();
 
      // Retrieve current HTTPSession object. If none, create one.
      HttpSession session = request.getSession(true);
      Cart cart;
      synchronized (session) {  // synchronized to prevent concurrent updates
         // Retrieve the shopping cart for this session, if any. Otherwise, create one.
         cart = (Cart) session.getAttribute("cart");
         if (cart == null) {  // No cart, create one.
            cart = new Cart();
            session.setAttribute("cart", cart);  // Save it into session
         }
      }
 
      Connection conn   = null;
      Statement  stmt   = null;
      ResultSet  rset   = null;
      String     sqlStr = null;
 
      try {
         conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/emobileshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         stmt = conn.createStatement();
 
         out.println("<html><head><title>Shopping Cart</title></head><body style='background:linear-gradient(#fff1eb,#ace0f9);opacity:0.7;'>");
         out.println("<br/>");
         out.println("<br/>");
         out.println("<br/>");
         out.println("<h2><center>Your Shopping Cart</center></h2>");
 
         // This servlet handles 4 cases:
         // (1) todo=add id=1001 qty1001=5 [id=1002 qty1002=1 ...]
         // (2) todo=update id=1001 qty1001=5
         // (3) todo=remove id=1001
         // (4) todo=view
 
         String todo = request.getParameter("todo");
         if (todo == null) todo = "view";  // to prevent null pointer
 
         if (todo.equals("add") || todo.equals("update")) {
            // (1) todo=add id=1001 qty1001=5 [id=1002 qty1002=1 ...]
            // (2) todo=update id=1001 qty1001=5
            String[] ids = request.getParameterValues("id");
            if (ids == null) {
               out.println("<h3>Please Select a Phone!</h3></body></html>");
               return;
            }
            for (String id : ids) {
               sqlStr = "SELECT * FROM mobiles WHERE id = " + id;
               //System.out.println(sqlStr);  // for debugging
               rset = stmt.executeQuery(sqlStr);
               rset.next(); // Expect only one row in ResultSet
               String name = rset.getString("name");
               String brand = rset.getString("brand");
               float price = rset.getFloat("price");
 
               // Get quantity ordered - no error check!
               int qtyOrdered = Integer.parseInt(request.getParameter("qty" + id));
               int idInt = Integer.parseInt(id);
               if (todo.equals("add")) {
                  cart.add(idInt, name, brand, price, qtyOrdered);
               } else if (todo.equals("update")) {
                  cart.update(idInt, qtyOrdered);
                  if (qtyOrdered<=0){
                     cart.remove(idInt);
                  }
               }
            }
 
         } 
 
         // All cases - Always display the shopping cart
         if (cart.isEmpty()) {
            out.println("<p>Your shopping cart is empty</p>");
         } else {
            out.println("<center><table border='1' cellpadding='6'>");
            out.println("<tr>");
            out.println("<th>BRAND</th>");
            out.println("<th>PRODUCT NAME</th>");
            out.println("<th>PRICE</th>");
            out.println("<th>QTY</th>");
            //out.println("<th>REMOVE</th></tr>");
            out.println("</tr>");
 
            float totalPrice = 0f;
            for (CartItem item : cart.getItems()) {
               int id = item.getId();
               String brand = item.getBrand();
               String name = item.getName();
               float price = item.getPrice();
               int qtyOrdered = item.getQtyOrdered();
 
               out.println("<tr>");
               out.println("<td>" + brand + "</td>");
               out.println("<td>" + name +  "</td>");
               out.println("<td>" + price +  "</td>");
 
               out.println("<td><form method='get'>");
               out.println("<input type='hidden' name='todo' value='update' />");
               out.println("<input type='hidden' name='id' value='" + id + "' />");
               out.println("<input type='text' size='3' name='qty"
                       + id + "' value='" + qtyOrdered + "' />" );
               out.println("<input type='submit' value='Update' />");
               out.println("</form></td>");
 
               
               out.println("</tr>");
               totalPrice += price * qtyOrdered;
            }
            out.println("<tr><td colspan='5' align='right'>Total Price: $");
            out.printf("%.2f</td></tr>", totalPrice);
            out.println("</table></center>");
         }
 
         out.println("<p><center><a href='start'>Select More Items...</a></center></p>");
 
         // Display the Checkout
         if (!cart.isEmpty()) {
            out.println("<br /><br />");
            out.println("<form method='get' action='checkout'><center>");
            
            out.println("<p>Please fill in your particular before checking out:</p>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<td>Enter your Name:</td>");
            out.println("<td><input type='text' name='cust_name' /></td></tr>");
            out.println("<tr>");
            out.println("<td>Enter your Address:</td>");
            out.println("<td><input type='text' name='cust_address' /></td></tr>");
            out.println("<tr>");
            out.println("<td>Enter your Phone Number:</td>");
            out.println("<td><input type='text' name='cust_phone' /></td></tr>");
            out.println("</table>");
            out.println("<br /><br />");
            out.println("<input type='submit' value='CHECK OUT'>");
            out.println("</center></form>");
         }
 
         out.println("</body></html>");
 
      } catch (SQLException ex) {
         out.println("<h3>Service not available. Please try again later!</h3></body></html>");
         Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         out.close();
         try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();  // return the connection to the pool
         } catch (SQLException ex) {
            Logger.getLogger(CartServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
 
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      doGet(request, response);
   }
}
