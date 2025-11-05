# Group7 Sales and Inventory Management System for The Lion's Den Grocery

[![Java](https://img.shields.io/badge/Java-93%25-blue.svg)](https://www.java.com/)
[![Python](https://img.shields.io/badge/Python-6.6%25-yellow.svg)](https://www.python.org/)
[![Batchfile](https://img.shields.io/badge/Batchfile-0.4%25-lightgrey.svg)](https://www.microsoft.com/windows)

## Overview

**Local inventory & sales management app for small businesses. Tracks stock, records transactions, generates CSV reports. No internet required. Ideal for simple, offline operations.**

This application is designed for small grocery stores, providing an easy way to track stock items, record sales transactions, and generate reports—**all without any internet dependency**. Built primarily in Java, with additional scripting and optional Python utilities.

---

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [CSV Reporting](#csv-reporting)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Inventory Management:** Add, update, or remove items from stock
- **Sales Recording:** Log transactions quickly as they happen
- **Reporting:** Generate CSV reports of sales and inventory status
- **Offline First:** Runs smoothly with no internet connection
- **Simple UI:** Designed for ease of use by non-technical users
- **Data Security:** Your data stays on your local machine
- **Extensible Design:** Optional scripting via Python for automation

---

## Requirements

- **Java** (8 or higher)
- **Python** (optional, for advanced reporting or scripting)
- **Windows OS** (Batchfile scripts included; may work on other platforms with small adjustments)
- **No Internet Connection Required**

---

## Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/DAKSie/Group7-Sales-and-Inventory-Management-System-for-The-Lion-s-Den-Grocery.git
   cd Group7-Sales-and-Inventory-Management-System-for-The-Lion-s-Den-Grocery
   ```

2. **Build the Java Application:**
    We have implemented a run script for windows
   - Or via command line:
     ```bash
     run
     ```

3. **(Optional) Set Up Python Scripts:**
   - If you want enhanced reporting (see [CSV Reporting](#csv-reporting)), ensure you have Python 3+ installed.

4. **Run:**
   - Windows: double-click the provided batch file(s) or execute from terminal.
   - Other OS: run the Java application as per above.

---

## Usage

- **Inventory Management:**
  - Add, edit, or delete inventory records from the app interface.
- **Sales:**
  - Record each transaction, specifying purchased items and quantities.
- **Reporting:**
  - Select "Generate Report" to save sales/inventory reports as CSV files.
- **No Internet Needed:** All actions are performed and stored locally.

---

## CSV Reporting

- Find exported CSV files in the `reports/` directory.
- Format follows:  
  - Sales: Date, Item, Quantity Sold, Price, Total
  - Inventory: Item, Current Stock, Unit Price, Last Updated
- Use provided Python utilities (see `scripts/`) to further analyze, filter, or visualize reports.

---

## Architecture

- **Java:** Main application logic, UI, and data storage
- **Python:** Optional analytics and enhanced reporting scripts
- **Batchfile:** Windows automation for launching the app and managing data
- **Data Storage:** CSV files (readable, editable, and portable)

---

## Contributing

Contributions, suggestions, and bug reports are welcome!

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Please abide by our [Code of Conduct](CODE_OF_CONDUCT.md) and ensure changes are tested locally.

---

## License

This project is licensed under the MIT License. See [`LICENSE`](LICENSE) for details.

---

## Contact

Maintainer: [DAKSie](https://github.com/DAKSie)  
For help or discussion, open an [issue](https://github.com/DAKSie/Group7-Sales-and-Inventory-Management-System-for-The-Lion-s-Den-Grocery/issues).

---

> Made with ❤️ by Group 7