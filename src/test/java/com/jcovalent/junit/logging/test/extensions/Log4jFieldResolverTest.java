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
package com.jcovalent.junit.logging.test.extensions;

import static com.jcovalent.junit.logging.utils.Utils.qualifiedMemberName;
import static org.assertj.core.api.Assertions.assertThat;

import com.jcovalent.junit.logging.JCovalentLoggingSupport;
import com.jcovalent.junit.logging.ManagedLogger;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

@JCovalentLoggingSupport
class Log4jFieldResolverTest {

    private Logger logger;

    @ManagedLogger(name = "test")
    private Logger managedLogger;

    private static Logger staticPlainLogger;

    @ManagedLogger(name = "staticManaged")
    private static Logger staticManagedLogger;

    @ManagedLogger(name = "staticLeveled", logLevel = org.slf4j.event.Level.INFO)
    private static Logger staticLeveledLogger;

    @Test
    void testBasicLoggerField() {
        assertThat(logger)
                .isNotNull()
                .isInstanceOf(Logger.class)
                .extracting(Logger::getName)
                .isEqualTo(qualifiedMemberName(this.getClass(), "logger"));
    }

    @Test
    void testNamedLoggerField() {
        assertThat(managedLogger)
                .isNotNull()
                .isInstanceOf(Logger.class)
                .extracting(Logger::getName)
                .isEqualTo("test");
    }

    @Test
    void testStaticPlainLoggerField() {
        assertThat(staticPlainLogger)
                .isNotNull()
                .isInstanceOf(Logger.class)
                .extracting(Logger::getName)
                .isEqualTo(qualifiedMemberName(this.getClass(), "staticPlainLogger"));
    }

    @Test
    void testStaticManagedLoggerField() {
        assertThat(staticManagedLogger)
                .isNotNull()
                .isInstanceOf(Logger.class)
                .extracting(Logger::getName)
                .isEqualTo("staticManaged");
    }

    @Test
    void testStaticLeveledLoggerField() {
        assertThat(staticLeveledLogger)
                .isNotNull()
                .isInstanceOf(Logger.class)
                .extracting(Logger::getName)
                .isEqualTo("staticLeveled");

        assertThat(staticLeveledLogger).extracting(Logger::isInfoEnabled).isEqualTo(true);
        assertThat(staticLeveledLogger).extracting(Logger::isTraceEnabled).isEqualTo(false);
    }
}
