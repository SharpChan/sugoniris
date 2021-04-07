package com.sugon.iris.sugondata.jpa.intf.system;

import com.sugon.iris.sugondomain.beans.menuBeans.Menu;
import com.sugon.iris.sugondomain.entities.menuEntities.MenuEntity;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.List;

public interface MenuServiceDaoIntf {

       public List<MenuEntity> getMenuInfos(Menu menu, List<Error> errorList);

       public int deleteMenu(Long id, List<Error> errorList);

       public int insertMenu(MenuEntity MenuEntity, List<Error> errorList);

       public int updateMenu(MenuEntity MenuEntity, List<Error> errorList);

       public Long getMenu_seq(List<Error> errorList);

       public void initMenuSeq();
}
