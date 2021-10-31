package com.sugon.iris.sugonweb.nginx;


import com.sugon.iris.sugonannotation.annotation.system.LogInCheck;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.dtos.securityModuleDtos.WhiteIpDto;
import com.sugon.iris.sugonservice.service.securityModuleService.WhiteIpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/nginx")
@Api(value = "安全模块", tags = "通过nginx反向代理进行安全控制相关接口")
public class NginxController {
    private static final String FAILED = "FAILED";

    @Autowired
    private WhiteIpService whiteIpServiceImpl;

    //@Autowired
    //private NginxLogListener nginxLogListener;

    @PostMapping("/save/whiteIp")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "保存白名单信息")
    @ApiImplicitParam(name = "whiteIpDto", value = "白名单信息")
    public RestResult<Integer> saveWhiteIp(@Valid @RequestBody WhiteIpDto whiteIpDto) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(whiteIpServiceImpl.saveWhiteIp(whiteIpDto,errorList));
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("保存失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("保存成功");
        }
        return restResult;
    }

    @PostMapping("/queryWhiteList")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "查询白名单信息")
    @ApiImplicitParam(name = "whiteIpDto", value = "白名单信息")
    public RestResult<List<WhiteIpDto>> queryWhiteList(@RequestBody WhiteIpDto whiteIpDto) {
        RestResult<List<WhiteIpDto>> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(whiteIpServiceImpl.getWhiteIpList(whiteIpDto,errorList));
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("查询失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("查询成功");
        }
        return restResult;
    }

    @PostMapping("/deleteWhiteIp")
    @LogInCheck(doLock = true,doProcess = true)
    @ApiOperation(value = "删除白名单信息")
    @ApiImplicitParam(name = "id", value = "白名单信息id")
    public RestResult<Integer> deleteWhiteIp(@RequestParam(value = "id") Long id) {
        RestResult<Integer> restResult = new RestResult();
        List<Error> errorList = new ArrayList<>();
        try {
            restResult.setObj(whiteIpServiceImpl.deleteWhiteIp(id,errorList));
        }catch(Exception e){
            e.printStackTrace();
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage("删除失败！");
            restResult.setErrorList(errorList);
        }else{
            restResult.setMessage("删除成功");
        }
        return restResult;
    }

    //@PostMapping("/test")
    //public void test() throws Exception {
        //nginxLogListener.saveNginxLogs();
   // }
}
