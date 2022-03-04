package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.BussLog;
import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.beans.system.User;
import com.sugon.iris.sugondomain.dtos.systemDtos.UserDto;
import com.sugon.iris.sugonservice.service.systemService.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 主页控制器
 */
@Api(value = "后台管理", tags = "主页控制器")
@Controller
public class IndexController {

    @Autowired
    private AccountService accountServiceImpl;


    @GetMapping(value = "/")
    public String index() {

        return "index.html";
    }

    @PostMapping(value = "/checkLoginLok")
    @LogInCheck(doLock = true,doProcess = true)
    @ResponseBody
    @ApiOperation(value = "校验登录和锁定")
    public RestResult<Object>  checkLoginLok() {
        RestResult<Object> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        restResult.setErrorList(errorList);
        return restResult;
    }

    @ApiOperation(value = "ca用户登录")
    @RequestMapping("/account/loginForCa")
    @BussLog
    @ResponseStatus(HttpStatus.CREATED)
    @ApiImplicitParam(name = "UserDto", value = "用户信息")
    public void loginForCa(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws IOException {
        //警号
        String code = request.getRemoteUser();
        //身份证号
        AttributePrincipal principal =(AttributePrincipal) request.getUserPrincipal();
        Map m = principal.getAttributes();
        String sfzh = String.valueOf(m.get("idcard"));

        UserDto userDto = new UserDto();
        userDto.setPoliceNo(code);
        userDto.setIdCard(sfzh);

        List<Error> errorList = new ArrayList<>();
        User user = null;
        try {
            user = accountServiceImpl.getUserInfoForCa(userDto, errorList);
        }catch (Exception e){
            e.printStackTrace();
        }
        session .setAttribute("user",user);
        Cookie cookie = new Cookie("jsessionid", session.getId());
        response.addCookie(cookie);
        //以秒为单位，即在没有活动120分钟后，session将失效
        //设置默认值30分钟
        if(StringUtils.isEmpty(PublicUtils.getConfigMap().get("systemExpiration"))){
            session.setMaxInactiveInterval(30*60);
        }else {
            session.setMaxInactiveInterval(Integer.parseInt(PublicUtils.getConfigMap().get("systemExpiration")) * 60);
        }
        response.sendRedirect("http://50.73.71.248:8090/#/page/home");
    }

    @ApiOperation(value = "ca用户登录")
    @RequestMapping("/account/loginForCaTest")
    @BussLog
    @ResponseStatus(HttpStatus.CREATED)
    public void loginForCaTest(HttpServletResponse response,  HttpSession session,@RequestParam(value = "code") String  code,@RequestParam(value = "sfzh") String  sfzh) throws IOException {

        UserDto userDto = new UserDto();
        userDto.setPoliceNo(code);
        userDto.setIdCard(sfzh);

        List<Error> errorList = new ArrayList<>();
        User user = null;
        try {
            user = accountServiceImpl.getUserInfoForCa(userDto, errorList);
        }catch (Exception e){
            e.printStackTrace();
        }
        session .setAttribute("user",user);
        Cookie cookie = new Cookie("jsessionid", session.getId());
        response.addCookie(cookie);
        //以秒为单位，即在没有活动120分钟后，session将失效
        //设置默认值30分钟
        if(StringUtils.isEmpty(PublicUtils.getConfigMap().get("systemExpiration"))){
            session.setMaxInactiveInterval(30*60);
        }else {
            session.setMaxInactiveInterval(Integer.parseInt(PublicUtils.getConfigMap().get("systemExpiration")) * 60);
        }
        //response.sendRedirect("http://50.73.71.248:8090/#/app/dashboard");
    }

    //重定向测试(测试)
    @RequestMapping("/account/getUserInfo4CaTest")
    @ApiOperation(value = "重定向测试")
    public void  getUserInfo4CaTest(HttpServletResponse response) throws IOException {
       // return "indexCa.html";
        response.sendRedirect("http://192.168.217.90:8090/#/app/dashboard");
    }
}