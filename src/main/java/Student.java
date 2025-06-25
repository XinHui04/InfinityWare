import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private ShoppingCart cart;
    private OrderHistory order;
    private Receipt receipt;
    private double discountRate;

    public Student(String username, String phoneNumber, String email, String password, String userType) {
        super(username, phoneNumber, email, password, userType);
        this.cart = new ShoppingCart();
        this.order = new OrderHistory();
        this.receipt = new Receipt();
        this.discountRate = 0.10;
    }
    
    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public boolean isCustomer() {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean isStudent() {
        return true;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public OrderHistory getOrder() {
        return order;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    @Override
    public void viewProducts(ShoppingCart cart, String category, List<Product> products) {
        super.viewProducts(cart, category, products);
    }

    @Override
    public void viewCart(ShoppingCart cart) {
        super.viewCart(cart);
    }

    @Override
    public void addToCart(ShoppingCart cart, Product product, int quantity) {
        super.addToCart(cart, product, quantity);
    }

    @Override
    public void removeFromCart(CartItem cartItem, ShoppingCart cart, String productID, int quantity) {
        super.removeFromCart(cartItem, cart, productID, quantity);
    }
    
    @Override
    public void generateReceipt(Receipt receipt, List<CartItem> selectedItems, LocalDate deliveryDate, double total) {
        super.generateReceipt(receipt, selectedItems, deliveryDate, total);
    }

    @Override
    public void viewOrderHistory(OrderHistory orderHistory) {
        super.viewOrderHistory(orderHistory);
    }

    @Override
    public String toString() {
        return username + "," + phoneNumber + "," + email + "," + password;
    }
}