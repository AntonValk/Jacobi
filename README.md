# Jacobi
Solving the matrix equation Ax=B using the sequential and a parallelized version of the Jacobi method.
The parallelized form of Jacobi's algorithm uses Java's Fork/Join to recursively break down the problem into
smaller parts solved in parallel by many threads.
The two tester methods can be used to test the code on simple small size problems direcrly on the console.
For bigger size problems use the Input/Output class that uses la4j's I/O library.

Author: Antonios Valkanas
