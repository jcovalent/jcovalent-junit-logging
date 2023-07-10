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

import com.jcovalent.junit.logging.JCovalentLoggingSupport;
import com.jcovalent.junit.logging.LogPattern;
import com.jcovalent.junit.logging.logback.LogbackConfiguration;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Optional;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

public class TestSuiteLoggingExtension implements BeforeAllCallback, BeforeEachCallback {
    protected static final Namespace NAMESPACE = Namespace.create(TestSuiteLoggingExtension.class.getPackageName());

    protected static final String LOGBACK_CONFIG_KEY = "logback";

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        final Class<?> testClass = extractTestClass(context);

        final LogbackConfiguration logbackConfig = context.getStore(NAMESPACE)
                .getOrComputeIfAbsent(LOGBACK_CONFIG_KEY, k -> new LogbackConfiguration(), LogbackConfiguration.class);

        final JCovalentLoggingSupport annotation = testClass.getAnnotation(JCovalentLoggingSupport.class);

        if (annotation == null) {
            throw new ExtensionConfigurationException(
                    "Failed to find the JCovalentLoggingSupport annotation on the test suite");
        }

        logbackConfig.configureForSuite(annotation.rootLogLevel().name(), extractLogPattern(context));
    }

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        final Method testMethod = extractTestMethod(context);

        final LogbackConfiguration logbackConfig =
                context.getStore(NAMESPACE).get(LOGBACK_CONFIG_KEY, LogbackConfiguration.class);

        if (logbackConfig == null) {
            throw new ExtensionConfigurationException("Failed to locate the extension configuration via the store");
        }

        logbackConfig.configureForTest(extractLogPattern(context));
    }

    private static Method extractTestMethod(final ExtensionContext context) {
        final Optional<Method> testMethod = context.getTestMethod();

        if (testMethod.isEmpty()) {
            throw new ExtensionConfigurationException("The extension context failed to provided the test method");
        }

        return testMethod.get();
    }

    private static Class<?> extractTestClass(final ExtensionContext context) {
        final Optional<Class<?>> testClass = context.getTestClass();

        if (testClass.isEmpty()) {
            throw new ExtensionConfigurationException("The extension context failed to provided the test class");
        }

        return testClass.get();
    }

    private static String extractLogPattern(final ExtensionContext context) {
        final Optional<AnnotatedElement> element = context.getElement();
        if (element.isPresent()) {
            final AnnotatedElement ae = element.get();

            LogPattern pattern = ae.getAnnotation(LogPattern.class);
            if (pattern != null) {
                return pattern.value();
            }
        }

        return null;
    }
}
