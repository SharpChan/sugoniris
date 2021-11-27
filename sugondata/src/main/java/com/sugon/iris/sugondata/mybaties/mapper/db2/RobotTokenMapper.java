package com.sugon.iris.sugondata.mybaties.mapper.db2;

import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RebotEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface RobotTokenMapper {

    //插入
    @Insert("insert into robot_token(token,date,case_id,policeno) values(#{token},#{date},#{caseId},#{policeno})")
    public int insertRobotToken(RebotEntity rebotEntity);

    //通过id查询
    @Select("select * from robot_token where id = #{id}")
    public RebotEntity selectRobotTokenById(Long  id);

    //通过token查询
    @Select("select * from robot_token where token = #{token}")
    public RebotEntity selectRobotTokenByToken(String token);

    //修改
    @Update("update robot_token set date =#{date} where token = #{token}")
    public RebotEntity updateRobotTokenByToken(String  token);

}
