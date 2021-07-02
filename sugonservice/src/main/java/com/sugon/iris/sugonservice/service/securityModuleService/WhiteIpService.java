package com.sugon.iris.sugonservice.service.securityModuleService;


import com.sugon.iris.sugondomain.dtos.securityModuleDtos.WhiteIpDto;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.io.IOException;
import java.util.List;

public interface WhiteIpService {
    List<WhiteIpDto>  getWhiteIpList(WhiteIpDto whiteIpDto, List<Error> errorList) throws IllegalAccessException;

    int saveWhiteIp(WhiteIpDto whiteIpDto, List<Error> errorList) throws IOException, IllegalAccessException;

    int deleteWhiteIp(Long id, List<Error> errorList) throws IOException;
}
