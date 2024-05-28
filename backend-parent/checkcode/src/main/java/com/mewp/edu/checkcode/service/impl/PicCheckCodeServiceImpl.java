package com.mewp.edu.checkcode.service.impl;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mewp.edu.checkcode.model.dto.CheckCodeParamsDTO;
import com.mewp.edu.checkcode.model.dto.CheckCodeResultDTO;
import com.mewp.edu.checkcode.service.AbstractCheckCodeService;
import com.mewp.edu.common.utils.EncryptUtil;
import com.mewp.edu.common.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/5/28 00:49
 */
@Slf4j
@AllArgsConstructor
@Service("PicCheckCodeService")
public class PicCheckCodeServiceImpl extends AbstractCheckCodeService {
    private DefaultKaptcha kaptcha;

    @Autowired
    @Override
    public void setCheckCodeGenerator(CheckCodeGenerator checkCodeGenerator) {
        this.checkCodeGenerator = checkCodeGenerator;
    }

    @Autowired
    @Override
    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Autowired
    @Qualifier("redisCheckCodeStore")
    @Override
    public void setCheckCodeStore(CheckCodeStore checkCodeStore) {
        this.checkCodeStore = checkCodeStore;
    }

    @Override
    public CheckCodeResultDTO generate(CheckCodeParamsDTO checkCodeParams) {
        GenerateResult generate = generate(checkCodeParams, 4, "checkcode:", 120);
        String key = generate.getKey();
        String code = generate.getCode();
        String pic = createPic(code);
        return new CheckCodeResultDTO(key, pic);
    }

    /**
     * 生成图片验证码
     *
     * @param code 验证码字符串
     * @return Base64编码
     */
    private String createPic(String code) {
        if (StringUtil.isBlank(code)) {
            throw new IllegalArgumentException("验证码字符串不能为空");
        }
        ByteArrayOutputStream os;

        os = new ByteArrayOutputStream();
        String imgBase64Encoder = null;
        try {
            BufferedImage image = kaptcha.createImage(code);
            ImageIO.write(image, "png", os);
            imgBase64Encoder = "data:image/png;base64," + EncryptUtil.encodeBase64(os.toByteArray());
        } catch (IOException e) {
            log.error("生成验证码图片失败: {}", e.getMessage(), e);
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                log.error("关闭ByteArrayOutputStream时发生异常", e);
            }
        }
        return imgBase64Encoder;
    }
}
