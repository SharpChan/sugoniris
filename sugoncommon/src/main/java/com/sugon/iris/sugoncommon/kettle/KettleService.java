package com.sugon.iris.sugoncommon.kettle;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class KettleService {

    @Value("${kettle.script.path}")
    private String dirPath;

    /**
     * 执行ktr文件
     * @param filename
     * @param params
     * @return
     */
    public boolean runKtr(String filename, Map<String, String> params, List<Error> errorList) {
        try {
            KettleEnvironment.init();
            TransMeta tm = new TransMeta(dirPath + File.separator + filename);
            Trans trans = new Trans(tm);
            if (params != null) {
                Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, String> entry = entries.next();
                    trans.setParameterValue(entry.getKey(),entry.getValue());
                }
            }
            trans.execute(null);
            trans.waitUntilFinished();
        } catch (Exception e) {
            errorList.add(new Error("sys-kettle-001","kettle调用ktr执行失败",e.toString()));
            return false;
        }
        return true;
    }

    /**
     * 执行kjb文件
     * @param filename
     * @param params
     * @return
     */
    public boolean runKjb(String filename, Map<String, String> params,List<Error> errorList) {
        try {
            KettleEnvironment.init();
            JobMeta jm = new JobMeta(dirPath + File.separator + filename, null);
            Job job = new Job(null, jm);
            if (params != null) {
                Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, String> entry = entries.next();
                    job.setVariable(entry.getKey(), entry.getValue());
                }
            }
            job.start();
            job.waitUntilFinished();
        } catch (Exception e) {
            errorList.add(new Error("sys-kettle-001","kettle调用执行kjb失败",e.toString()));
            return false;
        }
        return true;
    }
}
