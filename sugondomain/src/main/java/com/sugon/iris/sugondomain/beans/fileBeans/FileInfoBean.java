package com.sugon.iris.sugondomain.beans.fileBeans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sugon.iris.sugondomain.enums.FileType_Enum;
import lombok.Data;
import java.util.Date;

@Data
public class FileInfoBean {

    /**
     *文件名
     */
    private String fileName ;

    /**
     * 绝对路径
     */
    private String absolutePath;

    /**
     * 文件类型
     */
    private FileType_Enum fileTypeEnum;

    /**
     *允许同步数据
     */
    private boolean canUpload;

    /**
     * 数据已经同步
     */
    private boolean isUploaded;

    /**
     * 创建时间呢
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime = new Date();

    /**
     *修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime = new Date();

}
