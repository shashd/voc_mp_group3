import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.python.Object;
import org.python.stdlib.datetime.TimeDelta;
import org.python.types.Bool;
import org.python.types.Float;
import org.python.types.Int;
import org.python.types.Str;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeDeltaTest {

    private TimeDelta constructTimeDelta(long days, long seconds, long microseconds) {
        return new TimeDelta(new Object[] {Int.getInt(days), Int.getInt(seconds), Int.getInt(microseconds)}, Collections.emptyMap());
    }

    @Test
    public void testConstructor() {
        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("days", org.python.types.Int.getInt(1));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(1), timeDelta.days);
        }

        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("weeks", org.python.types.Int.getInt(1));
            kwargs.put("days", org.python.types.Int.getInt(2));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(9), timeDelta.days);
        }

        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("weeks", org.python.types.Int.getInt(1));
            TimeDelta timeDelta = new TimeDelta(new Object[] {Int.getInt(3) }, kwargs);

            assertEquals(org.python.types.Int.getInt(10), timeDelta.days);
        }

        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("hours", org.python.types.Int.getInt(24));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(1), timeDelta.days);
        }

        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("minutes", org.python.types.Int.getInt(12));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(12*60), timeDelta.seconds);
        }

        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("milliseconds", org.python.types.Int.getInt(1_001));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(1), timeDelta.seconds);
            assertEquals(org.python.types.Int.getInt(1000), timeDelta.microseconds);
        }

        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("incorrect parameter", org.python.types.Int.getInt(1));
            Assert.assertThrows(Exception.class, () -> new TimeDelta(new Object[0], kwargs));
        }

        { // This test defines days two ways, which should throw
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("days", org.python.types.Int.getInt(1));
            Assert.assertThrows(Exception.class, () ->  new TimeDelta(new Object[] { Int.getInt(1) }, kwargs));
        }

        { // Try too many arguments
            HashMap<String, Object> kwargs = new HashMap<>();

            Assert.assertThrows(Exception.class, () ->  new TimeDelta(new Object[] {
                    Int.getInt(1),
                    Int.getInt(2),
                    Int.getInt(3),
                    Int.getInt(4),
                    Int.getInt(5),
                    Int.getInt(6),
                    Int.getInt(7),
                    Int.getInt(8),
                }, kwargs)
            );
        }
    }

    @Test
    public void testNormalization() {
        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("microseconds", org.python.types.Int.getInt(1_000_000_000));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(1000), timeDelta.seconds);
        }

        {
            HashMap<String, Object> kwargs = new HashMap<>();

            kwargs.put("microseconds", org.python.types.Int.getInt(1_234_567_890));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(1234), timeDelta.seconds);
            assertEquals(org.python.types.Int.getInt(567890), timeDelta.microseconds);
        }
        {
            HashMap<String, Object> kwargs = new HashMap<>();

            // as many seconds as a day plus one
            kwargs.put("seconds", org.python.types.Int.getInt(86400 + 1));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(1), timeDelta.days);
            assertEquals(org.python.types.Int.getInt(1), timeDelta.seconds);
        }

        {
            HashMap<String, Object> kwargs = new HashMap<>();

            // as many microseconds as 10 days
            kwargs.put("microseconds", org.python.types.Int.getInt(86400L * 10_000_000L));
            TimeDelta timeDelta = new TimeDelta(new Object[0], kwargs);

            assertEquals(org.python.types.Int.getInt(10), timeDelta.days);
        }

        { // Negative seconds should decrease day
            TimeDelta timeDelta = constructTimeDelta(0, -1, 0);

            assertEquals(org.python.types.Int.getInt(-1), timeDelta.days);
            assertEquals(org.python.types.Int.getInt(60*60*24-1), timeDelta.seconds);
        }
    }

    @Test
    public void testEquals() {
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 3);

            assertEquals(Bool.getBool(true), td0.__eq__(td1));
            assertEquals(Bool.getBool(true), td1.__eq__(td0));
        }

        {

            TimeDelta td0 = constructTimeDelta(1, 2, 3);

            // Microseconds different
            TimeDelta td1 = constructTimeDelta(1, 2, 2);
            // Seconds different
            TimeDelta td2 = constructTimeDelta(1, 1, 3);
            // days different
            TimeDelta td3 = constructTimeDelta(0, 2, 3);

            assertEquals(Bool.getBool(false), td0.__eq__(td1));
            assertEquals(Bool.getBool(false), td1.__eq__(td0));

            assertEquals(Bool.getBool(false), td0.__eq__(td2));
            assertEquals(Bool.getBool(false), td2.__eq__(td0));

            assertEquals(Bool.getBool(false), td0.__eq__(td3));
            assertEquals(Bool.getBool(false), td3.__eq__(td0));
        }

        {
            TimeDelta td = constructTimeDelta(0, 0, 1);

            assertEquals(Bool.getBool(false), td.__eq__( Int.getInt(1) ));
            assertEquals(Bool.getBool(false), td.__eq__( new Str("1") ));
        }
    }

    @Test
    public void testNotEquals() {
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 =constructTimeDelta(0, 2, 3);

            assertEquals(Bool.getBool(true), td0.__ne__(td1));
            assertEquals(Bool.getBool(true), td1.__ne__(td0));
        }
    }

    @Test
    public void testLesserThan() {
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 4);

            assertEquals(Bool.getBool(true), td0.__lt__(td1));
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 3);

            assertEquals(Bool.getBool(false), td0.__lt__(td1));
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 20000, 300000);
            TimeDelta td1 = constructTimeDelta(2, 0, 0);

            assertEquals(Bool.getBool(true), td0.__lt__(td1));
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);

            Assert.assertThrows(Exception.class, () -> td0.__lt__( Int.getInt(1) ));
        }
    }

    @Test
    public void testLessOrEqualThan() {
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 4);

            assertEquals(Bool.getBool(true), td0.__le__(td1));
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 3);

            assertEquals(Bool.getBool(true), td0.__le__(td1));
        }
    }

    @Test
    public void testGreaterThan() {
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 2);

            assertEquals(Bool.getBool(true), td0.__gt__(td1));
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 3);

            assertEquals(Bool.getBool(false), td0.__gt__(td1));
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 0, 0);
            TimeDelta td1 = constructTimeDelta(0, 20000, 300000);

            assertEquals(Bool.getBool(true), td0.__gt__(td1));
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);

            Assert.assertThrows(Exception.class, () -> td0.__gt__( Int.getInt(1) ));
        }
    }

    @Test
    public void testGreaterOrEqualThan() {
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 2);

            assertEquals(Bool.getBool(true), td0.__ge__(td1));
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(1, 2, 3);

            assertEquals(Bool.getBool(true), td0.__ge__(td1));
        }
    }

    @Test
    public void testAdd() {
        {
            TimeDelta td0 = constructTimeDelta(1, 2, 3);
            TimeDelta td1 = constructTimeDelta(2, 3, 4);

            TimeDelta sum = (TimeDelta)td0.__add__(td1);

            assertEquals(Int.getInt(3), sum.days);
            assertEquals(Int.getInt(5), sum.seconds);
            assertEquals(Int.getInt(7), sum.microseconds);
        }
        {
            TimeDelta td0 = constructTimeDelta(0, 60*60*24-1, 0);
            TimeDelta td1 = constructTimeDelta(0, 1, 0);

            TimeDelta sum = (TimeDelta)td0.__add__(td1);

            assertEquals(Int.getInt(1), sum.days);
            assertEquals(Int.getInt(0), sum.seconds);
            assertEquals(Int.getInt(0), sum.microseconds);
        }
    }

    @Test
    public void testSubtract() {
        {
            TimeDelta td0 = constructTimeDelta(2, 5, 7);
            TimeDelta td1 = constructTimeDelta(1, 3, 4);

            TimeDelta difference = (TimeDelta)td0.__sub__(td1);

            assertEquals(Int.getInt(1), difference.days);
            assertEquals(Int.getInt(2), difference.seconds);
            assertEquals(Int.getInt(3), difference.microseconds);
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 0, 0);
            TimeDelta td1 = constructTimeDelta(0, 1, 0);

            TimeDelta difference = (TimeDelta)td0.__sub__(td1);

            assertEquals(Int.getInt(0), difference.days);
            assertEquals(Int.getInt(60*60*24-1), difference.seconds);
            assertEquals(Int.getInt(0), difference.microseconds);
        }
    }

    @Test
    public void testMultiply() {
        {
            TimeDelta td0 = constructTimeDelta(0, 1, 0);
            Int other = Int.getInt(2);

            TimeDelta product = (TimeDelta)td0.__mul__(other);

            assertEquals(Int.getInt(0), product.days);
            assertEquals(Int.getInt(2), product.seconds);
            assertEquals(Int.getInt(0), product.microseconds);
        }
        {
            TimeDelta td0 = constructTimeDelta(0, 0, 10000);
            Int other = Int.getInt(10000);

            TimeDelta product = (TimeDelta)td0.__mul__(other);

            assertEquals(Int.getInt(0), product.days);
            assertEquals(Int.getInt(100), product.seconds);
            assertEquals(Int.getInt(0), product.microseconds);
        }
        {
            TimeDelta td0 = constructTimeDelta(0, 1, 0);
            Float other = new Float(1.5f);

            TimeDelta product = (TimeDelta)td0.__mul__(other);

            assertEquals(Int.getInt(0), product.days);
            assertEquals(Int.getInt(1), product.seconds);
            assertEquals(Int.getInt(500_000), product.microseconds);
        }
        {
            TimeDelta td0 = constructTimeDelta(-1, 0, 0);
            Float other = new Float(1.5f);

            TimeDelta product = (TimeDelta)td0.__mul__(other);

            // This might look strange, but since days is the only value that goes negative to represent -1.5 days it needs to be 2 with positive seconds.
            assertEquals(Int.getInt(-2), product.days);
            assertEquals(Int.getInt(86400/2), product.seconds);
            assertEquals(Int.getInt(0), product.microseconds);
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 0, 0);
            Float other = new Float(-1.5f);

            TimeDelta product = (TimeDelta)td0.__mul__(other);

            // Ditto to above test
            assertEquals(Int.getInt(-2), product.days);
            assertEquals(Int.getInt(86400/2), product.seconds);
            assertEquals(Int.getInt(0), product.microseconds);
        }
        {
            // You can't multiply deltas with each other
            TimeDelta td0 = constructTimeDelta(1, 0, 0);
            TimeDelta td1 = constructTimeDelta(1, 0, 0);

            Assert.assertThrows(Exception.class, () -> td0.__mul__(td1 ));
        }
    }

    @Test
    public void testTotalSeconds() {
        {
            TimeDelta td0 = constructTimeDelta(0, 1, 0);
            Float result = new Float(1);

            assertEquals(result, td0.total_seconds());
        }
        {
            TimeDelta td0 = constructTimeDelta(1, 0, 0);
            Float result = new Float(60*60*24);

            assertEquals(result, td0.total_seconds());
        }
        {
            TimeDelta td0 = constructTimeDelta(0, 0, 1000000);
            Float result = new Float(1);

            assertEquals(result, td0.total_seconds());
        }
        {
            TimeDelta td0 = constructTimeDelta(0, 0, 100000);
            Float result = new Float(0.1);

            assertEquals(result, td0.total_seconds());
        }
        {
            TimeDelta td0 = constructTimeDelta(0, 0, 1);
            Float result = new Float(0.000001);

            assertEquals(result, td0.total_seconds());
        }
        {
            TimeDelta td0 = constructTimeDelta(999999999, 3600*24, 1000000);
            Float result = new Float(86400000000001.0);

            assertEquals(result, td0.total_seconds());
        }
        {
            TimeDelta td0 = constructTimeDelta(-999999999, 0, 0);
            Float result = new Float(-86399999913600.0);

            assertEquals(result, td0.total_seconds());
        }
    }

    @Test
    public void testPos() {
        {
            TimeDelta td0 = constructTimeDelta(0, 0, 0);

            TimeDelta sum = (TimeDelta)td0.__pos__();

            assertEquals(Int.getInt(0), sum.days);
            assertEquals(Int.getInt(0), sum.seconds);
            assertEquals(Int.getInt(0), sum.microseconds);
        }
        {
            TimeDelta td0 = constructTimeDelta(999999999, 3600*24, 1000000);

            TimeDelta pos = (TimeDelta)td0.__pos__();

            assertEquals(Int.getInt(1000000000), pos.days);
            assertEquals(Int.getInt(1), pos.seconds);
            assertEquals(Int.getInt(0), pos.microseconds);
        }
    }

    @Test
    public void testStr() {
        {
            TimeDelta td0 = constructTimeDelta(0, 0, 0);

            Str resultString = td0.__str__();
            Str comp = new Str("0 days, seconds: 0, microseconds: 0");

            assertTrue(resultString.equals(comp));
        }
        {
            TimeDelta td0 = constructTimeDelta(999999999, 3600*24, 1000000);

            Str resultString = td0.__str__();
            Str comp = new Str("1000000000 days, seconds: 1, microseconds: 0");

            assertTrue(resultString.equals(comp));
        }
    }
}
