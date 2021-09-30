import org.python.Object;
import org.python.types.Int;
import org.python.types.List;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class WorkloadList {
    public static List insertRandomInt() {
        List list = new List();
        Random rand = new Random();

        for (int i=0; i < 322000; i++) {
            org.python.types.Int rand_int = org.python.types.Int.getInt(rand.nextInt(1000));
            list.append(rand_int);
        }

        for (int j=0; j < 1000; j++) {
            org.python.types.Int rand_index = org.python.types.Int.getInt(rand.nextInt((int) list.__len__().value));
            list.insert(rand_index, org.python.types.Int.getInt(556));
        }
        System.out.println(list.__len__().value);
        return list;
    }

    public static void deleteAllItems(org.python.types.List list){
        long  num = list.__len__().value;
        for (int index =0;index < num; index++){
            list.__delitem__(org.python.types.Int.getInt(0));
        }
    }

    public static void main(String[] str){
        long start = System.nanoTime();

        List insertList = insertRandomInt();

        long end = System.nanoTime();
        System.out.println(String.format("InsertManyItems spends %f seconds.",((double) end-start)/1_000_000_000));

        start = System.nanoTime();

        deleteAllItems(insertList);

        end = System.nanoTime();

        System.out.println(String.format("DeleteManyItems spends %f seconds.",((double) end-start)/1_000_000_000));
    }

}
