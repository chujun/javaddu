package com.jun.chu.java.collection;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author chujun
 * @date 2021/12/1
 */
public abstract class MyAbstractMap<K, V> implements MyMap<K, V> {
    protected MyAbstractMap() {
    }

    // Query Operations

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsValue(Object value) {
        Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
        if (value == null) {
            while (i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                if (e.getValue() == null) {
                    return true;
                }
            }
        } else {
            while (i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                if (value.equals(e.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean containsKey(Object key) {
        Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
        if (key == null) {
            while (i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                if (e.getKey() == null) {
                    return true;
                }
            }
        } else {
            while (i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                if (key.equals(e.getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
        if (key == null) {
            while (i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                if (e.getKey() == null) {
                    return e.getValue();
                }
            }
        } else {
            while (i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                if (key.equals(e.getKey())) {
                    return e.getValue();
                }
            }
        }
        return null;
    }

    // Modification Operations
    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
        MyMap.MyEntry<K, V> correctEntry = null;
        if (key == null) {
            while (correctEntry == null && i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                if (e.getKey() == null) {
                    correctEntry = e;
                }
            }
        } else {
            while (correctEntry == null && i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                if (key.equals(e.getKey())) {
                    correctEntry = e;
                }
            }
        }

        V oldValue = null;
        if (correctEntry != null) {
            oldValue = correctEntry.getValue();
            i.remove();
        }
        return oldValue;
    }

    // Bulk Operations
    @Override
    public void putAll(MyMap<? extends K, ? extends V> m) {
        for (MyMap.MyEntry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        entrySet().clear();
    }

    // Views
    transient Set<K> keySet;
    transient Collection<V> values;


    /**
     * 默认实现是线程不安全的
     *
     * @return
     */
    @Override
    public Set<K> keySet() {
        Set<K> ks = keySet;
        if (ks == null) {
            ks = new AbstractSet<K>() {
                @Override
                public Iterator<K> iterator() {
                    return new Iterator<K>() {
                        private Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();

                        @Override
                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        @Override
                        public K next() {
                            return i.next().getKey();
                        }

                        @Override
                        public void remove() {
                            i.remove();
                        }
                    };
                }

                @Override
                public int size() {
                    return MyAbstractMap.this.size();
                }

                @Override
                public boolean isEmpty() {
                    return MyAbstractMap.this.isEmpty();
                }

                @Override
                public void clear() {
                    MyAbstractMap.this.clear();
                }

                @Override
                public boolean contains(Object k) {
                    return MyAbstractMap.this.containsKey(k);
                }
            };
            keySet = ks;
        }
        return ks;
    }

    @Override
    public Collection<V> values() {
        Collection<V> vals = values;
        if (vals == null) {
            vals = new AbstractCollection<V>() {
                @Override
                public Iterator<V> iterator() {
                    return new Iterator<V>() {
                        private Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();

                        @Override
                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        @Override
                        public V next() {
                            return i.next().getValue();
                        }

                        @Override
                        public void remove() {
                            i.remove();
                        }
                    };
                }

                @Override
                public int size() {
                    return MyAbstractMap.this.size();
                }

                @Override
                public boolean isEmpty() {
                    return MyAbstractMap.this.isEmpty();
                }

                @Override
                public void clear() {
                    MyAbstractMap.this.clear();
                }

                @Override
                public boolean contains(Object v) {
                    return MyAbstractMap.this.containsValue(v);
                }
            };
            values = vals;
        }
        return vals;
    }

    @Override
    public abstract Set<MyMap.MyEntry<K, V>> entrySet();

    // Comparison and hashing
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Map)) {
            return false;
        }
        Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size()) {
            return false;
        }

        try {
            Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
            while (i.hasNext()) {
                MyMap.MyEntry<K, V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key) == null && m.containsKey(key))) {
                        return false;
                    }
                } else {
                    if (!value.equals(m.get(key))) {
                        return false;
                    }
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
        while (i.hasNext()) {
            h += i.next().hashCode();
        }
        return h;
    }

    @Override
    public String toString() {
        Iterator<MyMap.MyEntry<K, V>> i = entrySet().iterator();
        if (!i.hasNext()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (; ; ) {
            MyMap.MyEntry<K, V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (!i.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',').append(' ');
        }
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        MyAbstractMap<?, ?> result = (MyAbstractMap<?, ?>) super.clone();
        result.keySet = null;
        result.values = null;
        return result;
    }

    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static class MySimpleEntry<K, V>
        implements MyMap.MyEntry<K, V>, java.io.Serializable {

        private final K key;
        private V value;

        public MySimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public MySimpleEntry(MyMap.MyEntry<? extends K, ? extends V> entry) {
            this.key = entry.getKey();
            this.value = entry.getValue();
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(final V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MyMap.MyEntry)) {
                return false;
            }
            MyMap.MyEntry<?, ?> e = (MyMap.MyEntry<?, ?>) o;
            return eq(key, e.getKey()) && eq(value, e.getValue());
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^
                (value == null ? 0 : value.hashCode());
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    public static class MySimpleImmutableEntry<K, V>
        implements MyMap.MyEntry<K, V>, java.io.Serializable {
        private static final long serialVersionUID = 7138329143949025153L;

        private final K key;
        private final V value;

        public MySimpleImmutableEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public MySimpleImmutableEntry(MyMap.MyEntry<? extends K, ? extends V> entry) {
            this.key = entry.getKey();
            this.value = entry.getValue();
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(final V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MyMap.MyEntry)) {
                return false;
            }
            MyMap.MyEntry<?, ?> e = (MyMap.MyEntry<?, ?>) o;
            return eq(key, e.getKey()) && eq(value, e.getValue());
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^
                (value == null ? 0 : value.hashCode());
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
