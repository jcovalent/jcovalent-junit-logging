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
package com.jcovalent.junit.logging.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class Log4jExternalLoggers {

    private static final Logger LOG = LogManager.getLogger(Log4jExternalLoggers.class);
    private static final Marker GENERAL_MARKER = MarkerManager.getMarker("GENERAL");

    private final Logger logger =
            LogManager.getLogger(String.format("%s.%s", Log4jExternalLoggers.class.getName(), "instanceLogger"));

    public Log4jExternalLoggers() {}

    public static void logRepeatedNTimes(final int n, final Level level) {
        assertThat(n).isPositive();

        for (int i = 1; i <= n; i++) {
            LOG.log(
                    level,
                    GENERAL_MARKER,
                    "[[Static]] logRepeatedNTimes at {} level for {} iterations, currently on" + " iteration {}",
                    level,
                    n,
                    i);
        }
    }

    public void infoNTimes(final int n) {
        assertThat(n).isPositive();

        for (int i = 0; i < n; i++) {
            logger.info("[[Instance]] infoNTimes for {} iterations, currently on iteration {}", n, i);
        }
    }
}
