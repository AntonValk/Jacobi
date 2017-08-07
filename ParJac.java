//This method uses Fork/Join in order to create a parallelized version of the Jacobi Algorithm to solve Ax=B.
//This is the tester class, for large problems use the I/O class.

package jacobi;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class ParJac extends RecursiveTask<double[]> {

	double[][] matrixA;
	double[] column;
	int start, stop;
	double[] solution;
	int thread_id;
	final int SEQUENTIAL_THRESHOLD = 250;

	public ParJac(double[][] matrixA, double[] column, double[] solution, int start, int stop)
	{
		this.matrixA = matrixA;
		this.column = column;
		this.solution = solution;
		this.start = start;
		this.stop = stop;
	}

	// if the size of the problem is more than threshold split the problem
	// if not directly compute the solution
	protected double[] compute()
	{
			if(start - stop <= SEQUENTIAL_THRESHOLD) {
				for(int i = start; i < stop; i++)
					solution = Arrays.copyOfRange(oneJacobiIteration(matrixA, column, solution, start, stop), start, stop);
				return solution;		

			} else {
				int mid = (start + stop) / 2;
				ParJac left = new ParJac(matrixA,column,solution,start,mid);
				ParJac right = new ParJac(matrixA,column,solution,mid,stop);
				left.fork(); //only split from the left to reduce the number of threads per level
				right.compute();
				left.join();
				return solution;
			}
	}

	// this method method applies the fork/join structure to the jacobi algorithm
	// INPUT: Matrix A, Vector b, integer maxIt -> maximum number of iteration, double error -> radius of convergence
	// OUTPUT: Solution vector
	static double[] jacobi(double[][] matrix, double[] vector, int maxIt, double error){
		assert (matrix.length == vector.length); //check problem size
		int numIt = 0;
		int numConverged = 0;
		double[] ans = new double[vector.length]; //current guess vector
		double[] previousAns = new double[vector.length]; //previous guess vector
		
		while (numIt < maxIt){
			numIt++;
			numConverged = 0;
			previousAns = Arrays.copyOfRange(ans, 0, ans.length);// copy current guess to previous guess
			//update the current guess
			ans = Arrays.copyOfRange(ForkJoinPool.commonPool().invoke(new ParJac(matrix, vector, ans, 0, vector.length)), 0, vector.length);
			//check how many of the elements of the vector have converged
			for(int i = 0; i < vector.length; i++)
				if(Math.abs(ans[i] - previousAns[i]) <= error){
					numConverged++;
				}
			//if all the elements of the vector have converged exit the loop
			if(numConverged == ans.length){
				System.out.println("Convergence reached at iteration number: " + numIt);
				break;
			}
		}
		
		if(numIt >= maxIt)
			System.out.println("Max iteration number reached.");
		
		return ans;
	}

	// Does one iteration of the Jacobi Algorithm over a range of rows (between start and stop)
	public static double[] oneJacobiIteration(double [][]A, double []b, double[] guess, int start, int stop){

		double []dx = new double[A.length];
		double []y = new double[A.length];	
		int i, j;	//temporary variables 

		for(i = start; i < stop; i++)
		{
			dx[i] = b[i];
			for(j = 0; j < A.length; j++)
				dx[i] -= A[i][j]*guess[j];
			dx[i] /= A[i][i];
			y[i] += dx[i] + guess[i];
		}
		for(i = start; i < stop; i++) guess[i] = y[i];

		return guess;
	}
	
	//Helper method used to check if the answer is right. Input A,x should yield an output approximately equal to vector B.
	public static double[] multiply(double[][] a, double[] x) {
        int m = a.length; 				//horizontal length of A
        int n = a[0].length; 	//vertical length of A
        if (x.length != m){ 	//check if the horizontal length of A is incompatible with the length of the vector
        System.out.println("Matrix dimensions error.");
        return x; 
        }
        double[] y = new double[n]; 	//output vector
        for (int j = 0; j < n; j++){ 	//vertical coordinate
            for (int i = 0; i < m; i++){//horizontal coordinate
                y[j] += a[j][i] * x[i];
            }
            System.out.println();
        }
        return y;
    }


	//Main method used to test cases
	public static void main(String args[]){		
		
		double c[][] = new double[1000][1000];
		double d[] = new double[1000];
		for(int i = 0; i < c.length; i++)
			for(int j = 0; j< c.length; j++){
				c[i][j] = Math.random()*10;
				if(i == j)
					c[i][j] *= 10000;
			}
		for(int i = 0; i < c.length; i++)
			d[i] = Math.random()+1;
		
		System.out.println("Matrices filled... ");
		System.out.println("Now solving... ");
		
		long start_time = System.nanoTime();
		double[] xVector = jacobi(c, d, 20, 0.00001);

		//For small problems size <= 10 use the print command to check if the answer converged
		//For larger problems handle using I/O class
		
//		System.out.println(Arrays.deepToString(c));;
//
//		System.out.println(java.util.Arrays.toString(d));
//
//		System.out.println(java.util.Arrays.toString(xVector));
//		
//		System.out.println(java.util.Arrays.toString(multiply(c,xVector)));

		long stop_time = System.nanoTime(); 
		long time_diff_millis = (stop_time - start_time)/(long)1.E6;
		System.out.println("Done.");
		System.out.println("Time in ms: " + time_diff_millis);
		System.out.println(Runtime.getRuntime().availableProcessors() + " processors used.");
	}

}




