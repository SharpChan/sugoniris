package com.sugon.iris.sugonservice.impl.securityModuleImpl;

import com.jcraft.jsch.Session;
import com.sugon.iris.sugoncommon.SSHRemote.SSHConfig;
import com.sugon.iris.sugoncommon.SSHRemote.SSHServiceBs;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.NginxServiceDao;
import com.sugon.iris.sugondomain.dtos.securityModuleDtos.WhiteIpDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.securityModuleEntities.WhiteIpEntity;
import com.sugon.iris.sugonservice.service.fileService.TxtFileService;
import com.sugon.iris.sugonservice.service.securityModuleService.WhiteIpService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;

@Service
public class WhiteIpServiceImpl implements WhiteIpService {

    @Autowired
    private NginxServiceDao nginxServiceDaoImpl;

    @Autowired
    private TxtFileService txtFileService;

    @Override
    public List<WhiteIpDto> getWhiteIpList(WhiteIpDto whiteIpDto, List<Error> errorList) throws IllegalAccessException {

        WhiteIpEntity whiteIpEntity = new WhiteIpEntity();
        PublicUtils.trans(whiteIpDto,whiteIpEntity);
        List<WhiteIpEntity> whiteIpEntityList = nginxServiceDaoImpl.queryWhiteIp(whiteIpEntity,errorList);
        List<WhiteIpDto> whiteIpDtoList = new ArrayList<>();
        for(WhiteIpEntity whiteIpEntityBean : whiteIpEntityList){
            WhiteIpDto whiteIpDtoOut = new WhiteIpDto();
            PublicUtils.trans(whiteIpEntityBean,whiteIpDtoOut);
            whiteIpDtoList.add(whiteIpDtoOut);
        }
        return whiteIpDtoList;
    }

    @Override
    public int saveWhiteIp(WhiteIpDto whiteIpDto, List<Error> errorList) throws IOException, IllegalAccessException {
        if(StringUtils.isBlank(whiteIpDto.getIp())){
            errorList.add(new Error("{iris-02-005}","?????????IP??????",""));
        }
        if(StringUtils.isBlank(whiteIpDto.getIdCard()) &&  StringUtils.isBlank(whiteIpDto.getPoliceNo())){
            errorList.add(new Error("{iris-02-005}","???????????????????????????????????????",""));
        }


        //???ip????????????????????????
        String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(whiteIpDto.getIp());
        if(!m.matches()){
            errorList.add(new Error("{iris-02-005}","ip?????????????????????",""));
        }

        if(!CollectionUtils.isEmpty(errorList)){
            return 0;
        }

        WhiteIpEntity whiteIpEntity = new WhiteIpEntity();
        PublicUtils.trans(whiteIpDto,whiteIpEntity);
        nginxServiceDaoImpl.saveWhiteIp(whiteIpEntity,errorList);

        String filePath = doNginxWhiteIp(errorList);
        checkConfFile(filePath);
        return 1;
    }

    @Override
    public int deleteWhiteIp(Long id, List<Error> errorList) throws IOException {
         nginxServiceDaoImpl.deleteWhiteIp(id,errorList);
         String filePath = doNginxWhiteIp(errorList);
         checkConfFile(filePath);
        return 1;
    }

    /**
     * ???????????????????????????
     * @param errorList
     * @throws IOException
     */
    private String doNginxWhiteIp(List<Error> errorList) throws IOException {
        List<WhiteIpEntity> whiteIpEntityList = nginxServiceDaoImpl.queryWhiteIp(null,errorList);
        String fileName = "whitelist.conf";
        String whiteIp = "";
        for(WhiteIpEntity whiteIpEntity : whiteIpEntityList){
            whiteIp += "allow "+whiteIpEntity.getIp()+";\r\n";
        }
        txtFileService.deleteFile(txtFileService.creatTxtFile(fileName));//?????????????????????????????????????????????????????????
        String filePath = txtFileService.creatTxtFile(fileName);
        //??????txt??????
        txtFileService.writeTxtFile(whiteIp, filePath);
        return filePath;
    }


    /**
     * ???????????????????????????????????????whitelist.conf?????????????????????????????????????????????
     */
    private void checkConfFile(String filePath){

        try {
            Session session = new SSHConfig().getSession();
            SSHServiceBs sSHServiceBs = new SSHServiceBs(session);
            //2.??????????????????
            String command = "ls /usr/local/nginx/conf ";
            List<String> cmdResult =  sSHServiceBs.execCommand(command);
            if(cmdResult.contains("whitelist.conf")){
                command = "rm -rf /usr/local/nginx/conf/whitelist.conf ";
                sSHServiceBs.execCommand(command);
            }
            //??????????????????
            String directory = "/usr/local/nginx/conf/whitelist.conf";
            sSHServiceBs.uploadFile(directory,filePath);
            command = "pkill nginx";
            sSHServiceBs.execCommand(command);
            command = "/usr/local/nginx/sbin/nginx";
            sSHServiceBs.execCommand(command);
            sSHServiceBs.closeSession(session);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
