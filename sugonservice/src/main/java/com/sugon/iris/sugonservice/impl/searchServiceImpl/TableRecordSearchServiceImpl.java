package com.sugon.iris.sugonservice.impl.searchServiceImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileDataGroupDetailMapper;
import com.sugon.iris.sugondomain.dtos.searchDtos.TableRecordSearchDto;
import com.sugon.iris.sugonservice.service.searchService.TableRecordSearchService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class TableRecordSearchServiceImpl implements TableRecordSearchService {
    @Resource
    private FileDataGroupDetailMapper fileDataGroupDetailMapper;

    @Override
    public List<TableRecordSearchDto> getRecordsByUserId(Long userId, String condition) {
        //1.对查询条件进行校验，排除关键字
        String str = PublicUtils.checkSql(condition);
        //1.查询出拥有权限的数据表

        return null;
    }
}
