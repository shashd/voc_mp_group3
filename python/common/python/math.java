package python;

public class math extends org.python.types.Module {

    @org.python.Method(
            __doc__ = "Return the square root of x.",
            args = {"number"}
    )
    public static org.python.Object sqrt(org.python.Object number){
        if (number instanceof org.python.types.Int){
            long val = ((org.python.types.Int) number).value;
            if (val < 0){
                throw new org.python.exceptions.TypeError("input should be bigger than zero");
            }
            return new org.python.types.Float (Math.sqrt(val));
        } else if (number instanceof org.python.types.Float){
            double val = ((org.python.types.Float) number).value;
            if (val < 0){
                throw new org.python.exceptions.TypeError("input should be bigger than zero");
            }
            return new org.python.types.Float(Math.sqrt(val));

        } else if (number instanceof org.python.types.Bool){
            if (((org.python.types.Bool) number).value){
                return new org.python.types.Float(1.0);
            } else{
                return new org.python.types.Float(0.0);
            }
        }
        throw new org.python.exceptions.TypeError("the input type should be int, float or bool");
    }
}
