package com.sugon.iris.sugondomain.dtos.systemDtos;

import com.sugon.iris.sugondomain.beans.system.BusinessLog;
import lombok.Data;

@Data
public class BusinessLogDto extends BusinessLog {

    private String time;
}
