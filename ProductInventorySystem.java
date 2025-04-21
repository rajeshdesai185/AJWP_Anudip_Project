import java.sql.*;
import java.util.*;

public class ProductInventorySystem {

    // 1. Product class
    static class Product {
        private int id;
        private String name;
        private double price;
        private int quantity;

        public Product(int id, String name, double price, int quantity) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    // 2. DB Connection
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/inventory_db", "root", "Raj@3280");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 3. DAO methods
    public static void addProduct(Product product) {
        String sql = "INSERT INTO products(name, price, quantity) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.executeUpdate();
            System.out.println("Product added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewProducts() {
        String sql = "SELECT * FROM products";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("ID | Name | Price | Quantity");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getDouble("price") + " | " +
                                rs.getInt("quantity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateProduct(Product product) {
        String sql = "UPDATE products SET name=?, price=?, quantity=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getQuantity());
            stmt.setInt(4, product.getId());
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("Product updated.");
            else
                System.out.println("Product not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("Product deleted.");
            else
                System.out.println("Product not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. Main method for menu
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Product Inventory System ---");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    sc.nextLine();
                    String name = sc.nextLine();
                    System.out.print("Enter price: ");
                    double price = sc.nextDouble();
                    System.out.print("Enter quantity: ");
                    int quantity = sc.nextInt();
                    addProduct(new Product(0, name, price, quantity));
                    break;

                case 2:
                    viewProducts();
                    break;

                case 3:
                    System.out.print("Enter product ID to update: ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new name: ");
                    String uname = sc.nextLine();
                    System.out.print("Enter new price: ");
                    double uprice = sc.nextDouble();
                    System.out.print("Enter new quantity: ");
                    int uquantity = sc.nextInt();
                    updateProduct(new Product(uid, uname, uprice, uquantity));
                    break;

                case 4:
                    System.out.print("Enter product ID to delete: ");
                    int did = sc.nextInt();
                    deleteProduct(did);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
