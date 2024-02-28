package com.mewp.edu.content.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mewp.edu.common.exception.CommonError;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.common.utils.StringUtil;
import com.mewp.edu.content.config.MultipartSupportConfig;
import com.mewp.edu.content.feignclient.MediaServiceClient;
import com.mewp.edu.content.mapper.CourseBaseMapper;
import com.mewp.edu.content.mapper.CourseMarketMapper;
import com.mewp.edu.content.mapper.CoursePublishMapper;
import com.mewp.edu.content.mapper.CoursePublishPreMapper;
import com.mewp.edu.content.model.converter.PoDtoConvertMapper;
import com.mewp.edu.content.model.dto.CourseBaseInfoDTO;
import com.mewp.edu.content.model.dto.CoursePreviewDTO;
import com.mewp.edu.content.model.dto.TeachPlanDTO;
import com.mewp.edu.content.model.po.CourseBase;
import com.mewp.edu.content.model.po.CourseMarket;
import com.mewp.edu.content.model.po.CoursePublish;
import com.mewp.edu.content.model.po.CoursePublishPre;
import com.mewp.edu.content.service.CourseBaseService;
import com.mewp.edu.content.service.CoursePublishService;
import com.mewp.edu.content.service.TeachplanService;
import com.mewp.edu.messagesdk.model.po.MqMessage;
import com.mewp.edu.messagesdk.service.MqMessageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 课程发布 服务实现类
 * </p>
 *
 * @author mewp
 */
