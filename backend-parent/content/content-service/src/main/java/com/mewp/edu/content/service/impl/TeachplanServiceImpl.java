package com.mewp.edu.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.content.mapper.TeachplanMapper;
import com.mewp.edu.content.mapper.TeachplanMediaMapper;
import com.mewp.edu.content.model.converter.PoDtoConvertMapper;
import com.mewp.edu.content.model.dto.AddOrUpdateTeachPlanDTO;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.model.po.Teachplan;
import com.mewp.edu.content.model.po.TeachplanMedia;
import com.mewp.edu.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Resource
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachPlanDTO> findTeachPlanTree(Long courseId) {
        return baseMapper.selectTreeNodes(courseId);
    }

    @Override
    public void saveOrUpdatePlan(AddOrUpdateTeachPlanDTO teacherPlanDTO) {
        Long planId = teacherPlanDTO.getId();
        Teachplan teachplan = PoDtoConvertMapper.INSTANCE.teachPlanDto2Po(teacherPlanDTO);
        //计划id为空，则为新增操作
        if (Objects.isNull(planId)) {
            //取出同父同级别的课程计划数量
            int count = getTeachPlanCount(teacherPlanDTO.getCourseId(), teacherPlanDTO.getParentid());
            //设置排序号
            teachplan.setOrderby(count + 1);
            baseMapper.insert(teachplan);
        } else {
            //计划id不为空，则为修改操作
            Teachplan teachPlanTemp = baseMapper.selectById(planId);
            if (Objects.isNull(teachPlanTemp)) {
                CustomException.cast("该课程不存在");
            }
            baseMapper.updateById(teachplan);
        }
    }

    @Transactional
    @Override
    public void deleteTeachPlan(Long id) {
        Teachplan teachplan = findTeachPlan(id);
        if (Objects.isNull(teachplan)) {
            CustomException.cast("未找到对应的课程计划信息");
        }
        Long parentId = teachplan.getParentid();
        // 判断当前课程计划是章还是节
        Integer grade = teachplan.getGrade();
        // 第一级大章节
        if (parentId == 0 && grade == 1) {
            // 查询当前课程计划下是否有小节
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid, id);
            Integer count = baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                CustomException.cast("课程计划信息还有子级信息，无法操作");
            }
            baseMapper.deleteById(id);
            return;
        }
        // 第二级小章节
        if (grade == 2) {
            // 课程计划为节，删除该小节课程计划
            baseMapper.deleteById(id);
            LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachplanMedia::getTeachplanId, id);
            // 删除媒资信息中对应的teachplanId的数据
            teachplanMediaMapper.delete(queryWrapper);
        }
    }

    @Transactional
    @Override
    public void moveTeachPlan(String moveType, Long id) {
        Teachplan teachPlan = findTeachPlan(id);
        Integer grade = teachPlan.getGrade();
        Integer orderBy = teachPlan.getOrderby();
        Long courseId = teachPlan.getCourseId();
        Long parentId = teachPlan.getParentid();
        //找到新位置的课程计划
        Teachplan moveTeachPlan = findMoveTeachPlan(moveType, grade, orderBy, courseId, parentId);
        //交换位置
        swapTeachPlan(teachPlan, moveTeachPlan);
    }

    /**
     * 交换顺序
     *
     * @param teachPlan     旧顺序
     * @param moveTeachPlan 新顺序
     */
    private void swapTeachPlan(Teachplan teachPlan, Teachplan moveTeachPlan) {
        Integer oldOrderBy = teachPlan.getOrderby();
        Integer newOrderBy = moveTeachPlan.getOrderby();
        teachPlan.setOrderby(newOrderBy);
        moveTeachPlan.setOrderby(oldOrderBy);
        baseMapper.updateById(teachPlan);
        baseMapper.updateById(moveTeachPlan);
    }

    /**
     * 找到需要移动到位置的课程计划
     *
     * @param moveType 移动类型
     * @param grade    层级
     * @param orderBy  排序等级
     * @param courseId 课程ID
     * @param parentId 父节点
     * @return 课程计划
     */
    private Teachplan findMoveTeachPlan(String moveType, Integer grade, Integer orderBy,
                                        Long courseId, Long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getGrade, grade).eq(Teachplan::getCourseId, courseId)
                .eq(Teachplan::getParentid, parentId);
        if ("moveup".equals(moveType)) {
            queryWrapper.lt(Teachplan::getOrderby, orderBy);
        } else if ("movedown".equals(moveType)) {
            queryWrapper.gt(Teachplan::getOrderby, orderBy);
        }
        queryWrapper.orderByDesc(Teachplan::getOrderby).last("limit 1");
        Teachplan teachplan = baseMapper.selectOne(queryWrapper);
        if (Objects.isNull(teachplan)) {
            CustomException.cast("已经到头了，不能再移动了");
        }
        return teachplan;
    }

    /**
     * 根据计划ID查询数据，查询不到则抛出异常
     *
     * @param id 计划ID
     * @return 课程计划信息
     */
    private Teachplan findTeachPlan(Long id) {
        Teachplan teachplan = baseMapper.selectById(id);
        if (Objects.isNull(teachplan)) {
            CustomException.cast("课程计划不存在");
        }
        return teachplan;
    }

    /**
     * 获取最新的排序号
     *
     * @param courseId 课程ID
     * @param parentId 父课程计划ID
     * @return 最新排序号
     */
    private int getTeachPlanCount(Long courseId, long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId)
                .eq(Teachplan::getParentid, parentId);
        return baseMapper.selectCount(queryWrapper);
    }
}
