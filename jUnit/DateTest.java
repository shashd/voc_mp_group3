import org.junit.jupiter.api.Test;
import org.python.Object;
import org.python.exceptions.BaseException;
import org.python.stdlib.datetime.Date;
import org.python.types.*;
import java.util.Collections;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTest {

    @Test
    public void testConstructor() {

        Object[] args = {Int.getInt(2000), Int.getInt(2), Int.getInt(1)};
        Date date = new Date(args, Collections.emptyMap());
        long y = ((Int) date.year).value;
        long m = ((Int) date.month).value;
        long d = ((Int) date.day).value;
        assertEquals(y, 2000);
        assertEquals(m, 2);
        assertEquals(d, 1);

        java.util.Map<java.lang.String, org.python.Object> kwargs = new HashMap<>();
        kwargs.put("year", Int.getInt(2000));
        kwargs.put("month", Int.getInt(2));
        kwargs.put("day", Int.getInt(1));

        Object[] empty = new Object[0];
        date = new Date(empty, kwargs);
        y = ((Int) date.year).value;
        m = ((Int) date.month).value;
        d = ((Int) date.day).value;
        assertEquals(y, 2000);
        assertEquals(m, 2);
        assertEquals(d, 1);

        kwargs.clear();
        args = new Object[]{Int.getInt(2000), Int.getInt(2)};
        kwargs.put("day", Int.getInt(1));
        date = new Date(args, kwargs);
        y = ((Int) date.year).value;
        m = ((Int) date.month).value;
        d = ((Int) date.day).value;
        assertEquals(y, 2000);
        assertEquals(m, 2);
        assertEquals(d, 1);
    }

    private void createErrorDate(Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs, String errorMsg) {
        try {
            Date date = new Date(args, kwargs);
        } catch (BaseException exception) {
            assertEquals(errorMsg, exception.getMessage());
        }
    }

    @Test
    public void testConstructorFail() {

//      when  args.length + kwargs.size() == 3
        Object[] args = {Int.getInt(2000), Int.getInt(2), Int.getInt(1), Int.getInt(4)};
        createErrorDate(args, Collections.emptyMap(), "function takes at most 3 arguments (4 given)");

        java.util.Map<java.lang.String, org.python.Object> kwargs = new HashMap<>();
        kwargs.put("year", Int.getInt(2000));
        args = new Object[]{Int.getInt(2), Int.getInt(1)};
        createErrorDate(args, kwargs, "positional argument follows keyword argument");

        kwargs.clear();
        kwargs.put("month", Int.getInt(2));
        args = new Object[]{Int.getInt(2000), Int.getInt(1)};
        createErrorDate(args, kwargs, "positional argument follows keyword argument");

        kwargs.clear();
        kwargs.put("day", Int.getInt(1));
        args = new Object[]{Int.getInt(2000), Int.getInt(2)};
        createErrorDate(args, kwargs, "positional argument follows keyword argument");

        args = new Object[]{Int.getInt(10000), Int.getInt(2), Int.getInt(1)};
        createErrorDate(args, Collections.emptyMap(), "year 10000 is out of range");

        args = new Object[]{Int.getInt(0), Int.getInt(2), Int.getInt(1)};
        createErrorDate(args, Collections.emptyMap(), "year 0 is out of range");

        args = new Object[]{Int.getInt(2000), Int.getInt(0), Int.getInt(1)};
        createErrorDate(args, Collections.emptyMap(), "month must be in 1..12");

        args = new Object[]{Int.getInt(2000), Int.getInt(13), Int.getInt(1)};
        createErrorDate(args, Collections.emptyMap(), "month must be in 1..12");

        args = new Object[]{Int.getInt(2000), Int.getInt(2), Int.getInt(32)};
        createErrorDate(args, Collections.emptyMap(), "day is out of range for month");

        args = new Object[]{new org.python.types.Float(2000.0), Int.getInt(2), Int.getInt(1)};
        createErrorDate(args, Collections.emptyMap(), "integer argument expected, got float");

        args = new Object[]{Int.getInt(2000), new org.python.types.Float(2.0), Int.getInt(1)};
        createErrorDate(args, Collections.emptyMap(), "integer argument expected, got float");

        args = new Object[]{Int.getInt(2000), Int.getInt(2), new org.python.types.Float(1.0)};
        createErrorDate(args, Collections.emptyMap(), "integer argument expected, got float");

        args = new Object[]{new org.python.types.Str("2000"), Int.getInt(2), Int.getInt(1)};
        createErrorDate(args, Collections.emptyMap(), "integer argument expected, got str");

        //  when args.length + kwargs.size() == 2

        args = new Object[]{new Str("2000"), Int.getInt(2)};
        createErrorDate(args, Collections.emptyMap(), "integer argument expected, got str");

        kwargs.clear();
        kwargs.put("year", Int.getInt(2000));
        args = new Object[]{Int.getInt(2)};
        createErrorDate(args, kwargs, "positional argument follows keyword argument");

        args = new Object[]{Int.getInt(2000), new Str("2000")};
        createErrorDate(args, Collections.emptyMap(), "integer argument expected, got str");

        kwargs.clear();
        kwargs.put("month", Int.getInt(2));
        kwargs.put("day", Int.getInt(1));
        args = new Object[0];
        createErrorDate(args, kwargs, "function missing required argument 'year' (pos 1)");

        kwargs.clear();
        kwargs.put("year", Int.getInt(2000));
        kwargs.put("day", Int.getInt(1));
        args = new Object[0];
        createErrorDate(args, kwargs, "function missing required argument 'month' (pos 2)");

        args = new Object[]{Int.getInt(2000), Int.getInt(2)};
        createErrorDate(args, Collections.emptyMap(), "function missing required argument 'day' (pos 3)");

        //  when args.length + kwargs.size() == 1

        kwargs.clear();
        kwargs.put("year", Int.getInt(2000));
        args = new Object[0];
        createErrorDate(args, kwargs, "function missing required argument 'month' (pos 2)");

        args = new Object[]{Int.getInt(2000)};
        createErrorDate(args, Collections.emptyMap(), "function missing required argument 'month' (pos 2)");

        kwargs.clear();
        kwargs.put("month", Int.getInt(2));
        args = new Object[0];
        createErrorDate(args, kwargs, "function missing required argument 'year' (pos 1)");

        kwargs.clear();
        kwargs.put("day", Int.getInt(1));
        args = new Object[0];
        createErrorDate(args, kwargs, "function missing required argument 'year' (pos 1)");

        args = new Object[]{new Str("2000")};
        createErrorDate(args, Collections.emptyMap(), "integer argument expected, got str");

        //  when args.length + kwargs.size() == 0
        args = new Object[0];
        createErrorDate(args, Collections.emptyMap(), "function missing required argument 'year' (pos 1)");

    }

    @Test
    public void testCtime() {
        Object[] args = {Int.getInt(2000), Int.getInt(2), Int.getInt(1)};
        Date date = new Date(args, Collections.emptyMap());
        Str str = (Str) date.ctime();
        assertEquals(str.value, "Tue Feb  1 00:00:00 2000");
    }

    @Test
    public void test__repr__() {
        Object[] args = {Int.getInt(200), Int.getInt(2), Int.getInt(1)};
        Date date = new Date(args, Collections.emptyMap());
        Str str = (Str) date.__repr__();
        assertEquals(str.value, "0200-02-01");

        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(11)};
        date = new Date(args, Collections.emptyMap());
        str = (Str) date.__repr__();
        assertEquals(str.value, "2000-10-11");
    }

    @Test
    public void testConstant_4() {
        Int constant4 = (Int) Date.constant_4();
        assertEquals(constant4.value, 4);
    }

    @Test
    public void testFromisoformat() {
        Object[] args = {Int.getInt(2000), Int.getInt(2), Int.getInt(1)};
        Date date = new Date(args, Collections.emptyMap());

        Date newDate = date.fromisoformat(new org.python.types.Str("2019-12-04"));
        long y = ((Int) newDate.year).value;
        long m = ((Int) newDate.month).value;
        long d = ((Int) newDate.day).value;
        assertEquals(y, 2019);
        assertEquals(m, 12);
        assertEquals(d, 4);

        newDate = date.fromisoformat(new org.python.types.Str("0200-02-11"));
        y = ((Int) newDate.year).value;
        m = ((Int) newDate.month).value;
        d = ((Int) newDate.day).value;
        assertEquals(y, 200);
        assertEquals(m, 2);
        assertEquals(d, 11);
    }

    @Test
    public void testFromisoformatFail() {
        Object[] args = {Int.getInt(2000), Int.getInt(2), Int.getInt(1)};
        Date date = new Date(args, Collections.emptyMap());

        try {
            Date newDate = date.fromisoformat(new org.python.types.Str("2019-12-04-01"));
        } catch (BaseException exception) {
            assertEquals("Need YYYY-MM-DD", exception.getMessage());
        }

        try {
            Date newDate = date.fromisoformat(new org.python.types.Str("110-12-04"));
        } catch (BaseException exception) {
            assertEquals("Year format is error", exception.getMessage());
        }
        try {
            Date newDate = date.fromisoformat(new org.python.types.Str("2001-2-04"));
        } catch (BaseException exception) {
            assertEquals("Month format is error", exception.getMessage());
        }
        try {
            Date newDate = date.fromisoformat(new org.python.types.Str("2001-02-4"));
        } catch (BaseException exception) {
            assertEquals("Day format is error", exception.getMessage());
        }
        try {
            Date newDate = date.fromisoformat(new org.python.types.Str("20.1-02-04"));
        } catch(BaseException exception){
            assertEquals("YYYY, MM and DD in YYYY-MM-DD need to be integers", exception.getMessage());
        }
    }
    @Test
    public void testToday(){

        Date today = (Date) Date.today();

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        int y = now.getYear();
        int m = now.getMonthValue();
        int d = now.getDayOfMonth();

        assertEquals(((Int) today.year).value, y);
        assertEquals(((Int) today.month).value, m);
        assertEquals(((Int) today.day).value, d);
    }


}
