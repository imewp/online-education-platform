package com.mewp.edu.system.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author mewp
 */
@Data
@TableName("dictionary")
@ApiModel(value = "Dictionary", description = "数据字典")
public class Dictionary implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "数据字典名称")
    private String name;

    @ApiModelProperty(value = "数据字典代码")
    private String code;

    @ApiModelProperty(value = "数据字典项--json格式")
    private String itemValues;
}
