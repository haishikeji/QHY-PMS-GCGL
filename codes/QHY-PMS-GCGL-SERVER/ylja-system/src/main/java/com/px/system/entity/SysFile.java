package com.px.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * sys_file表对应的实体类
 *
 * @author 品讯科技
 * @since 2023-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String fileName;

    /**
     * 内容关联id
     */
    private String contextId;

    /**
     * 资源地址
     */
    private String fileUrl;

    /**
     * 文件类型：1.
     */
    private Integer fileType;

    /**
     * 状态：1.正常，2.删除
     */
    private Integer fileStatus;

    /**
     * 文件后缀
     */
    private String filePostfix;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 备注
     */
    private String remark;


}
