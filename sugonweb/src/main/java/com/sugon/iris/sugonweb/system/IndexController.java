package com.sugon.iris.sugonweb.system;

import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页控制器
 */
@Controller
public class IndexController {



    @RequestMapping(value = "/")
    public String index() {

        return "index.html";
    }

    @RequestMapping(value = "/checkLoginLok")
    @LogInCheck(doLock = true,doProcess = true)
    @ResponseBody
    public RestResult<Object>  checkLoginLok() {
        RestResult<Object> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        restResult.setErrorList(errorList);
        return restResult;
    }
}