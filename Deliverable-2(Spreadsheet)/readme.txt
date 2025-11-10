# Spreadsheet – Deliverable 2

This repo contains the code for Deliverable 2 of the Spreadsheet project.

## How to run
- Java 17+.
- Open in IntelliJ and run `Program` (text menu), or compile and run from CLI.

## What’s implemented (per brief)
- Domain classes: `Spreadsheet`, `Cell`, `Coord`, `ColumnRef`,
  `Content` (+ `TextContent`, `NumericContent`, `FormulaContent`).
- Load/Save in S2V: `S2VStorage` (handles `;` ↔ `,` for function args).
- Text menu to **set** and **view the content** of cells (raw; no evaluation).

_Not in this deliverable:_ formula evaluation, dependency recomputation, cycle detection.

## Quick test
Set: `A1 = =C1+C2`, `B1 = 4`, `C1 = 1`, `C2 = 2`, `B3 = TOTAL`, `C3 = =A1+B1`.  
Save → file contains:
