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
package com.jcovalent.junit.logging;

import static com.jcovalent.junit.logging.assertj.LoggingOutputAssert.assertThatLogEntriesHaveMessages;
import static com.jcovalent.junit.logging.utils.TestUtils.safeAssertEmptyOutput;
import static com.jcovalent.junit.logging.utils.TestUtils.safeAssertOutputOfNItems;
import static org.assertj.core.api.Assertions.assertThat;

import com.jcovalent.junit.logging.logback.InMemoryLogStorage;
import com.jcovalent.junit.logging.utils.Log4jExternalLoggers;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@JCovalentLoggingSupport
class LoggingOutputTest {

    @Test
    void testInjectedLogger(final Logger logger, final LoggingOutput loggingOutput) {
        assertThat(loggingOutput)
                .isNotNull()
                .isInstanceOf(InMemoryLogStorage.class)
                .extracting(LoggingOutput::getScope)
                .isEqualTo(LoggingOutputScope.TEST);

        final int n = 10;

        safeAssertEmptyOutput(loggingOutput);
        for (int i = 1; i <= n; i++) {
            logger.info(
                    MarkerManager.getMarker("INJECTED"),
                    "[[Injected]] repeatedNTimes at INFO level for {} iterations, currently on"
                            + " iteration {}",
                    n,
                    i);
        }
        safeAssertOutputOfNItems(loggingOutput::allEntries, 10);
        safeAssertOutputOfNItems(loggingOutput::infoEntries, 10);
        safeAssertOutputOfNItems(loggingOutput::infoLines, 10);
    }

    @Test
    void testExternalStaticLogging(final LoggingOutput loggingOutput) {
        assertThat(loggingOutput)
                .isNotNull()
                .isInstanceOf(InMemoryLogStorage.class)
                .extracting(LoggingOutput::getScope)
                .isEqualTo(LoggingOutputScope.TEST);

        safeAssertEmptyOutput(loggingOutput);

        Log4jExternalLoggers.logRepeatedNTimes(10, Level.INFO);
        loggingOutput.writeLogStream(System.out);

        safeAssertOutputOfNItems(loggingOutput::allEntries, 10);
        safeAssertOutputOfNItems(loggingOutput::infoEntries, 10);
        safeAssertOutputOfNItems(loggingOutput::infoLines, 10);
    }

    @Test
    @DisplayName("Assert the log level and message match")
    void testAssertLogMessage(final Logger logger, final LoggingOutput loggingOutput) {
        logger.info("This is a test message");
        assertThatLogEntriesHaveMessages(
                loggingOutput,
                List.of(
                        LogEntryBuilder.builder()
                                .level(org.slf4j.event.Level.INFO)
                                .message("This is a test message")
                                .build()));
    }
}
