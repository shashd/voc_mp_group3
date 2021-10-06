package voc;

import org.junit.Test;
import org.python.exceptions.AttributeError;
import org.python.exceptions.KeyError;
import org.python.exceptions.TypeError;
import org.python.types.*;
import org.python.types.List;
import org.python.types.Object;
import org.python.types.Set;

import java.util.*;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

public class DictTest {

    @Test(expected = AttributeError.class)
    public void test_setattr() {
	try {
	    Dict dict = new Dict();
	    dict.__setattr__("attr", Int.getInt(42));
	} catch (AttributeError e) {
	    assertEquals(e.__str__().toJava(), "'dict' object has no attribute 'attr'");
	    throw e;
	}
    }

    @Test(expected = AttributeError.class)
    public void test_getattr() {
	try {
	    Dict dict = new Dict();
	    dict.__getattr__("attr");
	} catch (AttributeError e) {
	    assertEquals(e.__str__().toJava(), "'dict' object has no attribute 'attr'");
	    throw e;
	}
    }

    @Test
    public void test_creation() {
	Map<org.python.Object, org.python.Object> dict_map = new HashMap<>();
	Dict dict = new Dict();
	assertEquals(dict.__repr__().toJava(), "{}");
	dict_map.put(new Str("a"), Int.getInt(1));
	dict = new Dict(dict_map);
	assertEquals(dict.__repr__().toJava(), "{'a': 1}");
    }

    @Test(expected = KeyError.class)
    public void test_getitem() {
	Int y = Int.getInt(37);
	Map<org.python.Object, org.python.Object> dict_map = new HashMap<>();
	dict_map.put(new Str("a"), Int.getInt(1));
	dict_map.put(new Str("b"), Int.getInt(2));
	dict_map.put(new Str("c"), y);
	Dict dict = new Dict(dict_map);
	assertTrue(dict.__contains__(new Str("a")).toBoolean());
	assertFalse(!dict.__contains__(new Str("a")).toBoolean());
	assertEquals(dict.__getitem__(new Str("a")), Int.getInt(1));

	dict_map = new HashMap<>();
	dict_map.put(new Str("a"), Int.getInt(1));
	dict_map.put(new Str("b"), Int.getInt(2));
	dict = new Dict(dict_map);
	assertFalse(dict.__contains__(new Str("c")).toBoolean());
	assertTrue(!dict.__contains__(new Str("c")).toBoolean());

	try {
	    dict.__getitem__(new Str("c"));
	} catch (KeyError e) {
	    assertEquals(e.__str__().toJava(), "'c'");
	    throw e;
	}
    }

    @Test
    public void test_clear() {
	Map<org.python.Object, org.python.Object> dict_map = new HashMap<>();
	dict_map.put(new Str("a"), Int.getInt(1));
	dict_map.put(new Str("b"), Int.getInt(2));
	Dict dict = new Dict(dict_map);
	assertTrue(dict.__contains__(new Str("a")).toBoolean());
	dict.clear();
	assertFalse(dict.__contains__(new Str("a")).toBoolean());
	assertEquals(dict.__repr__().toJava(), "{}");

	dict = new Dict();
	assertFalse(dict.__contains__(new Str("a")).toBoolean());
	dict.clear();
	assertFalse(dict.__contains__(new Str("a")).toBoolean());
	assertEquals(dict.__repr__().toJava(), "{}");
    }

    @Test
    public void test_builtin_constructor() {
	Dict x = new Dict();
	assertEquals(x.__repr__().toJava(), "{}");
	assertFalse(x.__contains__(new Str("a")).toBoolean());

	Object[] args = new Object[1];
	java.util.List<org.python.Object> org = new ArrayList<>(2);
	List testList = new List(org);
	java.util.List<org.python.Object> tempList = new ArrayList<>();
	tempList.add(new Str("a"));
	tempList.add(Int.getInt(1));
	Tuple tuple1 = new Tuple(tempList);
	java.util.List<org.python.Object> tempList2 = new ArrayList<>();
	tempList2.add(new Str("b"));
	tempList2.add(Int.getInt(2));
	Tuple tuple2 = new Tuple(tempList2);
	org.add(tuple1);
	org.add(tuple2);
	args[0] = testList;
	x = new Dict(args, new HashMap<>());
	assertTrue(x.__contains__(new Str("a")).toBoolean());
	assertEquals(x.get(new Str("a"), null), Int.getInt(1));
	assertFalse(x.__contains__(new Str("c")).toBoolean());
    }

