package com.sugon.iris.sugonservice.service.dataSyncService;

import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import java.util.List;

public interface DataSyncService {

  void   dataSync(List<Error> errorList);

}
