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
package com.jcovalent.junit.logging.test.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.jcovalent.junit.logging.LoggingOutput;
import java.util.List;
import java.util.function.Supplier;

public final class TestUtils {

    private TestUtils() {}

    public static void safeAssertEmptyOutput(final LoggingOutput loggingOutput) {
        assertThat(loggingOutput.allEntries()).isNotNull().isEmpty();
        assertThat(loggingOutput.allLines()).isNotNull().isEmpty();

        assertThat(loggingOutput.traceEntries()).isNotNull().isEmpty();
        assertThat(loggingOutput.traceLines()).isNotNull().isEmpty();

        assertThat(loggingOutput.debugEntries()).isNotNull().isEmpty();
        assertThat(loggingOutput.debugLines()).isNotNull().isEmpty();

        assertThat(loggingOutput.infoEntries()).isNotNull().isEmpty();
        assertThat(loggingOutput.infoLines()).isNotNull().isEmpty();

        assertThat(loggingOutput.warnEntries()).isNotNull().isEmpty();
        assertThat(loggingOutput.warnLines()).isNotNull().isEmpty();

        assertThat(loggingOutput.errorEntries()).isNotNull().isEmpty();
        assertThat(loggingOutput.errorLines()).isNotNull().isEmpty();
    }

    public static <T> void safeAssertOutputOfNItems(final Supplier<List<T>> outputSupplier, final int expectedCount) {
        assertThat(expectedCount).isPositive();
        assertThat(outputSupplier).isNotNull();
        assertThat(outputSupplier.get()).isNotNull().hasSize(expectedCount);
    }
}
