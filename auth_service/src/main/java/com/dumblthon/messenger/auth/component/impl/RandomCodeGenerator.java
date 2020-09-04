package com.dumblthon.messenger.auth.component.impl;

import com.dumblthon.messenger.auth.component.CodeGenerator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Random;

/**
 * Реализации интерфейса для генерации одноразового пароля
 * длиной 6-8 символов с помощью корейского рандома
 */
@Component
public class RandomCodeGenerator implements CodeGenerator {

    public static final int MIN_LENGTH = 6;
    public static final int MAX_LENGTH = 8;

    @Override
    public String generate(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH)
            throw new IllegalArgumentException("Кол-во символов должно быть от " + MIN_LENGTH + " до " + MAX_LENGTH);

        int lowerBound = bound(length);
        int upperBound = 9 * bound(length);

        int number = lowerBound + new Random().nextInt(upperBound);
        return padLeft(Integer.toString(number), length, '0');
    }

    private String padLeft(String str, int length, char c) {
        int count = length - str.length();
        char[] chars = new char[count];
        Arrays.fill(chars, c);
        return String.valueOf(chars) + str;
    }

    private int bound(int length) {
        return length == 1 ? 1 : bound(length - 1) * 10;
    }

}
