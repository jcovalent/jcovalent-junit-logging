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
package com.jcovalent.junit.logging.test;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcovalent.junit.logging.LogEntry;
import com.jcovalent.junit.logging.LogEntryBuilder;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

public class LogEntryBuilderTest {
    @Test
    void testLogEntryBuilder() {
        Throwable throwable = new Exception();
        Instant timestamp = Instant.now();

        LogEntry logEntry = LogEntryBuilder.builder()
                .sequenceNumber(1)
                .timestamp(timestamp)
                .level(Level.DEBUG)
                .markerName("markerName")
                .threadName("threadName")
                .loggerName("loggerName")
                .message("my message")
                .throwable(throwable)
                .formattedLogLine("formattedLogLine")
                .build();

        assertThat(logEntry.sequenceNumber()).isEqualTo(1);
        assertThat(logEntry.timestamp()).isEqualTo(timestamp);
        assertThat(logEntry.level()).isEqualTo(Level.DEBUG);
        assertThat(logEntry.markerName()).isEqualTo("markerName");
        assertThat(logEntry.threadName()).isEqualTo("threadName");
        assertThat(logEntry.loggerName()).isEqualTo("loggerName");
        assertThat(logEntry.message()).isEqualTo("my message");
        assertThat(logEntry.throwable()).isEqualTo(throwable);
        assertThat(logEntry.formattedLogLine()).isEqualTo("formattedLogLine");
    }
}
