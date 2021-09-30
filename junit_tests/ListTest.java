package voc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ListTest {
    /**
     * Creates a Python list object of Python integers from Java integers.
     *
     * Example: py_list_from_java_ints(1, 2, 3) is equal to
     *
     * org.python.types.List list = new org.python.types.List();
     * list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(1));
     * list.insert(org.python.types.Int.getInt(1), org.python.types.Int.getInt(2));
     * list.insert(org.python.types.Int.getInt(2), org.python.types.Int.getInt(3));
     */
    public static org.python.types.List py_list_from_java_ints(Integer... ns) {
	java.util.List<org.python.Object> list_of_ints = Arrays.stream(ns).map(n -> org.python.types.Int.getInt(n)).collect(Collectors.toList());

	return new org.python.types.List(list_of_ints);
    }

    private org.python.types.List list;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @org.junit.Before
    public void setUp() {
	this.list = new org.python.types.List();
    }

    @org.junit.After
    public void tearDown() {
	this.list = null;
    }

    @Test
    public void testIsHashable() {
	assertEquals(list.isHashable(), false);
    }

    @Test
    public void test__hash__() {
	exceptionRule.expect(org.python.exceptions.AttributeError.class);
	exceptionRule.expectMessage("'list' object has no attribute '__hash__'");
	list.__hash__();
    }

    @Test
    public void testHashCode() {
	ArrayList<Integer> eqv_java_list = new ArrayList<>();
	assertEquals(list.hashCode(), eqv_java_list.hashCode());

	list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(3));
	assertNotEquals(list.hashCode(), eqv_java_list.hashCode());
	eqv_java_list.add(3);

	assertEquals(list.hashCode(), eqv_java_list.hashCode());

	list.insert(org.python.types.Int.getInt(1), org.python.types.Int.getInt(5));
	assertNotEquals(list.hashCode(), eqv_java_list.hashCode());
	eqv_java_list.add(5);

	assertEquals(list.hashCode(), eqv_java_list.hashCode());

	list.insert(org.python.types.Int.getInt(5), org.python.types.Int.getInt(2));
	assertNotEquals(list.hashCode(), eqv_java_list.hashCode());
	eqv_java_list.add(2);

	assertEquals(list.hashCode(), eqv_java_list.hashCode());

    }

    @Test
    public void testToJava() {
	ArrayList<Integer> eqv_java_list = new ArrayList<>();

	list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(3));
	eqv_java_list.add(3);

	list.insert(org.python.types.Int.getInt(1), org.python.types.Int.getInt(5));
	eqv_java_list.add(5);

	list.insert(org.python.types.Int.getInt(5), org.python.types.Int.getInt(2));
	eqv_java_list.add(2);

	// Create new List consisting of Python representations of the integers
	java.util.List<org.python.Object> eqv_java_list_py_objs = eqv_java_list.stream().map(n -> org.python.types.Int.getInt(n)).collect(Collectors.toList());

	// Compare the two lists
	assertEquals(list.toJava(), eqv_java_list_py_objs);
    }

    @Test
    public void test__iadd__empty_list_left() {
	org.python.types.List iadd_java_list;
	list = py_list_from_java_ints();
	iadd_java_list = py_list_from_java_ints(1, 2, 3);
	list.__iadd__(iadd_java_list);
	org.python.types.List result_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(result_list, list);
    }

    @Test
    public void test__iadd__empty_list_right() {
	org.python.types.List iadd_java_list;
	list = py_list_from_java_ints(1, 2, 3);
	iadd_java_list = py_list_from_java_ints();
	list.__iadd__(iadd_java_list);
	org.python.types.List result_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(result_list, list);
    }

    @Test
    public void test__iadd__list_identical() {
	org.python.types.List iadd_java_list;
	list = py_list_from_java_ints(1, 2, 3);
	iadd_java_list = py_list_from_java_ints(1, 2, 3);
	list.__iadd__(iadd_java_list);
	org.python.types.List result_list = py_list_from_java_ints(1, 2, 3, 1, 2, 3);

	assertEquals(result_list, list);
    }

    @Test
    public void test__iadd__tuple() {
	list = py_list_from_java_ints(1, 2, 3);
	ArrayList<org.python.Object> iadd_java_tuple_elems = new ArrayList<>();
	iadd_java_tuple_elems.add(org.python.types.Int.getInt(1));
	iadd_java_tuple_elems.add(org.python.types.Int.getInt(2));
	org.python.types.Tuple iadd_java_tuple = new org.python.types.Tuple(iadd_java_tuple_elems);

	list.__iadd__(iadd_java_tuple);
	org.python.types.List result_list = py_list_from_java_ints(1, 2, 3, 1, 2);

	assertEquals(result_list, list);
    }

    @Test
    public void test__iadd__emptytuple() {
	list = py_list_from_java_ints(1, 2, 3);
	org.python.types.Tuple iadd_java_tuple = new org.python.types.Tuple();
	list.__iadd__(iadd_java_tuple);
	org.python.types.List result_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(result_list, list);
    }

    @Test
    public void test__iadd__set() {
	list = py_list_from_java_ints(1, 2, 3);
	HashSet<org.python.Object> iadd_java_Set_elems = new HashSet<>();
	iadd_java_Set_elems.add(org.python.types.Int.getInt(1));
	iadd_java_Set_elems.add(org.python.types.Int.getInt(3));
	iadd_java_Set_elems.add(org.python.types.Int.getInt(4));

	org.python.types.Set iadd_java_Set = new org.python.types.Set(iadd_java_Set_elems);
	list.__iadd__(iadd_java_Set);
	org.python.types.List result_list = py_list_from_java_ints(1, 2, 3, 1, 3, 4);

	assertEquals(result_list, list);
    }

    @Test
    public void test__iadd__emptyset() {
	list = py_list_from_java_ints(1, 2, 3);
	org.python.types.Set iadd_java_Set = new org.python.types.Set();
	list.__iadd__(iadd_java_Set);
	org.python.types.List result_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(result_list, list);
    }

    @Test
    public void test__iadd__frozenset() {
	list = py_list_from_java_ints(1, 2, 3);

	HashSet<org.python.Object> iadd_java_set = new HashSet<>();
	iadd_java_set.add(org.python.types.Int.getInt(1));
	iadd_java_set.add(org.python.types.Int.getInt(2));
	iadd_java_set.add(org.python.types.Int.getInt(3));
	org.python.types.FrozenSet frozenset = new org.python.types.FrozenSet(iadd_java_set);

	list.__iadd__(frozenset);
	org.python.types.List result_list = py_list_from_java_ints(1, 2, 3, 1, 2, 3);

	assertEquals(result_list, list);
    }

    @Test
    public void test__iadd__string() {
	org.python.types.List iadd_java_list;
	list = py_list_from_java_ints(1, 2, 3);
	org.python.types.Str string = new org.python.types.Str("hej");
	list.__iadd__(string);

	ArrayList<org.python.Object> result_list_java = new ArrayList<>();
	result_list_java.add(org.python.types.Int.getInt(1));
	result_list_java.add(org.python.types.Int.getInt(2));
	result_list_java.add(org.python.types.Int.getInt(3));
	result_list_java.add(new org.python.types.Str("h"));
	result_list_java.add(new org.python.types.Str("e"));
	result_list_java.add(new org.python.types.Str("j"));
	org.python.types.List result_list = new org.python.types.List(result_list_java);

	assertEquals(result_list, list);
    }

    @Test
    public void test__lt__identical() {
	org.python.types.List lt_java_list;
	list = py_list_from_java_ints(1, 2, 3);
	lt_java_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.FALSE, list.__lt__(lt_java_list));
    }

    @Test
    public void test__lt__empty_lists() {
	org.python.types.List empty_lt_java_list;
	org.python.types.List empty_lt_java_list_other;

	empty_lt_java_list = py_list_from_java_ints();
	empty_lt_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.FALSE, empty_lt_java_list.__lt__(empty_lt_java_list_other));
    }

    @Test
    public void test__lt__left_list_empty() {
	org.python.types.List empty_two_lt_java_list;
	org.python.types.List not_empty_lt_java_list_other;

	empty_two_lt_java_list = py_list_from_java_ints();
	not_empty_lt_java_list_other = py_list_from_java_ints(1, 2);

	assertEquals(org.python.types.Bool.TRUE, empty_two_lt_java_list.__lt__(not_empty_lt_java_list_other));
    }

    @Test
    public void test__lt__right_list_empty() {
	org.python.types.List not_empty_lt_java_list;
	org.python.types.List empty_two_lt_java_list_other;

	not_empty_lt_java_list = py_list_from_java_ints(1, 2);
	empty_two_lt_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.FALSE, not_empty_lt_java_list.__lt__(empty_two_lt_java_list_other));
    }

    @Test
    public void test__lt__different_on_multiple_elements() {
	org.python.types.List less_lt_java_list;
	org.python.types.List greater_lt_java_list_other;

	less_lt_java_list = py_list_from_java_ints(1, 3, 3);
	greater_lt_java_list_other = py_list_from_java_ints(1, 2, 2);

	assertEquals(org.python.types.Bool.FALSE, less_lt_java_list.__lt__(greater_lt_java_list_other));
    }

    @Test
    public void test__lt__swapped() {
	org.python.types.List not_lt_java_list;
	org.python.types.List not_lt_java_list_other;

	not_lt_java_list = py_list_from_java_ints(1, 2, 3);
	not_lt_java_list_other = py_list_from_java_ints(1, 3, 2);

	assertEquals(org.python.types.Bool.TRUE, not_lt_java_list.__lt__(not_lt_java_list_other));
    }

    @Test
    public void test__lt__different_on_last_index() {
	org.python.types.List not_lt_length_java_list_true;
	org.python.types.List not_lt_length_java_list_other_true;

	not_lt_length_java_list_true = py_list_from_java_ints(1, 2, 2);
	not_lt_length_java_list_other_true = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.TRUE, not_lt_length_java_list_true.__lt__(not_lt_length_java_list_other_true));
    }

    @Test
    public void test__lt__different_lengths_different_elements() {
	org.python.types.List not_lt_length_invert_java_list_true;
	org.python.types.List not_lt_length_invert_java_list_other_true;

	not_lt_length_invert_java_list_true = py_list_from_java_ints(1, 2, 2, 4);
	not_lt_length_invert_java_list_other_true = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.TRUE, not_lt_length_invert_java_list_true.__lt__(not_lt_length_invert_java_list_other_true));
    }

    @Test
    public void test__lt__right_list_longer() {

	org.python.types.List not_lt_length_java_list;
	org.python.types.List not_lt_length_java_list_other;

	not_lt_length_java_list = py_list_from_java_ints(1, 2, 3);
	not_lt_length_java_list_other = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.TRUE, not_lt_length_java_list.__lt__(not_lt_length_java_list_other));
    }

    @Test
    public void test__lt__left_list_longer() {
	org.python.types.List not_lt_length_invert_java_list;
	org.python.types.List not_lt_length_invert_java_list_other;

	not_lt_length_invert_java_list = py_list_from_java_ints(1, 2, 3, 4);
	not_lt_length_invert_java_list_other = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.FALSE, not_lt_length_invert_java_list.__lt__(not_lt_length_invert_java_list_other));
    }

    @Test
    public void test__le__empty_lists() {
	org.python.types.List empty_le_java_list;
	org.python.types.List empty_le_java_list_other;

	empty_le_java_list = py_list_from_java_ints();
	empty_le_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.TRUE, empty_le_java_list.__le__(empty_le_java_list_other));
    }

    @Test
    public void test__le__left_list_empty() {
	org.python.types.List empty_two_le_java_list;
	org.python.types.List not_empty_le_java_list_other;

	empty_two_le_java_list = py_list_from_java_ints();
	not_empty_le_java_list_other = py_list_from_java_ints(1, 2);

	assertEquals(org.python.types.Bool.TRUE, empty_two_le_java_list.__le__(not_empty_le_java_list_other));
    }

    @Test
    public void test__le__right_list_empty() {
	org.python.types.List not_empty_le_java_list;
	org.python.types.List empty_two_le_java_list_other;

	not_empty_le_java_list = py_list_from_java_ints(1, 2);
	empty_two_le_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.FALSE, not_empty_le_java_list.__le__(empty_two_le_java_list_other));
    }

    @Test
    public void test__le__different_on_multiple_elements() {
	org.python.types.List less_le_java_list;
	org.python.types.List greater_le_java_list_other;

	less_le_java_list = py_list_from_java_ints(1, 3, 3);
	greater_le_java_list_other = py_list_from_java_ints(1, 2, 2);

	assertEquals(org.python.types.Bool.FALSE, less_le_java_list.__le__(greater_le_java_list_other));
    }

    @Test
    public void test__le__swapped() {
	org.python.types.List not_le_java_list;
	org.python.types.List not_le_java_list_other;

	not_le_java_list = py_list_from_java_ints(1, 2, 3);
	not_le_java_list_other = py_list_from_java_ints(1, 3, 2);

	assertEquals(org.python.types.Bool.TRUE, not_le_java_list.__le__(not_le_java_list_other));
    }

    @Test
    public void test__le__different_on_last_index() {
	org.python.types.List not_le_length_java_list_true;
	org.python.types.List not_le_length_java_list_other_true;

	not_le_length_java_list_true = py_list_from_java_ints(1, 2, 2);
	not_le_length_java_list_other_true = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.TRUE, not_le_length_java_list_true.__le__(not_le_length_java_list_other_true));
    }

    @Test
    public void test__le__different_lengths_different_lists() {
	org.python.types.List not_le_length_invert_java_list_true;
	org.python.types.List not_le_length_invert_java_list_other_true;

	not_le_length_invert_java_list_true = py_list_from_java_ints(1, 2, 2, 4);
	not_le_length_invert_java_list_other_true = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.TRUE, not_le_length_invert_java_list_true.__le__(not_le_length_invert_java_list_other_true));
    }

    @Test
    public void test__le__right_list_longer() {

	org.python.types.List not_le_length_java_list;
	org.python.types.List not_le_length_java_list_other;

	not_le_length_java_list = py_list_from_java_ints(1, 2, 3);
	not_le_length_java_list_other = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.TRUE, not_le_length_java_list.__le__(not_le_length_java_list_other));
    }

    @Test
    public void test__le__left_list_longer() {
	org.python.types.List not_le_length_invert_java_list;
	org.python.types.List not_le_length_invert_java_list_other;

	not_le_length_invert_java_list = py_list_from_java_ints(1, 2, 3, 4);
	not_le_length_invert_java_list_other = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.FALSE, not_le_length_invert_java_list.__le__(not_le_length_invert_java_list_other));
    }

    @Test
    public void test__le__identical() {
	org.python.types.List le_java_list;
	list = py_list_from_java_ints(1, 2, 3);
	le_java_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.TRUE, list.__le__(le_java_list));
    }

    @Test
    public void test__eq__empty_lists() {
	org.python.types.List empty_eq_java_list;
	org.python.types.List empty_eq_java_list_other;

	empty_eq_java_list = py_list_from_java_ints();
	empty_eq_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.TRUE, empty_eq_java_list.__eq__(empty_eq_java_list_other));
    }

    @Test
    public void test__eq__left_list_empty() {
	org.python.types.List empty_two_eq_java_list;
	org.python.types.List not_empty_eq_java_list_other;

	empty_two_eq_java_list = py_list_from_java_ints();
	not_empty_eq_java_list_other = py_list_from_java_ints(1, 2);

	assertEquals(org.python.types.Bool.FALSE, empty_two_eq_java_list.__eq__(not_empty_eq_java_list_other));
    }

    @Test
    public void test__eq__right_list_empty() {
	org.python.types.List not_empty_eq_java_list;
	org.python.types.List empty_two_eq_java_list_other;

	not_empty_eq_java_list = py_list_from_java_ints(1, 2);
	empty_two_eq_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.FALSE, not_empty_eq_java_list.__eq__(empty_two_eq_java_list_other));
    }

    @Test
    public void test__eq__different_on_multiple_elements() {
	org.python.types.List eqss_eq_java_list;
	org.python.types.List greater_eq_java_list_other;

	eqss_eq_java_list = py_list_from_java_ints(1, 3, 3);
	greater_eq_java_list_other = py_list_from_java_ints(1, 2, 2);

	assertEquals(org.python.types.Bool.FALSE, eqss_eq_java_list.__eq__(greater_eq_java_list_other));
    }

    @Test
    public void test__eq__swapped() {
	org.python.types.List not_eq_java_list;
	org.python.types.List not_eq_java_list_other;

	not_eq_java_list = py_list_from_java_ints(1, 2, 3);
	not_eq_java_list_other = py_list_from_java_ints(1, 3, 2);

	assertEquals(org.python.types.Bool.FALSE, not_eq_java_list.__eq__(not_eq_java_list_other));
    }

    @Test
    public void test__eq__different_on_last_index() {
	org.python.types.List not_eq_eqngth_java_list_true;
	org.python.types.List not_eq_eqngth_java_list_other_true;

	not_eq_eqngth_java_list_true = py_list_from_java_ints(1, 2, 2);
	not_eq_eqngth_java_list_other_true = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.FALSE, not_eq_eqngth_java_list_true.__eq__(not_eq_eqngth_java_list_other_true));
    }

    @Test
    public void test__eq__different_lengths_different_lists() {
	org.python.types.List not_eq_eqngth_invert_java_list_true;
	org.python.types.List not_eq_eqngth_invert_java_list_other_true;

	not_eq_eqngth_invert_java_list_true = py_list_from_java_ints(1, 2, 2, 4);
	not_eq_eqngth_invert_java_list_other_true = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.FALSE, not_eq_eqngth_invert_java_list_true.__eq__(not_eq_eqngth_invert_java_list_other_true));
    }

    @Test
    public void test__eq__right_list_longer() {

	org.python.types.List not_eq_eqngth_java_list;
	org.python.types.List not_eq_eqngth_java_list_other;

	not_eq_eqngth_java_list = py_list_from_java_ints(1, 2, 3);
	not_eq_eqngth_java_list_other = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.FALSE, not_eq_eqngth_java_list.__eq__(not_eq_eqngth_java_list_other));
    }

    @Test
    public void test__eq__left_list_longer() {
	org.python.types.List not_eq_eqngth_invert_java_list;
	org.python.types.List not_eq_eqngth_invert_java_list_other;

	not_eq_eqngth_invert_java_list = py_list_from_java_ints(1, 2, 3, 4);
	not_eq_eqngth_invert_java_list_other = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.FALSE, not_eq_eqngth_invert_java_list.__eq__(not_eq_eqngth_invert_java_list_other));
    }

    @Test
    public void test__eq__identical() {
	org.python.types.List eq_java_list;
	list = py_list_from_java_ints(1, 2, 3);
	eq_java_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.TRUE, list.__eq__(eq_java_list));
    }

    @Test
    public void test__ge__empty_lists() {
	org.python.types.List empty_ge_java_list;
	org.python.types.List empty_ge_java_list_other;

	empty_ge_java_list = py_list_from_java_ints();
	empty_ge_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.TRUE, empty_ge_java_list.__ge__(empty_ge_java_list_other));
    }

    @Test
    public void test__ge__left_list_empty() {
	org.python.types.List empty_two_ge_java_list;
	org.python.types.List not_empty_ge_java_list_other;

	empty_two_ge_java_list = py_list_from_java_ints();
	not_empty_ge_java_list_other = py_list_from_java_ints(1, 2);

	assertEquals(org.python.types.Bool.FALSE, empty_two_ge_java_list.__ge__(not_empty_ge_java_list_other));
    }

    @Test
    public void test__ge__right_list_empty() {
	org.python.types.List not_empty_ge_java_list;
	org.python.types.List empty_two_ge_java_list_other;

	not_empty_ge_java_list = py_list_from_java_ints(1, 2);
	empty_two_ge_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.TRUE, not_empty_ge_java_list.__ge__(empty_two_ge_java_list_other));
    }

    @Test
    public void test__ge__different_on_multiple_elements() {
	org.python.types.List gess_ge_java_list;
	org.python.types.List greater_ge_java_list_other;

	gess_ge_java_list = py_list_from_java_ints(1, 3, 3);
	greater_ge_java_list_other = py_list_from_java_ints(1, 2, 2);

	assertEquals(org.python.types.Bool.TRUE, gess_ge_java_list.__ge__(greater_ge_java_list_other));
    }

    @Test
    public void test__ge__swapped() {
	org.python.types.List not_ge_java_list;
	org.python.types.List not_ge_java_list_other;

	not_ge_java_list = py_list_from_java_ints(1, 2, 3);
	not_ge_java_list_other = py_list_from_java_ints(1, 3, 2);

	assertEquals(org.python.types.Bool.FALSE, not_ge_java_list.__ge__(not_ge_java_list_other));
    }

    @Test
    public void test__ge__different_on_last_index() {
	org.python.types.List not_ge_gength_java_list_true;
	org.python.types.List not_ge_gength_java_list_other_true;

	not_ge_gength_java_list_true = py_list_from_java_ints(1, 2, 2);
	not_ge_gength_java_list_other_true = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.FALSE, not_ge_gength_java_list_true.__ge__(not_ge_gength_java_list_other_true));
    }

    @Test
    public void test__ge__different_lengths_different_lists() {
	org.python.types.List not_ge_gength_invert_java_list_true;
	org.python.types.List not_ge_gength_invert_java_list_other_true;

	not_ge_gength_invert_java_list_true = py_list_from_java_ints(1, 2, 2, 4);
	not_ge_gength_invert_java_list_other_true = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.FALSE, not_ge_gength_invert_java_list_true.__ge__(not_ge_gength_invert_java_list_other_true));
    }

    @Test
    public void test__ge__right_list_longer() {

	org.python.types.List not_ge_gength_java_list;
	org.python.types.List not_ge_gength_java_list_other;

	not_ge_gength_java_list = py_list_from_java_ints(1, 2, 3);
	not_ge_gength_java_list_other = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.FALSE, not_ge_gength_java_list.__ge__(not_ge_gength_java_list_other));
    }

    @Test
    public void test__ge__left_list_longer() {
	org.python.types.List not_ge_gength_invert_java_list;
	org.python.types.List not_ge_gength_invert_java_list_other;

	not_ge_gength_invert_java_list = py_list_from_java_ints(1, 2, 3, 4);
	not_ge_gength_invert_java_list_other = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.TRUE, not_ge_gength_invert_java_list.__ge__(not_ge_gength_invert_java_list_other));
    }

    @Test
    public void test__ge__identical() {
	org.python.types.List ge_java_list;
	list = py_list_from_java_ints(1, 2, 3);
	ge_java_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.TRUE, list.__ge__(ge_java_list));
    }

    @Test
    public void test__gt__empty_lists() {
	org.python.types.List empty_gt_java_list;
	org.python.types.List empty_gt_java_list_other;

	empty_gt_java_list = py_list_from_java_ints();
	empty_gt_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.FALSE, empty_gt_java_list.__gt__(empty_gt_java_list_other));
    }

    @Test
    public void test__gt__left_list_empty() {
	org.python.types.List empty_two_gt_java_list;
	org.python.types.List not_empty_gt_java_list_other;

	empty_two_gt_java_list = py_list_from_java_ints();
	not_empty_gt_java_list_other = py_list_from_java_ints(1, 2);

	assertEquals(org.python.types.Bool.FALSE, empty_two_gt_java_list.__gt__(not_empty_gt_java_list_other));
    }

    @Test
    public void test__gt__right_list_empty() {
	org.python.types.List not_empty_gt_java_list;
	org.python.types.List empty_two_gt_java_list_other;

	not_empty_gt_java_list = py_list_from_java_ints(1, 2);
	empty_two_gt_java_list_other = py_list_from_java_ints();

	assertEquals(org.python.types.Bool.TRUE, not_empty_gt_java_list.__gt__(empty_two_gt_java_list_other));
    }

    @Test
    public void test__gt__different_on_multiple_elements() {
	org.python.types.List gtss_gt_java_list;
	org.python.types.List greater_gt_java_list_other;

	gtss_gt_java_list = py_list_from_java_ints(1, 3, 3);
	greater_gt_java_list_other = py_list_from_java_ints(1, 2, 2);

	assertEquals(org.python.types.Bool.TRUE, gtss_gt_java_list.__gt__(greater_gt_java_list_other));
    }

    @Test
    public void test__gt__swapped_elements() {
	org.python.types.List not_gt_java_list;
	org.python.types.List not_gt_java_list_other;

	not_gt_java_list = py_list_from_java_ints(1, 2, 3);
	not_gt_java_list_other = py_list_from_java_ints(1, 3, 2);

	assertEquals(org.python.types.Bool.FALSE, not_gt_java_list.__gt__(not_gt_java_list_other));
    }

    @Test
    public void test__gt__different_on_last_index() {
	org.python.types.List not_gt_gtngth_java_list_true;
	org.python.types.List not_gt_gtngth_java_list_other_true;

	not_gt_gtngth_java_list_true = py_list_from_java_ints(1, 2, 2);
	not_gt_gtngth_java_list_other_true = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.FALSE, not_gt_gtngth_java_list_true.__gt__(not_gt_gtngth_java_list_other_true));
    }

    @Test
    public void test__gt__different_lengths_different_lists() {
	org.python.types.List not_gt_gtngth_invert_java_list_true;
	org.python.types.List not_gt_gtngth_invert_java_list_other_true;

	not_gt_gtngth_invert_java_list_true = py_list_from_java_ints(1, 2, 2, 4);
	not_gt_gtngth_invert_java_list_other_true = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.FALSE, not_gt_gtngth_invert_java_list_true.__gt__(not_gt_gtngth_invert_java_list_other_true));
    }

    @Test
    public void test__gt__right_longer_otherwise_identical() {

	org.python.types.List not_gt_gtngth_java_list;
	org.python.types.List not_gt_gtngth_java_list_other;

	not_gt_gtngth_java_list = py_list_from_java_ints(1, 2, 3);
	not_gt_gtngth_java_list_other = py_list_from_java_ints(1, 2, 3, 4);

	assertEquals(org.python.types.Bool.FALSE, not_gt_gtngth_java_list.__gt__(not_gt_gtngth_java_list_other));
    }

    @Test
    public void test__gt__left_longer_otherwise_identical() {
	org.python.types.List not_gt_gtngth_invert_java_list;
	org.python.types.List not_gt_gtngth_invert_java_list_other;

	not_gt_gtngth_invert_java_list = py_list_from_java_ints(1, 2, 3, 4);
	not_gt_gtngth_invert_java_list_other = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.TRUE, not_gt_gtngth_invert_java_list.__gt__(not_gt_gtngth_invert_java_list_other));
    }

    @Test
    public void test__gt__identical_lists() {
	org.python.types.List gt_java_list;
	list = py_list_from_java_ints(1, 2, 3);
	gt_java_list = py_list_from_java_ints(1, 2, 3);

	assertEquals(org.python.types.Bool.FALSE, list.__gt__(gt_java_list));
    }

    @Test
    public void test_value() {
	ArrayList<Integer> eqv_java_list = new ArrayList<>();

	list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(3));
	eqv_java_list.add(3);

	list.insert(org.python.types.Int.getInt(1), org.python.types.Int.getInt(5));
	eqv_java_list.add(5);

	list.insert(org.python.types.Int.getInt(5), org.python.types.Int.getInt(2));
	eqv_java_list.add(2);

	// Create new List consisting of Python representations of the integers
	java.util.List<org.python.Object> eqv_java_list_py_objs = eqv_java_list.stream().map(n -> org.python.types.Int.getInt(n)).collect(Collectors.toList());

	// Compare the two lists
	assertEquals(list.value, eqv_java_list_py_objs);
    }

    @Test
    public void test__bool__() {
	assertEquals(list.__bool__(), org.python.types.Bool.FALSE);

	list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(3));
	assertEquals(list.__bool__(), org.python.types.Bool.TRUE);

	list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(5));
	assertEquals(list.__bool__(), org.python.types.Bool.TRUE);

	list.pop(null);
	assertEquals(list.__bool__(), org.python.types.Bool.TRUE);

	list.pop(null);
	assertEquals(list.__bool__(), org.python.types.Bool.FALSE);
    }

    @Test
    public void test__repr__() {
	assertEquals(list.__repr__(), new org.python.types.Str("[]"));

	list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(5));
	assertEquals(list.__repr__(), new org.python.types.Str("[5]"));

	list.insert(org.python.types.Int.getInt(1), new org.python.types.Str("foo"));
	assertEquals(list.__repr__(), new org.python.types.Str("[5, 'foo']"));

	list.insert(org.python.types.Int.getInt(2), org.python.types.Int.getInt(2));
	assertEquals(list.__repr__(), new org.python.types.Str("[5, 'foo', 2]"));

	list.insert(org.python.types.Int.getInt(3), org.python.types.Int.getInt(42));
	list.insert(org.python.types.Int.getInt(4), org.python.types.Int.getInt(27));
	list.insert(org.python.types.Int.getInt(5), org.python.types.Int.getInt(24));
	assertEquals(list.__repr__(), new org.python.types.Str("[5, 'foo', 2, 42, 27, 24]"));
    }

    @Test
    public void test__format__() {
	exceptionRule.expect(org.python.exceptions.NotImplementedError.class);
	exceptionRule.expectMessage("list.__format__() has not been implemented.");
	list.__format__();
    }

    @Test
    public void testListEmptyConstructor() {
	assertEquals((new org.python.types.List()).value, new java.util.ArrayList<org.python.Object>());
    }

    @Test
    public void testListListConstructor() {
	ArrayList<Integer> java_list = new ArrayList<>();

	java_list.add(3);
	java_list.add(5);
	java_list.add(2);
	java_list.add(4);

	list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(3));
	list.insert(org.python.types.Int.getInt(1), org.python.types.Int.getInt(5));
	list.insert(org.python.types.Int.getInt(2), org.python.types.Int.getInt(2));
	list.insert(org.python.types.Int.getInt(3), org.python.types.Int.getInt(4));

	// Create new List consisting of Python representations of the integers
	java.util.List<org.python.Object> java_list_py_objs = java_list.stream().map(n -> org.python.types.Int.getInt(n)).collect(Collectors.toList());

	assertEquals((new org.python.types.List(java_list_py_objs)).toJava(), java_list_py_objs);
	assertEquals((new org.python.types.List(java_list_py_objs)), list);
    }

    @Test
    public void testListConstructorMultipleArguments() {
	// Check with two arguments where first argument is non-null
	if (org.Python.VERSION < 0x03070000) {
	    exceptionRule.expect(org.python.exceptions.TypeError.class);
	    exceptionRule.expectMessage("list() takes at most 1 argument (2 given)");
	} else {
	    exceptionRule.expect(org.python.exceptions.TypeError.class);
	    exceptionRule.expectMessage("list expected takes at most 1 arguments, got 2");
	}

	org.python.Object foo = new org.python.types.List(new org.python.Object[] { new org.python.types.List(), null }, null);
    }

    @Test
    public void testListConstructorOneArgument() {
	// Check with one argument, null
	assertEquals((new org.python.types.List(new org.python.Object[] { null }, null)).value, new java.util.ArrayList<org.python.Object>());
    }

    @Test
    public void testListConstructorTwoNullArguments() {
	// Check with two arguments where first is null
	assertEquals((new org.python.types.List(new org.python.Object[] { null, null }, null)).value, new java.util.ArrayList<org.python.Object>());
    }

    @Test
    public void testListConstructorListArgument() {
	// Check with a List argument
	org.python.types.List list_123 = py_list_from_java_ints(1, 2, 3);
	ArrayList<Integer> list_java_123 = new ArrayList<>();
	list_java_123.add(1);
	list_java_123.add(2);
	list_java_123.add(3);

	java.util.List<org.python.Object> list_java_123_py_objs = list_java_123.stream().map(n -> org.python.types.Int.getInt(n)).collect(Collectors.toList());

	assertEquals((new org.python.types.List(new org.python.Object[] { list_123 }, null)).value, list_java_123_py_objs);
    }

    @Test
    public void testListConstructorTupleArgument() {
	// Check with a Tuple argument
	ArrayList<Integer> list_java_123 = new ArrayList<>();
	list_java_123.add(1);
	list_java_123.add(2);
	list_java_123.add(3);

	java.util.List<org.python.Object> list_java_123_py_objs = list_java_123.stream().map(n -> org.python.types.Int.getInt(n)).collect(Collectors.toList());

	org.python.types.Tuple tuple_123 = new org.python.types.Tuple(list_java_123_py_objs);

	assertEquals((new org.python.types.List(new org.python.Object[] { tuple_123 }, null)).value, list_java_123_py_objs);
    }

    @Test
    public void testListConstructorIteratorArgument() {
	// Check with an Iterator argument
	org.python.types.List list_123 = py_list_from_java_ints(1, 2, 3);
	ArrayList<Integer> list_java_123 = new ArrayList<>();
	list_java_123.add(1);
	list_java_123.add(2);
	list_java_123.add(3);

	java.util.List<org.python.Object> list_java_123_py_objs = list_java_123.stream().map(n -> org.python.types.Int.getInt(n)).collect(Collectors.toList());

	assertEquals((new org.python.types.List(new org.python.Object[] { list_123.__iter__() }, null)).value, list_java_123_py_objs);
    }

    @Test
    public void testListConstructorSetArgument() {
	// Check with a Set argument
	HashSet<org.python.Object> set_java_123 = new HashSet<>();
	set_java_123.add(org.python.types.Int.getInt(1));
	set_java_123.add(org.python.types.Int.getInt(2));
	set_java_123.add(org.python.types.Int.getInt(3));

	org.python.types.Set set_123 = new org.python.types.Set(set_java_123);

	ArrayList<Integer> list_java_123 = new ArrayList<>();
	list_java_123.add(1);
	list_java_123.add(2);
	list_java_123.add(3);

	java.util.List<org.python.Object> list_java_123_py_objs = list_java_123.stream().map(n -> org.python.types.Int.getInt(n)).collect(Collectors.toList());

	assertEquals((new org.python.types.List(new org.python.Object[] { set_123 }, null)).value, list_java_123_py_objs);
    }

    @Test
    public void testList__rmul__() {
	org.python.types.List list;
	list = py_list_from_java_ints(1);
	exceptionRule.expect(org.python.exceptions.NotImplementedError.class);
	exceptionRule.expectMessage("list.__rmul__() has not been implemented.");
	list.__rmul__(list);
    }

    @Test
    public void testList_append() {
	org.python.types.List list1 = py_list_from_java_ints(1, 2);
	org.python.types.List list2 = py_list_from_java_ints(1);
	list2.append(org.python.types.Int.getInt(2));
	assertEquals(list1, list2);
    }

    @Test
    public void testList_clear() {
	org.python.types.List java_list = py_list_from_java_ints(1, 2, 3);
	java_list.clear();
	org.python.Object empty_java_list = new org.python.types.List();
	assertEquals(list, empty_java_list);
    }

    @Test
    public void testList_copy() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(1, 2, 3);

	org.python.Object copied_java_list = java_list.copy();
	assertEquals(java_list, copied_java_list);
    }

    @Test
    // __imul__ replicates self*=value
    public void testList__imul__() {
	org.python.types.List list = py_list_from_java_ints(1);
	org.python.Object result = list.__imul__(org.python.types.Int.getInt(2));
	assertEquals(result, py_list_from_java_ints(1, 1));
    }

    @Test
    // Negative multiplicator should empty list
    public void testList__imul__negative() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	org.python.Object java_imul_result_list = java_list.__imul__(org.python.types.Int.getInt(-2));
	assertEquals(java_imul_result_list, new org.python.types.List());
    }

    @Test
    // Zero multiplicator should empty list
    public void testList__imul__zero() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	org.python.Object java_imul_result_list = java_list.__imul__(org.python.types.Int.getInt(0));
	assertEquals(java_imul_result_list, new org.python.types.List());
    }

    @Test
    // Float should produce TypeError
    public void testList__imul__float() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	org.python.types.Float the_float = new org.python.types.Float(0.1);
	exceptionRule.expect(org.python.exceptions.TypeError.class);
	exceptionRule.expectMessage("can't multiply sequence by non-int of type 'float'");
	java_list.__imul__(the_float);
    }

    @Test
    // Boolean True should return same object
    public void testList__imul__boolean_true() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	org.python.Object result = java_list.__imul__(org.python.types.Bool.getBool(true));
	assertEquals(java_list, result);
    }

    @Test
    // Boolean True should return same object
    public void testList__imul__boolean_false() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	org.python.Object result = java_list.__imul__(org.python.types.Bool.getBool(false));
	assertEquals(py_list_from_java_ints(), result);
    }

    @Test
    public void testList_count() {
	org.python.types.List java_list = py_list_from_java_ints(1, 1, 1);
	org.python.Object number_occurrences = java_list.count(org.python.types.Int.getInt(1));
	assertEquals(number_occurrences, org.python.types.Int.getInt(3));
    }

    @Test
    public void testList_count_zero() {
	org.python.types.List java_list = py_list_from_java_ints(1, 1, 1);
	org.python.Object number_occurrences = java_list.count(org.python.types.Int.getInt(2));
	assertEquals(number_occurrences, org.python.types.Int.getInt(0));
    }

    @Test
    public void testList_extend() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	java_list.extend(java_list);
	assertEquals(java_list, py_list_from_java_ints(1, 1));
    }

    @Test
    public void testList_extend_float() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	org.python.types.Float a_float = new org.python.types.Float(0.1);
	exceptionRule.expect(org.python.exceptions.TypeError.class);
	exceptionRule.expectMessage("'float' object is not iterable");
	java_list.extend(a_float);
    }

    @Test
    public void testList_extend_string() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	org.python.types.Str str = new org.python.types.Str("Hi");
	java_list.extend(str);
	org.python.types.List java_expected_list = new org.python.types.List();
	java_expected_list.append(org.python.types.Int.getInt(1));
	java_expected_list.append(new org.python.types.Str("H"));
	java_expected_list.append(new org.python.types.Str("i"));
	assertEquals(java_list, java_expected_list);
    }

    @Test
    public void testList_insert() {
	org.python.types.List java_list = py_list_from_java_ints();
	java_list.insert(org.python.types.Int.getInt(0), org.python.types.Int.getInt(1)); // (index, value);
	assertEquals(java_list, py_list_from_java_ints(1));
    }

    @Test
    public void testList_insert_index_greater_than_length() {
	org.python.types.List java_list = py_list_from_java_ints();
	java_list.insert(org.python.types.Int.getInt(1), org.python.types.Int.getInt(1));
	assertEquals(java_list, py_list_from_java_ints(1));
    }

    @Test
    public void testList_insert_float_index() {
	org.python.types.List java_list = py_list_from_java_ints();
	org.python.types.Float a_float_index = new org.python.types.Float(0.1);
	exceptionRule.expect(org.python.exceptions.TypeError.class);
	exceptionRule.expectMessage("'float' object cannot be interpreted as an integer"); // python 3.5
	java_list.insert(a_float_index, org.python.types.Int.getInt(1));
    }

    @Test
    public void testList_pop() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	java_list.pop(null);// requires at least null
	assertEquals(java_list, py_list_from_java_ints());
    }

    @Test
    public void testList_pop_check_returned_value() {
	org.python.types.List java_list = py_list_from_java_ints(1, 2, 3);
	org.python.Object returned_value = java_list.pop(null);// requires at least null
	assertEquals(returned_value, org.python.types.Int.getInt(3));
    }

    @Test
    public void testList_pop_check_returned_value_with_index() {
	org.python.types.List java_list = py_list_from_java_ints(1, 2, 3);
	org.python.Object returned_value = java_list.pop(org.python.types.Int.getInt(1));// requires at least null
	assertEquals(returned_value, org.python.types.Int.getInt(2));
    }

    @Test
    public void testList_pop_with_index() {
	org.python.types.List java_list = py_list_from_java_ints(1, 2, 3);
	java_list.pop(org.python.types.Int.getInt(1));
	assertEquals(java_list, py_list_from_java_ints(1, 3));
    }

    @Test
    public void testList_pop_from_empty() {
	org.python.types.List java_list = py_list_from_java_ints();
	exceptionRule.expect(org.python.exceptions.IndexError.class);
	exceptionRule.expectMessage("pop from empty list"); // python 3.5
	java_list.pop(null);
    }

    @Test
    public void testList_pop_with_index_out_of_range() {
	org.python.types.List java_list = py_list_from_java_ints(1, 2);
	exceptionRule.expect(org.python.exceptions.IndexError.class);
	exceptionRule.expectMessage("pop index out of range"); // python 3.5
	java_list.pop(org.python.types.Int.getInt(2));
    }

    @Test
    public void testList_remove() {
	org.python.types.List java_list = py_list_from_java_ints(1);
	java_list.remove(org.python.types.Int.getInt(1));
	assertEquals(java_list, py_list_from_java_ints());
    }

    @Test
    public void testList_remove_with_not_found_value() {
	org.python.types.List java_list = py_list_from_java_ints(3, 4);
	exceptionRule.expect(org.python.exceptions.ValueError.class);
	exceptionRule.expectMessage("list.remove(x): x not in list");
	java_list.remove(org.python.types.Int.getInt(1));
    }

    @Test
    public void testList_reverse() {
	org.python.types.List java_list = py_list_from_java_ints(1, 2, 3, 4);
	java_list.reverse();
	assertEquals(java_list, py_list_from_java_ints(4, 3, 2, 1));
    }

    @Test
    public void test__setattr_null() {
	org.python.types.List java_list;
	org.python.Object random_value;
	java_list = py_list_from_java_ints();
	random_value = null;
	String java_string = "ex";
	boolean bool_value = java_list.__setattr_null(java_string, random_value);
	assertFalse(bool_value);
    }

    @Test
    public void test__len__True() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(1, 2, 3);
	assertEquals(java_list.__len__(), org.python.types.Int.getInt(3));
	java_list = py_list_from_java_ints(1);
	assertEquals(java_list.__len__(), org.python.types.Int.getInt(1));
    }

    @Test
    public void test__len__Failing() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(1, 2);
	assertNotEquals(java_list.__len__(), org.python.types.Int.getInt(3));
    }

    @Test
    public void test__len__ListEmpty() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints();
	assertEquals(java_list.__len__(), org.python.types.Int.getInt(0));
    }

    @Test
    public void test__getitem__getFirstItemInList() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(10, 20);
	assertEquals(java_list.__getitem__(org.python.types.Int.getInt(0)), org.python.types.Int.getInt(10));
    }

    @Test
    public void test__getitem__getLastItemInList() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
	assertEquals(java_list.__getitem__(org.python.types.Int.getInt(9)), org.python.types.Int.getInt(100));
    }

    @Test
    public void test__getitem__NegativeIndex() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(10, 20);
	assertEquals(java_list.__getitem__(org.python.types.Int.getInt(-1)), org.python.types.Int.getInt(20));
    }

    @Test
    public void test__getitem__OutOfRange() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(10, 20);
	exceptionRule.expect(org.python.exceptions.IndexError.class);
	exceptionRule.expectMessage("list index out of range");
	java_list.__getitem__(org.python.types.Int.getInt(3));
    }

    @Test
    public void test__getitem__NegativeOutOfRange() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(10, 20);
	exceptionRule.expect(org.python.exceptions.IndexError.class);
	exceptionRule.expectMessage("list index out of range");
	java_list.__getitem__(org.python.types.Int.getInt(-3));
    }

    @Test
    public void test__setitem__ChangeExistingItem() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
	java_list.__setitem__((org.python.types.Int.getInt(3)), org.python.types.Int.getInt(900));
	assertEquals(java_list.__getitem__(org.python.types.Int.getInt(3)), org.python.types.Int.getInt(900));
    }

    @Test
    public void test__setitem__ListEmptyError() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints();
	exceptionRule.expect(org.python.exceptions.IndexError.class);
	exceptionRule.expectMessage("list assignment index out of range");
	java_list.__setitem__((org.python.types.Int.getInt(0)), org.python.types.Int.getInt(900));
    }

    @Test
    public void test__setitem__NegativeListEmptyError() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints();
	exceptionRule.expect(org.python.exceptions.IndexError.class);
	exceptionRule.expectMessage("list assignment index out of range");
	java_list.__setitem__((org.python.types.Int.getInt(-2)), org.python.types.Int.getInt(900));
    }

    @Test
    public void test__delitem__RemoveAnItem() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
	java_list.__delitem__((org.python.types.Int.getInt(3)));
	assertEquals(java_list.__getitem__(org.python.types.Int.getInt(3)), org.python.types.Int.getInt(50));
    }

    @Test
    public void test__delitem__EmptyTheList() {
	org.python.types.List java_list;
	java_list = py_list_from_java_ints(10, 20, 30);
	java_list.__delitem__((org.python.types.Int.getInt(2)));
	java_list.__delitem__((org.python.types.Int.getInt(1)));
	java_list.__delitem__((org.python.types.Int.getInt(0)));
	assertEquals(java_list.__len__(), org.python.types.Int.getInt(0));
    }

    @Test
    public void test__iter__() {
	org.python.types.List java_list;
	org.python.types.Iterator iter_list;
	java_list = py_list_from_java_ints(10, 20, 30);
	org.python.Object ex;
	iter_list = (org.python.types.Iterator) java_list.__iter__();
	assertEquals(iter_list.__next__(), org.python.types.Int.getInt(10));
	assertEquals(iter_list.__next__(), org.python.types.Int.getInt(20));
	assertEquals(iter_list.__next__(), org.python.types.Int.getInt(30));
    }

    public void test__reversed__() {
	org.python.types.List java_list_test = new org.python.types.List();
	org.python.types.List java_list = py_list_from_java_ints(1, 2, 3);
	org.python.types.Iterator i = (org.python.types.Iterator) java_list.__reversed__();

	assertEquals(i.__next__(), org.python.types.Int.getInt(3));
	assertEquals(i.__next__(), org.python.types.Int.getInt(2));
	assertEquals(i.__next__(), org.python.types.Int.getInt(1));
	assertEquals(i.__next__(), org.python.types.NoneType.NONE);
    }

    @Test
    public void test__contains__ints() {
	org.python.types.List java_list = py_list_from_java_ints(1, 2, 3);
	assertEquals(java_list.__contains__(org.python.types.Int.getInt(1)), org.python.types.Bool.TRUE);
	assertEquals(java_list.__contains__(org.python.types.Int.getInt(2)), org.python.types.Bool.TRUE);
	assertEquals(java_list.__contains__(org.python.types.Int.getInt(3)), org.python.types.Bool.TRUE);
	assertEquals(java_list.__contains__(org.python.types.Int.getInt(4)), org.python.types.Bool.FALSE);
    }

    @Test
    public void test__add__() {
	org.python.types.List java_list = py_list_from_java_ints(1, 2, 3, 4, 5);
	org.python.types.List java_list_a;
	org.python.types.List java_list_b;
	org.python.types.List java_list_res;
	java_list_a = py_list_from_java_ints(1, 2, 3);
	java_list_b = py_list_from_java_ints(4, 5);
	java_list_res = (org.python.types.List) java_list_a.__add__(java_list_b);
	assertEquals(java_list_res, java_list);
    }

    @Test
    public void test__mul__() {
	int imul = 2;
	org.python.types.Int mul = org.python.types.Int.getInt(imul);
	org.python.types.List java_list = new org.python.types.List();
	org.python.types.List java_list_a;
	org.python.types.List java_list_res;
	java_list_a = py_list_from_java_ints(1, 2, 3);
	for (int i = 0; i < imul; i++) {
	    java_list = (org.python.types.List) java_list.__add__(java_list_a);
	}
	java_list_res = (org.python.types.List) java_list_a.__mul__(mul);
	assertEquals(java_list_res, java_list);
    }

    @Test
    public void test__mul__neg() {
	int imul = -2;
	org.python.types.Int mul = org.python.types.Int.getInt(imul);
	org.python.types.List java_list = new org.python.types.List();
	org.python.types.List java_list_a;
	org.python.types.List java_list_res;
	java_list_a = py_list_from_java_ints(1, 2, 3);
	java_list_res = (org.python.types.List) java_list_a.__mul__(mul);
	assertEquals(java_list_res, java_list);
    }

    @Test
    public void test__round__() {
	org.python.types.List java_list = py_list_from_java_ints();
	exceptionRule.expect(org.python.exceptions.TypeError.class);
	exceptionRule.expectMessage("type list doesn't define __round__ method");
	java_list.__round__(org.python.types.Int.getInt(3));
    }

    @Test
    public void testSortInt() {
	org.python.types.List java_list = py_list_from_java_ints(5, 4, 47, 62, -5);
	java_list.sort(null, null);
	assertEquals(java_list, py_list_from_java_ints(-5, 4, 5, 47, 62));
    }

    @Test
    public void testSortIntReverse() {
	org.python.types.List java_list = py_list_from_java_ints(5, 4, 47, 62, -5);
	java_list.sort(null, org.python.types.Bool.TRUE);
	assertEquals(java_list, py_list_from_java_ints(62, 47, 5, 4, -5));
    }

}
