import org.junit.jupiter.api.Test;
import org.python.exceptions.BaseException;
import org.python.stdlib.datetime.DateTime;

import org.python.types.Int;
import org.python.types.Str;
import org.python.Object;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class DateTimeTest {

    // writing some helper functions to generate args and kwargs
    // In your JUnit tests please cover cases for args, kwargs and combinations thereof!
    // Your tests should cover a good variety of cases: basic inputs and likely/interesting
    // exceptions and edge cases.
    // To give an example from the previous sprint: for math.sqrt,
    // this could e.g. be positive ints and floats, negative ints and floats (ValueError) and
    // 1-2 examples for non-numerical inputs, e.g. strings and maybe also some form of collection/iterable,
    // like list (TypeErrors).

    private int getIndexOfArray(String target, String[] arr){
        for (int i = 0; i < arr.length; i++){
            if (arr[i].equals(target)){
                return i;
            }
        }
        return -1;
    }

    private void testVariables(DateTime dateTime, Object[] args, HashMap<String,Object> kwargs){

        Str year = dateTime.__year__();
        Str month = dateTime.__month__();
        Str day = dateTime.__day__();
        Str hour = dateTime.__hour__();
        Str minute = dateTime.__minute__();
        Str second = dateTime.__second__();
        Str microsecond = dateTime.__microsecond__();

        Str[] keys = new Str[]{year,month,day,hour,minute,second,microsecond};
        String[] str_keys = new String[]{"year","month","day","hour","minute","second","microsecond"};
        // args tests
        for (int i = 0; i < args.length; i++){
            String expected = new Str(args[i] + "").value;
            String actual = keys[i].value;
            assertEquals(expected,actual);
        }
        // kwargs tests
        String[] temp_keys = kwargs.keySet().toArray(new String[kwargs.size()]);
        for (int i = 0; i < temp_keys.length; i++){
            int keyIndex = getIndexOfArray(temp_keys[i],str_keys);
            if (keyIndex != -1){
                String expected = new Str(kwargs.get(temp_keys[i]) + "").value;
                String actual = keys[keyIndex].value;
                assertEquals( expected, actual);
            }
        }
    }

    @Test
    public void testConstructor(){
        // use args and empty kwargs to create instance
        Object[] args = { Int.getInt(2021), Int.getInt(9), Int.getInt(18),
                    Int.getInt(14), Int.getInt(0), Int.getInt(0), Int.getInt(0 / 1000) };
        DateTime dateTime = new DateTime(args,Collections.emptyMap());
        HashMap<String,Object> kwargs = new HashMap<>();
        testVariables(dateTime,args,kwargs);

        // use empty args and kwargs to create instance
        args = new Object[]{};
        kwargs.put("year",Int.getInt(2020));
        kwargs.put("month",Int.getInt(10));
        kwargs.put("day",Int.getInt(19));
        kwargs.put("hour",Int.getInt(15));
        kwargs.put("minute",Int.getInt(1));
        kwargs.put("second",Int.getInt(1));
        kwargs.put("microsecond",Int.getInt(1));
        dateTime = new DateTime(args,kwargs);
        testVariables(dateTime,args,kwargs);

        // use both args and kwargs to create instance
        kwargs.clear();
        args = new Object[]{Int.getInt(2021), Int.getInt(9), Int.getInt(18)};
        kwargs.put("hour",Int.getInt(16));
        kwargs.put("minute",Int.getInt(2));
        kwargs.put("second",Int.getInt(2));
        kwargs.put("microsecond",Int.getInt(2));
        dateTime = new DateTime(args,kwargs);
        testVariables(dateTime,args,kwargs);
    }

    private void createErrorDateTime(Object[] args, HashMap<String, Object> kwargs, String errorMsg){
        try {
            DateTime dateTime = new DateTime(args,kwargs);
        }
        catch (BaseException exception){
            assertEquals(errorMsg, exception.getMessage());
        }
    }

    @Test
    public void testConstructorError(){

        // args.length < 3
        Object[] args = { Int.getInt(2021), Int.getInt(9) };
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
        kwargs.put("year",Int.getInt(2022));
        args = new Object[]{ Int.getInt(2021), Int.getInt(9) };
        errorMsg = "positional argument follows keyword argument";
        createErrorDateTime(args, kwargs, errorMsg);

        // year out of range
        kwargs.clear();
        args = new Object[]{ Int.getInt(-10), Int.getInt(9), Int.getInt(10) };
        errorMsg = "year -10is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{ Int.getInt(10000), Int.getInt(9), Int.getInt(10) };
        errorMsg = "year 10000is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // month out of range
        args = new Object[]{ Int.getInt(2000), Int.getInt(0), Int.getInt(10) };
        errorMsg = "month 0is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{ Int.getInt(2000), Int.getInt(15), Int.getInt(10) };
        errorMsg = "month 15is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // day out of range
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(0) };
        errorMsg = "day 0is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(32) };
        errorMsg = "day 32is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // hour out of range
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(-1) };
        errorMsg = "hour -1is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(25) };
        errorMsg = "hour 25is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // minute out of range
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(-1)};
        errorMsg = "minute -1is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
                Int.getInt(100)};
        errorMsg = "minute 100is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // seconds out of range
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(2), Int.getInt(-1)};
        errorMsg = "second -1is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(2), Int.getInt(100)};
        errorMsg = "second 100is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // microsecond out of range
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(2), Int.getInt(50), Int.getInt(-1)};
        errorMsg = "microsecond -1is out of range";
        createErrorDateTime(args, kwargs, errorMsg);
        args = new Object[]{ Int.getInt(2000), Int.getInt(10), Int.getInt(2), Int.getInt(2),
            Int.getInt(2), Int.getInt(50), Int.getInt(1000000)};
        errorMsg = "microsecond 1000000is out of range";
        createErrorDateTime(args, kwargs, errorMsg);

        // todo: with other types of inputs, no matter works or throws error

    }


    @Test
    public void testDate() {
        Object[] args = { Int.getInt(2000),Int.getInt(2),Int.getInt(1)};
        DateTime dateTime = new DateTime(args,Collections.emptyMap());
        Str str = (Str) dateTime.date().__str__();
        assertEquals("2000-02-01", str.value);
    }


    @Test
    public void testWeekDay(){
        Object[] args = { Int.getInt(2021),Int.getInt(9),Int.getInt(18)};
        DateTime dateTime = new DateTime(args,Collections.emptyMap());
        Object weekday = dateTime.weekday();
        Str str = new Str(weekday + "");
        assertEquals("5", str.value);
    }
}
