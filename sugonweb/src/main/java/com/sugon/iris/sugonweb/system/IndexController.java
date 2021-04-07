package com.sugon.iris.sugonweb.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主页控制器
 */
@Controller
public class IndexController {



    @RequestMapping(value = "/")
    public String index() {

        return "index.html";
    }
}