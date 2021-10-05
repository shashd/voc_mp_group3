import org.junit.jupiter.api.Test;
import org.python.Object;
import org.python.stdlib.collections.OrderedDict;
import org.python.stdlib.collections.OrderedDictKeys;
import org.python.types.Dict;
import org.python.types.Int;
import org.python.types.List;
import org.python.types.Str;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderedDictWorkLoad {

    private static final int LENGTH = 25000;
    private static Object[] args;
    private static Map<String, Object> kwargs;
    private static OrderedDict orderedDict;

    /**
     * Generate k-v and init orderDict
     */
    public static void insertDictByKwargs(){
        for (int i = 0; i < LENGTH; i++){
            kwargs.put(String.valueOf(i), Int.getInt(i));
        }
        orderedDict = new OrderedDict(args, kwargs);
    }

    /**
     * Delete all items from OrderedDict
     */
    public static void deleteDictItems(){
        for (int i = 0; i < LENGTH; i++){
            orderedDict.popitem();
        }
    }

    /**
     * Update Dict by args or kwargs
     */
    public static void updateDict(){
        List tuple_list = new List();
        ArrayList<Object> tuple = new ArrayList<>(2);
        for (int i = 0; i < LENGTH; i++){
            if (i % 2 == 0){
                // update by args
                tuple.add(new Str(String.valueOf(i)));
                tuple.add(Int.getInt(i));
                tuple_list.append(new org.python.types.Tuple(tuple));
                orderedDict.update(tuple_list, null);
                tuple.clear();
                tuple_list.clear();
            } else{
                // update by kwargs
                kwargs.put(String.valueOf(i), Int.getInt(i));
                Dict temp = new Dict(args, kwargs);
                orderedDict.update(null, temp);
                kwargs.clear();
            }
        }
    }

    /**
     * Move a certain k-v to the end
     */
    public static void moveToEnd(){
        for (int i = 0; i < LENGTH; i++){
            orderedDict.move_to_end(new Str(String.valueOf(i)),null);
        }
    }

    /**
     * Reset all the values with the given keys
     */
    public static void fromKeys(){
        OrderedDictKeys odk = (OrderedDictKeys) orderedDict.keys();
        orderedDict = (OrderedDict) OrderedDict.fromkeys(odk,Int.getInt(1));
    }

    public static void performanceTest(){
        args = new Object[]{null};
        kwargs = new HashMap<String, Object>();
        long totalBegin, totalEnd;
        
        // insert
        long start = System.nanoTime();
        totalBegin = start;
        insertDictByKwargs();
        long end = System.nanoTime();
        System.out.println(String.format("insertDictByKwargs spends %f seconds.",((double) end-start)/1_000_000_000));

        // remove
        start = System.nanoTime();
        deleteDictItems();
        end = System.nanoTime();
        System.out.println(String.format("deleteDictItems spends %f seconds.",((double) end-start)/1_000_000_000));

        kwargs.clear();

        // update
        start = System.nanoTime();
        updateDict();
        end = System.nanoTime();
        System.out.println(String.format("updateDict spends %f seconds.",((double) end-start)/1_000_000_000));

        // move to end
        start = System.nanoTime();
        moveToEnd();
        end = System.nanoTime();
        System.out.println(String.format("moveToEnd spends %f seconds.",((double) end-start)/1_000_000_000));

        // fromkeys
        start = System.nanoTime();
        fromKeys();
        end = System.nanoTime();
        totalEnd = end;
        System.out.println(String.format("fromKeys spends %f seconds.",((double) end-start)/1_000_000_000));

        System.out.println(String.format("All functions spends %f seconds.",((double) totalEnd-totalBegin)/1_000_000_000));

    }

    public static void main(String[] args) {
        performanceTest();
     }

}
