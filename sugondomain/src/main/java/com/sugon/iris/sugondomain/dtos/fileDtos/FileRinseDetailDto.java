package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileRinseDetailBean;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularShowDto;
import lombok.Data;

import java.util.List;

@Data
public class FileRinseDetailDto extends FileRinseDetailBean {

    /**
     * 配置的多个正则表达式
     */
    List<RegularShowDto>  regularShowDtoListY;

    /**
     * 正则排除
     */
    List<RegularShowDto>  regularShowDtoListN;
}
