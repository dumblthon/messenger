package com.dumblthon.messenger.auth.repository.converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Converts between Postgres "timestamp without time zone" (JOOQ LocalDateTime) and Java java.util.Date
 */
public class LocalDateTimeToDateConverter implements org.jooq.Converter<LocalDateTime, Date> {

    @Override
    public Date from(LocalDateTime timestamp) {
        return Date.from(timestamp.toInstant(ZoneOffset.UTC));
    }

    @Override
    public LocalDateTime to(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }

    @Override
    public Class<LocalDateTime> fromType() {
        return LocalDateTime.class;
    }

    @Override
    public Class<Date> toType() {
        return Date.class;
    }

}
