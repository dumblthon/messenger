package com.dumblthon.messenger.auth.repository.impl;

import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.repository.NamedParamJdbcOps;
import com.dumblthon.messenger.auth.repository.UserOtpRepository;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

import static com.dumblthon.messenger.auth.userinfo.Tables.USER_OTP;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.param;

@Repository
public class UserOtpRepositoryImpl implements UserOtpRepository, NamedParamJdbcOps<UserOtp> {

    private final NamedParameterJdbcOperations namedJdbcOperations;

    private final DSLContext create = DSL.using(SQLDialect.POSTGRES);

    private final RowMapper<UserOtp> mapper =
            (rs, rowNum) -> new UserOtp(
                rs.getLong(USER_OTP.USER_ID.getName()),
                rs.getString(USER_OTP.DEVICE_ID.getName()),
                rs.getString(USER_OTP.CODE.getName()),
                rs.getTimestamp(USER_OTP.SENT_AT.getName())
            );

    @Autowired
    public UserOtpRepositoryImpl(NamedParameterJdbcOperations namedJdbcOperations) {
        this.namedJdbcOperations = namedJdbcOperations;
    }

    public NamedParameterJdbcOperations getNamedParamJdbcOperations() {
        return namedJdbcOperations;
    }

    @Override
    public RowMapper<UserOtp> getMapper() {
        return mapper;
    }

    @Override
    public Optional<UserOtp> findByUserIdAndDeviceId(long userId, String deviceId) {
        Param<Long> paramUserId = param("userId", userId);
        Param<String> paramDeviceId = param("deviceId", deviceId);

        Query query = create.select().from(USER_OTP)
                .where(USER_OTP.USER_ID.eq(paramUserId)
                        .and(USER_OTP.DEVICE_ID.eq(paramDeviceId)));

        return queryForOptionalObject(query);
    }

    @Override
    public UserOtp save(UserOtp otp) {
        Param<Long> paramUserId = param("userId", otp.getUserId());
        Param<String> paramDeviceId = param("deviceId", otp.getDeviceId());
        Param<String> paramCode = param("code", otp.getCode());
        Param<Date> paramSentAt = param("sentAt", otp.getSentAt());

        Query query = create.insertInto(USER_OTP)
                .set(USER_OTP.USER_ID, paramUserId)
                .set(USER_OTP.DEVICE_ID, paramDeviceId)
                .set(USER_OTP.CODE, paramCode)
                .set(USER_OTP.SENT_AT, paramSentAt)
                .onConflictOnConstraint(USER_OTP.getPrimaryKey())
                .doUpdate()
                .set(USER_OTP.CODE, paramCode)
                .set(USER_OTP.SENT_AT, paramSentAt)
                .returningResult(asterisk());

        return queryForObject(query);
    }

}
