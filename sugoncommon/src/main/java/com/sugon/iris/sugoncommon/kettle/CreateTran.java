package com.sugon.iris.sugoncommon.kettle;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.plugins.PluginRegistry;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.steps.insertupdate.InsertUpdateMeta;
import org.pentaho.di.trans.steps.tableinput.TableInputMeta;
import org.springframework.stereotype.Service;

//@Service
public class CreateTran{
    /**
     * 两个库中的表名
     */
    public static String bjdt_tablename = "lwz";
    public static String kettle_tablename = "TESTKETTLE";
    public static final String[] databasesXML = {
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<connection>" +
                    "<name>oracle</name>" +
                    "<server>192.168.217.140</server>" +
                    "<type>oracle</type>" +
                    "<access>连接类型</access>" +
                    "<database>orcl</database>" +
                    "<port>1521</port>" +
                    "<username>system</username>" +
                    "<password>123456</password>" +
                    "</connection>",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<connection>" +
                    "<name>mysql</name>" +
                    "<server>192.168.217.140</server>" +
                    "<type>mysql</type>" +
                    "<access>连接类型</access>" +
                    "<database>iris</database>" +
                    "<port>3306</port>" +
                    "<username>root</username>" +
                    "<password>root</password>" +
                    "</connection>"
    };


    /**
     * 生成一个转化,把一个数据库中的数据转移到另一个数据库中,只有两个步骤,第一个是表输入,第二个是表插入与更新操作
     * @return
     * @throws KettleXMLException
     */
    public TransMeta generateMyOwnTrans() throws KettleXMLException{

        System.out.println("************start to generate my own transformation***********");

        TransMeta transMeta = new TransMeta();

        //设置转化的名称
        transMeta.setName("insert_update");

        //添加转换的数据库连接
        for (int i=0;i<databasesXML.length;i++){
            DatabaseMeta databaseMeta = new DatabaseMeta(databasesXML[i]);
            transMeta.addDatabase(databaseMeta);
        }

        //registry是给每个步骤生成一个标识Id用
        PluginRegistry registry = PluginRegistry.getInstance();

        //******************************************************************

        //第一个表输入步骤(TableInputMeta)
        TableInputMeta tableInput = new TableInputMeta();
        String tableInputPluginId = registry.getPluginId(StepPluginType.class, tableInput);
        //给表输入添加一个DatabaseMeta连接数据库
        DatabaseMeta database_bjdt = transMeta.findDatabase("bjdt");
        tableInput.setDatabaseMeta(database_bjdt);
        String select_sql = "SELECT year, con1, CON2,CON3,CON4,AAA FROM "+bjdt_tablename;
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
        DatabaseMeta database_kettle = transMeta.findDatabase("kettle");
        insertUpdateMeta.setDatabaseMeta(database_kettle);
        //设置操作的表
        insertUpdateMeta.setTableName(kettle_tablename);

        //设置用来查询的关键字
        insertUpdateMeta.setKeyLookup(new String[]{"year"});
        insertUpdateMeta.setKeyStream(new String[]{"year"});
        insertUpdateMeta.setKeyStream2(new String[]{""});//一定要加上
        insertUpdateMeta.setKeyCondition(new String[]{"="});

        //设置要更新的字段
        String[] updatelookup = {"YEAR","CON1","CON2","CON3","CON4","AAA"} ;
        String [] updateStream = {"YEAR","CON1","CON2","CON3","CON4","AAA"};
        Boolean[] updateOrNot = {false,true,true,true,true,true,true};
        insertUpdateMeta.setUpdateLookup(updatelookup);
        insertUpdateMeta.setUpdateStream(updateStream);
        insertUpdateMeta.setUpdate(updateOrNot);
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

    /*
    public static void main(String[] args) {
        try {
            KettleEnvironment.init();
            CreateTran transDemo = new CreateTran();
            TransMeta transMeta = transDemo.generateMyOwnTrans();
            String transXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+transMeta.getXML();
            //System.out.println("transXml:"+transXml);
            String transName = "C:\\kettle_test\\copy-one-table.ktr";
            File file = new File(transName);
            FileUtils.writeStringToFile(file, transXml, "UTF-8");

            //System.out.println(databasesXML.length+"\n"+databasesXML[0]+"\n"+databasesXML[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
*/
}
