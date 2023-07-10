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

import static com.jcovalent.junit.logging.utils.Utils.reflectOnFieldSettingValue;
import static com.jcovalent.junit.logging.utils.Utils.reflectOnFieldsOfType;

import com.jcovalent.junit.logging.ManagedLogger;
import java.lang.reflect.Field;
import java.util.function.Function;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

abstract class LogFieldResolver<L> extends LogResolver<L> implements TestInstancePostProcessor {

    LogFieldResolver(Class<L> loggerClass, Function<String, L> loggerInstanceSupplier) {
        super(loggerClass, loggerInstanceSupplier);
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        final Class<?> testClass = testInstance.getClass();
        processFields(context, testInstance, testClass, testClass.getFields());
        processFields(context, testInstance, testClass, testClass.getDeclaredFields());
    }

    private void processFields(
            final ExtensionContext context, final Object testInstance, final Class<?> testClass, final Field[] fields) {
        reflectOnFieldsOfType(getLoggerClass(), fields, field -> {
            final ManagedLogger ml = field.getAnnotation(ManagedLogger.class);
            reflectOnFieldSettingValue(
                    field, testInstance, () -> resolve(context, testClass, new String[] {field.getName()}, ml));
        });
    }
}
