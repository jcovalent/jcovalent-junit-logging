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
package com.jcovalent.junit.logging;

import com.jcovalent.junit.logging.extensions.Log4jFieldResolver;
import com.jcovalent.junit.logging.extensions.Log4jParameterResolver;
import com.jcovalent.junit.logging.extensions.OutputResolverExtension;
import com.jcovalent.junit.logging.extensions.Slf4jFieldResolver;
import com.jcovalent.junit.logging.extensions.Slf4jParameterResolver;
import com.jcovalent.junit.logging.extensions.TestSuiteLoggingExtension;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.event.Level;

@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({
    TestSuiteLoggingExtension.class,
    Log4jFieldResolver.class,
    Slf4jFieldResolver.class,
    Log4jParameterResolver.class,
    Slf4jParameterResolver.class,
    OutputResolverExtension.class
})
public @interface JCovalentLoggingSupport {
    Level rootLogLevel() default Level.TRACE;
}
