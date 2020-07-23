package com.dumblthon.messenger.auth.service;

import com.dumblthon.messenger.auth.component.OtpSender;

public interface OtpSenderProvider {

    /**
     * Выбирает реализацию для отправки одноразового пароля
     * @param userId идентификатор пользователя
     * @param deviceId идентификатор устройства
     * @return реализацию интерфейса отправителя
     */
    OtpSender get(Long userId, String deviceId);

}
