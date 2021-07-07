package com.sugon.iris.sugonservice.impl.securityModuleImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugoncommon.publicUtils.SSHRemoteCall;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.NginxServiceDaoIntf;
import com.sugon.iris.sugondomain.dtos.securityModuleDtos.WhiteIpDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.securityModuleEntities.WhiteIpEntity;
import com.sugon.iris.sugonservice.service.FileService.TxtFileService;
import com.sugon.iris.sugonservice.service.securityModuleService.WhiteIpService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    String userName = PublicUtils.getConfigMap().get("linux.userName");
    String password = PublicUtils.getConfigMap().get("linux.password");
    String ipAddress = PublicUtils.getConfigMap().get("linux.ipAddress");

    @Autowired
    private NginxServiceDaoIntf nginxServiceDaoImpl;

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
            errorList.add(new Error("{iris-02-005}","请填写IP地址",""));
        }
        if(StringUtils.isBlank(whiteIpDto.getIdCard()) &&  StringUtils.isBlank(whiteIpDto.getPoliceNo())){
            errorList.add(new Error("{iris-02-005}","身份证号和警号至少填写一个",""));
        }


        //对ip地址进行正则校验
        String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(whiteIpDto.getIp());
        if(!m.matches()){
            errorList.add(new Error("{iris-02-005}","ip地址格式不正确",""));
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
     * 创建白名单配置文件
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
        txtFileService.deleteFile(txtFileService.creatTxtFile(fileName));//获取文件路径，然后通过文件路径进行删除
        String filePath = txtFileService.creatTxtFile(fileName);
        //写入txt文件
        txtFileService.writeTxtFile(whiteIp, filePath);
        return filePath;
    }


    /**
     * 查看远程服务器是否已经配置whitelist.conf白名单配置文件，有的话进行删除
     */
    private void checkConfFile(String filePath){

        try {
            //1.首先远程连接ssh
            SSHRemoteCall.getInstance().sshRemoteCallLogin(ipAddress, userName, password);
            //2.查看配置目录
            String command = "ls /usr/local/nginx/conf ";
            List<String> cmdResult = SSHRemoteCall.getInstance().execCommand(command);
            if(cmdResult.contains("whitelist.conf")){
                command = "rm -rf /usr/local/nginx/conf/whitelist.conf ";
                SSHRemoteCall.getInstance().execCommand(command);
            }
            //进行文件上传
            String directory = "/usr/local/nginx/conf/whitelist.conf";
            SSHRemoteCall.getInstance().uploadFile(directory, filePath);
            command = "pkill nginx";
            SSHRemoteCall.getInstance().execCommand(command);
            command = "/usr/local/nginx/sbin/nginx";
            SSHRemoteCall.getInstance().execCommand(command);
            SSHRemoteCall.getInstance().closeSession();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
