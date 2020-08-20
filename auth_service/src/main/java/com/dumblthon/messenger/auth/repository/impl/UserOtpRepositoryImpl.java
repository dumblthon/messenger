package com.dumblthon.messenger.auth.repository.impl;

import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import static com.dumblthon.messenger.auth.userinfo.Tables.USER_OTP;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.param;

@Repository
public class UserOtpRepositoryImpl implements UserOtpRepository {

    private final NamedParameterJdbcOperations namedJdbcOperations;

    private final DSLContext create = DSL.using(SQLDialect.POSTGRES);

    private final RowMapper<UserOtp> mapper =
            (rs, rowNum) -> new UserOtp(
                rs.getLong(USER_OTP.USER_ID.getName()),
                rs.getString(USER_OTP.DEVICE_ID.getName()),
                rs.getString(USER_OTP.CODE.getName()),
                rs.getDate(USER_OTP.SENT_AT.getName())
            );

    @Autowired
    public UserOtpRepositoryImpl(NamedParameterJdbcOperations namedJdbcOperations) {
        this.namedJdbcOperations = namedJdbcOperations;
    }

    @Override
    public Optional<UserOtp> findByUserIdAndDeviceId(long userId, String deviceId) {
        Param<Long> paramUserId = param("userId", Long.class);
        Param<String> paramDeviceId = param("deviceId", String.class);

        String sql = create.select().from(USER_OTP)
                .where(USER_OTP.USER_ID.eq(paramUserId)
                        .and(USER_OTP.DEVICE_ID.eq(paramDeviceId)))
                .getSQL();

        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue(paramUserId.getName(), userId)
                .addValue(paramDeviceId.getName(), deviceId);

        return Optional.ofNullable(namedJdbcOperations
                .queryForObject(sql, paramSource, mapper));
    }

    @Override
    public UserOtp save(UserOtp otp) {
        Param<Long> paramUserId = param("userId", Long.class);
        Param<String> paramDeviceId = param("deviceId", String.class);
        Param<String> paramCode = param("code", String.class);
        Param<LocalDateTime> paramSentAt = param("sentAt", LocalDateTime.class);

        String sql = create.insertInto(USER_OTP)
                .set(USER_OTP.USER_ID, paramUserId)
                .set(USER_OTP.DEVICE_ID, paramDeviceId)
                .set(USER_OTP.CODE, paramCode)
                .set(USER_OTP.SENT_AT, paramSentAt)
                .onConflictOnConstraint(USER_OTP.getPrimaryKey())
                .doUpdate()
                .set(USER_OTP.CODE, paramCode)
                .set(USER_OTP.SENT_AT, paramSentAt)
                .returningResult(asterisk())
                .getSQL();

        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue(paramUserId.getName(), otp.getUserId())
                .addValue(paramDeviceId.getName(), otp.getDeviceId())
                .addValue(paramCode.getName(), otp.getCode())
                .addValue(paramSentAt.getName(), toLocalDateTime(otp.getSentAt()));

        return namedJdbcOperations.queryForObject(sql, paramSource, mapper);
    }

    // todo force type on jooq generator ?
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }

}
