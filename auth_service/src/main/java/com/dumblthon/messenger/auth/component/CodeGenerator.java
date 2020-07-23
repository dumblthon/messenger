package com.dumblthon.messenger.auth.component;

public interface CodeGenerator {

    /**
     * Генерирует одноразовый пароль для аутентификации
     * @param length кол-во символов в пароле
     * @return одноразовый пароль, указанной длины
     */
    String generate(int length);

}
