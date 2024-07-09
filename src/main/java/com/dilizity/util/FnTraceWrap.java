package com.dilizity.util;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FnTraceWrap implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(FnTraceWrap.class);

    private String methodName;
    private String className;
    private Instant startTime;
    private boolean disposed = false;

    public FnTraceWrap() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 2) {
            StackTraceElement element = stackTrace[2];
            this.className = element.getClassName();
            this.methodName = element.getMethodName();
            this.startTime = Instant.now();
            //logger.info( "{0}.{1} - Begin", new Object[]{className, methodName});
            logger.info("{}.{} - Begin ()", className, methodName);

        }
    }

    public FnTraceWrap(Object... args) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 2) {
            StackTraceElement element = stackTrace[2];
            this.className = element.getClassName();
            this.methodName = element.getMethodName();
            String parameters = String.join("|", toStringArray(args));
            this.startTime = Instant.now();
            logger.info("{}.{} - Begin ({})", className, methodName, parameters);
        }
    }

    public void traceMessage(String format, Object... args) {
        String message = String.format(format, args);
        logger.info(message);
    }

    @Override
    public void close() {
        if (!disposed) {
            disposed = true;
            Instant endTime = Instant.now();
            Duration duration = Duration.between(startTime, endTime);
            logger.info("{}.{} - End - Execution Time: {} ms", className, methodName, duration.toMillis());
        }
    }

    private String[] toStringArray(Object[] args) {
        String[] strArray = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            strArray[i] = args[i].toString();
        }
        return strArray;
    }
}