    @Test
    public void test_builtin_constructor_kwargs() {
	Map<java.lang.String, org.python.Object> dict_map = new HashMap<>();
	dict_map.put("a", Int.getInt(1));
	dict_map.put("b", Int.getInt(2));
	Dict d = new Dict(new Object[1], dict_map);

	assertTrue(d.__contains__(new Str("a")).toBoolean());
	assertTrue(d.__contains__(new Str("b")).toBoolean());
	assertFalse(d.__contains__(new Str("c")).toBoolean());
	assertEquals(d.__getitem__(new Str("b")), Int.getInt(2));

	Map<java.lang.String, org.python.Object> dict_map2 = new HashMap<>();
	dict_map2.put("b", Int.getInt(3));
	Object[] args = new Object[1];
	args[0] = d;
	Dict d2 = new Dict(args, dict_map2);

	assertTrue(d2.__contains__(new Str("a")).toBoolean());
	assertTrue(d2.__contains__(new Str("b")).toBoolean());
	assertFalse(d2.__contains__(new Str("c")).toBoolean());
	assertEquals(d2.__getitem__(new Str("b")), Int.getInt(3));
    }

    @Test
    public void test_method_pop() {
	Map<org.python.Object, org.python.Object> dict_map = new HashMap<>();
	dict_map.put(Int.getInt(1), Int.getInt(1));
	dict_map.put(Int.getInt(2), Int.getInt(2));
	Dict dict = new Dict(dict_map);

	assertEquals(dict.pop(Int.getInt(1), null), Int.getInt(1));
	assertEquals(dict.pop(Int.getInt(2), Int.getInt(37)), Int.getInt(2));

	try {
	    dict.pop(Int.getInt(8), null);
	} catch (KeyError e) {
	    assertEquals("8", e.__str__().toJava());
	}

	assertEquals(dict.pop(Int.getInt(7), Int.getInt(42)), Int.getInt(42));
    }

    public void test_method_setdefault() {
	Map<org.python.Object, org.python.Object> dict_map = new HashMap<>();
	dict_map.put(Int.getInt(42), new Str("Babel"));
	Dict x = new Dict(dict_map);
	assertEquals(x.setdefault(Int.getInt(42), null), new Str("Babel"));
	assertEquals(x.setdefault(Int.getInt(1), null), null);
	assertTrue(x.__getitem__(Int.getInt(1)) == null);
	assertEquals(x.setdefault(new Str("Davied"), new Str("Gilmour")), new Str("Babel"));

	try {
	    x.setdefault(new List(), Int.getInt(42));
	} catch (TypeError err) {
	    assertEquals("unhashable type: 'list'", err.toString());
	}
    }

    @Test
    public void test_copy() {
	Map<org.python.Object, org.python.Object> x = new HashMap<>();
	x.put(Int.getInt(42), new Str("Babel"));

	Dict dict_x = new Dict(x);
	Dict dict_y = (Dict) dict_x.copy();

	assertEquals(dict_y.__repr__().toJava(), "{42: 'Babel'}");

	assertTrue(dict_x.__eq__(dict_y).toBoolean());

	assertTrue(dict_x != dict_y);
    }

    @Test
    public void test_update() {
	Map<org.python.Object, org.python.Object> emptyMap = new HashMap<>();
	Str a = new Str("a");
	Str b = new Str("b");
	Int one = Int.getInt(1);
	Int two = Int.getInt(2);
	Map<org.python.Object, org.python.Object> kwargsMap = new HashMap<>();
	kwargsMap.put(a, one);
	kwargsMap.put(b, two);
	Dict kwargsDictInput = new Dict(kwargsMap);
	Dict kwargsDict = new Dict(emptyMap);
	kwargsDict.update(null, kwargsDictInput);
	assertEquals(kwargsDict.__repr__().toJava(), "{'a': 1, 'b': 2}");

	Map<org.python.Object, org.python.Object> dictInput = new HashMap<>();
	Dict dictDictInput = new Dict(dictInput);
	dictInput.put(a, one);
	dictInput.put(b, two);
	Dict dictDict = new Dict(emptyMap);
	dictDict.update(dictDictInput, null);
	assertEquals(dictDict.__repr__().toJava(), "{'a': 1, 'b': 2}");

	Object[] objectList = new Object[1];
	java.util.List<org.python.Object> org = new ArrayList(2);
	List testList = new org.python.types.List(org);

	java.util.List<org.python.Object> tempList = new ArrayList<>();
	tempList.add(a);
	tempList.add(one);
	Tuple tuple1 = new Tuple(tempList);

	java.util.List<org.python.Object> tempList2 = new ArrayList<>();
	tempList2.add(a);
	tempList2.add(one);
	Tuple tuple2 = new Tuple(tempList2);

	org.add(tuple1);
	org.add(tuple2);

	objectList[0] = testList;

	Dict tupleDict = new Dict(emptyMap);
	tupleDict.update(testList, null);
	assertEquals(tupleDict.__repr__().toJava(), "{'a': 1, 'b': 2}");
    }

