package com.sugon.iris.sugonservice.impl.userInfoImpl;


import com.sugon.iris.sugondata.mybaties.mapper.db2.RoleMapper;
import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RoleEntity;
import com.sugon.iris.sugonservice.service.userInfoService.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<RoleEntity> selectRoleList() {
        return this.roleMapper.selectRoleList();
    }

    // 注：不是主数据源必须要声明其数据源，否则事务不起作用
    @Transactional(value = "db2TransactionManager")
    @Override
    public void saveRole(RoleEntity role) {
        this.roleMapper.saveRole(role);
//        throw new RuntimeException();
    }
}