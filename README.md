# University Inventory Management System

A **console-based Java application** for managing university inventory items (Equipment, Furniture, and Lab Equipment) and assigning equipment to staff members. The project demonstrates **object‑oriented programming**, **exception handling**, **arrays**, **searching**, and **report generation using different loop types**.

---

## Features

* Add and manage different inventory item types using **polymorphism**

  * Equipment
  * Furniture
  * LabEquipment
* Register staff members and assign equipment (max **5 items per staff**)
* Prevent invalid assignments using **custom exceptions**
* Search equipment by:

  * Name
  * Category (with optional availability filter)
  * Warranty range
* Generate reports using different loop constructs:

  * Inventory summary (for loop)
  * Expired warranties (while loop)
  * Assignments by department (foreach loop)
  * Utilisation rate (nested loops)
  * Maintenance schedule (do‑while loop)
* Menu‑driven user interface with input validation

---

## Project Structure

```
UniversityInventorySystem.java   // Main application (menu & UI)

models/
 ├─ InventoryItem.java           // Abstract base class
 ├─ Equipment.java               // Equipment model
 ├─ Furniture.java               // Furniture model
 ├─ LabEquipment.java            // Lab equipment model
 └─ StaffMember.java             // Staff model

managers/
 ├─ InventoryManager.java        // Core inventory & assignment logic
 └─ InventoryReports.java        // Reporting functions

exceptions/
 ├─ InventoryException.java
 ├─ EquipmentNotAvailableException.java
 ├─ AssignmentLimitExceededException.java
 └─ StaffMemberNotFoundException.java
```

---

## Core Design

### InventoryItem (Abstract Class)

Base class for all inventory items.

Common fields:

* `id`
* `name`
* `isAvailable`

Implemented by:

* `Equipment`
* `Furniture`
* `LabEquipment`

---

### Equipment

Extends `InventoryItem` and adds:

* `assetId`
* `brand`
* `category`
* `warrantyMonths`

Supports searching by category, availability, and warranty period.

---

### StaffMember

Represents university staff.

Key rules:

* Each staff member can hold **up to 5 equipment items**
* Equipment is stored in a fixed‑size array

---

### InventoryManager

Handles:

* Adding inventory items
* Registering staff members
* Assigning and returning equipment
* Searching equipment
* Validation logic for assignments

Uses **custom exceptions** to enforce business rules.

---

### InventoryReports

Generates analytical reports demonstrating different loop types required by coursework.

---

## Exception Handling

Custom exceptions are used to keep logic clean and readable:

* `InventoryException` (base exception)
* `StaffMemberNotFoundException`
* `EquipmentNotAvailableException`
* `AssignmentLimitExceededException`

All exceptions are handled gracefully in the main menu loop.

---

## How to Run

1. Compile all Java files:

```bash
javac UniversityInventorySystem.java
```

2. Run the application:

```bash
java UniversityInventorySystem
```

3. Follow the on‑screen menu prompts.

---

## Demo Data

The system automatically loads sample data on startup, including:

* Staff members from different departments
* Equipment, furniture, and lab equipment
* Items with expired and valid warranties

This allows immediate testing without manual data entry.

---

## Learning Outcomes Demonstrated

* Object‑Oriented Programming (inheritance, polymorphism, encapsulation)
* Exception handling and validation
* Array management (no collections used)
* Menu‑driven console applications
* Clean separation of concerns (models, managers, reports)

