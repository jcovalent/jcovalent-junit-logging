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
package com.jcovalent.junit.logging.assertj;

import com.jcovalent.junit.logging.LoggingOutput;
import org.slf4j.Logger;

public final class Assertions {

    private Assertions() {}

    public static Object assertThat(final Logger actual) {
        return new Slf4jLoggerAssert(actual);
    }

    public static Object assertThat(final org.apache.logging.log4j.Logger actual) {
        return new Log4jLoggerAssert(actual);
    }

    public static Object assertThat(final LoggingOutput actual) {
        return new LoggingOutputAssert(actual);
    }
}
