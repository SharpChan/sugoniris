package com.sugon.iris.sugondomain.xstream.systemLog;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("Aspt")
public class Aspt {

    @XStreamAlias("Version")
    private String version;

    @XStreamAlias("RegID")
    private String regID;

    @XStreamAlias("Logs")
    List<Log> logs;
}
