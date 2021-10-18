package com.sugon.iris.sugonservice.impl.fileServiceImpl;

import com.rnkrsoft.bopomofo4j.Bopomofo4j;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileRinseDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.RinseBusinessNullMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.RinseBusinessRepeatMapper;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.fileDtos.FileTemplateDetailDto;
import com.sugon.iris.sugondomain.dtos.rinseBusinessDto.RinseBusinessRepeatDto;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileRinseDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RinseBusinessRepeatEntity;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import com.sugon.iris.sugonservice.service.fileService.FileTemplateDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileTemplateDetailServiceImpl implements FileTemplateDetailService {

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @Resource
    private FileRinseDetailMapper fileRinseDetailMapper;

    @Resource
    private RinseBusinessNullMapper rinseBusinessNullMapper;

    @Resource
    private RinseBusinessRepeatMapper rinseBusinessRepeatMapper;

    @Override
    public List<FileTemplateDetailDto> getFileTemplateDetailDtoList(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException {
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = null;
        List<FileTemplateDetailDto> fileTemplateDetailDtoList = new ArrayList<>();
        FileTemplateDetailEntity fileTemplateDetailEntity = new FileTemplateDetailEntity();
        fileTemplateDetailEntity.setUserId(user.getId());
        fileTemplateDetailEntity.setTemplateId(fileTemplateDetailDto.getTemplateId());
        try {
            fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"查询表file_template_detail出错",e.toString()));
        }
       if(!CollectionUtils.isEmpty(fileTemplateDetailEntityList)){
           for(FileTemplateDetailEntity fileTemplateDetailEntityBean : fileTemplateDetailEntityList) {
               //通过清洗类型的id获取清洗类型信息
               FileRinseDetailEntity fileRinseDetailEntity = null;
               if(null != fileTemplateDetailEntityBean.getFileRinseDetailId()) {
                   fileRinseDetailEntity = fileRinseDetailMapper.selectByPrimaryKey(fileTemplateDetailEntityBean.getFileRinseDetailId());
               }
               FileTemplateDetailDto fileTemplateDetailDtoBean = new FileTemplateDetailDto();
               PublicUtils.trans(fileTemplateDetailEntityBean, fileTemplateDetailDtoBean);
               if (null != fileRinseDetailEntity){
                    fileTemplateDetailDtoBean.setFileRinseDetailTypeName(fileRinseDetailEntity.getTypeName());
               }
               fileTemplateDetailDtoList.add(fileTemplateDetailDtoBean);
           }
       }

        return fileTemplateDetailDtoList;
    }

    @Override
    public int fileTemplateDetailInsert(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException {
        int i = 0;
        if (checkRepet(user, fileTemplateDetailDto, errorList)) return 0;

        FileTemplateDetailEntity fileTemplateDetailEntity = new FileTemplateDetailEntity();
        PublicUtils.trans(fileTemplateDetailDto,fileTemplateDetailEntity);
        fileTemplateDetailEntity.setUserId(user.getId());
        if (checkSortNo(errorList, fileTemplateDetailEntity)){
            return i;
        }

        try{
           i = fileTemplateDetailMapper.fileTemplateDetailInsert(fileTemplateDetailEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"插入表file_template_detail出错",e.toString()));
        }
        return i;
    }


    @Override
    public int updateFileTemplateDetail(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException {
        int i = 0;

        if (checkRepet(user, fileTemplateDetailDto, errorList)) return 0;

        FileTemplateDetailEntity fileTemplateDetailEntity = new FileTemplateDetailEntity();
        PublicUtils.trans(fileTemplateDetailDto,fileTemplateDetailEntity);
        fileTemplateDetailEntity.setUserId(user.getId());

        //判断sort_no是否在一个模板内重复
        //通过模板号查询出所有的字段
        FileTemplateDetailEntity fileTemplateDetailEntityBean = new FileTemplateDetailEntity();
        fileTemplateDetailEntityBean.setTemplateId(fileTemplateDetailDto.getTemplateId());
        List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntityBean);
        for(FileTemplateDetailEntity fileTemplateDetail : fileTemplateDetailEntityList){
            if(!fileTemplateDetail.getId().equals(fileTemplateDetailDto.getId())){
               if( fileTemplateDetail.getSortNo().equals(fileTemplateDetailDto.getSortNo())){
                   errorList.add(new Error(ErrorCode_Enum.SUGON_01_002.getCode(), ErrorCode_Enum.SUGON_01_002.getMessage()));
                   return i;
               }
            }
        }


        try{
            i = fileTemplateDetailMapper.updateFileTemplateDetail(fileTemplateDetailEntity);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表file_template_detail出错",e.toString()));
        }
        return i;
    }

    @Override
    public int removeBoundByTemplateId(Long templateId, List<Error> errorList) {
        int result =0;
        try{
            result = fileTemplateDetailMapper.updateByTemplateIdSelective(templateId);
        }catch (Exception e){
        e.printStackTrace();
        errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"修改表file_template_detail出错",e.toString()));
        }
        return result;
    }

    private boolean checkSortNo(List<Error> errorList, FileTemplateDetailEntity fileTemplateDetailEntity) {
        FileTemplateDetailEntity fileTemplateDetailEntityBean = new FileTemplateDetailEntity();
        fileTemplateDetailEntityBean.setSortNo(fileTemplateDetailEntity.getSortNo());
        fileTemplateDetailEntityBean.setTemplateId(fileTemplateDetailEntity.getTemplateId());
        try {
            //检查排序字段有无被占用，被占用直接报错退出
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailList(fileTemplateDetailEntityBean);
            if (!CollectionUtils.isEmpty(fileTemplateDetailEntityList)) {
                errorList.add(new Error(ErrorCode_Enum.SUGON_01_002.getCode(), ErrorCode_Enum.SUGON_01_002.getMessage()));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(), "查询表file_template_detail出错", e.toString()));
        }
        return false;
    }

    @Override
    public int deleteFileTemplateDetail( String[] idArr, List<Error> errorList) {
        int count = 0;
        try{
            count =  fileTemplateDetailMapper.deleteFileTemplateDetailById(idArr);
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_DB_001.getCode(),"删除表file_template_detail出错",e.toString()));
        }
        return count;
    }

    @Override
    public String getPinyin(String chinese, List<Error> errorList) {
        //汉语句子->无音调拼音
        String pinyinStr = Bopomofo4j.pinyin(chinese,2, false, false, ",");
        String[]  pinyinStrArr = pinyinStr.split(",");
        String result = "";
        for(String str : pinyinStrArr){
            if(!StringUtils.isEmpty(str)){
                result += str.substring(0, 1);
            }
        }
        return result;
    }

    private boolean checkRepet(User user, FileTemplateDetailDto fileTemplateDetailDto, List<Error> errorList) throws IllegalAccessException {
        //校验字段名称格式是否正确
        if(!fileTemplateDetailDto.getFieldName().matches("^[a-zA-Z][a-zA-Z0-9_]*$")){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_011.getCode(), ErrorCode_Enum.SUGON_01_011.getMessage()));
            return true;
        }

        //获取所有的字段，判断关键字有没有重复
        List<String> keyList_template = new ArrayList<>();
        List<String> nameList_template = new ArrayList<>();
        List<FileTemplateDetailDto>  fileTemplateDetailDtoList = this.getFileTemplateDetailDtoList(user,fileTemplateDetailDto,errorList);
        for(FileTemplateDetailDto fileTemplateDetailDtoBean : fileTemplateDetailDtoList){
            if(!(fileTemplateDetailDtoBean.getId().equals(fileTemplateDetailDto.getId()))) {
                keyList_template.addAll(Arrays.asList(fileTemplateDetailDtoBean.getFieldKey().split("&&")));
                fileTemplateDetailDtoBean.getFieldKeyList().addAll(Arrays.asList(fileTemplateDetailDtoBean.getFieldKey().split("&&")));
                fileTemplateDetailDtoBean.getExcludeList().addAll(Arrays.asList(fileTemplateDetailDtoBean.getExclude().split("&&")));
                nameList_template.add(fileTemplateDetailDtoBean.getFieldName());
            }
        }
        List<String> keyList_templateDetail = Arrays.asList(fileTemplateDetailDto.getFieldKey().split("&&"));
        List<String> excludeList__templateDetail = new ArrayList<>();
        if(!StringUtils.isEmpty(fileTemplateDetailDto.getExclude())) {
            excludeList__templateDetail.addAll(Arrays.asList(fileTemplateDetailDto.getExclude().split("&&"))) ;
        }
        for(String str : keyList_templateDetail){
            for(String keyStr : keyList_template){
                if(keyStr.contains(str)){
                    boolean flag = true;
                    for(String str_exclude: excludeList__templateDetail){
                        if(!StringUtils.isEmpty(str_exclude)) {
                            if (keyStr.contains(str_exclude)) {
                                flag = false;
                                break;
                            }
                        }
                    }
                    //关键字包含，又没有设置排除
                    if(flag) {
                        errorList.add(new Error(ErrorCode_Enum.SUGON_01_009.getCode(), ErrorCode_Enum.SUGON_01_009.getMessage()));
                        return true;
                    }
                }
            }
        }
        //判断之前的字段有没有被现在的字段包含
        for (FileTemplateDetailDto fileTemplateDetailDtoBean: fileTemplateDetailDtoList){
            for(String  fieldKeyStr:fileTemplateDetailDtoBean.getFieldKeyList()){
                //如果之前的字段关键字被现在的字段包含，且之前的字段配置的排除字段不被现在的关键字段包含
                if(fileTemplateDetailDto.getFieldKey().contains(fieldKeyStr)){
                    boolean flag = true;
                    if(!CollectionUtils.isEmpty(fileTemplateDetailDtoBean.getExcludeList())) {
                        for (String str_exclude : fileTemplateDetailDtoBean.getExcludeList()) {
                            if(!StringUtils.isEmpty(str_exclude)) {
                                if (fileTemplateDetailDto.getFieldKey().contains(str_exclude)) {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(flag){
                        errorList.add(new Error(ErrorCode_Enum.SUGON_01_009.getCode(), ErrorCode_Enum.SUGON_01_009.getMessage()));
                        return true;
                    }
                }
            }
        }
        //判断字段名称有没有重复
        if(nameList_template.contains(fileTemplateDetailDto.getFieldName())){
            errorList.add(new Error(ErrorCode_Enum.SUGON_01_010.getCode(),ErrorCode_Enum.SUGON_01_010.getMessage()));
            return true;
        }
        return false;
    }

}
