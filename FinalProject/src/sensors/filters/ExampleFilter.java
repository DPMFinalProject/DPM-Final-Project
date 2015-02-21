package sensors.filters;

public class ExampleFilter extends Filter {
	double lastValue = 0;
	@Override
	
	// Removes all 255 values.
	public double filter(double value) {
		if (value != 255) {
			lastValue = value;
		}
		
		return lastValue;
	}

}
