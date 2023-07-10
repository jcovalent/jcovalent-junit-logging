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
package com.jcovalent.junit.logging.extensions;

import static com.jcovalent.junit.logging.extensions.TestSuiteLoggingExtension.LOGBACK_CONFIG_KEY;
import static com.jcovalent.junit.logging.extensions.TestSuiteLoggingExtension.NAMESPACE;
import static com.jcovalent.junit.logging.utils.Utils.qualifiedMemberName;

import com.jcovalent.junit.logging.ManagedLogger;
import com.jcovalent.junit.logging.logback.LogbackConfiguration;
import java.util.function.Function;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

abstract class LogResolver<L> {
    private final Class<L> loggerClass;
    private final Function<String, L> loggerInstanceSupplier;

    LogResolver(Class<L> loggerClass, Function<String, L> loggerInstanceSupplier) {
        this.loggerClass = loggerClass;
        this.loggerInstanceSupplier = loggerInstanceSupplier;
    }

    public Class<L> getLoggerClass() {
        return loggerClass;
    }

    public Object resolve(
            final ExtensionContext extensionContext,
            final Class<?> declaringClass,
            final String[] members,
            final ManagedLogger ml) {
        final LogbackConfiguration logbackConfig =
                extensionContext.getStore(NAMESPACE).get(LOGBACK_CONFIG_KEY, LogbackConfiguration.class);

        if (logbackConfig == null) {
            throw new ParameterResolutionException("Failed to locate the extension configuration via the store");
        }

        String loggerName = null;
        String loggerLevel = null;
        if (ml != null) {
            if (ml.name() != null && !ml.name().isBlank()) {
                loggerName = ml.name();
            } else if (ml.loggedClass() != null && !Class.class.equals(ml.loggedClass())) {
                loggerName = ml.loggedClass().getName();
            }

            loggerLevel = ml.logLevel().name();
        }

        if (loggerName == null) {
            if (members != null && members.length > 0) {
                loggerName = qualifiedMemberName(declaringClass, members);
            } else {
                loggerName = declaringClass.getName();
            }
        }

        logbackConfig.configureLogger(loggerName, loggerLevel);
        return loggerInstanceSupplier.apply(loggerName);
    }
}
