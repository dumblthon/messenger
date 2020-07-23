package com.dumblthon.messenger.auth.service.impl;

import com.dumblthon.messenger.auth.component.OtpSender;
import com.dumblthon.messenger.auth.service.OtpSenderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OtpSenderProviderStub implements OtpSenderProvider {

    private final OtpSender smsSender;
    private final OtpSender messengerSender;

    @Autowired
    public OtpSenderProviderStub(@Qualifier("smsSenderStub") OtpSender smsSender,
                                 @Qualifier("messengerSenderStub") OtpSender messengerSender) {
        this.smsSender = smsSender;
        this.messengerSender = messengerSender;
    }

    /**
     * @param userId идентификатор пользователя
     * @param deviceId идентификатор устройства
     * @return реализация, выполняющая отправку одноразового пароля в мессенджер,
     *  если пользователь существует, в противном случае - отправку SMS
     *
     *  Чтобы это перестало быть stub'ом, необходимо отслеживать активные сессии
     */
    @Override
    public OtpSender get(Long userId, String deviceId) {
        return userId != null ? // user exists
            messengerSender : smsSender;
    }

}
