
/**
 * The class CartItem models an item in the Cart.
 * This class shall not be accessed by the controlling logic directly.
 * Instead Use Cart.add() or Cart.remove() to add or remove an item from the Cart.
 */
public class CartItem {
 
   private int id;
   private String name;
   private String brand;
   private float price;
   private int qtyOrdered;
 
   // Constructor
   public CartItem(int id, String name,
           String brand, float price, int qtyOrdered) {
      this.id = id;
      this.name = name;
      this.brand = brand;
      this.price = price;
      this.qtyOrdered = qtyOrdered;
   }
 
   public int getId() {
      return id;
   }
 
   public String getBrand() {
      return brand;
   }
 
   public String getName() {
      return name;
   }
 
   public float getPrice() {
      return price;
   }
 
   public int getQtyOrdered() {
      return qtyOrdered;
   }
 
   public void setQtyOrdered(int qtyOrdered) {
      this.qtyOrdered = qtyOrdered;
   }
}