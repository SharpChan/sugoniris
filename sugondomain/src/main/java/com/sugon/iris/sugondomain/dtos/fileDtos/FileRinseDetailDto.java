package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileRinseDetailBean;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import lombok.Data;

import java.util.List;

@Data
public class FileRinseDetailDto extends FileRinseDetailBean {

    /**
     * 配置的多个正则表达式
     */
    List<RegularDetailDto>  regularDetailDtorListY;

    /**
     * 正则排除
     */
    List<RegularDetailDto>  regularDetailDtorListN;
}
