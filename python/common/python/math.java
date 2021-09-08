package python;

public class math extends org.python.types.Module {

    @org.python.Method(
        __doc__ = "ceil(number) -> number" +
                    "\n" +
                    "ceil returns the ceiling of x, the smallest integer greater than or equal to x.\n",
        args = {"number"}
    )
    public static org.python.Object ceil(org.python.Object number) {
    	if (number instanceof org.python.types.Float) {
    		double ceilArg = ((org.python.types.Float)number).value;
    		return new org.python.types.Int(Math.ceil(ceilArg));
    	}
    	else if (number instanceof org.python.types.Int) {
    		double ceilArg = ((org.python.types.Int)number).value;
    		return new org.python.types.Int(Math.ceil(ceilArg));
    	}
    	throw new org.python.exceptions.TypeError("Type Error: Must be a number");
    }
}