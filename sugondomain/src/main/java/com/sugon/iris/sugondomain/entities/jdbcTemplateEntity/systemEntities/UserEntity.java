package com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities;

import com.sugon.iris.sugondomain.beans.system.User;
import lombok.Data;

@Data
public class UserEntity extends User  {

    //建模平台的用户 id
   private Integer jmUserId;

}
