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

import com.jcovalent.junit.logging.ManagedLogger;
import java.util.Optional;
import java.util.function.Function;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

abstract class LogParameterResolver<L> extends LogResolver<L> implements ParameterResolver {
    LogParameterResolver(final Class<L> loggerClass, final Function<String, L> loggerInstanceSupplier) {
        super(loggerClass, loggerInstanceSupplier);
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final Class<?> paramType = parameterContext.getParameter().getType();
        return getLoggerClass().equals(paramType) || paramType.isAssignableFrom(getLoggerClass());
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {

        final Optional<ManagedLogger> annotation = parameterContext.findAnnotation(ManagedLogger.class);
        return resolve(
                extensionContext,
                parameterContext.getDeclaringExecutable().getDeclaringClass(),
                new String[] {
                    parameterContext.getDeclaringExecutable().getName(),
                    parameterContext.getParameter().getName()
                },
                annotation.orElse(null));
    }
}
