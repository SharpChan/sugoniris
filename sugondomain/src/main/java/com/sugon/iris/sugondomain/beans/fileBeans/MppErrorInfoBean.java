package com.sugon.iris.sugondomain.beans.fileBeans;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MppErrorInfoBean {

    /**
     * 自增序列
     */
    @ApiModelProperty(value="自增序列")
    private Long id;

    /**
     *导入的文件包信息id
     */
    @ApiModelProperty(value="导入的文件包信息id")
    private Long fileAttachmentId;

    /**
     *包内子文件信息
     */
    @ApiModelProperty(value="包内子文件信息")
    private Long fileDetailId;

    /**
     *mysql的错误信息表，mpp导入数据的表，mpp记录错误数据的表，进行数据关联
     */
    @ApiModelProperty(value="mysql的错误信息表，mpp导入数据的表，mpp记录错误数据的表，进行数据关联")
    private Long  mppid2errorid;

    /**
     * 清洗字段id
     */
    @ApiModelProperty(value="清洗字段id")
    private Long fileRinseDetailId;

    /**
     * 案件编号
     */
    @ApiModelProperty(value="案件编号")
    private Long fileCaseId;

    /**
     *对应数据的表名
     */
    @ApiModelProperty(value="对应数据的表名")
    private String mppTableName;
}
