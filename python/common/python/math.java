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
    		double ceil_arg = ((org.python.types.Float)number).value;
    		return new org.python.types.Int(Math.ceil(ceil_arg));
    	}
    	else if (number instanceof org.python.types.Int) {
    		double ceil_arg = ((org.python.types.Int)number).value;
    		return new org.python.types.Int(Math.ceil(ceil_arg));
    	} 
        else if (number instanceof org.python.types.Bool) {
            int int_arg = ((org.python.types.Bool)number).value ? 1 : 0;
            return new org.python.types.Int(Math.ceil(int_arg));
        }
        else {
    	   throw new org.python.exceptions.TypeError("must be real number, not " + number.typeName());
        }
    }
}