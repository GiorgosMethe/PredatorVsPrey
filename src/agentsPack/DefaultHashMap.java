package agentsPack;

import java.util.HashMap;
import java.util.Map;

public class DefaultHashMap<K, V> extends HashMap<K, V> {
	public interface DefaultFactory<Input, Output> {
		public Output getDefault(Input key);
	}

	public class SimpleDefaultFactory<Input, Output> implements
			DefaultFactory<Input, Output> {
		public Output defaultValue;

		SimpleDefaultFactory(Output defaultValue) {
			this.defaultValue = defaultValue;
		}

		public Output getDefault(Input key) {
			return this.defaultValue;
		}
	}

	/**
	 * This made Eclipse really happy.
	 */
	private static final long serialVersionUID = 1554353438471176710L;

	protected DefaultFactory<K, V> defaultFactory;

	public DefaultHashMap(V defaultValue) {
		// TODO Auto-generated constructor stub
		super();
		this.defaultFactory = new SimpleDefaultFactory<K, V>(defaultValue);
	}

	public DefaultHashMap(DefaultFactory<K, V> defaultFactory) {
		// TODO Auto-generated constructor stub
		super();
		this.defaultFactory = defaultFactory;
	}

	public DefaultHashMap(int initialCapacity, V defaultValue) {
		super(initialCapacity);
		this.defaultFactory = new SimpleDefaultFactory<K, V>(defaultValue);
	}

	public DefaultHashMap(int initialCapacity,
			DefaultFactory<K, V> defaultFactory) {
		super(initialCapacity);
		this.defaultFactory = defaultFactory;
	}

	public DefaultHashMap(Map<? extends K, ? extends V> m, V defaultValue) {
		super(m);
		this.defaultFactory = new SimpleDefaultFactory<K, V>(defaultValue);
	}

	public DefaultHashMap(Map<? extends K, ? extends V> m,
			DefaultFactory<K, V> defaultFactory) {
		super(m);
		this.defaultFactory = defaultFactory;
	}

	public DefaultHashMap(int initialCapacity, float loadFactor, V defaultValue) {
		super(initialCapacity, loadFactor);
		this.defaultFactory = new SimpleDefaultFactory<K, V>(defaultValue);
	}

	public DefaultHashMap(int initialCapacity, float loadFactor,
			DefaultFactory<K, V> defaultFactory) {
		super(initialCapacity, loadFactor);
		this.defaultFactory = defaultFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		V value = super.get(key);
		if (value != null && this.containsKey(key)) {
			return value;
		}
		this.put((K) key, this.defaultFactory.getDefault((K) key));
		return this.get(key);
	}
}
