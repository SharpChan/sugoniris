package com.sugon.iris.sugondomain.beans.system;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class BusinessLog {

    private Long id;

    private Long userId;

    private String business;

    private String ip;

    private Timestamp accessTime;
}
