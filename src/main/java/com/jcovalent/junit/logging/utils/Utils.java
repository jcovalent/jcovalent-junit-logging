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

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.platform.commons.support.ModifierSupport;

public final class Utils {

    private Utils() {}

    public static String qualifiedMemberName(final Class<?> declaringClass, final String... members) {
        final StringBuilder builder = new StringBuilder();
        builder.append(declaringClass.getName());

        if (members != null && members.length > 0) {
            for (String m : members) {
                builder.append(".").append(m);
            }
        }

        return builder.toString();
    }

    public static void reflectOnFieldsOfType(
            final Class<?> fieldType, final Field[] fields, final Consumer<Field> consumer) {
        for (Field field : fields) {
            if (!isSupportedField(fieldType, field)) {
                continue;
            }

            consumer.accept(field);
        }
    }

    @SuppressWarnings("java:S3011")
    public static void reflectOnFieldSettingValue(
            final Field field, final Object instance, final Supplier<Object> valueSupplier) {
        final Object invocationTarget = ModifierSupport.isNotStatic(field) ? instance : null;
        try {
            final Object currentValue = field.get(invocationTarget);

            if (currentValue == null) {
                field.set(invocationTarget, valueSupplier.get());
            }
        } catch (IllegalAccessException ex) {
            throw new ExtensionConfigurationException("Failed to update the field value", ex);
        }
    }

    private static boolean isSupportedField(final Class<?> fieldType, final Field field) {
        if (ModifierSupport.isFinal(field)
                || (!fieldType.equals(field.getType()) && !field.getType().isAssignableFrom(fieldType))) {
            return false;
        }

        return ModifierSupport.isPublic(field) || field.trySetAccessible();
    }
}
