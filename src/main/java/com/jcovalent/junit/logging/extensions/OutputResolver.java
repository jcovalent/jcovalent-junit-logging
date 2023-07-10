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

import com.jcovalent.junit.logging.logback.LogbackConfiguration;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;

abstract class OutputResolver {
    public Object resolve(final ExtensionContext extensionContext, final Class<?> declaringClass, final Member member) {
        final LogbackConfiguration logbackConfig =
                extensionContext.getStore(NAMESPACE).get(LOGBACK_CONFIG_KEY, LogbackConfiguration.class);

        if (logbackConfig == null) {
            throw new ParameterResolutionException("Failed to locate the extension configuration via the store");
        }

        if (member instanceof Method) {
            return logbackConfig.getTestStorage();
        }

        return logbackConfig.getSuiteStorage();
    }
}
