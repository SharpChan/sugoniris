package com.sugon.iris.sugoncommon.kettle;

import com.jcraft.jsch.Session;
import com.sugon.iris.sugoncommon.SSHRemote.SSHConfig;
import com.sugon.iris.sugoncommon.SSHRemote.SSHServiceBs;
import com.sugon.iris.sugoncommon.publicUtils.PublicUtils;
import com.sugon.iris.sugondomain.beans.baseBeans.Error;
import com.sugon.iris.sugondomain.enums.DBType_Enum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Component
public class KettleService {

    @Value("${kettle.script.path}")
    private String dirPath;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    /**
     * 生成ktr文件然后kettle执行
     * @param source
     */
    public boolean createKtrAndUploadToServer(Map<String, String> source) {
        String ip = (String)source.get("ip");
        String port = (String)source.get("port");
        int dataType = Integer.valueOf(source.get("type"));
        String userName = (String)source.get("user");
        String password = (String)source.get("pass");
        String instanceName = (String)source.get("instance");
        String dbname = (String) source.get("dbname");
        String original = (String)source.get("original");
        String target = (String)source.get("target");
        String mppusername = (String)source.get("mppusername");
        String mpppassword = (String) source.get("mpppassword");
        String mppport = (String)source.get("mppport");
        String mppip = (String)source.get("mppip");
        String mppcatalog = (String)source.get("mppcatalog");
        String columns = (String) source.get("columns");
        String dataBaseType="";
        if (DBType_Enum.MYSQL.getnCode() == dataType)
        {
            dataBaseType = "mysql";
        }
        if (DBType_Enum.ORACLE.getnCode() == dataType)
        {
            dataBaseType = "oracle";
        }
        if (DBType_Enum.SQLSERVER.getnCode() == dataType)
        {
            dataBaseType = "sqlserver";
        }
        if (DBType_Enum.DAMENG.getnCode() == dataType)
        {
            dataBaseType = "DAMENG";
        }

        String[] databasesXML = {
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<connection>" +
                        "<name>"+dataBaseType+"</name>" +
                        "<server>"+ip+"</server>" +
                        "<type>"+dataBaseType+"</type>" +
                        "<access>连接类型</access>" +
                        "<database>"+dbname+"</database>" +
                        "<port>"+port+"</port>" +
                        "<username>"+userName+"</username>" +
                        "<password>"+password+"</password>" +
                        "</connection>",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<connection>" +
                        "<name>public</name>" +
                        "<server>"+mppip+"</server>" +
                        "<type>PostgreSQL</type>" +
                        "<access>Native</access>" +
                        "<database>"+mppcatalog+"</database>" +
                        "<port>"+mppport+"</port>" +
                        "<username>"+mppusername+"</username>" +
                        "<password>"+mpppassword+"</password>" +
                        "</connection>"
        };
        log.info("XML"+databasesXML.toString());
        TransMeta transMeta = null;
        String ktrName = target+"-trans.ktr";
        String transName = "";
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            transName = "E:\\ktr\\"+ktrName;
        }
        else {
            transName = "/usr/local/kettle/ktr/" + ktrName;
        }
        try {
            KettleEnvironment.init();
            transMeta  = generateTrans(databasesXML,original,target,columns);
            String transXml = transMeta.getXML();
            File file = new File(transName);
            FileUtils.writeStringToFile(file, transXml, "UTF-8");
        } catch (KettleXMLException e) {
            e.printStackTrace();
            return false;
        } catch (KettleException | IOException e) {
            e.printStackTrace();
            return false;
        }

