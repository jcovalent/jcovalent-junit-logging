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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.encoder.Encoder;
import com.jcovalent.junit.logging.LoggingOutputScope;
import java.lang.reflect.Method;
import org.slf4j.LoggerFactory;

public class LogbackConfiguration {

    private static final String DEFAULT_PATTERN =
            "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5p %-16marker <%t> %c{0}: %msg%n";

    private String suiteLogPattern;

    private String testLogPattern;

    private final InMemoryLogStorage suiteStorage;

    private final InMemoryLogStorage testStorage;

    private Appender<ILoggingEvent> suiteAppender;

    private Appender<ILoggingEvent> testAppender;

    public LogbackConfiguration() {
        this.suiteLogPattern = DEFAULT_PATTERN;
        this.suiteStorage = new InMemoryLogStorage("suiteStorage", LoggingOutputScope.SUITE);
        this.testStorage = new InMemoryLogStorage("testStorage", LoggingOutputScope.TEST);
    }

    public void configureForSuite(
            final Class<?> testClass, final String rootLevel, final String suitePattern) {
        final Logger rootLogger = getRootLogger();
        final Level rootLoggerLevel =
                rootLevel != null && !rootLevel.isBlank() ? Level.toLevel(rootLevel) : Level.ALL;

        suiteLogPattern =
                suitePattern != null && !suitePattern.isBlank() ? suitePattern : DEFAULT_PATTERN;

        getContext().reset();
        rootLogger.setLevel(rootLoggerLevel);

        suiteStorage.reset();
        testStorage.reset();

        suiteAppender = createAppender("suiteInMemoryAppender", createSuiteEncoder(), suiteStorage);
        testAppender = null;

        applyAppenderTo(getRootLogger(), suiteAppender);
    }

    public void configureForTest(
            final Class<?> testClass, final Method testMethod, final String testPattern) {
        testLogPattern =
                testPattern != null && !testPattern.isBlank() ? testPattern : suiteLogPattern;

        testStorage.reset();

        if (testAppender == null) {
            testAppender = createAppender("testInMemoryAppender", createTestEncoder(), testStorage);
        }

        applyAppenderTo(getRootLogger(), suiteAppender);
        applyAppenderTo(getRootLogger(), testAppender);
    }

    public void configureLogger(final String name, final String level) {
        Logger logger = getContext().exists(name);
        if (logger == null) {
            logger = getContext().getLogger(name);
        }

        if (level != null && !level.isBlank()) {
            logger.setLevel(Level.toLevel(level));
        }
    }

    private void applyAppenderTo(final Logger logger, final Appender<ILoggingEvent> appender) {
        if (logger.getAppender(appender.getName()) == null) {
            logger.addAppender(appender);
        }
    }

    private PatternLayoutEncoder createSuiteEncoder() {
        return createEncoder(suiteLogPattern);
    }

    private PatternLayoutEncoder createTestEncoder() {
        return createEncoder(testLogPattern);
    }

    private PatternLayoutEncoder createEncoder(final String pattern) {
        final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(getContext());
        encoder.setPattern(pattern);
        encoder.start();

        return encoder;
    }

    private MemoryAppender<ILoggingEvent> createAppender(
            final String name,
            final Encoder<ILoggingEvent> encoder,
            final InMemoryLogStorage storage) {
        final MemoryAppender<ILoggingEvent> appender = new MemoryAppender<>();
        appender.setName(name);
        appender.setContext(getContext());
        appender.setEncoder(encoder);
        appender.setLogStorage(storage);
        appender.start();

        return appender;
    }

    private static Logger getRootLogger() {
        return getContext().getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
    }

    private static LoggerContext getContext() {
        return (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    public InMemoryLogStorage getSuiteStorage() {
        return suiteStorage;
    }

    public InMemoryLogStorage getTestStorage() {
        return testStorage;
    }
}
