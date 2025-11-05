# migrations/create_sims_schema.py
import mysql.connector
from mysql.connector import errorcode

DB_NAME = 'sims'

TABLES = {}

TABLES['users'] = (
    """
    CREATE TABLE IF NOT EXISTS `users` (
      `user_id` INT AUTO_INCREMENT,
      `user_name` VARCHAR(255) NOT NULL,
      `user_phone` VARCHAR(255),
      `user_username` VARCHAR(255) NOT NULL,
      `user_password` VARCHAR(255) NOT NULL,
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (`user_id`),
      UNIQUE KEY `uk_user_username` (`user_username`)
    ) ENGINE=InnoDB;
    """
)

TABLES['Product'] = (
    """
    CREATE TABLE IF NOT EXISTS `Product` (
      `product_id` INT AUTO_INCREMENT,
      `product_name` VARCHAR(255) NOT NULL,
      `product_brand` VARCHAR(255),
      `product_price` DECIMAL(10,2),
      `product_markup` DECIMAL(10,2),
      `product_stock` INT(11),
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (`product_id`)
    ) ENGINE=InnoDB;
    """
)

TABLES['Sales'] = (
    """
    CREATE TABLE IF NOT EXISTS `Sales` (
      `sale_id` INT AUTO_INCREMENT,
      `product_id` INT NOT NULL,
      `sale_item` VARCHAR(255),
      `sale_price` DECIMAL(10,2),
      `sale_quantity` INT(11),
      `sale_total` DECIMAL(10,2),
      `sale_user` VARCHAR(255),
      `sale_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (`sale_id`),
      KEY `fk_sales_product` (`product_id`),
      CONSTRAINT `fk_sales_product`
        FOREIGN KEY (`product_id`)
        REFERENCES `Product` (`product_id`)
        ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB;
    """
)

TABLES['Inventory'] = (
    """
    CREATE TABLE IF NOT EXISTS `Inventory` (
      `inventory_id` INT AUTO_INCREMENT,
      `or_number` VARCHAR(255) NOT NULL,
      `product_id` INT NOT NULL,
      `inventory_quantity` INT(11),
      `unit_price` DECIMAL(10,2),
      `inventory_price` DECIMAL(10,2),
      `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (`inventory_id`),
      UNIQUE KEY `uk_or_number` (`or_number`),
      KEY `fk_inventory_product` (`product_id`),
      CONSTRAINT `fk_inventory_product`
        FOREIGN KEY (`product_id`)
        REFERENCES `Product` (`product_id`)
        ON DELETE RESTRICT ON UPDATE CASCADE
    ) ENGINE=InnoDB;
    """
)

def create_database(cursor):
    try:
        cursor.execute(
            f"CREATE DATABASE IF NOT EXISTS `{DB_NAME}` DEFAULT CHARACTER SET 'utf8mb4'"
        )
    except mysql.connector.Error as err:
        print(f"Failed creating database: {err}")
        exit(1)

def generate_or_number(prefix="SIM", sequence=1):
    """Generate OR number in XXX-000 format"""
    return f"{prefix}-{sequence:03d}"

