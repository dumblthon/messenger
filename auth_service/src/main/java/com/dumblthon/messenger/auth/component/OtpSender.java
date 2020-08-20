package com.dumblthon.messenger.auth.component;

import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;

/**
 * Интерфейс для отправки одноразового пароля пользователю
 */
public interface OtpSender {

    /**
     * @return уникальный идентификатор реализации
     */
    String getId();

    /**
     * Выполняет отправку одноразового пароля пользователю
     * @param userInfo информация о пользователя
     * @param userOtp информация об одноразовом пароле
     */
    void send(User userInfo, UserOtp userOtp);

}
