package com.sugon.iris.sugonservice.impl.systemImpl;

import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondata.jdbcTemplate.intf.system.TranslateServiceDao;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.dtos.systemDtos.TranslateDto;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.TranslateEntity;
import com.sugon.iris.sugonservice.service.systemService.TranslateService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

@Service
public class TranslateServiceImpl implements TranslateService {

    @Resource
    private TranslateServiceDao  translateServiceDaoImpl;

    @Override
    public Map<String,?> getTranslateList(String tsType, List<Error> errorList) throws IllegalAccessException {
        Map<String,Map<String, Map>> map = new HashMap<>();
        List<TranslateDto> translateDtoList = new ArrayList<>();
        TranslateEntity translateEntity4Sql = new TranslateEntity();
        translateEntity4Sql.setTsType(tsType);
        List<TranslateEntity>  translateEntityList =  translateServiceDaoImpl.getZhCnTranslate(translateEntity4Sql,errorList);
        for(TranslateEntity translateEntity : translateEntityList){
            TranslateDto translateDto = new TranslateDto();
            PublicUtils.trans(translateEntity,translateDto);
            translateDtoList.add(translateDto);
        }

        for(TranslateDto translateDto1 : translateDtoList){
            for(TranslateDto translateDto2 : translateDtoList){
                if (translateDto1.getId().equals(translateDto2.getFatherId())){
                    translateDto1.getChildrenList().add(translateDto2);
                }
        }
        }

        for(Iterator<TranslateDto> it = translateDtoList.iterator(); it.hasNext();){
            TranslateDto translateDto = it.next();
            if(!translateDto.getGrade().equals("1")){
                it.remove();
            }
        }

        list2Map(translateDtoList,map);
        return map;
    }

    @Override
    public Integer addTranslate(String tsStr,Long menuId,String tsType,String translat, List<Error> errorList) throws IllegalAccessException {
        Integer result = 0;
        if(StringUtils.isEmpty(tsStr)){
            return 0;
        }
        List<TranslateEntity> translateEntity4SqlListAdd = new ArrayList<>();
        String[] arr = tsStr.split("\\.");
        List<String> aa = Arrays.asList(arr);
        Long fatherId = null;
        for(int i=0;i<arr.length;i++){
            TranslateEntity translateEntity4Sql = new TranslateEntity();
            translateEntity4Sql.setGrade(String.valueOf(i+1));
            translateEntity4Sql.setSource(arr[i]);
            translateEntity4Sql.setTsType(tsType);
            //最后一个
            if((i+1) == arr.length){
                translateEntity4Sql.setMenuId(menuId);
                translateEntity4Sql.setValue(translat);
            }

            translateEntity4Sql.setFatherId(fatherId);
            List<TranslateEntity>  translateEntityList =  translateServiceDaoImpl.getZhCnTranslate(translateEntity4Sql,errorList);
            if(!CollectionUtils.isEmpty(translateEntityList)){
                    fatherId = translateEntityList.get(0).getId();
                    continue;
            }
            translateServiceDaoImpl.saveTranslate(translateEntity4Sql,errorList);
            fatherId = translateEntity4Sql.getId();
            result ++;
        }
        return result;
    }

    @Override
    public Integer updateTranslate(String tsStr,Long menuId,String tsType,String translat, List<Error> errorList) throws IllegalAccessException {
        Integer result = 0;
        if(StringUtils.isEmpty(tsStr)){
            return 0;
        }
        List<Object[]> objDeleteList = new ArrayList<>();
        //从叶节点向前找，直到有分叉停止查找，全部删除
        getDeleteNode(menuId,null,tsType,objDeleteList,errorList);
        translateServiceDaoImpl.deleteTranslate(objDeleteList,errorList);
        //进行重新插入
        result = this.addTranslate(tsStr,menuId,tsType,translat, errorList);
        return result;
    }

    @Override
    public int[] deleteTranslate(Long menuId,String tsType , List<Error> errorList) {
        List<Object[]> objDeleteList = new ArrayList<>();
        //从叶节点向前找，直到有分叉停止查找，全部删除
        getDeleteNode(menuId,null,tsType,objDeleteList,errorList);
        return translateServiceDaoImpl.deleteTranslate(objDeleteList,errorList);
    }


    private void  getDeleteNode(Long menuId,Long id,String tsType,List<Object[]> objDeleteList,List<Error> errorList){
        TranslateEntity translateEntity4Sql = new TranslateEntity();
        if(null != menuId) {
            translateEntity4Sql.setMenuId(menuId);
        }
        if(null != id){
            translateEntity4Sql.setId(id);
        }
        translateEntity4Sql.setTsType(tsType);
        List<TranslateEntity>  translateEntityList =  translateServiceDaoImpl.getZhCnTranslate(translateEntity4Sql,errorList);
        if(CollectionUtils.isEmpty(translateEntityList)){
            return;
        }
        //没有父节点，或者是叶子节点的删除
        if((null == translateEntityList.get(0).getFatherId() && id != null) || null == id){
            Object[] objArr = new Object[1];
            objArr[0] = translateEntityList.get(0).getId();
            objDeleteList.add(objArr);
            return;
        }else{
            //通过fatherId，查他下面有几个子节点，只有一个进行删除
            TranslateEntity translateEntity4SqlChildren = new TranslateEntity();
            translateEntity4SqlChildren.setFatherId(translateEntityList.get(0).getFatherId());
            translateEntity4SqlChildren.setTsType(tsType);
            List<TranslateEntity>  translateEntityList2 =  translateServiceDaoImpl.getZhCnTranslate(translateEntity4Sql,errorList);
            if(translateEntityList2.size() == 1){
                Object[] objArr = new Object[1];
                objArr[0] = translateEntityList.get(0).getFatherId();
                objDeleteList.add(objArr);
                getDeleteNode(null,translateEntityList.get(0).getFatherId(),tsType,objDeleteList,errorList);
            }else{
                //找到有多个子节点的直接退出
                return;
            }
        }
    }

    private void list2Map(List<TranslateDto> translateDtoList,Map map){
        for(TranslateDto translateDto : translateDtoList){
             if(CollectionUtils.isEmpty(translateDto.getChildrenList())){
                 map.put(translateDto.getSource(),translateDto.getValue());
             }else{
                 Map<String,Map> map1 = new HashMap();
                 map.put(translateDto.getSource(),map1);
                 list2Map(translateDto.getChildrenList(),map1);
             }
        }
    }
}