        Session session = null;
        String kettleUserName = PublicUtils.getConfigMap().get("kettle.linux.userName");
        String kettlePassword = PublicUtils.getConfigMap().get("kettle.linux.password");
        String kettleIpAddress = PublicUtils.getConfigMap().get("kettle.linux.ipAddress");
        int kettlePort =Integer.parseInt(PublicUtils.getConfigMap().get("kettle.linux.port"));
        try {
            session = new SSHConfig(kettleUserName,kettlePassword,kettleIpAddress,kettlePort).getSession();
            if(System.getProperty("os.name").toLowerCase().contains("win")) {
                String strCmd = "E:\\data-integration\\Pan.bat /file E:\\ktr\\"+ktrName;
                Runtime runtime = Runtime.getRuntime();
                try {
                      runtime.exec(strCmd);
                    } catch (Exception e) {
                    e.printStackTrace();
                  System.out.println("Error!");
                 }
            }else{
                SSHServiceBs sSHServiceBs = new SSHServiceBs(session);
                sSHServiceBs.execCommand("sh /usr/local/kettle/data-integration/pan.sh -file=/usr/local/kettle/ktr/" + ktrName);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

    }

    /**
     * 生成一个转化,把一个数据库中的数据转移到另一个数据库中,只有两个步骤,第一个是表输入,第二个是表插入与更新操作
     * @return
     * @throws KettleXMLException
     */
    public TransMeta generateTrans(String[] databasesXML,String original,String target,String columns) throws KettleXMLException{

        System.out.println("************start to generate my own transformation***********");

        TransMeta transMeta = new TransMeta();

        //设置转化的名称
        transMeta.setName("insert_update");

        //添加转换的数据库连接
        for (int i=0;i<databasesXML.length;i++){
            DatabaseMeta databaseMeta = new DatabaseMeta(databasesXML[i]);
            transMeta.addDatabase(i,databaseMeta);
        }

        //registry是给每个步骤生成一个标识Id用
        PluginRegistry registry = PluginRegistry.getInstance();

        //******************************************************************

        //第一个表输入步骤(TableInputMeta)
        TableInputMeta tableInput = new TableInputMeta();
        String tableInputPluginId = registry.getPluginId(StepPluginType.class, tableInput);
        //给表输入添加一个DatabaseMeta连接数据库
        DatabaseMeta database_bjdt = transMeta.getDatabase(0);
        tableInput.setDatabaseMeta(database_bjdt);
        String select_sql = "SELECT "+columns+" FROM "+original;
        tableInput.setSQL(select_sql);

        //添加TableInputMeta到转换中
        StepMeta tableInputMetaStep = new StepMeta(tableInputPluginId,"table input",tableInput);

        //给步骤添加在spoon工具中的显示位置
        tableInputMetaStep.setDraw(true);
        tableInputMetaStep.setLocation(100, 100);

        transMeta.addStep(tableInputMetaStep);
        //******************************************************************

        //******************************************************************
        //第二个步骤插入与更新
        InsertUpdateMeta insertUpdateMeta = new InsertUpdateMeta();
        String insertUpdateMetaPluginId = registry.getPluginId(StepPluginType.class,insertUpdateMeta);
        //添加数据库连接
        DatabaseMeta database_kettle = transMeta.getDatabase(1);
        insertUpdateMeta.setDatabaseMeta(database_kettle);
        //设置操作的表
        insertUpdateMeta.setTableName(target);
        String[] cc = columns.split(",");
        //设置用来查询的关键字
        insertUpdateMeta.setKeyLookup(new String[]{cc[0]});
        insertUpdateMeta.setKeyStream(new String[]{cc[0]});
        insertUpdateMeta.setKeyStream2(new String[]{""});//一定要加上
        insertUpdateMeta.setKeyCondition(new String[]{"="});

        //设置要更新的字段
        String[] updatelookup = cc ;
        String [] updateStream = cc;
        Boolean[] bs = new Boolean[cc.length];
        for(int i=0;i<bs.length;i++){
            bs[i] = false;
        }
        insertUpdateMeta.setUpdateLookup(updatelookup);
        insertUpdateMeta.setUpdateStream(updateStream);
        insertUpdateMeta.setUpdate(bs);
        String[] lookup = insertUpdateMeta.getUpdateLookup();
        //System.out.println("******:"+lookup[1]);
        //System.out.println("insertUpdateMetaXMl:"+insertUpdateMeta.getXML());
        //添加步骤到转换中
        StepMeta insertUpdateStep = new StepMeta(insertUpdateMetaPluginId,"insert_update",insertUpdateMeta);
        insertUpdateStep.setDraw(true);
        insertUpdateStep.setLocation(250,100);
        transMeta.addStep(insertUpdateStep);
        //******************************************************************

        //******************************************************************
        //添加hop把两个步骤关联起来
        transMeta.addTransHop(new TransHopMeta(tableInputMetaStep, insertUpdateStep));
        System.out.println("***********the end************");
        return transMeta;
    }
}
