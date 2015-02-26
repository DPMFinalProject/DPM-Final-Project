/**
 *	DPM Final Project
 *	Team 10
 *	ECSE 211: Design Principles and Methods
 *
 *	SavitzkyGolay.java
 *	Created On:	Feb 25, 2015
 */
package sensors.filters;

import util.MovingWindow;
import lejos.util.Matrix;

/**
 *	A Filter that uses a weighted moving average using convolution coefficients 
 *	with the aim of having a faster and more effective smoothing out of signal noise
 * @author Auguste
 */
public class SavitzkyGolay extends Filter {
	private MovingWindow window;
	public int polynomialOrder;
	public int windowSize;
	public double[] convCoefs;
	
	public SavitzkyGolay(int windowSize, int polynomialOrder) {
		window = new MovingWindow(windowSize);
		this.polynomialOrder = polynomialOrder;
		this.windowSize = windowSize;
		
		convCoefs = new double[windowSize];
		
		calculateCoefficients(convCoefs);
	}
	
	@Override
	public double filter(double value) {
		window.add(value);
		return window.weightedAverage(convCoefs);
	}
	
	public void calculateCoefficients(double[] coefficients)
	{
		Matrix Jacobian = new Matrix(windowSize, polynomialOrder+1);
		populate(Jacobian);
		
		Matrix JTranspose = Jacobian.transpose();
		
		Matrix Convolutions = JTranspose.times(Jacobian).inverse().times(JTranspose);
		
		getRow(coefficients, Convolutions, 0);
		
	}
	
	public void getRow(double[] row, Matrix m, int rowNumber)
	{
		for(int i=0; i<windowSize; i++)
		{
			row[i] = m.get(rowNumber, i);
		}
	}
	
	public void populate(Matrix J)
	{
		for(int i=0; i < windowSize;i++)//row index
		{
			for(int j=0; j<polynomialOrder+1 ;j++)//column index
			{
				int value = (-windowSize/2)+i;
				value = (int)Math.pow(value, j);
				J.set(i,j, value);
			}
		}
	}
}