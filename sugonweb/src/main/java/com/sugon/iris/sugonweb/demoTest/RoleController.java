package com.sugon.iris.sugonweb.demoTest;


import com.sugon.iris.sugondomain.entities.mybatiesEntity.db2.RoleEntity;
import com.sugon.iris.sugonservice.service.systemService.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/webrole")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/select/list")
    public List<RoleEntity> selectRoleList() {

        return this.roleService.selectRoleList();
    }

    @GetMapping("/save")
    public void saveRole(RoleEntity role) {

        this.roleService.saveRole(role);
    }


}