package com.mewp.edu.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mewp.edu.content.model.dto.BindTeachPlanMediaDTO;
import com.mewp.edu.content.model.po.TeachplanMedia;

/**
 * <p>
 * 课程计划与媒资关系 服务类
 * </p>
 *
 * @author mewp
 * @since 2023-08-20
 */
public interface TeachplanMediaService extends IService<TeachplanMedia> {

    /**
     * 教学计划绑定媒资
     *
     * @param bindTeachPlanMediaDto 提交信息
     * @return 教学计划绑定结果
     */
    TeachplanMedia associationMedia(BindTeachPlanMediaDTO bindTeachPlanMediaDto);

    /**
     * 教学计划解除绑定
     *
     * @param teachPlanId 课程计划ID
     * @param mediaId 媒资文件ID
     */
    void deleteAssociationMedia(Long teachPlanId, String mediaId);
}
