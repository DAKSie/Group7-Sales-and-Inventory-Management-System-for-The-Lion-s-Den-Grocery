# Group7 Sales and Inventory Management System for The Lion's Den Grocery

[![Java](https://img.shields.io/badge/Java-92.6%25-blue.svg)](https://www.java.com/)
[![Python](https://img.shields.io/badge/Python-6.6%25-yellow.svg)](https://www.python.org/)
[![Batchfile](https://img.shields.io/badge/Batchfile-0.2%25-lightgrey.svg)](https://www.microsoft.com/windows)
[![Bash](https://img.shields.io/badge/Bash-0.2%25-lightgrey.svg)](https://www.microsoft.com/windows)

## Overview

**Local inventory & sales management app for small businesses. Tracks stock, records transactions, generates CSV reports. No internet required. Ideal for simple, offline operations.**

This application is designed for small grocery stores, providing an easy way to track stock items, record sales transactions, and generate reports, all without any internet dependency. Built primarily in Java, with additional scripting and optional Python utilities.

---

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [CSV Reporting](#csv-reporting)
- [Architecture](#architecture)
- [Contributing](#contributing)

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
- **Python** (3.12.5)
- **Windows OS** (Batchfile scripts included; may work on other platforms with small adjustments)
- **Bash** (Bash script included)
- **No Internet Connection Required**

---

## Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/DAKSie/Group7-Sales-and-Inventory-Management-System-for-The-Lion-s-Den-Grocery.git
   cd Group7-Sales-and-Inventory-Management-System-for-The-Lion-s-Den-Grocery
   ```

2. **Run the Database Migrations**
   Initialize the local SQLite schema.
   - On Windows (PowerShell or CMD):
     ```bash
     py migrations\SimsSchema.py
     ```
   - On macOS/Linux:
     ```bash
     python3 migrations/SimsSchema.py
     ```
     If `python3` isn’t available, try:
     ```bash
     python migrations/SimsSchema.py
     ```

3. **(Optional) Seed the Database with Example Data**
   If you’d like to explore the app with sample data for testing, use the available seed script (if provided) in the repository.
   - On Windows:
     ```bash
     py path\to\your\seed_script.py
     ```
   - On macOS/Linux:
     ```bash
     python3 path/to/your/seed_script.py
     ```
   Note: If a dedicated seed script isn’t provided, you can manually add a few items and sales via the app UI after launch.

4. **Build and Run the Java Application**
   We have implemented run scripts for convenience.

   - Windows:
     ```bash
     compile_and_run_windows
     ```

   - Bash:
     ```bash
     ./compile_and_run_bash.sh
     ```

5. **(Optional) Set Up Python Scripts**
   If you want enhanced reporting (see [CSV Reporting](#csv-reporting)), ensure you have Python 3+ installed.

6. **Run**
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

---

## CSV Reporting

- Export sales and inventory data to CSV files for external analysis or archival.
- Make sure the target directory has write permissions.
- Optional Python utilities can be used to post-process or aggregate CSV outputs.

---

## Architecture

- **Core Application:** Java-based desktop application.
- **Persistence:** Local database (initialized via `migrations/SimsSchema.py`).
- **Utilities:** Optional Python scripts for reporting or automation.
- **Scripts:** Convenience scripts for building and running on Windows and Bash environments.

---

## Contributing

Contributions are welcome! Please open an issue to discuss changes or submit a pull request with a clear description of your updates. For larger changes, consider proposing them first to align on approach.
