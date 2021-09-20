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

}
