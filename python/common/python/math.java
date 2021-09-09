package python;

@org.python.Module(
        __doc__ = "This module provides access to the mathematical functions defined by the C standard."
)
public class math extends org.python.types.Module {

    @org.python.Attribute()
    public static org.python.Object __cached__ = org.python.types.NoneType.NONE;  // TODO;
    @org.python.Attribute()
    public static org.python.Object __file__ = new org.python.types.Str("python/common/python/math.java");
    @org.python.Attribute()
    public static org.python.Object __loader__ = org.python.types.NoneType.NONE;  // TODO
    @org.python.Attribute()
    public static org.python.Object __name__ = new org.python.types.Str("math");
    @org.python.Attribute()
    public static org.python.Object __package__ = new org.python.types.Str("");
    @org.python.Attribute()
    public static org.python.Object __spec__ = org.python.types.NoneType.NONE;  // TODO


    @org.python.Method(
            __doc__ = "Return the square root of x.",
            args = {"number"}
    )
    public static org.python.Object sqrt(org.python.Object number){
        if (number instanceof org.python.types.Int){
            long val = ((org.python.types.Int) number).value;
            if (val < 0){
                throw new org.python.exceptions.ValueError("math domain error");
            }
            return new org.python.types.Float (Math.sqrt(val));
        } else if (number instanceof org.python.types.Float){
            double val = ((org.python.types.Float) number).value;
            if (val < 0){
                throw new org.python.exceptions.ValueError("math domain error");
            }
            return new org.python.types.Float(Math.sqrt(val));

        } else if (number instanceof org.python.types.Bool){
            if (((org.python.types.Bool) number).value){
                return new org.python.types.Float(1.0);
            } else{
                return new org.python.types.Float(0.0);
            }
        }
        throw new org.python.exceptions.ValueError("math domain error");
    }

    @org.python.Method(
        __doc__ = "Return the absolute value of x as a float.",
        args = {"x"}
    )
    public static org.python.Object fabs(org.python.Object number) {    
        if (number instanceof org.python.types.Float) {
            org.python.types.Float casted_number = (org.python.types.Float)number.__float__();
            return new org.python.types.Float(Math.abs(casted_number.value));
        }
        if (number instanceof org.python.types.Int) {
            org.python.types.Int casted_number = (org.python.types.Int)number.__int__();
            // Fabs always returns a float, even if the input is an int
            return new org.python.types.Float(Math.abs(casted_number.value));
        }

        throw new org.python.exceptions.TypeError("must be real number, not " + number.typeName() + "");
    }
}
