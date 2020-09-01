package com.dumblthon.messenger.auth.repository;

import org.jooq.Param;
import org.jooq.Query;
import org.jooq.conf.ParamType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface NamedParamJdbcOps<T> {

    NamedParameterJdbcOperations getNamedParamJdbcOperations();

    RowMapper<T> getMapper();

    default T queryForObject(Query query) {
        return queryForObject(query, getMapper());
    }

    default <R> R queryForObject(Query query, RowMapper<R> mapper) {
        return getNamedParamJdbcOperations()
                .queryForObject(
                        query.getSQL(ParamType.NAMED),
                        toParamSource(query), mapper);
    }

    default Optional<T> queryForOptionalObject(Query query) {
        return queryForOptionalObject(query, getMapper());
    }

    default <R> Optional<R> queryForOptionalObject(Query query, RowMapper<R> mapper) {
        try {
            R result = queryForObject(query, mapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    default Map<String, Object> toParamSource(Query query) {
        return query.getParams().values().stream()
                .collect(Collectors.toMap(Param::getName, Param::getValue));
    }

}
