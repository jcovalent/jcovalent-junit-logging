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

import com.jcovalent.junit.logging.LoggingOutput;
import java.lang.reflect.Field;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class OutputResolverExtension extends OutputResolver implements TestInstancePostProcessor, ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final Class<?> paramType = parameterContext.getParameter().getType();
        return LoggingOutput.class.equals(paramType) || paramType.isAssignableFrom(LoggingOutput.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return resolve(
                extensionContext,
                parameterContext.getDeclaringExecutable().getDeclaringClass(),
                parameterContext.getParameter().getDeclaringExecutable());
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        final Class<?> testClass = testInstance.getClass();
        processFields(context, testInstance, testClass, testClass.getFields());
        processFields(context, testInstance, testClass, testClass.getDeclaredFields());
    }

    private void processFields(
            final ExtensionContext context, final Object testInstance, final Class<?> testClass, final Field[] fields) {
        reflectOnFieldsOfType(LoggingOutput.class, fields, field -> {
            reflectOnFieldSettingValue(field, testInstance, () -> resolve(context, testClass, field));
        });
    }
}
