import org.python.types.Dict;
import org.python.types.Int;
import org.python.types.Str;
import java.util.*;
import java.util.Map;

public class DictWorkLoad {

    private static final int MAX = 100000;
    private static Map<org.python.Object, org.python.Object> dict_map;
    private static Dict dict;

    public static void main(String[] args) {
        dict_map = new HashMap<>();

        // Creates a Dict
        long start = System.nanoTime();
        for (int i = 0; i < MAX; i++){
            dict_map.put(new Str(""+i), Int.getInt(i));
        }
        dict = new Dict(dict_map);
        long end = System.nanoTime();
        System.out.println(String.format("Dict create takes %f seconds.",((double) end-start)/1_000_000_000));

        // Contains
        start = System.nanoTime();
        for (int i = 0; i < MAX; i++){
            dict.__contains__(new Str(""+i)).toBoolean();
        }
        end = System.nanoTime();
        System.out.println(String.format("Dict contains takes %f seconds.",((double) end-start)/1_000_000_000));

        // Pop
        start = System.nanoTime();
        for (int i = 0; i < MAX/2; i++){
            dict.pop(new Str(""+i), null);
        }
        end = System.nanoTime();
        System.out.println(String.format("Dict pop takes %f seconds.",((double) end-start)/1_000_000_000));

        // Popitem
        start = System.nanoTime();
        for (int i = 0; i < MAX/2; i++){
            dict.popitem();
        }
        end = System.nanoTime();
        System.out.println(String.format("Dict popitem takes %f seconds.",((double) end-start)/1_000_000_000));

        // Update
        start = System.nanoTime();
        for (int i = 0; i < MAX; i++){
            dict_map = new HashMap<>();
            dict_map.put(new Str("a"+i), Int.getInt(1));
            Dict dict2 = new Dict(dict_map);
            dict.update(dict2, null);
        }
        end = System.nanoTime();
        System.out.println(String.format("Dict update takes %f seconds.",((double) end-start)/1_000_000_000));
    }
}
