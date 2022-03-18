package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sugon.iris.sugoncommon.fileUtils.TxtUtil;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.UserGroupDao;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileRinseGroupMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.shengTing.ShanghaiMinhangBean;
import com.sugon.iris.sugondomain.beans.shengTing.ShanghaikeyunBean;
import com.sugon.iris.sugondomain.beans.shengTing.base.*;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto4Txt;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto4Txt;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseGroupEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateGroupEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.impl.excelServiceImpl.ImportService;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateDetailService;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FileTemplateServiceImpl implements FileTemplateService {

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private FileRinseGroupMapper fileRinseGroupMapper;

    @Resource
    private UserGroupDao userGroupDaoImpl;

    @Resource
    private FileTemplateDetailService fileTemplateDetailServiceImpl;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ImportService importService;



    @Override
    public List<FileTemplateDto> getFileTemplateDtoList(User user, FileTemplateDto fileTemplateDto, List<Error> errorList) throws IllegalAccessException {
        List<FileTemplateDto> fileTemplateDtoList = new ArrayList<>();
        List<FileTemplateEntity> fileTemplateEntityList = new ArrayList<>();
        try{
            List<Long> userIdList = userGroupDaoImpl.getGroupUserIdList(user.getId(),errorList);
            List<FileTemplateGroupEntity> fileTemplateGroupEntitylist = new ArrayList<>();
            for(Long userId : userIdList){
                FileTemplateEntity fileTemplateEntity = new FileTemplateEntity();
                PublicUtils.trans(fileTemplateDto,fileTemplateEntity);
                fileTemplateEntity.setUserId(userId);
                List<FileTemplateEntity> fileTemplateEntityListPer =  fileTemplateMapper.selectFileTemplateList(fileTemplateEntity);
                if(!CollectionUtils.isEmpty(fileTemplateEntityListPer)){
                    fileTemplateEntityList.addAll(fileTemplateEntityListPer);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template出错",e.toString()));
        }
        if(!CollectionUtils.isEmpty(fileTemplateEntityList)){
            for(FileTemplateEntity fileTemplateEntityBean : fileTemplateEntityList){
                //通过清洗字段组id获取字段组信息
                FileRinseGroupEntity fileRinseGroupEntity = null;
                if(null != fileTemplateEntityBean.getFileRinseGroupId()) {
                    fileRinseGroupEntity = fileRinseGroupMapper.selectByPrimaryKey(fileTemplateEntityBean.getFileRinseGroupId());
                }
                FileTemplateDto fileTemplateDtoBean = new FileTemplateDto();
                PublicUtils.trans(fileTemplateEntityBean,fileTemplateDtoBean);
                if(null != fileRinseGroupEntity) {
                    fileTemplateDtoBean.setFileRinseName(fileRinseGroupEntity.getFileRinseName());
                }
                fileTemplateDtoList.add(fileTemplateDtoBean);
            }
        }
        return fileTemplateDtoList;
    }

    @Override
    public int fileTemplateInsert(User user, FileTemplateDto fileTemplateDto, List<Error> errorList) throws IllegalAccessException {
        if (checkKeys(fileTemplateDto, errorList)) return 0;
        int count = 0;
        FileTemplateEntity fileTemplateEntity = new FileTemplateEntity();
        PublicUtils.trans(fileTemplateDto,fileTemplateEntity);
        fileTemplateEntity.setUserId(user.getId());
        try{
            count =  fileTemplateMapper.fileTemplateInsert(fileTemplateEntity);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_template出错",e.toString()));
        }
        return 0;
    }

    @Override
    public int updateFileTemplate(User user, FileTemplateDto fileTemplateDto, List<Error> errorList) throws IllegalAccessException {

        //校验关键字，是否有包含关系，有的话提示返回
        if (checkKeys(fileTemplateDto, errorList)) return 0;

        int count = 0;
        FileTemplateEntity fileTemplateEntity = new FileTemplateEntity();
        fileTemplateEntity.setUserId(user.getId());
        PublicUtils.trans(fileTemplateDto,fileTemplateEntity);
        try{
            count =  fileTemplateMapper.updateFileTemplate(fileTemplateEntity);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"更新表file_template出错",e.toString()));
        }
        return count;
    }

    //判断关键字内是包含
    private boolean checkKeys(FileTemplateDto fileTemplateDto, List<Error> errorList) {
        if(!StringUtils.isEmpty(fileTemplateDto.getTemplateKey())){
            String[] keys = fileTemplateDto.getTemplateKey().split("&&");
             for(int i = 0 ; i < keys.length ; i++){
                 for(int j = 0 ; j < keys.length ; j++){
                     if(i == j ){
                         continue;
                     }
                    if(keys[i].contains(keys[j])){
                        errorList.add(new Error(ErrorCode_Enum.FILE_01_013.getCode(),ErrorCode_Enum.FILE_01_013.getMessage()));
                        return true;
                    }
                 }
             }
        }
        return false;
    }

    @Override
    public int deleteFileTemplate(String[] idArr, List<Error> errorList) {
        int count = 0;
        try{
            count =  fileTemplateMapper.deleteFileTemplateById(idArr);
            //删除相应的详细信息
            fileTemplateDetailMapper.deleteFileTemplateDetailByTemplatId(idArr);
        }catch (Exception e){
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_template出错",e.toString()));
        }
        return count;
    }

    @Override
    public List<FileTemplateDto> getAllFileTemplateDtoAndDetailsList(List<Error> errorList) throws IllegalAccessException {
        List<FileTemplateDto> fileTemplateDtoList = new ArrayList<>();
        FileTemplateEntity fileTemplateEntity4Sql = new FileTemplateEntity();
        List<FileTemplateEntity> fileTemplateEntityList = null;
        try{

            fileTemplateEntityList =  fileTemplateMapper.selectFileTemplateList(fileTemplateEntity4Sql);

        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template出错",e.toString()));
        }
        for(FileTemplateEntity fileTemplateEntity : fileTemplateEntityList){

            FileTemplateDto fileTemplateDto = new FileTemplateDto();
            PublicUtils.trans(fileTemplateEntity,fileTemplateDto);
            fileTemplateDtoList.add(fileTemplateDto);
            List<FileTemplateDetailDto> fileTemplateDetailDtoList = new ArrayList<>();
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = null;
            //获取模板字段信息
            try {
                 fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailByTemplateId(fileTemplateDto.getId());
            }catch (Exception e){
                e.printStackTrace();
                errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template_detail出错",e.toString()));
                return null;
            }
            for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
                FileTemplateDetailDto fileTemplateDetailDto = new FileTemplateDetailDto();
                PublicUtils.trans(fileTemplateDetailEntity,fileTemplateDetailDto);
                fileTemplateDetailDtoList.add(fileTemplateDetailDto);
            }
            fileTemplateDto.setFileTemplateDetailDtoList(fileTemplateDetailDtoList);
        }
        return fileTemplateDtoList;
    }

    @Override
    public FileTemplateDto4Txt getFileTemplateDtoAndDetailsListByTemplateId(Long templateId ,List<Error> errorList) throws IllegalAccessException {
        FileTemplateEntity fileTemplateEntity = null;
        try{

            fileTemplateEntity =  fileTemplateMapper.selectFileTemplateByPrimaryKey(templateId);

        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template出错",e.toString()));
        }

            FileTemplateDto4Txt fileTemplateDto = new FileTemplateDto4Txt();
            PublicUtils.trans(fileTemplateEntity,fileTemplateDto);
            List<FileTemplateDetailDto4Txt> fileTemplateDetailDtoList = new ArrayList<>();
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = null;
            //获取模板字段信息
            try {
                fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailByTemplateId(fileTemplateDto.getId());
            }catch (Exception e){
                e.printStackTrace();
                errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template_detail出错",e.toString()));
                return null;
            }
            for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
                FileTemplateDetailDto4Txt fileTemplateDetailDto = new FileTemplateDetailDto4Txt();
                PublicUtils.trans(fileTemplateDetailEntity,fileTemplateDetailDto);
                fileTemplateDetailDtoList.add(fileTemplateDetailDto);
            }
            fileTemplateDto.setFileTemplateDetailDtoList(fileTemplateDetailDtoList);

        return fileTemplateDto;
    }


    @Override
    public void getTemplateTxt(HttpServletResponse response,Long templateId , List<Error> errorList) throws IllegalAccessException {
        FileTemplateDto4Txt fileTemplateDto = getFileTemplateDtoAndDetailsListByTemplateId( templateId , errorList);
        Gson gson = new Gson();
        String json = gson.toJson(fileTemplateDto);
        TxtUtil.downloadTXT(response,fileTemplateDto.getTemplateName(),json);
    }

    @Override
    public boolean importTemplateTxt(MultipartFile file,User user, List<Error> errorList) throws IllegalAccessException {
        InputStream is = null;
        String txtStr = "";
        try {
            //io 输入流读文件
            is = file.getInputStream();
            //写入数据
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = (is.read(buffer))) != -1) {
                baos.write(buffer,0,len);//将缓冲区内容写进输出流，0是从起始偏移量，len是指定的字符个数
            }
            txtStr = new String(baos.toByteArray());//最终结果，将字节数组转换成字符串
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if(null!= is){
                try{
                    is.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        Gson gson = new Gson();
        txtStr = txtStr.replaceAll("\\s*","");
        log.info(txtStr);
        FileTemplateDto fileTemplateDto = gson.fromJson(txtStr, FileTemplateDto.class);
        List<FileTemplateDetailDto> fileTemplateDetailDtoList = fileTemplateDto.getFileTemplateDetailDtoList();
        FileTemplateEntity fileTemplateEntity = new FileTemplateEntity();
        PublicUtils.trans(fileTemplateDto,fileTemplateEntity);
        fileTemplateEntity.setUserId(user.getId());
        fileTemplateMapper.fileTemplateInsert(fileTemplateEntity);

        for(FileTemplateDetailDto fileTemplateDetailDto : fileTemplateDetailDtoList){
            fileTemplateDetailDto.setTemplateId(fileTemplateEntity.getId());
            fileTemplateDetailServiceImpl.fileTemplateDetailInsert(user,fileTemplateDetailDto,errorList);
        }

        return true;
    }

    @Override
    public List<StResponse> getKyxx(String cardId,String minRownum,String maxRownum) {

        StResponse<List<ShanghaikeyunBean>> stResponse = this.getKyzResponse(cardId,minRownum, maxRownum);
        List<StResponse> StResponseList = new ArrayList<>();
        StResponseList.add(stResponse);
        return StResponseList;
    }

    @Override
    public List<StResponse> getMinHanxx(String cardId, String minRownum, String maxRownum) {
        StResponse<List<ShanghaiMinhangBean>> stResponse = this.getMinHangResponse(cardId,minRownum, maxRownum);
        List<StResponse> StResponseList = new ArrayList<>();
        StResponseList.add(stResponse);
        return StResponseList;
    }

    @Override
    public List<StResponse<List<ShanghaiMinhangBean>>> getMinHanxxByExcel(List<MultipartFile> files) throws Exception {
        List<StResponse<List<ShanghaiMinhangBean>>> stResponseList = new ArrayList<>();
        Map<String,StResponse> map = new HashMap<>();

        List<String> idList = new ArrayList<>();
        for(MultipartFile file : files){
            InputStream inputStream = file.getInputStream();
            List<List<Object>> list = importService.getBankListByExcel(inputStream, file.getOriginalFilename());
            inputStream.close();
            if(CollectionUtils.isEmpty(list)){
                 continue;
            }
            for(List<Object> cellList : list){
               if(!CollectionUtils.isEmpty(cellList)){
                   if(null != cellList.get(0) &&  cellList.get(0).toString().trim().length()>0){
                       idList.add(cellList.get(0).toString().trim());
                   }
               }
            }
        }
        if(CollectionUtils.isEmpty(idList)){
            return stResponseList;
        }

        for(String str : idList){
            StResponse<List<ShanghaiMinhangBean>> stResponse =    getMinHangResponse(str,"1","1");
            map.put(str,stResponse);
        }

        for(Map.Entry<String,StResponse> entry : map.entrySet()){
              if("error".equals(entry.getValue().getStatus()) || "1".equals(entry.getValue().getTotal()) || "-999".equals(entry.getValue().getTotal()) || "0".equals(entry.getValue().getTotal())){
                  stResponseList.add(entry.getValue());
              }else{
                  StResponse<List<ShanghaiMinhangBean>> stResponse =    getMinHangResponse(entry.getKey(),"1",entry.getValue().getTotal());
                  stResponseList.add(stResponse);
              }
        }
        return stResponseList;
    }


    @Override
    public List<StResponse<List<ShanghaikeyunBean>>> getKeyunByExcel(List<MultipartFile> files) throws Exception {
        List<StResponse<List<ShanghaikeyunBean>>> stResponseList = new ArrayList<>();
        Map<String,StResponse> map = new HashMap<>();

        List<String> idList = new ArrayList<>();
        for(MultipartFile file : files){
            InputStream inputStream = file.getInputStream();
            List<List<Object>> list = importService.getBankListByExcel(inputStream, file.getOriginalFilename());
            inputStream.close();
            if(CollectionUtils.isEmpty(list)){
                continue;
            }
            for(List<Object> cellList : list){
                if(!CollectionUtils.isEmpty(cellList)){
                    if(null != cellList.get(0) &&  cellList.get(0).toString().trim().length()>0){
                        idList.add(cellList.get(0).toString().trim());
                    }
                }
            }
        }
        if(CollectionUtils.isEmpty(idList)){
            return stResponseList;
        }

        for(String str : idList){
            StResponse<List<ShanghaikeyunBean>> stResponse =    getKyzResponse(str,"1","1");
            map.put(str,stResponse);
        }

        for(Map.Entry<String,StResponse> entry : map.entrySet()){
            if("error".equals(entry.getValue().getStatus()) || "1".equals(entry.getValue().getTotal()) || "-999".equals(entry.getValue().getTotal()) || "0".equals(entry.getValue().getTotal())){
                stResponseList.add(entry.getValue());
            }else{
                StResponse<List<ShanghaikeyunBean>> stResponse =    getKyzResponse(entry.getKey(),"1",entry.getValue().getTotal());
                stResponseList.add(stResponse);
            }
        }
        return stResponseList;
    }

    private StResponse getKyzResponse(String cardId,String minRownum,String maxRownum){
        String url = "http://50.16.212.226:30281/apis/event/services/onlineQuerySearch";
        StRequest stRequest= new StRequest();

        RealInfo realInfo = new RealInfo();

        Required required = new Required();

        required.setMinRownum(minRownum);

        required.setMaxRownum(maxRownum);

        required.setRequiredItems("zjlid,ysxtjrzj,xxsc_pdbz,jlid,dwd_zjid,zjlxdm,zjlx,zjhm,xm,fcrq,fcsj,bc,scz,sczdz,scz_jd,scz_wd,scz_geohash,scz_geohash4,scz_geohash5,scz_geohash6,scz_geohash7,ddz,spztdm,spzt,sprq,spsj,jpztdm,jpzt,jprq,jpsj,jpk,fcdh,pz,ph,zwh,pj,xlbh,xldj,sfd,zdd,xldk,cygsid,cygsmc,lc,dw_rksj,dw_yxzt,sjly,yshck_rksj,yshck_gxsj");

        Validate validate = new Validate();

        validate.setFwmc("上海客运售票记录查询服务");

        validate.setYybh("YY320500232021062300001");

        validate.setFwbh("S-320000260100-10000001-00189");

        validate.setZrrgmsfhm("320502198502091792");

        stRequest.setRealInfo(realInfo);

        stRequest.setRequired(required);

        stRequest.setValidate(validate);

        required.setCondition("1=1 and zjhm = '"+cardId+"'");

        Gson gson = new Gson();
        String json = gson.toJson(stRequest);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String res = restTemplate.postForObject(url, request ,String.class);

        log.info(res);

        StResponse<List<ShanghaikeyunBean>> stResponse = gson.fromJson(res, new TypeToken<StResponse<List<ShanghaikeyunBean>>>(){}.getType());
        return stResponse;
    }

    private StResponse<List<ShanghaiMinhangBean>> getMinHangResponse(String cert_no, String minRownum, String maxRownum){
        String url = "http://50.16.212.226:30281/apis/event/services/onlineQuerySearch";
        StRequest stRequest= new StRequest();

        RealInfo realInfo = new RealInfo();

        Required required = new Required();

        required.setMinRownum(minRownum);

        required.setMaxRownum(maxRownum);

        required.setRequiredItems("zjlid,ysxtjrzj,xxsc_pdbz,id,border_type,flt_airlcode,flt_number,flt_suffix,flt_date,seg_dept_code,seg_dest_code,sta_depttm,sta_arvetm,pdt_dept,pdt_dest,psr_name,psr_chnname,pdt_lastname,pdt_midname,pdt_firstname,pdt_birthday,pdt_bir_adress,psr_gender,cert_type,cert_no,pdt_exprirydate,pdt_issuedate,pdt_issue_country,pdt_country,psr_ics,psr_ckipid,psr_office,psr_agent,psr_ckitime,psr_seg_seatnbr,rs_czsj,cz,dt,ds,rksj,sjly,yshck_rksj,yshck_gxsj");

        Validate validate = new Validate();

        validate.setFwmc("上海民航售票及离港信息查询服务");

        validate.setYybh("YY320500232021062300001");

        validate.setFwbh("S-320000260100-10000001-00188");

        validate.setZrrgmsfhm("320502198502091792");

        stRequest.setRealInfo(realInfo);

        stRequest.setRequired(required);

        stRequest.setValidate(validate);

        required.setCondition("1=1 and cert_no = '"+cert_no+"'");

        Gson gson = new Gson();
        String json = gson.toJson(stRequest);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String res = restTemplate.postForObject(url, request ,String.class);
        log.info(res);

       StResponse<List<ShanghaiMinhangBean>> stResponse = gson.fromJson(res, new TypeToken<StResponse<List<ShanghaiMinhangBean>>>(){}.getType());
       return stResponse;
    }
}
