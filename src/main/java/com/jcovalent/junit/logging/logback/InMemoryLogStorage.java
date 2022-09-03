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
package com.jcovalent.junit.logging.logback;

import com.jcovalent.junit.logging.LogEntry;
import com.jcovalent.junit.logging.LoggingOutput;
import com.jcovalent.junit.logging.LoggingOutputScope;
import com.jcovalent.junit.logging.storage.GroupingMemoryStorage;
import com.jcovalent.junit.logging.storage.GroupingStorage;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.event.Level;

public class InMemoryLogStorage implements LoggingOutput {

    private final GroupingStorage<Long, LogEntry, Level> backingStorage;

    private final String name;

    private final LoggingOutputScope scope;

    public InMemoryLogStorage(final String name, final LoggingOutputScope scope) {
        this(name, scope, new GroupingMemoryStorage<>(LogEntry::level));
    }

    public InMemoryLogStorage(
            final String name,
            final LoggingOutputScope scope,
            final GroupingStorage<Long, LogEntry, Level> backingStorage) {
        this.backingStorage = backingStorage;
        this.name = name;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    @Override
    public LoggingOutputScope getScope() {
        return scope;
    }

    @Override
    public List<LogEntry> allEntries() {
        return new ArrayList<>(backingStorage.values());
    }

    @Override
    public List<LogEntry> traceEntries() {
        return new ArrayList<>(backingStorage.groupOf(Level.TRACE).values());
    }

    @Override
    public List<LogEntry> debugEntries() {
        return new ArrayList<>(backingStorage.groupOf(Level.DEBUG).values());
    }

    @Override
    public List<LogEntry> infoEntries() {
        return new ArrayList<>(backingStorage.groupOf(Level.INFO).values());
    }

    @Override
    public List<LogEntry> warnEntries() {
        return new ArrayList<>(backingStorage.groupOf(Level.WARN).values());
    }

    @Override
    public List<LogEntry> errorEntries() {
        return new ArrayList<>(backingStorage.groupOf(Level.ERROR).values());
    }

    public GroupingStorage<Long, LogEntry, Level> getBackingStorage() {
        return backingStorage;
    }

    public void reset() {
        backingStorage.reset();
    }
}