@Slf4j
@Service
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {
    private final CourseBaseMapper courseBaseMapper;
    private final CourseBaseService courseBaseService;
    private final TeachplanService teachplanService;
    private final CourseMarketMapper courseMarketMapper;
    private final CoursePublishPreMapper coursePublishPreMapper;
    private final CoursePublishMapper coursePublishMapper;

    private final MqMessageService mqMessageService;

    @Resource
    private MediaServiceClient mediaServiceClient;

    public CoursePublishServiceImpl(CourseBaseService courseBaseService, TeachplanService teachplanService,
                                    CourseMarketMapper courseMarketMapper, CoursePublishPreMapper coursePublishPreMapper,
                                    CourseBaseMapper courseBaseMapper, CoursePublishMapper coursePublishMapper,
                                    MqMessageService mqMessageService) {
        this.courseBaseMapper = courseBaseMapper;
        this.courseBaseService = courseBaseService;
        this.teachplanService = teachplanService;
        this.courseMarketMapper = courseMarketMapper;
        this.coursePublishPreMapper = coursePublishPreMapper;
        this.coursePublishMapper = coursePublishMapper;
        this.mqMessageService = mqMessageService;
    }

    @Override
    public CoursePreviewDTO getCoursePreviewInfo(Long courseId) {
        CoursePreviewDTO coursePreviewDto = new CoursePreviewDTO();
        // 根据课程ID查询 课程基本信息、营销信息
        List<TeachPlanDTO> teachPlanTree = teachplanService.findTeachPlanTree(courseId);
        // 根据课程ID，查询课程计划
        CourseBaseInfoDTO courseBaseInfoDto = courseBaseService.queryCourseBaseInfo(courseId);
        // 封装返回
        coursePreviewDto.setCourseBase(courseBaseInfoDto);
        coursePreviewDto.setTeachPlans(teachPlanTree);
        return coursePreviewDto;
    }

    @Transactional
    @Override
    public void commitAudit(Long companyId, Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        // ==================== 约束 ====================
        // 已提交审核的课程允许提交审核
        String auditStatus = courseBase.getAuditStatus();
        if ("202003".equals(auditStatus)) {
            CustomException.cast("当前为等待审核状态，审核完成后可以再次提交");
        }
        // 本机构只允许提交本机构的课程
        if (!courseBase.getCompanyId().equals(companyId)) {
            CustomException.cast("不允许提交其它机构的课程");
        }
        // 没有上传图片不允许提交审核
        if (StringUtil.isBlank(courseBase.getPic())) {
            CustomException.cast("提交失败，请上传课程图片");
        }
        // 没有添加课程计划不允许提交审核
        List<TeachPlanDTO> teachPlanTree = teachplanService.findTeachPlanTree(courseId);
        if (CollectionUtils.isEmpty(teachPlanTree)) {
            CustomException.cast("提交失败，还没有添加课程计划");
        }

        // ==================== 信息存储 ====================
        // 课程基本信息+部分营销信息
        CourseBaseInfoDTO courseBaseInfo = courseBaseService.queryCourseBaseInfo(courseId);
        CoursePublishPre coursePublishPre = PoDtoConvertMapper.INSTANCE.courseBaseDto2CoursePublishPrePo(courseBaseInfo);
        //将课程营销信息json数据放入课程预发布表
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        String courseMarketJson = JSON.toJSONString(courseMarket);
        coursePublishPre.setMarket(courseMarketJson);
        //将课程计划信息json数据放入课程预发布表
        String teachPlanTreeJson = JSON.toJSONString(teachPlanTree);
        coursePublishPre.setTeachplan(teachPlanTreeJson);
        // 设置预发布记录状态，已提交
        coursePublishPre.setStatus("202003");
        // 教学机构id
        coursePublishPre.setCompanyId(companyId);

        CoursePublishPre coursePublishPreUpdate = coursePublishPreMapper.selectById(courseId);
        // 不存在，则新增
        if (Objects.isNull(coursePublishPreUpdate)) {
            coursePublishPreMapper.insert(coursePublishPre);
        } else {
            // 存在，则更新
            coursePublishPreMapper.updateById(coursePublishPre);
        }
        // 更新课程基本表的审核状态
        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);
    }

    @Transactional
    @Override
    public void publish(Long companyId, Long courseId) {
        // ==================== 约束校验 ====================
        // 查询课程预发布
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (Objects.isNull(coursePublishPre)) {
            CustomException.cast("请先提交课程审核，审核通过才可以发布");
        }
        // 本机构只允许提交本机构的课程
        if (!coursePublishPre.getCompanyId().equals(companyId)) {
            CustomException.cast("不允许发布其它机构的课程");
        }
        // 课程审核状态
        String status = coursePublishPre.getStatus();
        if (!"202004".equals(status)) {
            CustomException.cast("操作失败，课程审核通过后才可以发布");
        }

        // =================== 业务处理 ====================
        // 保存课程发布消息
        saveCoursePublish(courseId, coursePublishPre);
        // 保存消息表
        saveCoursePublishMessage(courseId);
        // 删除课程预发布表对应记录
        coursePublishPreMapper.deleteById(courseId);
    }

    @Override
    public File generateCourseHtml(Long courseId) {
        // 静态化文件
        File file = null;

        try {
            // 配置Freemarker
            Configuration configuration = new Configuration(Configuration.getVersion());
            String classPath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
            configuration.setDirectoryForTemplateLoading(new File(classPath + "/templates/"));
            // 设置字符编码
            configuration.setDefaultEncoding("utf-8");
            // 加载模版，并指定模版文件名
            Template template = configuration.getTemplate("course_template.ftl");
            CoursePreviewDTO coursePreviewInfo = this.getCoursePreviewInfo(courseId);
            Map<String, Object> map = new HashMap<>();
            map.put("model", coursePreviewInfo);
            // 生成HTML页面，并转成字符串
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            InputStream is = IOUtils.toInputStream(content);
            // 创建静态化文件
            file = File.createTempFile("course", ".html");
            log.debug("课程静态化，生成静态化文件：{}", file.getAbsoluteFile());
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(is, fos);
        } catch (Exception e) {
            log.error("课程静态化异常：{}", e.getMessage(), e);
            CustomException.cast("课程静态化异常");
        }
        return file;
    }

    @Override
    public void uploadCourseHtml(Long courseId, File file) {
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        String content = mediaServiceClient.uploadFile(multipartFile, "course", courseId + ".html");
        if (StringUtil.isBlank(content)) {
            CustomException.cast("上传静态文件异常");
        }
    }

    /**
     * 保存课程发布消息
     *
     * @param courseId         课程ID
     * @param coursePublishPre 课程预发布信息
     */
    private void saveCoursePublish(Long courseId, CoursePublishPre coursePublishPre) {
        if (Objects.isNull(coursePublishPre)) {
            CustomException.cast("课程预发布信息为空");
        }

        CoursePublish coursePublish = PoDtoConvertMapper.INSTANCE.coursePublishPrePo2CoursePublishPo(coursePublishPre);
        // 已发布状态
        coursePublish.setStatus("203002");
        CoursePublish coursePublishUpdate = coursePublishMapper.selectById(courseId);
        // 保存或更新课程发布信息
        if (Objects.isNull(coursePublishUpdate)) {
            coursePublishMapper.insert(coursePublish);
        } else {
            coursePublishMapper.updateById(coursePublish);
        }
        // 更新课程基本信息的发布状态
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        // 已发布状态
        courseBase.setStatus("203002");
        courseBaseMapper.updateById(courseBase);
    }

    /**
     * 保存消息记录
     *
     * @param courseId 课程ID
     */
    private void saveCoursePublishMessage(Long courseId) {
        MqMessage mqMessage = mqMessageService.addMessage("course_publish", String.valueOf(courseId),
                null, null);
        if (Objects.isNull(mqMessage)) {
            CustomException.cast(CommonError.UNKNOWN_ERROR);
        }
    }
}
