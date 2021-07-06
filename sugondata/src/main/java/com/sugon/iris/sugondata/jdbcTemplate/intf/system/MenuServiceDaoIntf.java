package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.beans.system.Menu;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.MenuEntity;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.List;

public interface MenuServiceDaoIntf {

       public List<MenuEntity> getMenuInfos(MenuEntity menuEntity, List<Error> errorList);

       public MenuEntity getNode(Long id, List<Error> errorList);

       public int deleteMenu(Long id, List<Error> errorList);

       public int insertMenu(MenuEntity MenuEntity, List<Error> errorList);

       public int updateMenu(MenuEntity MenuEntity, List<Error> errorList);

       public Long getMenu_seq(List<Error> errorList);

       public void initMenuSeq();
}
