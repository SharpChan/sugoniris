package com.sugon.iris.sugonfilerest.job;

import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTableMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateDetailMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db2.FileTemplateMapper;
import com.sugon.iris.sugondata.mybaties.mapper.db4.MppMapper;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTableEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateDetailEntity;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.FileTemplateEntity;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TestXxlJob {
    private static Logger logger = LoggerFactory.getLogger(TestXxlJob.class);

    @Resource
    private FileTemplateMapper fileTemplateMapper;

    @Resource
    private FileTableMapper fileTableMapper;

    @Resource
    private MppMapper mppMapper;

    @Resource
    private FileTemplateDetailMapper fileTemplateDetailMapper;

    @XxlJob("testJobHandler")
    public void demoJobHandler() throws Exception {

        XxlJobHelper.log("XXL-JOB, Hello World.");
        logger.info("aaa");

    }

    //经侦模板组的表汇聚
    @XxlJob("convergeJobHandler")
    public void convergeJobHandler() throws Exception {

        XxlJobHelper.log("XXL-JOB, convergeJob.");
        log.info("数据聚合开始");
        //获取所有的模板信息
        List<FileTemplateEntity> fileTemplateEntityList = fileTemplateMapper.selectFileTemplateForJingZhen();

        //获取所有的表信息
        List<FileTableEntity> fileTableEntityList = fileTableMapper.findFileTableList(new FileTableEntity());

        if(CollectionUtils.isEmpty(fileTemplateEntityList) || CollectionUtils.isEmpty(fileTableEntityList)){
             return;
        }

        Map<String,List<FileTableEntity>> map = new HashMap<>();
        for(FileTemplateEntity fileTemplateEntity : fileTemplateEntityList){
            List<FileTableEntity> fileTableEntityList_01 = new ArrayList<>();
             for(FileTableEntity fileTableEntity : fileTableEntityList){
                  if(fileTemplateEntity.getId().equals(fileTableEntity.getFileTemplateId())){
                      fileTableEntityList_01.add(fileTableEntity);
                  }
             }
             map.put(fileTemplateEntity.getTablePrefix(),fileTableEntityList_01);
        }

        //进行数据汇聚，creare  table tableName  as select 方式
        for(Map.Entry<String,List<FileTableEntity>> entry : map.entrySet()){

          if(CollectionUtils.isEmpty(entry.getValue())){
             continue;
          }

          //判断表是否存在，存在则删除
            String sql = "select count(*) from pg_class where relname = '"+entry.getKey()+"'";
            int count = Integer.parseInt(mppMapper.mppSqlExecForSearch(sql).get(0));
            //表存在则删除
            if(count > 0 ){
               String deleteSQL = "drop table " +entry.getKey();
                mppMapper.mppSqlExec(deleteSQL);
            }
           //删除后重新创建
          StringBuilder sb = new StringBuilder("Create table "+entry.getKey()+" as ");
          int i = 1;
          for(FileTableEntity fileTableEntity : entry.getValue()){
              if(i==  entry.getValue().size()) {
                  sb.append(" select * from " + fileTableEntity.getTableName());

              }else{
                  sb.append(" select * from " + fileTableEntity.getTableName() + " union ");
              }
              i++;
          }
            mppMapper.mppSqlExec(sb.toString());
        }

        //添加字段注释
        for(FileTemplateEntity fileTemplateEntity : fileTemplateEntityList){
            //判断表是否存在
            String sql = "select count(*) from pg_class where relname = '"+fileTemplateEntity.getTablePrefix()+"'";
            int count = Integer.parseInt(mppMapper.mppSqlExecForSearch(sql).get(0));
            if(count == 0){
                continue;
            }
            //通过模板id查询字段
            List<FileTemplateDetailEntity> fileTemplateDetailEntityList = fileTemplateDetailMapper.selectFileTemplateDetailByTemplateId(fileTemplateEntity.getId());
            for(FileTemplateDetailEntity fileTemplateDetailEntity : fileTemplateDetailEntityList){
               String commentSql = "COMMENT ON COLUMN "+fileTemplateEntity.getTablePrefix()+"."+fileTemplateDetailEntity.getFieldName() +" IS '"+fileTemplateDetailEntity.getFieldKey()+"'";
                mppMapper.mppSqlExec(commentSql);
            }
        }
        log.info("数据聚合结束");
    }
}