def main():
    config = {
      'user': 'root',
      'password': '',  # fill your root password (or use dedicated user)
      'host': '127.0.0.1',
      'raise_on_warnings': True
    }

    try:
        cnx = mysql.connector.connect(**config)
        cursor = cnx.cursor()
        create_database(cursor)
        cursor.execute(f"USE `{DB_NAME}`")
        
        # Create tables
        for table_name in TABLES:
            table_sql = TABLES[table_name]
            print(f"Creating table {table_name}: ", end='')
            try:
                cursor.execute(table_sql)
                print("OK")
            except mysql.connector.Error as err:
                print(f"Failed: {err}")
        
        # Add sample data for testing - Filipino grocery items
        print("Adding Filipino grocery items...")
        
        # Sample users
        sample_users = [
            ("Juan Dela Cruz", "0917-123-4567", "juan", "password123"),
            ("Maria Santos", "0918-987-6543", "maria", "password456"),
            ("The Lion's Den Admin", "0927-555-1234", "admin", "admin123")
        ]
        
        for user in sample_users:
            try:
                cursor.execute(
                    "INSERT IGNORE INTO users (user_name, user_phone, user_username, user_password) VALUES (%s, %s, %s, %s)",
                    user
                )
            except mysql.connector.Error as err:
                print(f"Failed to add user {user[2]}: {err}")
        
        # Filipino grocery products - common items in Philippine sari-sari stores
        filipino_products = [
            # Rice and Grains
            ("Jasmine Rice", "Dona Maria", 55.00, 10.0, 100),
            ("Sinandomeng Rice", "Angelica", 52.00, 10.0, 80),
            ("Monggo Beans", "Local", 45.00, 15.0, 50),
            
            # Canned Goods
            ("Spanish Sardines", "555", 28.00, 20.0, 60),
            ("Corned Beef", "Purefoods", 45.00, 18.0, 40),
            ("Luncheon Meat", "Argentina", 35.00, 20.0, 45),
            ("Sardines in Tomato Sauce", "Ligo", 22.00, 15.0, 70),
            
            # Noodles and Pasta
            ("Pancit Canton", "Lucky Me", 15.00, 25.0, 120),
            ("Instant Noodles", "Mi Goreng", 12.00, 30.0, 150),
            ("Bihon", "Eagle", 35.00, 15.0, 40),
            ("Spaghetti", "Monterey", 45.00, 20.0, 35),
            
            # Condiments and Sauces
            ("Soy Sauce", "Silver Swan", 25.00, 25.0, 80),
            ("Fish Sauce", "Tipid Tip", 20.00, 20.0, 60),
            ("Vinegar", "Datu Puti", 18.00, 15.0, 70),
            ("Banana Ketchup", "Jufran", 35.00, 18.0, 45),
            ("Bagoong", "Barrio Fiesta", 65.00, 20.0, 30),
            
            # Snacks and Biscuits
            ("Skyflakes Crackers", "Skyflakes", 10.00, 40.0, 200),
            ("Nova Crackers", "Nova", 12.00, 35.0, 150),
            ("Pillows Snack", "Cloud 9", 8.00, 50.0, 180),
            ("Boy Bawang", "Boy Bawang", 5.00, 60.0, 250),
            ("Chippy", "Chippy", 10.00, 45.0, 120),
            
            # Beverages
            ("3-in-1 Coffee", "Nescafe", 8.00, 50.0, 200),
            ("Milk Tea", "Quickbrew", 12.00, 40.0, 100),
            ("Coke 1.5L", "Coca-Cola", 65.00, 15.0, 50),
            ("Royal 1.5L", "Royal", 65.00, 15.0, 45),
            ("Sprite 1.5L", "Sprite", 65.00, 15.0, 40),
            
            # Personal Care
            ("Shampoo sachet", "Sunsilk", 8.00, 50.0, 150),
            ("Toothpaste", "Colgate", 45.00, 25.0, 60),
            ("Bath Soap", "Safeguard", 25.00, 30.0, 80),
            ("Facial Cream", "Pond's", 15.00, 40.0, 70),
            
            # Household Items
            ("Laundry Bar", "Tide", 12.00, 35.0, 90),
            ("Fabric Conditioner", "Downy", 10.00, 40.0, 85),
            ("Zonrox 500ml", "Zonrox", 35.00, 20.0, 40),
            
            # Frozen Goods
            ("Hotdog", "Purefoods", 120.00, 15.0, 25),
            ("Tocino", "Pampanga's Best", 95.00, 18.0, 20),
            ("Longganisa", "Virginia", 110.00, 20.0, 18),
            
            # Fresh Produce (simulated)
            ("Garlic", "Local", 80.00, 25.0, 30),
            ("Onion", "Local", 60.00, 30.0, 40),
            ("Tomato", "Local", 45.00, 35.0, 35)
        ]
        
        for product in filipino_products:
            try:
                cursor.execute(
                    "INSERT IGNORE INTO Product (product_name, product_brand, product_price, product_markup, product_stock) VALUES (%s, %s, %s, %s, %s)",
                    product
                )
            except mysql.connector.Error as err:
                print(f"Failed to add product {product[0]}: {err}")
        
        # Sample inventory - matching product quantities with OR numbers
        print("Adding inventory data...")
        cursor.execute("SELECT product_id, product_stock FROM Product")
        products = cursor.fetchall()
        
        for i, (product_id, stock) in enumerate(products, 1):
            try:
                # Get product price for calculation
                cursor.execute("SELECT product_price FROM Product WHERE product_id = %s", (product_id,))
                product_price = cursor.fetchone()[0]
                unit_price = product_price
                inventory_price = unit_price * stock
                
                # Generate OR number
                or_number = generate_or_number("SIM", i)
                
                cursor.execute(
                    "INSERT IGNORE INTO Inventory (or_number, product_id, inventory_quantity, unit_price, inventory_price) VALUES (%s, %s, %s, %s, %s)",
                    (or_number, product_id, stock, unit_price, inventory_price)
                )
            except mysql.connector.Error as err:
                print(f"Failed to add inventory for product {product_id}: {err}")
        
        # Sample sales data - typical sari-sari store transactions
        print("Adding sample sales data...")
        sample_sales = [
            (1, "Jasmine Rice", 60.50, 5, 302.50, "juan"),
            (4, "Spanish Sardines", 33.60, 10, 336.00, "maria"),
            (8, "Pancit Canton", 18.75, 20, 375.00, "juan"),
            (15, "Soy Sauce", 31.25, 15, 468.75, "maria"),
            (20, "Skyflakes Crackers", 14.00, 30, 420.00, "juan"),
            (25, "3-in-1 Coffee", 12.00, 50, 600.00, "maria"),
            (30, "Shampoo sachet", 12.00, 25, 300.00, "juan"),
            (35, "Laundry Bar", 16.20, 20, 324.00, "maria"),
            (38, "Hotdog", 138.00, 5, 690.00, "juan"),
            (40, "Garlic", 100.00, 10, 1000.00, "maria")
        ]
        
        for sale in sample_sales:
            try:
                cursor.execute(
                    "INSERT IGNORE INTO Sales (product_id, sale_item, sale_price, sale_quantity, sale_total, sale_user) VALUES (%s, %s, %s, %s, %s, %s)",
                    sale
                )
            except mysql.connector.Error as err:
                print(f"Failed to add sale for product {sale[0]}: {err}")
        
        cnx.commit()
        print("Filipino grocery data added successfully!")
        
        # Display summary
        cursor.execute("SELECT COUNT(*) FROM users")
        user_count = cursor.fetchone()[0]
        
        cursor.execute("SELECT COUNT(*) FROM Product")
        product_count = cursor.fetchone()[0]
        
        cursor.execute("SELECT COUNT(*) FROM Sales")
        sales_count = cursor.fetchone()[0]
        
        cursor.execute("SELECT SUM(sale_total) FROM Sales")
        total_revenue = cursor.fetchone()[0] or 0
        
        cursor.execute("SELECT COUNT(*) FROM Inventory")
        inventory_count = cursor.fetchone()[0]
        
        print(f"\n=== Database Summary ===")
        print(f"Users: {user_count}")
        print(f"Products: {product_count}")
        print(f"Inventory Records: {inventory_count}")
        print(f"Sales Transactions: {sales_count}")
        print(f"Total Revenue: ₱{total_revenue:,.2f}")
        print("The Lion's Den Grocery is ready for business! 🛒")
        
        # Show sample OR numbers
        cursor.execute("SELECT or_number FROM Inventory LIMIT 5")
        sample_or_numbers = [row[0] for row in cursor.fetchall()]
        print(f"Sample OR Numbers: {', '.join(sample_or_numbers)}")
        
        cursor.close()
        cnx.close()
        print("Database setup completed successfully!")
        
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password.")
        else:
            print(err)
        exit(1)

if __name__ == '__main__':
    main()