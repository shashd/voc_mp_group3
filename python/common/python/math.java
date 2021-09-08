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
        __doc__ = "Return the absolute value of x.",
        args = {"x"}
    )
    public static org.python.Object fabs(org.python.Object number) {    
        if (number instanceof org.python.types.Float) {
            org.python.types.Float castedNumber = (org.python.types.Float)number.__float__();
            return new org.python.types.Float(Math.abs(castedNumber.value));
        }
        if (number instanceof org.python.types.Int) {
            org.python.types.Int castedNumber = (org.python.types.Int)number.__int__();
            return new org.python.types.Float(Math.abs(castedNumber.value));
        }

        throw new org.python.exceptions.TypeError("must be real number, not " + number.typeName() + "");
    }
}
