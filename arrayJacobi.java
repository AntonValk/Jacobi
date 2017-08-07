package jacobi;

public class arrayJacobi {

	public static double[] run_jacobi_method(double [][]A, double []b, double epsilon, int maxit){

		int n = A.length;
		double []x = new double[n];
		double []dx = new double[n];
		double []y = new double[n];	
		int i, j, k, numit;	//temporary variables 
		/* main loop */

		for(k = 0; k < maxit; k++)
		{
			double sum = 0.0;
			for(i = 0; i < n; i++)
			{
				dx[i] = b[i];
				for(j = 0; j < n; j++)
					dx[i] -= A[i][j]*x[j];
				dx[i] /= A[i][i];
				y[i] += dx[i];
				sum += ( (dx[i] >= 0.0) ? dx[i] : -dx[i]);
			}
			for(i = 0; i < n; i++) x[i] = y[i];
			System.out.printf("%3d : %.3e\n", k, sum);
			if(sum <= epsilon) break;
		}
		/* main loop */	
		numit = k+1;
		System.out.println("Number of iterations: " + numit);	
		return x;
	}

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
	
	

	public static void main(String[] args){

//		double a[][] = new double[][] {  
//			{ 16.0, 1.0, -2.0 }, 
//			{ 3.0, -5.0, 1.0 }, 
//			{ 11.0, -2.0, 21.0 } 
//		}; 
//
//		double b[] = new double[] { 4.6, 3.3, -38.9 }; 
//		
//		
//		double[] x = run_jacobi_method(a, b, 0.0001, 6);
//		for(int i = 0; i < x.length; i++) 
//			System.out.println(x[i]);
		
		double c[][] = new double[10][10];
		double d[] = new double[10];
		for(int i = 0; i < c.length; i++)
			for(int j = 0; j< c.length; j++){
				c[i][j] = Math.random()*10;
				if(i ==j)
					c[i][j] *= 100;
			}
		for(int i = 0; i < c.length; i++)
			d[i] = Math.random()+1;
		
		System.out.println("Matrices filled... ");
		System.out.println("Now solving sequantially... ");
		
		long start_time = System.nanoTime();
		double[] xVector = run_jacobi_method(c,d, 0.001, 500000000);

		long stop_time = System.nanoTime(); 
		long time_diff_millis = (stop_time - start_time)/(long)1.E6;
		System.out.println("Done.");
		System.out.println("Time in ms: " + time_diff_millis);

	}
}
