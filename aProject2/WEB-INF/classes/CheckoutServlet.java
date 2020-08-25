
import java.io.*;
import java.sql.*;
import java.util.logging.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
//import javax.sql.DataSource;
@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
 
   
 
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();
 
      Connection conn = null;
      Statement stmt = null;
      ResultSet rset = null;
      String sqlStr = null;
      HttpSession session = null;
      Cart cart = null;
 
      try {
         conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/emobileshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "myuser", "xxxx");   // For MySQL
               // The format is: "jdbc:mysql://hostname:port/databaseName", "username", "password"

         // Step 2: Allocate a 'Statement' object in the Connection
         stmt = conn.createStatement();
 
         out.println("<html><head><title>Checkout</title></head><body style='background:linear-gradient(#fff1eb,#ace0f9);opacity:0.7;'>");
         out.println("<br/>");
         out.println("<br/>");
         out.println("<br/>");
         out.println("<h2><center>E-Shop - Checkout</center></h2>");
 
         // Retrieve the Cart
         session = request.getSession(false);
         if (session == null) {
            out.println("<h3>Your Shopping cart is empty!</h3></body></html>");
            return;
         }
         synchronized (session) {
            cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
               out.println("<h3>Your Shopping cart is empty!</h3></body></html>");
               return;
            }
         }
 
         // Retrieve and process request parameters: id(s), cust_name, cust_email, cust_phone
         String custName = request.getParameter("cust_name");
         boolean hasCustName = custName != null && ((custName = custName.trim()).length() > 0);
         String custAddress = request.getParameter("cust_address").trim();
         boolean hasCustAddress = custAddress != null && ((custAddress = custAddress.trim()).length() > 0);
         String custPhone = request.getParameter("cust_phone").trim();
         boolean hasCustPhone = custPhone != null && ((custPhone = custPhone.trim()).length() > 0);
 
         // Validate inputs
         if (!hasCustName) {

            out.println("<h3>Please Enter Your Name!</h3></body></html>");
            return;
         } else if (!hasCustAddress ) {
            out.println("<h3>Please Enter Your Address!</h3></body></html>");
            return;
         } else if (!hasCustPhone || custPhone.length() != 8) {
            out.println("<h3>Please Enter an 8-digit Phone Number!</h3></body></html>");
            return;
         }
 
         // Display the name, email and phone (arranged in a table)
         out.println("<center><table>");
         out.println("<tr>");
         out.println("<td>Customer Name:</td>");
         out.println("<td>" + custName + "</td></tr>");
         out.println("<tr>");
         out.println("<td>Customer Address:</td>");
         out.println("<td>" + custAddress + "</td></tr>");
         out.println("<tr>");
         out.println("<td>Customer Phone Number:</td>");
         out.println("<td>" + custPhone + "</td></tr>");
         out.println("</table></center>");
 
         // Print the book(s) ordered in a table
         out.println("<br />");
         out.println("<center><table border='1' cellpadding='6'>");
         out.println("<tr>");
         out.println("<th>BRAND</th>");
         out.println("<th>PRODUCT NAME</th>");
         out.println("<th>PRICE</th>");
         out.println("<th>QTY</th></tr>");
 
         float totalPrice = 0f;
         for (CartItem item : cart.getItems()) {
            int id = item.getId();
            String brand = item.getBrand();
            String name = item.getName();
            int qtyOrdered = item.getQtyOrdered();
            float price = item.getPrice();
 
            // No check for price and qtyAvailable change
            // Update the books table and insert an order record
            sqlStr = "UPDATE mobiles SET qty = qty - " + qtyOrdered + " WHERE id = " + id;
            //System.out.println(sqlStr);  // for debugging
            stmt.executeUpdate(sqlStr);
 
            sqlStr = "INSERT INTO order_records values ("
                    + id + ", " + qtyOrdered + ", '" + custName + "', '"
                    + custAddress + "', '" + custPhone + "')";
            //System.out.println(sqlStr);  // for debugging
            stmt.executeUpdate(sqlStr);
 
            // Show the book ordered
            out.println("<tr>");
            out.println("<td>" + brand + "</td>");
            out.println("<td>" + name + "</td>");
            out.println("<td>" + price + "</td>");
            out.println("<td>" + qtyOrdered + "</td></tr>");
            totalPrice += price * qtyOrdered;
         }
         out.println("<tr><td colspan='4' align='right'>Total Price: $");
         out.printf("%.2f</td></tr>", totalPrice);
         out.println("</table></center>");
 
         out.println("<h3><center>Payment: Cash on delivery</center></h3>");
         out.println("<center><a href='start'>Back to Search Menu</a></center>");
         out.println("</body></html>");
 
         cart.clear();   // empty the cart
      } catch (SQLException ex) {
         cart.clear();   // empty the cart
         out.println("<h3>Service not available. Please try again later!</h3></body></html>");
         Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         out.close();
         try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();  // Return the connection to the pool
         } catch (SQLException ex) {
            Logger.getLogger(CheckoutServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
 
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      doGet(request, response);
   }
}
