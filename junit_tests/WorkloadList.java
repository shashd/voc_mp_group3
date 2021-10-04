import org.python.Object;
import org.python.types.Int;
import org.python.types.List;
import org.python.types.Bool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class WorkloadList {
    public static List createRandomList() {
        List list = new List();
        Random rand = new Random();

        for (int i=0; i < 10000; i++) {
            org.python.types.Int rand_int = org.python.types.Int.getInt(rand.nextInt(1000));
            list.append(rand_int);
        }
        return list;
    }

    public static List insertRandomInt() {
        Random rand = new Random();
        List list = createRandomList();

        for (int j=0; j < 50000; j++) {
            org.python.types.Int rand_index = org.python.types.Int.getInt(rand.nextInt((int) list.__len__().value));
            list.insert(rand_index, org.python.types.Int.getInt(556));
        }
        System.out.println(list.__len__().value);
        return list;
    }

    public static List insertIntFront() {
        List list = createRandomList();

        for (int j=0; j < 50000; j++) {
            list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(556));
        }
        System.out.println(list.__len__().value);
        return list;
    }

    public static void deleteAllItemsRandom(org.python.types.List list){
        Random rand = new Random();
        long  num = list.__len__().value;
        for (int index =0;index < num; index++){
            org.python.types.Int rand_index = org.python.types.Int.getInt(rand.nextInt((int) list.__len__().value));
            //org.python.types.Int rand_index = org.python.types.Int.getInt(70);
            list.__delitem__(rand_index);
        }
    }

    public static void deleteAllItemsFront(org.python.types.List list){
        long  num = list.__len__().value;
        for (int index =0;index < num; index++){
            list.__delitem__(org.python.types.Int.getInt(0));
        }
    }



    public static void main(String[] str){

        long start = System.nanoTime();
        List insertList = insertRandomInt();
        long end = System.nanoTime();
        System.out.println(String.format("InsertManyItemsRandom spends %f seconds.",((double) end-start)/1_000_000_000));

        start = System.nanoTime();
        List insertFrontList = insertIntFront();
        end = System.nanoTime();
        System.out.println(String.format("InsertManyItemsFront spends %f seconds.",((double) end-start)/1_000_000_000));

        start = System.nanoTime();
        deleteAllItemsRandom(insertList);
        end = System.nanoTime();
        System.out.println(String.format("DeleteManyItemsRandom spends %f seconds.",((double) end-start)/1_000_000_000));

        List deleteFrontList = createRandomList();
        start = System.nanoTime();
        deleteAllItemsFront(deleteFrontList);
        end = System.nanoTime();
        System.out.println(String.format("DeleteManyItemsFront spends %f seconds.",((double) end-start)/1_000_000_000));

        start = System.nanoTime();
        for (int j=0; j < 100; j++) {
            List list = createRandomList();
            list.sort(null,Bool.getBool(false));
        }
        end = System.nanoTime();
        System.out.println(String.format("Sort spends %f seconds.",((double) end-start)/1_000_000_000));
    }

}
