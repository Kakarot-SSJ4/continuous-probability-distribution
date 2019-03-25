/* This program gives the probability in a certain interval of a standard normal probability distribution
 The approximation for calculating definite integrals works best for small intervals compared to N in the trapezoid rule
*/
import java.util.*;
class prob{
	public static  final double pi = Math.PI; // Mathematical constant pi
	public static final double e = Math.exp(1); // Euler's number
	public static double N = 100000;
/* The following function will return the definite integral of a standard normal distribution by the trapezoid rule.
   Probability distribution function for standard normal:
   1 / (2*pi)^1/2 * e^((-z^2)/2)
   Definite integral of f(x) from a to b with respect to x:
   (b - a)/2N * [f(a) + 2{ i=1->N-1 Σ f(a + i * (b - a)/N) } +f(b)]
*/
public static double integrate_normal(double a, double b){
	double sum = 0;
	// calculating the summation part in the definite integral approximation
	for(int i = 1; i < N-1 ; i++)
	{
		sum = sum + function_normal(a + i * (b - a)/N);
	}
	return ( (b - a)/(2 * N) * (function_normal(a) + 2*sum + function_normal(b)) );
}
/* The following function will return the value of the standard normal function at a specified point
   1 / (2*pi)^1/2 * e^((-z^2)/2)
*/
public static double function_normal(double z){
	return (1 / (Math.sqrt( 2 * pi )) * Math.pow( e , -1 * z * z / 2 ));
}
/* The following function returns the value of the double differentiation at a point in standard normal function
*/
public static double doublediff_normal(double z){
	return( Math.pow( e , -1 * Math.pow( z , 2) ) / Math.sqrt( 2 * pi ) * ( z * z - 1 ) );
}
/* The following function returns the maximum possible error in calculating the definite integral using the trapezoid rule
   Error = (b - a)^3 / (12 * N^2) * MAX(f''(x)) ; a<=x<=b
*/
public static double max_err_normal(double a, double b){
	double double_diff, double_diff_max = 0;
	for (double i = a ; i <= b ; i = i + 1/N) {
		double check_positive = doublediff_normal(i); 
		// making the double differential non negative
		double_diff = check_positive >= 0 ? check_positive : (-1 * check_positive);
		if(double_diff > double_diff_max)
			double_diff_max = double_diff;
	}
	return (Math.pow( (b - a) , 3) / (12*N*N));
}
// checks if the probability goes absurd, i.e., the divisions are not large as compared to the interval. Hence, the probability + maximum possible error may go above 1
public static boolean absurd(double a, double b){
	if( (integrate_normal(a,b) + max_err_normal(a,b)) > 1 )
		return true;
	else
		return false;
}
public static void main(String args[]){
	Scanner sc = new Scanner(System.in);
	System.out.println("Input the interval");
	double a = sc.nextDouble(); // lower limit of the integral
	if(a < 0.0) // probability distribution function is 0 for negative numbers
		a = 0.0;
	double b = sc.nextDouble(); // upper limit of the integral
	System.out.println("Enter number of divisions (N) for running the trapezoid rule (default 100000) ");
	N = sc.nextDouble(); // modified value of N according to the user
	if(absurd(a,b)) // Approximation leads to a possible probability greater than 1(including error)
		System.out.println("Approximation unsuccessful");
	else if(a >= b) // Invalid interval
		System.out.println("Interval invalid");
	else
	{
		System.out.println("Probability = " + integrate_normal(a,b));
		System.out.println("Maximum error = " + max_err_normal(a,b));
	}
}
}