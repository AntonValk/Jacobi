

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.LinearAlgebra;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;



public class InputOutput {



	public static void main(String[] args){


		try {
			Matrix matrix = Matrix.fromMatrixMarket(new String(Files.readAllBytes(Paths.get("MatrixIn.txt"))));
			Vector vector = Vector.fromMatrixMarket(new String(Files.readAllBytes(Paths.get("VectorIn.txt"))));
			Vector unknown = compute(matrix, vector);
			Files.write(Paths.get("output.mm"), unknown.toMatrixMarket().getBytes());

			//print output to console
			//System.out.println(unknown.toMatrixMarket());
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	public static Vector compute(Matrix a, Vector b){

		double[] xVector = ParJac.jacobi(a, b, 20, 0.00001);
		return xVector; 

	} 

}








