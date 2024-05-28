package com.mewp.edu.checkcode.service.impl;

import com.mewp.edu.checkcode.service.CheckCodeService;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 数字字母生成器
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/28 00:39
 */
@Component
public class NumberLetterCheckCodeGenerator implements CheckCodeService.CheckCodeGenerator {
    @Override
    public String generate(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(str.length());
            sb.append(str.charAt(index));
        }
        return sb.toString();
    }
}
