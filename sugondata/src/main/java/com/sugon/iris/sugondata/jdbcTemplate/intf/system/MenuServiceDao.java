package com.sugon.iris.sugondata.jdbcTemplate.intf.system;

import com.sugon.iris.sugondomain.beans.system.Menu;
import com.sugon.iris.sugondomain.entities.jdbcTemplateEntity.systemEntities.MenuEntity;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.List;

public interface MenuServiceDao {

        List<MenuEntity> getMenuInfos(MenuEntity menuEntity, List<Error> errorList);

        List<MenuEntity> getMenuInfosByUserId(Long userId, List<Error> errorList);

        MenuEntity getNode(Long id, List<Error> errorList);

        int deleteMenu(Long id, List<Error> errorList);

        int insertMenu(MenuEntity MenuEntity, List<Error> errorList);

        int updateMenu(MenuEntity MenuEntity, List<Error> errorList);

        Long getMenu_seq(List<Error> errorList);

        void initMenuSeq();
}
