package com.sugon.iris.sugondomain.xstream.systemLog;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("Log")
public class Log {

    @XStreamAlias("xtid")
    private String xtid;

    @XStreamAlias("xtmc")
    private String xtmc;

    @XStreamAlias("mkid")
    private String mkid;

    @XStreamAlias("mkmc")
    private String mkmc;

    @XStreamAlias("yhzh")
    private String yhzh;

    @XStreamAlias("yhxm")
    private String yhxm;

    @XStreamAlias("yhjh")
    private String yhjh;

    @XStreamAlias("yhsfzh")
    private String yhsfzh;

    @XStreamAlias("yhdwmc")
    private String yhdwmc;

    @XStreamAlias("yhdwdm")
    private String yhdwdm;

    @XStreamAlias("zddz")
    private String  zddz;

    @XStreamAlias("czlx")
    private String  czlx;

    @XStreamAlias("cztj")
    private String  cztj;

    @XStreamAlias("czsj")
    private String  czsj;

    @XStreamAlias("czjg")
    private String  czjg;

    @XStreamAlias("cznr")
    private String  cznr;

    @XStreamAlias("sbyy")
    private String  sbyy;

    @XStreamAlias("ywxtrzid")
    private String  ywxtrzid;
}
