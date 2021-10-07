package com.sugon.iris.sugondata.mybaties.mapper.db4;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db4.MppErrorInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

//@Mapper
public interface MppErrorInfoMapper {

    @Select("select nextval('error_seq')")
    public Long selectErrorSeq();

    //查询
    @Select("select * from error_info where id=#{id}")
    public MppErrorInfoEntity selectMppErrorInfoById(Long id);



    //删除
    @Delete("delete from error_info where id=#{id}")
    public int deleteMppErrorInfoById(Long id);


    //插入
    @Insert("insert into error_info(file_attachment_id,file_detail_id,mppid2errorid,file_rinse_detail_id) values(#{fileAttachmentId},#{fileDetailId},#{mppid2errorid},#{fileRinseDetailId})")
    public int insertMppErrorInfo(MppErrorInfoEntity mppErrorInfoEntity4Sql);



    //修改
    @Update("update error_info set file_attachment_id=#{fileAttachmentId},file_detail_id=#{fileDetailId},mppid2errorid=#{mppid2errorid},file_rinse_detail_id=#{fileRinseDetailId} where id=#{id}")
    public int UpdateMppErrorInfo(MppErrorInfoEntity mppErrorInfoEntity4Sql);

    public int errorInfoListInsert(List<MppErrorInfoEntity> mppErrorInfoEntityList);

}