    @Test(expected = NullPointerException.class)
    public void test_fromkeys_missing_iterable() {
	try {
	    System.out.println(Dict.fromkeys(null, null));
	} catch (TypeError err) {
	    assertEquals("fromkeys expected at least 1 arguments, got 0", err.toString());
	}
    }

    @Test
    public void test_values() {
	Map<org.python.Object, org.python.Object> dict_map = new HashMap<>();
	dict_map.put(Int.getInt(1), Int.getInt(1));
	dict_map.put(Int.getInt(2), Int.getInt(2));
	dict_map.put(Int.getInt(3), Int.getInt(3));

	Dict dict = new Dict(dict_map);

	org.python.Object y = dict.values();

	assertEquals(y.type().__repr__().toJava(), "<class 'dict_values'>");
	assertEquals(y.__len__(), Int.getInt(3));
	assertTrue(dict.__contains__(Int.getInt(3)).toBoolean());
	assertTrue(!dict.__contains__(Int.getInt(42)).toBoolean());

	Int z = (Int) y.__len__();

	assertTrue("Working as expected", z.value > 0);

	int s = 0;

	for (org.python.Object value : dict_map.values()) {
	    Int v = (Int) value;
	    s += v.value;
	}

	assertEquals(s, 6);
    }

    public Tuple createPythonIntegerTuple(int x, int y) {
	java.util.List<org.python.Object> tmpTupleList = new ArrayList<>();
	tmpTupleList.add(Int.getInt(x));
	tmpTupleList.add(Int.getInt(y));

	return new Tuple(tmpTupleList);
    }

    public Set javaSetToPythonSet(java.util.Set<java.util.Map.Entry<org.python.Object, org.python.Object>> set) {
	Set resultSet = new Set();

	for (java.util.Map.Entry<org.python.Object, org.python.Object> tuple : set) {
	    Int a = (Int) tuple.getKey();
	    Int b = (Int) tuple.getValue();
	    resultSet.add(createPythonIntegerTuple((int) a.value, (int) b.value));
	}
	return resultSet;
    }

    @Test
    public void test_items() {
	Map<org.python.Object, org.python.Object> dict_map_x = new HashMap<>();
	dict_map_x.put(Int.getInt(1), Int.getInt(1));
	dict_map_x.put(Int.getInt(2), Int.getInt(2));
	dict_map_x.put(Int.getInt(3), Int.getInt(3));

	Map<org.python.Object, org.python.Object> dict_map_x2 = new HashMap<>();
	dict_map_x2.put(Int.getInt(1), Int.getInt(1));
	dict_map_x2.put(Int.getInt(2), Int.getInt(2));
	dict_map_x2.put(Int.getInt(3), Int.getInt(3));
	dict_map_x2.put(Int.getInt(4), Int.getInt(4));

	Dict dict_x = new Dict(dict_map_x);
	Dict dict_x2 = new Dict(dict_map_x2);

	org.python.Object y = dict_x.items();
	org.python.Object y2 = dict_x2.items();

	Int len_of_y = (Int) y.__len__();

	assertEquals(len_of_y, Int.getInt(3));

	Tuple tmpTuple = createPythonIntegerTuple(1, 1);

	assertTrue(y.__contains__(tmpTuple).toBoolean());

	Tuple tmpTuple2 = createPythonIntegerTuple(1, 2);

	assertFalse(y.__contains__(tmpTuple2).toBoolean());

	int s1 = 0;
	int s2 = 0;

	for (org.python.Object key : dict_map_x.keySet()) {
	    Int k = (Int) key;
	    s1 += k.value;
	}

	for (org.python.Object value : dict_map_x.values()) {
	    Int v = (Int) value;
	    s2 += v.value;
	}

	assertEquals(s1, 6);
	assertEquals(s2, 6);

	java.util.Set<java.util.Map.Entry<org.python.Object, org.python.Object>> sList = new HashSet<Map.Entry<org.python.Object, org.python.Object>>();
	sList.add(new java.util.AbstractMap.SimpleEntry<>(Int.getInt(1), Int.getInt(1)));
	sList.add(new java.util.AbstractMap.SimpleEntry<>(Int.getInt(1), Int.getInt(2)));
	sList.add(new java.util.AbstractMap.SimpleEntry<>(Int.getInt(2), Int.getInt(2)));
	sList.add(new java.util.AbstractMap.SimpleEntry<>(Int.getInt(2), Int.getInt(3)));

	Set sListPython = javaSetToPythonSet(sList);
	Set yListPython = javaSetToPythonSet(dict_map_x.entrySet());

	assertEquals(yListPython.intersection(sListPython).__repr__().toJava(), "{(1, 1), (2, 2)}");
	assertEquals(yListPython.intersection(sListPython).__len__(), Int.getInt(2));

	assertEquals(yListPython.union(sListPython).__len__(), Int.getInt(5));

	assertEquals(yListPython.__xor__(sListPython).__len__(), Int.getInt(3));

	assertEquals(yListPython.difference(sListPython).__len__(), Int.getInt(1));
    }

