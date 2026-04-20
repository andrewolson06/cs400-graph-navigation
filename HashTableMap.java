///////////////////////////////////////////////////////////////////////////////
// Title:            P212.Hashtable
// Files:            HashTableMap.java
// Semester:         CS 400, Spring 2026
//
// Author:           Andrew Olson
// Email:            apolson7@wisc.edu
// Lecturer's Name:  Florian Heimerl
//
// Credits:          No help given or received.
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * This class builds a fully functional hash table using core methods and
 * additional helper methods. This class implements the MapADT class
 * which has the methods currently overridden.
 *
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

    /**
     * This inner class creates a pair of two values which are both inputted
     * into the hash table.
     */
    protected class Pair {
	public KeyType key;
	public ValueType value;

	/**
	 * This is the Pair class constructor which assigns the key and value
         * data pair.
         * @param key
         * @param value
	 */
	public Pair(KeyType key, ValueType value) {
	    this.key = key;
	    this.value = value;
	}
    }

    //protected and private fields
    protected LinkedList<Pair>[] table;
    private int size;

    /**
     * This is the constructor which is implemented with a capacity check.
     * In this, the capacity must be at least 1.
     * @param capacity
     */
    @SuppressWarnings("unchecked")
    public HashTableMap(int capacity) {
	if (capacity < 1) {
	    throw new IllegalArgumentException();
	}
	table = (LinkedList<Pair>[]) new LinkedList[capacity];
	size = 0;
    }

    /**
     * This is the constructor which assigns the default capacity of the
     * hash table to be 8.
     */
    public HashTableMap() {
	this(8);
    }

    /**
     * This helper method implements the hash function. The value that is
     * then found is used as the index for such key value pair.
     * @param key
     * @return int
     */
    private int getIndex(KeyType key) {
	return Math.abs(key.hashCode()) % table.length;
    }

    /**
     * This is the resize helper which is called when the load factor
     * exceeds 75%. This helper method doubles the size of the table and does
     * this creating a new table that is double the size as the original and
     * then copies all elements of that table to the new table.
     */
    @SuppressWarnings("unchecked")
    private void resize() {
	LinkedList<Pair>[] oldTable = table;

	table = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];
	size = 0;

	for (LinkedList<Pair> add : oldTable) {
	    if (add != null) {
		for (Pair pair : add) {
		    put(pair.key, pair.value);
		}
	    }
	}
    }

    /**
     * This is the core method which inserts key, value pairs into the hash
     * table map. It first checks the validity of the pair (non-null and
     * non-duplicate), then resizes the hash table map if the load factor was
     * exceeded (calls resize() method). Then adds the pair to the hash table map.
     * @param key
     * @param value
     * @throws IllegalArgumentException
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
	if (key == null) {
	    throw new NullPointerException();
	}

	int index = getIndex(key);

	if (table[index] != null) {
	    for (Pair pair : table[index]) {
	        if (pair.key.equals(key)) {
		    throw new IllegalArgumentException("Can not have duplicates");
	        }
	    }
	}

	if ((double)(size + 1) / table.length >= 0.75) {
	    resize();
	    index = getIndex(key);
	}

	if (table[index] == null) {
	    table[index] = new LinkedList<>();
	}

	table[index].add(new Pair(key, value));
	size++;
    }

    /**
     * This is the core method which checks if a key, value pair is included
     * in the hash table map. It uses the key to check if the pair is included.
     * Validity is checked, then the table is iterated through to see if the key is contained.
     * @param key
     * @return boolean
     */
    @Override
    public boolean containsKey(KeyType key) {
	if (key == null) {
	    throw new NullPointerException();
	}

	int index = getIndex(key);

	if (table[index] == null) {
	    return false;
	}

	for (Pair pair : table[index]) {
	    if (pair.key.equals(key)) {
		return true;
	    }
	}


	return false;
    }

    /**
     * This is the core method which removes key, value pairs from the hash table map.
     * The functionality of the method is similar to the containsKey method,
     * except returns and removes the pair when found, rather than return true or false based on if it was found or not.
     * @param key
     * @throws NoSuchElementException
     * @return ValueType
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
	if (key == null) {
	    throw new NullPointerException();
	}

	int index = getIndex(key);

	if (table[index] != null) {
	    for (int i = 0; i < table[index].size(); i++) {
		Pair pair = table[index].get(i);
		if (pair.key.equals(key)) {
		    table[index].remove(i);
		    size--;
		    return pair.value;
		}
	    }
	}

	throw new NoSuchElementException();
    }

    /**
     * This is the core method that returns the value for a pair based on
     * the corresponding index that the pair belongs to.
     * Validity checks are made, then the hash table map is iterated until
     * the correct pair is found and the value is then returned.
     * @return ValueType
     * @param key
     * @throws NoSuchElementException
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
	if (key == null) {
	    throw new NullPointerException();
	}

	int index = getIndex(key);

	if (table[index] != null) {
	    for (Pair pair : table[index]) {
		if (pair.key.equals(key)) {
		    return pair.value;
		}
	    }
	}

	throw new NoSuchElementException("Key was not found");
    }

    /**
     * This is the core method which clears the hash table map.
     * The table is iterated through until all indices are null.
     */
    @Override
    public void clear() {
	for (int i = 0; i < table.length; i++) {
	    table[i] = null;
	}
	size = 0;
    }

    /**
     * This is the core method which simply returns the size of the hash table map.
     * Size is defined how many pairs are in the table.
     * @return int
     */
    @Override
    public int getSize() {
	return size;
    }

    /**
     * This is the core method which returns the capacity of the hash table map.
     * Capacity is the entire size of the map, including indices without key, value pairs.
     * @return int
     */
    @Override
    public int getCapacity() {
	return table.length;
    }

    /**
     * This is the core method which returns all of the keys for every key, value pair.
     * This is implemented by using for loops that iterate through the entire
     * hash table map.
     * @return List<KeyType>
     */
    @Override
    public List<KeyType> getKeys() {
	LinkedList<KeyType> keys = new LinkedList<>();

	for (LinkedList<Pair> add : table) {
	    if (add != null) {
		for (Pair pair : add) {
		    keys.add(pair.key);
		}
	    }
	}

	return keys;
    }

    /**
     * This is the first test method which simply tests whether the put() and 
     * get() methods are functional. 
     */
    @Test
    public void testPutAndGet() {
	//create map
	HashTableMap<String, Integer> map = new HashTableMap<>();

	//add inputs
	map.put("one", 1);
	map.put("two", 2);
	map.put("three", 3);

	//test if those inputs have the correct key and value pair.
	Assertions.assertEquals(1, map.get("one"));
        Assertions.assertEquals(2, map.get("two"));
        Assertions.assertEquals(3, map.get("three"));
    }

    /**
     * This second test method simply checks whether the containsKey() method
     * is fully functional. containsKey() should return true if input belongs,
     * and false if input does not belong
     */
    @Test
    public void testContains() {
	//create map
	HashTableMap<String, Integer> map = new HashTableMap<>();

	//input me
	map.put("Andrew", 20);

	//the hash table map should include Andrew, but not Olson
	Assertions.assertTrue(map.containsKey("Andrew"));
	Assertions.assertFalse(map.containsKey("Olson"));
    }

    /**
     * This third test method checks the remove() method. It tests
     * that the method returns the value that is removed, and that the key, value
     * pair is removed and no longer contained within the hash table map.
     * This method also tests if the size variable stays accruate.
     */
    @Test
    public void testRemove() {
	//create map
	HashTableMap<String, Integer> map = new HashTableMap<>();

	//create inputs
	map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

	//First test tests the return value
	Assertions.assertEquals(3, map.remove("three"));
	//Second test tests that pair is no longer contained
	Assertions.assertFalse(map.containsKey("three"));
	//Third test tests if the size stays consistent
	Assertions.assertEquals(2, map.getSize());
    }

    /**
     * This fourth test method simply tests the clear() method.
     * The new size of the hash table map should be 0 and the default capacity
     * should be given (8). The inputs originally should also not be contained.
     */
    @Test
    public void testClear() {
	//create map
	HashTableMap<String, Integer> map = new HashTableMap<>();

	//give inputs
	map.put("Andrew", 1);
	map.put("Paul", 2);
	map.put("Olson", 3);

	//clear the map
	map.clear();

	//size should be 0, capacity should be 8, and inputs should be longer contained.
	Assertions.assertEquals(0, map.getSize());
	Assertions.assertEquals(8, map.getCapacity());
	Assertions.assertFalse(map.containsKey("Paul"));
    }

    /**
     * This fifth test method tests the resize() and put() method.
     * The test first checks if the capacity doubles after the load factor
     * is exceeded. It then checks to see if all the pairs are copied into the 
     * new map.
     */
    @Test
    public void testResize() {
	//create map
	HashTableMap<String, Integer> map = new HashTableMap<>();

	//give inputs (load factor not reached)
	map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
	map.put("four", 4);
	map.put("five", 5);

	//test that capacity only doubles when load factor is exceeded
	Assertions.assertEquals(8, map.getCapacity());

	//exceed the load factor
	map.put("six", 6);

	//test that capacity doubled and pairs from original map are still included.
	Assertions.assertEquals(16, map.getCapacity());
	Assertions.assertEquals(1, map.get("one"));
	Assertions.assertEquals(2, map.get("two"));
	Assertions.assertEquals(3, map.get("three"));
	Assertions.assertEquals(4, map.get("four"));
	Assertions.assertEquals(5, map.get("five"));
	Assertions.assertEquals(6, map.get("six"));
    }
}
