Spreadsheet – Deliverable 2

This repository contains the code for Deliverable 2 of the Spreadsheet project.

How to run
- Java 17+.
- Open the project in IntelliJ and run `Program` (text menu), or compile and run from the command line.

What is implemented (per the brief)
- Domain classes: Spreadsheet, Cell, Coord, ColumnRef, Content (with TextContent, NumericContent, FormulaContent).
- Load/Save in S2V: S2VStorage (handles “;” ↔ “,” inside function arguments as specified).
- Text menu to set and view the content of cells (raw content only, no evaluation).

Not in this deliverable
- Formula evaluation, dependency recomputation, circular dependency detection, and function execution.
