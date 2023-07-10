/*
 * Copyright (C) 2023 JCovalent
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
package com.jcovalent.junit.logging.assertj;

import com.jcovalent.junit.logging.LogEntry;
import com.jcovalent.junit.logging.LoggingOutput;
import java.util.List;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;

/**
 * AssertJ compatible extensions for {@link LoggingOutput} instances.
 */
public class LoggingOutputAssert extends AbstractAssert<LoggingOutputAssert, LoggingOutput> {

    /**
     * Creates a new {@link LoggingOutputAssert} to make assertions on {@link LoggingOutput} instances.
     *
     * @param actual the LoggingOutput instance to make assertions against.
     */
    public LoggingOutputAssert(final LoggingOutput actual) {
        super(actual, LoggingOutputAssert.class);
    }

    /**
     * Creates a new {@link LoggingOutputAssert} to make assertions on {@link LoggingOutput} instances.
     *
     * @param actual the LoggingOutput instance to make assertions against.
     * @return a new {@link LoggingOutputAssert} instance.
     */
    public static LoggingOutputAssert assertThat(final LoggingOutput actual) {
        return new LoggingOutputAssert(actual);
    }

    /**
     * Asserts that the given log entries contain the expected log entries only matching the level
     * and message.
     *
     * @param expectedLogEntries the expected log entries.
     */
    public void hasAtLeastOneEntry(List<LogEntry> expectedLogEntries) {
        org.assertj.core.api.Assertions.assertThat(actual.allEntries()).isNotNull();
        for (LogEntry logEntry : expectedLogEntries) {
            Assertions.assertThat(actual.allEntries())
                    .haveAtLeastOne(new Condition<>(
                            entry -> entry.level() == logEntry.level()
                                    && entry.message().contains(logEntry.message()),
                            "message contains '" + logEntry.message() + "'"));
        }
    }
}
