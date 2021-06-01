package com.sugon.iris.sugonrest.kettle;

import com.sugon.iris.sugoncommon.kettle.KettleService;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.beans.baseBeans.RestResult;
import com.sugon.iris.sugondomain.enums.ErrorCode_Enum;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/kettle")
public class KettleController {

    private KettleService kettleService;

    private static final String MESSAGE_01 = "[通过kettle执行ktr文件失败]";

    private static final String MESSAGE_02 = "[通过kettle执行kjb文件失败]";

    private static final String FAILED = "FAILED";

    private static final String SUCCESS = "FAILED";

    private static final String SUCCESS_MESSAGE = "[查询成功]";

    private static final String FAILED_MESSAGE = "[查询失败]";

    @PostMapping("patientTrans")
    public RestResult<Boolean> patientTrans () {

        RestResult<Boolean> restResult = new RestResult<Boolean>();
        List<Error> errorList = new ArrayList<>();
        String fileName = "patientTrans.ktr";
        try {
            restResult.setObj(kettleService.runKtr(fileName, null,errorList));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(), ErrorCode_Enum.SYS_SUGON_001.getMessage()+MESSAGE_01,e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setFlag(SUCCESS);
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;

    }

    @PostMapping("patientIn")
    public RestResult<Boolean> patientIn () {
        RestResult<Boolean> restResult = new RestResult<Boolean>();
        List<Error> errorList = new ArrayList<>();
        String fileName = "patientTrans.ktr";
        try {
            restResult.setObj(kettleService.runKjb(fileName, null,errorList));
        }catch (Exception e){
            e.printStackTrace();
            errorList.add(new Error(ErrorCode_Enum.SYS_SUGON_001.getCode(), ErrorCode_Enum.SYS_SUGON_001.getMessage()+MESSAGE_02,e.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            restResult.setFlag(FAILED);
            restResult.setMessage(FAILED_MESSAGE);
            restResult.setErrorList(errorList);
        }else{
            restResult.setFlag(SUCCESS);
            restResult.setMessage(SUCCESS_MESSAGE);
        }
        return restResult;

    }
}
