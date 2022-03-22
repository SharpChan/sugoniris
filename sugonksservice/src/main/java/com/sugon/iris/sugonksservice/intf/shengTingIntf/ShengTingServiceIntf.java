package com.sugon.iris.sugonksservice.intf.shengTingIntf;

import com.sugon.iris.sugondomain.beans.shengTing.ShanghaiMinhangBean;
import com.sugon.iris.sugondomain.beans.shengTing.ShanghaikeyunBean;
import com.sugon.iris.sugondomain.beans.shengTing.base.StResponse;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ShengTingServiceIntf {


    List<StResponse>  getKyxx(String cardId, String minRownum, String maxRownum);

    List<StResponse> getMinHanxx(String cardId, String minRownum, String maxRownum);

    List<StResponse<List<ShanghaiMinhangBean>>> getMinHanxxByExcel(List<MultipartFile> files) throws Exception;

    List<StResponse<List<ShanghaikeyunBean>>> getKeyunByExcel(List<MultipartFile> files) throws Exception;
}
