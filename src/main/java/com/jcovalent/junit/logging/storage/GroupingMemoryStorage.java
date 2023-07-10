/*
 * Copyright (C) 2022-2023 JCovalent
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

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class GroupingMemoryStorage<K extends Comparable<K>, V, G> extends MemoryStorage<K, V>
        implements GroupingStorage<K, V, G> {
    private final Map<G, Storage<K, V>> groups = new HashMap<>();
    private final Function<V, G> groupingKeyProvider;

    public GroupingMemoryStorage(final Function<V, G> groupingKeyProvider) {
        this.groupingKeyProvider = groupingKeyProvider;
    }

    public GroupingMemoryStorage(
            final Supplier<SortedMap<K, V>> backingStoreSupplier, final Function<V, G> groupingKeyProvider) {
        super(backingStoreSupplier);
        this.groupingKeyProvider = groupingKeyProvider;
    }

    protected GroupingMemoryStorage(final SortedMap<K, V> backingStore, final Function<V, G> groupingKeyProvider) {
        super(backingStore);
        this.groupingKeyProvider = groupingKeyProvider;
    }

    @Override
    public Storage<K, V> groupOf(final G groupingKey) {
        return groups.computeIfAbsent(groupingKey, gk -> new MemoryStorage<>());
    }

    @Override
    public V put(final K key, final V value) {
        final G groupingKey = groupingKeyProvider.apply(value);

        if (groupingKey != null) {
            groupOf(groupingKey).put(key, value);
        }

        return super.put(key, value);
    }

    @Override
    public void reset() {
        groups.forEach((k, v) -> {
            if (v != null) {
                v.reset();
            }
        });

        groups.clear();
        super.reset();
    }
}
