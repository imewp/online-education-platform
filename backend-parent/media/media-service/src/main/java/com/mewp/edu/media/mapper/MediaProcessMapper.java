package com.mewp.edu.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mewp.edu.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mewp
 */
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {
    /**
     * 根据分片参数获取待处理任务
     *
     * @param shardTotal 分片总数
     * @param shardIndex 分片序号
     * @param count      任务数
     * @return 任务列表
     */
    List<MediaProcess> selectListByShareIndex(@Param("shardTotal") int shardTotal,
                                              @Param("shardIndex") int shardIndex,
                                              @Param("count") int count);

    /**
     * 开启一个任务
     *
     * @param id 任务id
     * @return 更新记录数
     */
    int startTask(@Param("id") long id);
}
