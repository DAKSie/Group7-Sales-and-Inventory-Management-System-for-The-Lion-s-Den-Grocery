import View.LoginViewDesigner;

class Main {
    public static void main(String args[]) {
        DBContext.getConnection();
        LoginViewDesigner loginView = new LoginViewDesigner(); 
    }
}