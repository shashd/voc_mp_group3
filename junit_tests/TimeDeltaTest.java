import org.junit.jupiter.api.Test;
import org.python.Object;
import org.python.stdlib.datetime.TimeDelta;
import org.python.types.Int;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeDeltaTest {

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
    }

}
