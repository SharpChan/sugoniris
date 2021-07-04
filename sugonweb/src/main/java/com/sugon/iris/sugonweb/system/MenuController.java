package com.sugon.iris.sugonweb.system;


import com.sugon.iris.sugonannotation.annotation.system.CurrentUser;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.MenuDto;
import com.sugon.iris.sugonservice.service.systemService.MenuService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private static final String FAILED = "FAILED";

    @Resource
    private MenuService menuServiceImpl;

    /**
     * 菜单注册
     */
    @PostMapping("/saveMenu")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<Integer> saveMenu(@CurrentUser User user, @RequestBody MenuDto menuDto){
        menuDto.setUserId(user.getId());
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.saveMenu(menuDto,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }

    /**
     * 选择父节点
     */
    @PostMapping("/getFatherMenu")
    @LogInCheck(doLock = true,doProcess = true)
    public RestResult<List<MenuDto>> getFatherMenu(@CurrentUser User user, @RequestBody MenuDto menuDto){
        menuDto.setUserId(user.getId());
        RestResult<List<MenuDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try{
            restResult.setObj(menuServiceImpl.getMenu(menuDto,errorList));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setErrorList(errorList);
            restResult.setMessage("操作失败");
            return  restResult;
        }else{
            restResult.setMessage("操作成功");
        }
        return restResult;
    }
}
