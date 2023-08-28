package com.mewp.edu.system.api;

import com.mewp.edu.system.model.po.Dictionary;
import com.mewp.edu.system.service.DictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Api(value = "DictionaryController", tags = "数据字典管理")
@RestController
@RequestMapping("dictionary")
public class DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    @ApiOperation("获取所有数据字典")
    @GetMapping("/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @ApiOperation("获取单个数据字典")
    @GetMapping("/code/{code}")
    public Dictionary getByCode(@PathVariable String code) {
        return dictionaryService.getByCode(code);
    }

}
