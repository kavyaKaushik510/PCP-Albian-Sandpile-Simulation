# Parallel Abelian Sandpile Simulation

This project implements a parallel simulation of the Abelian Sandpile — a two-dimensional cellular automaton in which each cell contains grains of sand. When a cell accumulates more than 4 grains, it topples, distributing one grain to each of its four orthogonal neighbors. This process repeats until a stable configuration is reached.

The simulation reads the initial grid from a CSV file, uses Java’s Fork/Join framework for parallelization, and generates a PNG image that visually represents the final stable state.

---

## Objectives

- Develop a parallel version of the Abelian Sandpile simulation
- Achieve faster runtimes than the provided serial solution
- Avoid all data races and unsafe concurrent access
- Use only the Java Fork/Join framework for synchronization
- Validate outputs by comparing with the serial implementation
- Run the solution on at least two machines to evaluate portability and performance

---

## Features

- Reads sandpile configurations from `.csv` input files
- Simulates toppling behavior based on Abelian Sandpile rules
- Applies parallel processing using ForkJoin tasks
- Generates output images where each color represents a specific grain count:
  - 0 grains: Black
  - 1 grain: Green
  - 2 grains: Blue
  - 3 grains: Red

---

## Technologies Used

- Java
- Fork/Join Framework
- Standard I/O and CSV parsing libraries

