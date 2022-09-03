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
package com.jcovalent.junit.logging;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public interface LoggingOutput {
    LoggingOutputScope getScope();

    List<LogEntry> allEntries();

    List<LogEntry> traceEntries();

    List<LogEntry> debugEntries();

    List<LogEntry> infoEntries();

    List<LogEntry> warnEntries();

    List<LogEntry> errorEntries();

    default List<String> allLines() {
        return allEntries().stream().map(LogEntry::formattedLogLine).toList();
    }

    default List<String> traceLines() {
        return traceEntries().stream().map(LogEntry::formattedLogLine).toList();
    }

    default List<String> debugLines() {
        return debugEntries().stream().map(LogEntry::formattedLogLine).toList();
    }

    default List<String> infoLines() {
        return infoEntries().stream().map(LogEntry::formattedLogLine).toList();
    }

    default List<String> warnLines() {
        return warnEntries().stream().map(LogEntry::formattedLogLine).toList();
    }

    default List<String> errorLines() {
        return errorEntries().stream().map(LogEntry::formattedLogLine).toList();
    }

    default void writeLogStream(final OutputStream stream) {
        writeLogStream(new PrintStream(stream));
    }

    default void writeLogStream(final PrintStream stream) {
        allEntries().forEach(e -> stream.print(e.formattedLogLine()));
    }
}
