module com.jcovalent.junit.logging.test {
    opens com.jcovalent.junit.logging.test.extensions to
            org.junit.platform.commons,
            com.jcovalent.junit.logging;
    opens com.jcovalent.junit.logging.test.utils to
            org.junit.platform.commons,
            com.jcovalent.junit.logging;
    opens com.jcovalent.junit.logging.test to
            org.junit.platform.commons,
            com.jcovalent.junit.logging;

    requires ch.qos.logback.core;
    requires ch.qos.logback.classic;
    requires com.jcovalent.junit.logging;
    requires org.junit.jupiter.api;
    requires org.apache.logging.log4j;
    requires org.apache.logging.slf4j;
    requires org.slf4j;
    requires org.assertj.core;
}
