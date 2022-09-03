/*
 * Copyright (C) 2022 JCovalent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcovalent.junit.logging.storage;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Supplier;

public class MemoryStorage<K extends Comparable<K>, V> implements Storage<K, V> {
    private final SortedMap<K, V> backingStore;

    public MemoryStorage() {
        this.backingStore = new TreeMap<>();
    }

    public MemoryStorage(final Supplier<SortedMap<K, V>> backingStoreSupplier) {
        this.backingStore = backingStoreSupplier.get();
    }

    protected MemoryStorage(final SortedMap<K, V> backingStore) {
        this.backingStore = backingStore;
    }

    @Override
    public Storage<K, V> viewOfRange(final K fromKey, final K toKey) {
        return new MemoryStorage<>(backingStore.subMap(fromKey, toKey));
    }

    @Override
    public V get(final K key) {
        return backingStore.get(key);
    }

    @Override
    public V put(final K key, final V value) {
        return backingStore.put(key, value);
    }

    @Override
    public Iterator<V> iterator() {
        return backingStore.values().iterator();
    }

    @Override
    public Collection<V> values() {
        return backingStore.values();
    }

    @Override
    public Set<K> keySet() {
        return backingStore.keySet();
    }

    public Set<Entry<K, V>> entrySet() {
        return backingStore.entrySet();
    }

    @Override
    public int size() {
        return backingStore.size();
    }

    @Override
    public boolean isEmpty() {
        return backingStore.isEmpty();
    }

    @Override
    public void reset() {
        backingStore.clear();
    }

    protected SortedMap<K, V> getBackingStore() {
        return backingStore;
    }
}
