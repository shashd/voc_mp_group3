import org.junit.jupiter.api.Test;
import org.python.exceptions.BaseException;
import org.python.stdlib.datetime.DateTime;

import org.python.types.Bool;
import org.python.types.Float;
import org.python.types.Int;
import org.python.types.Str;
import org.python.Object;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DateTimeTest {

    // writing some helper functions to generate args and kwargs
    // In your JUnit tests please cover cases for args, kwargs and combinations thereof!
    // Your tests should cover a good variety of cases: basic inputs and likely/interesting
    // exceptions and edge cases.
    // To give an example from the previous sprint: for math.sqrt,
    // this could e.g. be positive ints and floats, negative ints and floats (ValueError) and
    // 1-2 examples for non-numerical inputs, e.g. strings and maybe also some form of collection/iterable,
    // like list (TypeErrors).

    private int getIndexOfArray(String target, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    private void testVariables(DateTime dateTime, Object[] args, HashMap<String, Object> kwargs) {

        Str year = dateTime.__year__();
        Str month = dateTime.__month__();
        Str day = dateTime.__day__();
        Str hour = dateTime.__hour__();
        Str minute = dateTime.__minute__();
        Str second = dateTime.__second__();
        Str microsecond = dateTime.__microsecond__();

        Str[] keys = new Str[]{year, month, day, hour, minute, second, microsecond};
        String[] str_keys = new String[]{"year", "month", "day", "hour", "minute", "second", "microsecond"};
        // args tests
        for (int i = 0; i < args.length; i++) {
            String expected = new Str(args[i] + "").value;
            String actual = keys[i].value;
            assertEquals(expected, actual);
        }
        // kwargs tests
        String[] temp_keys = kwargs.keySet().toArray(new String[kwargs.size()]);
        for (int i = 0; i < temp_keys.length; i++) {
            int keyIndex = getIndexOfArray(temp_keys[i], str_keys);
            if (keyIndex != -1) {
                String expected = new Str(kwargs.get(temp_keys[i]) + "").value;
                String actual = keys[keyIndex].value;
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testConstructor() {
        // use args and empty kwargs to create instance
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18),
            Int.getInt(14), Int.getInt(0), Int.getInt(0), Int.getInt(0 / 1000)};
        DateTime dateTime = new DateTime(args, Collections.emptyMap());
        HashMap<String, Object> kwargs = new HashMap<>();
        testVariables(dateTime, args, kwargs);

        // use empty args and kwargs to create instance
        args = new Object[]{};
        kwargs.put("year", Int.getInt(2020));
        kwargs.put("month", Int.getInt(10));
        kwargs.put("day", Int.getInt(19));
        kwargs.put("hour", Int.getInt(15));
        kwargs.put("minute", Int.getInt(1));
        kwargs.put("second", Int.getInt(1));
        kwargs.put("microsecond", Int.getInt(1));
        dateTime = new DateTime(args, kwargs);
        testVariables(dateTime, args, kwargs);

        // use both args and kwargs to create instance
        kwargs.clear();
        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        kwargs.put("hour", Int.getInt(16));
        kwargs.put("minute", Int.getInt(2));
        kwargs.put("second", Int.getInt(2));
        kwargs.put("microsecond", Int.getInt(2));
        dateTime = new DateTime(args, kwargs);
        testVariables(dateTime, args, kwargs);

        // use different input types
        kwargs.clear();
        long year = 2000;
        short month = 1;
        byte day = 1;
        args = new Object[]{Int.getInt(year), Int.getInt(month), Int.getInt(day)};
        dateTime = new DateTime(args, kwargs);
        testVariables(dateTime, args, kwargs);

    }

    private void createErrorDateTime(Object[] args, HashMap<String, Object> kwargs, String errorMsg) {
        try {
            DateTime dateTime = new DateTime(args, kwargs);
        } catch (BaseException exception) {
            assertEquals(errorMsg, exception.getMessage());
        }
    }

    @Test
    public void testConstructorError() {

        // args.length < 3
        Object[] args = {Int.getInt(2021), Int.getInt(9)};
        HashMap<String, Object> kwargs = new HashMap<>();
        String errorMsg = "Required argument 'day' (pos 3) not found";
        createErrorDateTime(args, kwargs, errorMsg);

        args = new Object[]{Int.getInt(2021)};
        errorMsg = "Required argument 'month' (pos 2) not found";
        createErrorDateTime(args, kwargs, errorMsg);

        args = new Object[]{};
        errorMsg = "Required argument 'year' (pos 1) not found";
        createErrorDateTime(args, kwargs, errorMsg);

        // duplicated parameter
        kwargs.put("year", Int.getInt(2022));
        args = new Object[]{Int.getInt(2021), Int.getInt(9)};
        errorMsg = "positional argument follows keyword argument";
        createErrorDateTime(args, kwargs, errorMsg);

        // year out of range
        kwargs.clear();
        args = new Object[]{Int.getInt(-10), Int.getInt(9), Int.getInt(10)};
        errorMsg = "year -10is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{Int.getInt(10000), Int.getInt(9), Int.getInt(10)};
        errorMsg = "year 10000is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // month out of range
        args = new Object[]{Int.getInt(2000), Int.getInt(0), Int.getInt(10)};
        errorMsg = "month 0is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{Int.getInt(2000), Int.getInt(15), Int.getInt(10)};
        errorMsg = "month 15is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // day out of range
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(0)};
        errorMsg = "day 0is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(32)};
        errorMsg = "day 32is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // hour out of range
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(-1)};
        errorMsg = "hour -1is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(25)};
        errorMsg = "hour 25is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // minute out of range
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(-1)};
        errorMsg = "minute -1is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(100)};
        errorMsg = "minute 100is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // seconds out of range
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(2), Int.getInt(-1)};
        errorMsg = "second -1is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(2), Int.getInt(100)};
        errorMsg = "second 100is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // microsecond out of range
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(2), Int.getInt(50), Int.getInt(-1)};
        errorMsg = "microsecond -1is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(2), Int.getInt(50), Int.getInt(1000000)};
        errorMsg = "microsecond 1000000is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // use Str types and Float types as input
        args = new Object[]{new Str("2021"), new Str("9"), new Str("1")};
        errorMsg = "an integer is required (got type str)";
        createErrorDateTime(args, kwargs, errorMsg);

        args = new Object[]{new Float(2021.0), new Float(9.1), new Float(21.2)};
        errorMsg = "integer argument expected, got float";
        createErrorDateTime(args, kwargs, errorMsg);

    }


    @Test
    public void testDate() {
        Object[] args = {Int.getInt(2000), Int.getInt(2), Int.getInt(1)};
        DateTime dateTime = new DateTime(args, Collections.emptyMap());
        Str str = (Str) dateTime.date().__str__();
        assertEquals("2000-02-01", str.value);
    }


    @Test
    public void testWeekDay() {
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime = new DateTime(args, Collections.emptyMap());
        Object weekday = dateTime.weekday();
        Str str = new Str(weekday + "");
        assertEquals("5", str.value);
    }

    /**
     * test all python comparison operators in python: == , != ,  > , < , >= , <=
     */
    @Test
    public void testLt() {
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime1 = new DateTime(args, Collections.emptyMap());
        args = new Object[]{Int.getInt(2022), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime2 = new DateTime(args, Collections.emptyMap());
        boolean actual = ((Bool) dateTime1.__lt__(dateTime2)).value;
        assertTrue(actual);

        args = new Object[]{Int.getInt(2000), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime3 = new DateTime(args, Collections.emptyMap());
        actual = ((Bool) dateTime2.__lt__(dateTime3)).value;
        assertFalse(actual);

        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(10)};
        DateTime dateTime4 = new DateTime(args, Collections.emptyMap());
        actual = ((Bool) dateTime1.__lt__(dateTime4)).value;
        assertFalse(actual);
    }

    @Test
    public void testLe() {
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime1 = new DateTime(args, Collections.emptyMap());
        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime2 = new DateTime(args, Collections.emptyMap());
        boolean actual = ((Bool) dateTime1.__le__(dateTime2)).value;
        assertTrue(actual);

        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(22)};
        DateTime dateTime3 = new DateTime(args, Collections.emptyMap());
        actual = ((Bool) dateTime3.__le__(dateTime2)).value;
        assertFalse(actual);

        args = new Object[]{Int.getInt(2022), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime4 = new DateTime(args, Collections.emptyMap());
        actual = ((Bool) dateTime4.__le__(dateTime2)).value;
        assertFalse(actual);
    }

    @Test
    public void testEq() {
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime1 = new DateTime(args, Collections.emptyMap());
        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime2 = new DateTime(args, Collections.emptyMap());
        boolean actual = ((Bool) dateTime1.__eq__(dateTime2)).value;
        assertTrue(actual);

        args = new Object[]{Int.getInt(2022), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime3 = new DateTime(args, Collections.emptyMap());
        actual = ((Bool) dateTime3.__eq__(dateTime2)).value;
        assertFalse(actual);
    }

    @Test
    public void testNotEq() {
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime1 = new DateTime(args, Collections.emptyMap());
        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime2 = new DateTime(args, Collections.emptyMap());
        boolean actual = !((Bool) dateTime1.__eq__(dateTime2)).value;
        assertFalse(actual);

        args = new Object[]{Int.getInt(2022), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime3 = new DateTime(args, Collections.emptyMap());
        actual = !((Bool) dateTime3.__eq__(dateTime2)).value;
        assertTrue(actual);
    }

    @Test
    public void testGt() {
        Object[] args = {Int.getInt(2023), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime1 = new DateTime(args, Collections.emptyMap());
        args = new Object[]{Int.getInt(2022), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime2 = new DateTime(args, Collections.emptyMap());
        boolean actual = ((Bool) dateTime1.__gt__(dateTime2)).value;
        assertTrue(actual);

        args = new Object[]{Int.getInt(2000), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime3 = new DateTime(args, Collections.emptyMap());
        actual = ((Bool) dateTime3.__gt__(dateTime1)).value;
        assertFalse(actual);
    }

    @Test
    public void testGe() {
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime1 = new DateTime(args, Collections.emptyMap());
        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime2 = new DateTime(args, Collections.emptyMap());
        boolean actual = ((Bool) dateTime1.__ge__(dateTime2)).value;
        assertTrue(actual);

        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(22)};
        DateTime dateTime3 = new DateTime(args, Collections.emptyMap());
        actual = ((Bool) dateTime3.__ge__(dateTime2)).value;
        assertTrue(actual);

        args = new Object[]{Int.getInt(2022), Int.getInt(9), Int.getInt(18)};
        DateTime dateTime4 = new DateTime(args, Collections.emptyMap());
        actual = ((Bool) dateTime2.__ge__(dateTime4)).value;
        assertFalse(actual);
    }

    /**
     * Test class method
     */
    @Test
    public void testFromisoformat() {

        Str str = new Str("2012-12-11");
        DateTime dateTime = (DateTime) DateTime.fromisoformat(str);
        Str year = dateTime.__year__();
        Str month = dateTime.__month__();
        Str day = dateTime.__day__();
        assertEquals("2012",year.value);
        assertEquals("12",month.value);
        assertEquals("11",day.value);

        str = new Str("2012-12-11T01:01:01");
        dateTime = (DateTime) DateTime.fromisoformat(str);
        year = dateTime.__year__();
        month = dateTime.__month__();
        day = dateTime.__day__();
        Str hour = dateTime.__hour__();
        Str minute = dateTime.__minute__();
        Str second = dateTime.__second__();
        assertEquals("2012",year.value);
        assertEquals("12",month.value);
        assertEquals("11",day.value);
        assertEquals("1",hour.value);
        assertEquals("1",minute.value);
        assertEquals("1",second.value);

        str = new Str("2012-12-11 02:02:02");
        dateTime = (DateTime) DateTime.fromisoformat(str);
        year = dateTime.__year__();
        month = dateTime.__month__();
        day = dateTime.__day__();
        hour = dateTime.__hour__();
        minute = dateTime.__minute__();
        second = dateTime.__second__();
        assertEquals("2012",year.value);
        assertEquals("12",month.value);
        assertEquals("11",day.value);
        assertEquals("2",hour.value);
        assertEquals("2",minute.value);
        assertEquals("2",second.value);

        str = new Str("2012-12-11 02:02:02.111");
        dateTime = (DateTime) DateTime.fromisoformat(str);
        year = dateTime.__year__();
        month = dateTime.__month__();
        day = dateTime.__day__();
        hour = dateTime.__hour__();
        minute = dateTime.__minute__();
        second = dateTime.__second__();
        Str microsecond = dateTime.__microsecond__();
        assertEquals("2012",year.value);
        assertEquals("12",month.value);
        assertEquals("11",day.value);
        assertEquals("2",hour.value);
        assertEquals("2",minute.value);
        assertEquals("2",second.value);
        assertEquals("111",microsecond.value);

        str = new Str("2012-12-11T02:02:02.111");
        dateTime = (DateTime) DateTime.fromisoformat(str);
        year = dateTime.__year__();
        month = dateTime.__month__();
        day = dateTime.__day__();
        hour = dateTime.__hour__();
        minute = dateTime.__minute__();
        second = dateTime.__second__();
        microsecond = dateTime.__microsecond__();
        assertEquals("2012",year.value);
        assertEquals("12",month.value);
        assertEquals("11",day.value);
        assertEquals("2",hour.value);
        assertEquals("2",minute.value);
        assertEquals("2",second.value);
        assertEquals("111",microsecond.value);

    }

    private void createFromisoformatErrorHandler(Str str, String errorMsg) {
        try {
            DateTime dateTime = (DateTime) DateTime.fromisoformat(str);
        } catch (BaseException exception) {
            assertEquals(errorMsg, exception.getMessage());
        }
    }

    @Test
    public void testFromisoformatError() {
        Str str = new Str("2012-12-11TTT02:02:02.111");
        String errorMsg = "Invalid isoformat string: " + str.value;
        createFromisoformatErrorHandler(str,errorMsg);

        str = new Str("2012-12");
        errorMsg = "Invalid isoformat string: " + str.value;
        createFromisoformatErrorHandler(str,errorMsg);

        str = new Str("2012-12-11 02:02:");
        errorMsg = "Invalid isoformat string: " + str.value;
        createFromisoformatErrorHandler(str,errorMsg);

    }


    /**
     * Test instance method
     */
    @Test
    public void testReplace() {
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18),
            Int.getInt(14), Int.getInt(0), Int.getInt(0), Int.getInt(0 / 1000)};
        DateTime dateTime1 = new DateTime(args, Collections.emptyMap());
        HashMap<String, Object> kwargs = new HashMap<>();
        kwargs.put("year", Int.getInt(2022));
        DateTime dateTime2 = (DateTime) dateTime1.replace(kwargs);
        String year = ((Str) dateTime2.__year__()).value;
        assertEquals("2022", year);

    }

    @Test
    public void testReplaceError() {
        Object[] args = {Int.getInt(2021), Int.getInt(9), Int.getInt(18),
            Int.getInt(14), Int.getInt(0), Int.getInt(0), Int.getInt(0 / 1000)};
        DateTime dateTime1 = new DateTime(args, Collections.emptyMap());
        HashMap<String, Object> kwargs = new HashMap<>();

        // Str type as input
        kwargs.put("year", new Str("2022"));
        String errorMsg = "an integer is required (got type str)";
        try {
            DateTime dateTime2 = (DateTime) dateTime1.replace(kwargs);
        } catch (BaseException exception){
            assertEquals(errorMsg, exception.getMessage());
        }

        // Float type as input
        kwargs.clear();
        kwargs.put("year", new Float(2022));
        errorMsg = "integer argument expected, got float";
        try {
            DateTime dateTime2 = (DateTime) dateTime1.replace(kwargs);
        } catch (BaseException exception){
            assertEquals(errorMsg, exception.getMessage());
        }

    }

}