    @Test
    public void testPythonConstructorArgs() {
	org.python.Object[] args = { null };
	Dict dict = new Dict(args, new HashMap<>());
	assertEquals(dict.__repr__().toJava(), "{}");

	org.python.Object[] args2 = { dict };
	dict = new Dict(args2, new HashMap<>());
	assertEquals(dict.__repr__().toJava(), "{}");
    }

    @Test
    public void testPythonConstructorKwargs() {
	org.python.Object[] args = { null };
	Map<String, org.python.Object> kwargs = new HashMap<>();

	Dict dict = new Dict(args, kwargs);
	assertEquals(dict.__repr__().toJava(), "{}");

	kwargs.put("a", org.python.types.Int.getInt(42));
	dict = new Dict(args, kwargs);
	assertEquals(dict.__repr__().toJava(), "{'a': 42}");

	kwargs = new HashMap<>();
	kwargs.put("a", org.python.types.Int.getInt(42));
	kwargs.put("b", org.python.types.Int.getInt(2));
	kwargs.put("c", org.python.types.Int.getInt(1));
	kwargs.put("d", org.python.types.Int.getInt(8));
	dict = new Dict(args, kwargs);
	assertEquals(dict.__repr__().toJava(), "{'a': 42, 'b': 2, 'c': 1, 'd': 8}");
    }

    @Test
    public void testPythonConstructorTuples() {
	Map<String, org.python.Object> kwargs = new HashMap<>();
	java.util.List<org.python.Object> dictionaryElements = new ArrayList<>();

	org.python.types.List arg = new org.python.types.List();
	org.python.Object[] args = { arg };
	Dict dict = new Dict(args, kwargs);
	assertEquals(dict.__repr__().toJava(), "{}");

	arg = new org.python.types.List();
	dictionaryElements = new ArrayList<>();
	dictionaryElements.add(new org.python.types.Str("a"));
	dictionaryElements.add(org.python.types.Int.getInt(42));

	arg.append(new org.python.types.Tuple(dictionaryElements));
	org.python.Object[] args2 = { arg };
	dict = new Dict(args2, kwargs);
	assertEquals(dict.__repr__().toJava(), "{'a': 42}");
    }

    @Test
    public void testPythonConstructorLists() {
	Map<String, org.python.Object> kwargs = new HashMap<>();

	org.python.types.List dictionaryElement = new org.python.types.List();
	dictionaryElement.append(new org.python.types.Str("a"));
	dictionaryElement.append(org.python.types.Int.getInt(42));

	org.python.types.List arg = new org.python.types.List();
	arg.append(dictionaryElement);

	org.python.Object[] args = { arg };
	Dict dict = new Dict(args, kwargs);
	assertEquals(dict.__repr__().toJava(), "{'a': 42}");
    }

    @Test
    public void testPythonConstructorStrings() {
	Map<String, org.python.Object> kwargs = new HashMap<>();

	org.python.types.Str dictionaryElement = new org.python.types.Str("ab");

	org.python.types.List arg = new org.python.types.List();
	arg.append(dictionaryElement);

	org.python.Object[] args = { arg };
	Dict dict = new Dict(args, kwargs);
	assertEquals(dict.__repr__().toJava(), "{'a': 'b'}");
    }

    @Test(expected = org.python.exceptions.TypeError.class)
    public void testPythonConstructorIcorrectArg() {
	Map<String, org.python.Object> kwargs = new HashMap<>();

	org.python.types.List arg = new org.python.types.List();
	arg.append(org.python.types.Bool.getBool(true));

	org.python.Object[] args2 = { arg };
	Dict dict = new Dict(args2, kwargs);
    }

    @Test(expected = org.python.exceptions.ValueError.class)
    public void testPythonConstructorIncorrectLengthOfIterable() {
	Map<String, org.python.Object> kwargs = new HashMap<>();
	java.util.List<org.python.Object> dictionaryElements = new ArrayList<>();

	org.python.types.List arg = new org.python.types.List();
	dictionaryElements.add(new org.python.types.Str("a"));
	dictionaryElements.add(org.python.types.Int.getInt(42));
	dictionaryElements.add(org.python.types.Int.getInt(100));

	arg.append(new org.python.types.Tuple(dictionaryElements));
	org.python.Object[] args2 = { arg };
	Dict dict = new Dict(args2, kwargs);
    }
}
