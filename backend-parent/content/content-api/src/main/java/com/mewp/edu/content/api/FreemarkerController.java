package com.mewp.edu.content.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author mewp
 * @version 1.0
 * @date 2024/2/20 13:50
 */
@Controller
public class FreemarkerController {
    @GetMapping("/testfreemarker")
    public ModelAndView test() {
        ModelAndView modelAndView = new ModelAndView();
        // 设置模版名称
        modelAndView.setViewName("test");
        // 设置模版数据
        modelAndView.addObject("name", "学成在线");
        return modelAndView;
    }
}
