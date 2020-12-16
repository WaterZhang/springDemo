package com.zhangzemiao.www.springdemo.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class SpringLogger {
    static final private String EVENT_ID = "eventId";
    static final private String EVENT_NAME = "eventName";

    private Logger logger;
    private MDCWrapper mdcWrapper;

    static public SpringLogger getLogger(final String name) {
        return new SpringLogger(name);
    }

    static public SpringLogger getLogger(final Class clazz) {
        return new SpringLogger(clazz);
    }

    private SpringLogger(final String name) {
        logger = LoggerFactory.getLogger(name);
        mdcWrapper = new MDCWrapper();
    }

    private SpringLogger(final Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
        mdcWrapper = new MDCWrapper();
    }

    public void debug(final ISystemEvent event, final String msg) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.debug(msg);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void debug(final ISystemEvent event, final String msg, final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.debug(appendEventDataToMsg(msg, eventData));
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void debug(final ISystemEvent event, final String format, final Object... objects) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.debug(format, objects);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void debug(final ISystemEvent event, final String msg, final Throwable throwable) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.debug(msg, throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void debug(final ISystemEvent event,
                      final String msg,
                      final Throwable throwable,
                      final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.debug(appendEventDataToMsg(msg, eventData), throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void error(final ISystemEvent event, final String msg) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.error(msg);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void error(final ISystemEvent event,
                      final String msg,
                      final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.error(appendEventDataToMsg(msg, eventData));
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void error(final ISystemEvent event,
                      final String format,
                      final Object... objects) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.error(format, objects);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void error(final ISystemEvent event,
                      final String msg,
                      final Throwable throwable) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.error(msg, throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void error(final ISystemEvent event,
                      final String msg,
                      final Throwable throwable,
                      final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.error(appendEventDataToMsg(msg, eventData), throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void info(final ISystemEvent event, final String msg) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.info(msg);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void info(final ISystemEvent event,
                     final String msg,
                     final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.info(appendEventDataToMsg(msg, eventData));
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void info(final ISystemEvent event,
                     final String format,
                     final Object... objects) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.info(format, objects);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void info(final ISystemEvent event,
                     final String msg,
                     final Throwable throwable) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.info(msg, throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void info(final ISystemEvent event,
                     final String msg,
                     final Throwable throwable,
                     final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.info(appendEventDataToMsg(msg, eventData), throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void trace(final ISystemEvent event, final String msg) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.trace(msg);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void trace(final ISystemEvent event, final String msg, final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.trace(appendEventDataToMsg(msg, eventData));
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void trace(final ISystemEvent event, final String format, final Object... objects) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.trace(format, objects);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void trace(final ISystemEvent event, final String msg, final Throwable throwable) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.trace(msg, throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void trace(final ISystemEvent event,
                      final  String msg,
                      final Throwable throwable,
                      final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.trace(appendEventDataToMsg(msg, eventData), throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void warn(final ISystemEvent event, final String msg) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.warn(msg);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void warn(final ISystemEvent event, final String msg, final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.warn(appendEventDataToMsg(msg, eventData));
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void warn(final ISystemEvent event, final String format, final Object... objects) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.warn(format, objects);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void warn(final ISystemEvent event, final String msg, final Throwable throwable) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.warn(msg, throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public void warn(final ISystemEvent event, final String msg, final Throwable throwable, final SystemEventData eventData) {
        Validate.isTrue(event != null, "System event should not be null");
        try {
            saveSystemEventInMDC(event);
            logger.warn(appendEventDataToMsg(msg, eventData), throwable);
        } finally {
            clearSystemEventFromMDC();
        }
    }

    public String getName() {
        return logger.getName();
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    private void saveSystemEventInMDC(final ISystemEvent event) {
        mdcWrapper.put(EVENT_ID, event.getId());
        mdcWrapper.put(EVENT_NAME, event.getName());
    }

    private void clearSystemEventFromMDC() {
        mdcWrapper.remove(EVENT_ID);
        mdcWrapper.remove(EVENT_NAME);
    }

    private String appendEventDataToMsg(final String msg, final SystemEventData eventData) {
        final Map<String, Object> data = eventData.getData();
        final List<String> eventEntries = new ArrayList<>();

        for(final Map.Entry<String, Object> dataEntry : data.entrySet()){
            eventEntries.add(dataEntry.getKey() + '=' + Objects.toString(dataEntry.getValue()));
        }
        return msg + ' ' + StringUtils.join(eventEntries, ',');
    }


    static /* package */ class MDCWrapper {

        public void put(final String key, final Object value) {
            MDC.put(key, Objects.toString(value));
        }

        public void remove(final String key) {
            MDC.remove(key);
        }
    }
}
