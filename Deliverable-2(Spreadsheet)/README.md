# Spreadsheet — Deliverable 2

## How to run
- Java 17+.
- Open the project in IntelliJ and run **`Program`** (text menu), or compile from CLI:
  - `javac *.java`
  - `java Program`

## Implemented (per brief)
- **Domain classes:** `Spreadsheet`, `Cell`, `Coord`, `ColumnRef`, `Content` (`TextContent`, `NumericContent`, `FormulaContent`).
- **Load / Save (S2V):** `S2VStorage` with `;` ↔ `,` mapping inside function arguments.
- **Text menu:** set and view **raw content** of cells (no evaluation).

## Not in this deliverable
- Formula evaluation, dependency recomputation, circular dependency detection, function execution.
