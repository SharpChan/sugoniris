package com.sugon.iris.sugonweb.dataSync;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugonservice.service.dataSyncService.DataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;


@WebListener
@EnableScheduling   // 2.开启定时任务
public class JianMoListener implements ServletContextListener {

   @Autowired
   private DataSyncService dataSyncServiceImpl;



    @Scheduled(fixedRate=10000)
  public void dataSync() throws Exception{

        try {
            List<Error> errorList = new ArrayList<>();
            dataSyncServiceImpl.dataSync(errorList);
        }catch (Exception e) {
            e.printStackTrace ();
        }
  }

}
