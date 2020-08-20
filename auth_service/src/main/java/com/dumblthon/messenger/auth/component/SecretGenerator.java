package com.dumblthon.messenger.auth.component;

public interface SecretGenerator {

    /**
     * Генерирует пользовательский ключ для шифрования одноразовых паролей
     * @param length кол-во символов в ключе
     * @return случайный пользовательский ключ
     */
    String generate(int length);

}
