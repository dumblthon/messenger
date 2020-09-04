package com.dumblthon.messenger.auth.component;

import java.security.InvalidKeyException;

// message authentication code
public interface MacGenerator {

    /**
     * Применяет MAC алгоритм к входному сообщению
     * @param message входные данные (одноразовый пароль)
     * @param secret секретный ключ пользователя
     * @return набор символов (кек) для проверки подлинности
     */
    String generate(String message, String secret) throws InvalidKeyException;

}
