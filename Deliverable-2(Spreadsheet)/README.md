# Spreadsheet — Deliverable 2

## How to run
- Java 17+.
- Open the project in IntelliJ and run **`Program`** (text menu):
  - `javac *.java`
  - `java Program`

## Implemented (per deliverable 2 question on atenea)
- **Domain classes:** `Spreadsheet`, `Cell`, `Coord`, `ColumnRef`, `Content` (`TextContent`, `NumericContent`, `FormulaContent`).
- **Load / Save (S2V):** `S2VStorage` with `;` ↔ `,` mapping inside function arguments.
- **Text menu:** set and view **raw content** of cells (no evaluation).

## Not using in this deliverable
- Formula evaluation, dependency recomputation, circular dependency detection, function execution.
