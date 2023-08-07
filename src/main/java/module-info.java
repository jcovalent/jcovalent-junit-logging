import ch.qos.logback.classic.spi.Configurator;
import com.jcovalent.junit.logging.extensions.Log4jFieldResolver;
import com.jcovalent.junit.logging.extensions.Log4jParameterResolver;
import com.jcovalent.junit.logging.extensions.OutputResolverExtension;
import com.jcovalent.junit.logging.extensions.Slf4jFieldResolver;
import com.jcovalent.junit.logging.extensions.Slf4jParameterResolver;
import com.jcovalent.junit.logging.extensions.TestSuiteLoggingExtension;
import com.jcovalent.junit.logging.logback.DefaultConfigurator;
import org.junit.jupiter.api.extension.Extension;

module com.jcovalent.junit.logging {
    exports com.jcovalent.junit.logging;
    exports com.jcovalent.junit.logging.assertj;

    opens com.jcovalent.junit.logging.extensions to
            org.junit.platform.commons;

    requires org.junit.jupiter.api;
    requires org.apache.logging.log4j;
    requires org.slf4j;
    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires org.assertj.core;

    provides Extension with
            TestSuiteLoggingExtension,
            Slf4jFieldResolver,
            Log4jFieldResolver,
            Slf4jParameterResolver,
            Log4jParameterResolver,
            OutputResolverExtension;
    provides Configurator with
            DefaultConfigurator;
}
