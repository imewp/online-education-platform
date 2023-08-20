package com.mewp.edu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.system.model.po.Dictionary;

import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author mewp
 * @since 2023-08-20
 */
public interface DictionaryService extends IService<Dictionary> {

    /**
     * 获取所有数据字典
     *
     * @return 字典列表
     */
    List<Dictionary> queryAll();

    /**
     * 获取单个数据字典
     *
     * @param code 数据字典代码
     * @return 数据字典
     */
    Dictionary getByCode(String code);
}
