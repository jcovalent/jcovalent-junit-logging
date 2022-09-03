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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.status.ErrorStatus;
import com.jcovalent.junit.logging.LogEntry;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryAppender<E extends ILoggingEvent> extends AppenderBase<E> {
    private static final AtomicLong sequenceNumber = new AtomicLong();

    private InMemoryLogStorage logStorage;

    private Encoder<E> encoder;

    @Override
    public void start() {
        if (this.encoder == null) {
            addStatus(
                    new ErrorStatus(
                            "No encoder set for the appender named \"" + name + "\".", this));
            return;
        }

        super.start();
    }

    @Override
    public void stop() {
        if (!isStarted()) {
            return;
        }

        if (encoder != null && encoder.isStarted()) {
            encoder.stop();
        }

        super.stop();
    }

    public InMemoryLogStorage getLogStorage() {
        return logStorage;
    }

    public void setLogStorage(final InMemoryLogStorage logStorage) {
        if (isStarted()) {
            return;
        }

        this.logStorage = logStorage;
    }

    public Encoder<E> getEncoder() {
        return encoder;
    }

    public void setEncoder(final Encoder<E> encoder) {
        if (isStarted()) {
            return;
        }

        this.encoder = encoder;
    }

    @Override
    protected void append(final E eventObject) {
        if (!isStarted()) {
            return;
        }

        final long sequence = sequenceNumber.incrementAndGet();

        eventObject.prepareForDeferredProcessing();
        logStorage.getBackingStorage().put(sequence, createLogEntry(sequence, eventObject));
    }

    private LogEntry createLogEntry(final long sequence, final E event) {
        Throwable throwable = null;
        if (event.getThrowableProxy() instanceof ThrowableProxy realProxy) {
            throwable = realProxy.getThrowable();
        }

        final byte[] evBytes = encoder.encode(event);
        final String evString = new String(evBytes, StandardCharsets.UTF_8);

        return new LogEntry(
                sequence,
                event.getInstant(),
                toSlf4jLevel(event.getLevel()),
                event.getMarkerList() != null && !event.getMarkerList().isEmpty()
                        ? event.getMarkerList().get(0).getName()
                        : null,
                event.getThreadName(),
                event.getLoggerName(),
                event.getFormattedMessage(),
                throwable,
                evString);
    }

    private static org.slf4j.event.Level toSlf4jLevel(final Level level) {
        return switch (level.toInt()) {
            case Level.TRACE_INT -> org.slf4j.event.Level.TRACE;
            case Level.DEBUG_INT -> org.slf4j.event.Level.DEBUG;
            case Level.INFO_INT -> org.slf4j.event.Level.INFO;
            case Level.WARN_INT -> org.slf4j.event.Level.WARN;
            case Level.ERROR_INT -> org.slf4j.event.Level.ERROR;
            default -> throw new IllegalArgumentException(
                    String.format("Unknown SLF4J Level: %s", level));
        };
    }
}
