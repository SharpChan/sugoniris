package com.sugon.iris.sugondomain.dtos.fileDtos;

import com.sugon.iris.sugondomain.beans.fileBeans.FileRinseDetailBean;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularDetailDto;
import com.sugon.iris.sugondomain.dtos.regularDtos.RegularShowDto;
import lombok.Data;

import java.util.List;

@Data
public class FileRinseDetailDto extends FileRinseDetailBean {

    /**
     * 配置的多个正则表达式,用于展示
     */
    List<RegularShowDto>  regularShowDtoListY;

    /**
     *正则表达式,匹配
     */
    List<RegularDetailDto> regularDetailDtoListY;

    /**
     * 正则排除,用于展示
     */
    List<RegularShowDto>  regularShowDtoListN;

    /**
     *则表达式,排除
     */
    List<RegularDetailDto> regularDetailDtoListN;
}
