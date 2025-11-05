import Controller.DBContext;
import View.LoginView;

class Main {
    public static void main(String args[]) {
        try {
            // Test database connection
            System.out.println("Testing database connection...");
            if (DBContext.getConnection() != null) {
                System.out.println("Database connected successfully");
            } else {
                System.out.println("Database connection failed - continuing without database");
            }
            
            // Create login view
            System.out.println("Starting application...");
            new LoginView();
            
        } catch (Exception e) {
            System.err.println("Fatal error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}