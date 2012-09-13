package myPack;

import java.util.HashMap;
import java.util.Map;

public class DefaultHashMap<K, V> extends HashMap<K, V> {
	/**
	 * This made Eclipse really happy.
	 */
	private static final long serialVersionUID = 1554353438471176710L;
	
	protected V defaultValue;
	
	public DefaultHashMap(V defaultValue) {
		// TODO Auto-generated constructor stub
		super();
		this.defaultValue = defaultValue;
	}

	public DefaultHashMap(int initialCapacity, V defaultValue) {
		super(initialCapacity);
		this.defaultValue = defaultValue;
	}

	public DefaultHashMap(Map<? extends K, ? extends V> m, V defaultValue) {
		super(m);
		this.defaultValue = defaultValue;
	}
	

	public DefaultHashMap(int initialCapacity, float loadFactor, V defaultValue) {
		super(initialCapacity, loadFactor);
		this.defaultValue = defaultValue;
	}

	@Override
	public V get(Object key) {
		V value = super.get(key);
		return ((value == null) && !this.containsKey(key)) ? this.defaultValue : value;
	}
}
