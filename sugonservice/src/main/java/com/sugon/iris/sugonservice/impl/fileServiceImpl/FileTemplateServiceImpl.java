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
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.beans.webSocket.WebSocketRequestDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto4Txt;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDto4Txt;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseGroupEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateGroupEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateDetailService;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
}
