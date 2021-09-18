import org.junit.jupiter.api.Test;
import org.python.stdlib.datetime.DateTime;

import org.python.types.Int;
import org.python.types.Str;
import org.python.Object;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class DateTimeTest {

    @Test
    public void testDate() {
        Object[] args = { Int.getInt(2000),Int.getInt(2),Int.getInt(1)};
        DateTime dateTime = new DateTime(args,Collections.emptyMap());
        Str str = (Str) dateTime.date().__str__();
        assertEquals("2000-02-01", str.value);

    }
}
