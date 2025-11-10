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
      `product_supplier` VARCHAR(255),
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
        
        cnx.commit()
        cursor.close()
        cnx.close()
        print("Database schema created successfully!")
        
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password.")
        else:
            print(err)
        exit(1)

if __name__ == '__main__':
    main()
