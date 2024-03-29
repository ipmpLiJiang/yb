package cc.mrbird.febs.common.utils;

import cc.mrbird.febs.common.properties.JcoProperties;
import cc.mrbird.febs.yb.entity.YbTYDJB;
import cc.mrbird.febs.yb.entity.YbWHYBYS;
import cc.mrbird.febs.yb.entity.YbZZDJB;
import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class RfcNOC {
    private static final String ABAP_AS_POOLED = "ECC";
    String message = "";

    //    @Autowired
//    private JcoProperties jcoProperties;
    static {
        Properties connectProperties = new Properties();
//        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, JcoProperties.getAshost());//服务器
//        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, JcoProperties.getSysnr());        //系统编号
//        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, JcoProperties.getClient());       //SAP集团
//        connectProperties.setProperty(DestinationDataProvider.JCO_USER, JcoProperties.getUser());  //SAP用户名
//        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, JcoProperties.getPassw());     //密码
//        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, JcoProperties.getLang());        //登录语言

        // 正式的地址
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "192.168.64.26");//服务器
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, "01");        //系统编号
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "800");       //SAP集团
        connectProperties.setProperty(DestinationDataProvider.JCO_USER, "COM_SCM");  //SAP用户名
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "822019");     //密码
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG, "ZH");        //登录语言


        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, "5");  //最大连接数
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT, "10");     //最大连接线程

        createDataFile(ABAP_AS_POOLED, "jcoDestination", connectProperties);
    }

    /**
     * 创建SAP接口属性文件。
     *
     * @param name       ABAP管道名称
     * @param suffix     属性文件后缀
     * @param properties 属性文件内容
     */
    private static void createDataFile(String name, String suffix, Properties properties) {
        File cfg = new File(name + "." + suffix);
        if (cfg.exists()) {
            cfg.deleteOnExit();
        }
        try {
            FileOutputStream fos = new FileOutputStream(cfg, false);
            properties.store(fos, "for tests only !");
            fos.close();
        } catch (Exception e) {
            log.error("Create Data file fault, error msg: " + e.toString());
            throw new RuntimeException("Unable to create the destination file " + cfg.getName(), e);
        }
    }

    /**
     * 获取SAP连接
     *
     * @return SAP连接对象
     */
    public static JCoDestination GetDestination() {
        JCoDestination destination = null;

        try {
            destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
        } catch (JCoException e) {
            log.error("Connect SAP fault, error msg: " + e.toString());

        }
        return destination;
    }

    /**
     * 武汉市医保应收明细表
     *
     * @param ZYB_YQ 院区
     * @param IPERI  工资发放所在期
     * @return
     */
    public List<YbWHYBYS> getWhYbysList(int ZYB_YQ, String IPERI) throws Exception {
        String fuName = "ZYB00_FM_001";
        log.info("getWhYbysList begin");
        List<YbWHYBYS> list = new ArrayList<>();
        JCoDestination destination;
        boolean isError = false;
        try {
            destination = RfcNOC.GetDestination();
            if (destination == null) {
                log.error("配置信息出错");
                return null;
            }

            JCoFunction myfun = null;
            myfun = destination.getRepository().getFunction(fuName);
            if (myfun == null)
                log.info(fuName + " is null");

            myfun.getImportParameterList().setValue("ZYB_YQ", ZYB_YQ);
            myfun.getImportParameterList().setValue("IPERI", IPERI);

            //提前实例化一个空的表结构出来
            myfun.execute(destination);//执行
            log.info(fuName + "myfun.Invoke succeed");
            JCoTable rfcReturn = myfun.getTableParameterList().getTable("T_WHYBYS");
            if (rfcReturn == null)
                log.info("rfcReturn is null");
            // log.info(String.format("rfcReturn.Count is %s", rfcReturn.getNumRows()));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = "";
            for (int i = 0; i < rfcReturn.getNumRows(); i++) {
                rfcReturn.setRow(i);
                log.info(String.format("index is %s", i));
                YbWHYBYS pur = new YbWHYBYS();
                pur.setId(UUID.randomUUID().toString());
                pur.setZyq(rfcReturn.getString("ZYQ"));//院区
                pur.setIperi(rfcReturn.getString("IPERI"));//周期
                pur.setYbfl(rfcReturn.getString("YBFL"));//医保类型
                pur.setJsxq(rfcReturn.getString("JSXQ"));//辖区
                pur.setYshj(rfcReturn.getString("YSHJ"));//应收合计
                pur.setHkhj(rfcReturn.getString("HKHJ"));//回款合计
                pur.setT1T21(rfcReturn.getString("T1T21"));//基本医疗-统1+统2
                pur.setGrzh2(rfcReturn.getString("GRZH2"));//基本医疗-个人账户、门诊统筹
                pur.setHke12(rfcReturn.getString("HKE12"));//基本医疗-回款额
                dateStr = "";
                dateStr = rfcReturn.getString("HKRQ12");
                pur.setHkrq12(dateStr.equals("0000-00-00") ? "" : dateStr);//基本医疗-回款时间
                pur.setYsk3(rfcReturn.getString("YSK3"));//公务员补贴-应收款
                pur.setHke3(rfcReturn.getString("HKE3"));//公务员补贴-回款额
                dateStr = "";
                dateStr = rfcReturn.getString("HKRQ3");
                pur.setHkrq3(dateStr.equals("0000-00-00") ? "" : dateStr);//公务员补贴-回款时间
                pur.setYsk4(rfcReturn.getString("YSK4"));//大额费用、大病保险-应收款
                pur.setHke4(rfcReturn.getString("HKE4"));//大额费用、大病保险-回款额
                dateStr = "";
                dateStr = rfcReturn.getString("HKRQ4");
                pur.setHkrq4(dateStr.equals("0000-00-00") ? "" : dateStr);//大额费用、大病保险-回款时间
                pur.setYsk5(rfcReturn.getString("YSK5"));//门诊个人账户-应收款
                pur.setHke5(rfcReturn.getString("HKE5"));//门诊个人账户-回款额
                dateStr = "";
                dateStr = rfcReturn.getString("HKRQ5");
                pur.setHkrq5(dateStr.equals("0000-00-00") ? "" : dateStr);//门诊个人账户-回款时间
                list.add(pur);
            }
            log.info("list fill succeed ,getWhYbysList end");

        } catch (Exception ex) {
            message = ex.getMessage();
            isError = true;
        } finally {
            destination = null;
        }
        if (isError)
            throw new Exception(message);
        return list;
    }

    /**
     * 重症登记表
     *
     * @param ZZBH 重症编号
     * @param XM   姓名
     * @param YBXZ 医保性质
     * @param YBKH 医保卡号
     * @param BZBH 病种编号
     * @return
     */
    public List<YbZZDJB> getZzDjBList(String ZZBH, String XM, String YBXZ, String YBKH, String BZBH) throws Exception {
        String fuName = "ZYB00_FM_002";
        log.info("getZzDjBList begin");
        List<YbZZDJB> list = new ArrayList<>();
        boolean isError = false;
        JCoDestination destination;
        try {
            destination = RfcNOC.GetDestination();
            if (destination == null) {
                log.error("配置信息出错");
                return null;
            }

            JCoFunction myfun = null;
            myfun = destination.getRepository().getFunction(fuName);
            if (myfun == null)
                log.info(fuName + " is null");

            myfun.getImportParameterList().setValue("ZZBH", ZZBH);
            myfun.getImportParameterList().setValue("XM", XM);
            myfun.getImportParameterList().setValue("YBXZ", YBXZ);
            myfun.getImportParameterList().setValue("YBKH", YBKH);
            myfun.getImportParameterList().setValue("BZBH", BZBH);

            //提前实例化一个空的表结构出来
            myfun.execute(destination);//执行
            log.info(fuName + "myfun.Invoke succeed");
            JCoTable rfcReturn = myfun.getTableParameterList().getTable("T_ZZDJB");
            if (rfcReturn == null)
                log.info("rfcReturn is null");
            // log.info(String.format("rfcReturn.Count is %s", rfcReturn.getNumRows()));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < rfcReturn.getNumRows(); i++) {
                rfcReturn.setRow(i);
                log.info(String.format("index is %s", i));
                YbZZDJB pur = new YbZZDJB();
                pur.setId(UUID.randomUUID().toString());
                pur.setZzbh(rfcReturn.getString("ZZBH"));//	重症编号
                pur.setXm(rfcReturn.getString("XM"));//姓名
                pur.setYbxz(rfcReturn.getString("YBXZ"));//医保性质
                pur.setYbkh(rfcReturn.getString("YBKH"));//医保卡号
                pur.setBzbh(rfcReturn.getString("BZBH"));//病种编号
                pur.setBzmc(rfcReturn.getString("BZMC"));//病种名称
                pur.setLxfs(rfcReturn.getString("LXFS"));//联系方式
                pur.setZrsj(rfcReturn.getString("ZRSJ"));//转入时间
                pur.setXq(rfcReturn.getString("XQ"));//辖区
                pur.setYbjbr(rfcReturn.getString("YBJBR"));//医保办经办人
                pur.setZcsj(rfcReturn.getString("ZCSJ"));//转出时间
                pur.setBz(rfcReturn.getString("BZ"));//备注
                pur.setZcjbr(rfcReturn.getString("ZCJBR"));//转出经办人

                list.add(pur);
            }
            log.info("list fill succeed ,getZzDjBList end");

        } catch (Exception ex) {
            message = ex.getMessage();
            isError = true;
        } finally {
            destination = null;
        }
        if (isError)
            throw new Exception(message);
        return list;
    }

    /**
     * 特药登记表
     *
     * @param YBKH 医保卡号
     * @param SFZH 身份证号
     * @param XM   姓名
     * @return
     */
    public List<YbTYDJB> getTyDjBList(String YBKH, String SFZH, String XM) throws Exception {
        String fuName = "ZYB00_FM_003";
        log.info("getTyDjBList begin");
        List<YbTYDJB> list = new ArrayList<>();
        boolean isError = false;
        JCoDestination destination;
        try {
            destination = RfcNOC.GetDestination();
            if (destination == null) {
                log.error("配置信息出错");
                return null;
            }

            JCoFunction myfun = null;
            myfun = destination.getRepository().getFunction(fuName);
            if (myfun == null)
                log.info(fuName + " is null");


            myfun.getImportParameterList().setValue("YBKH", YBKH);
            myfun.getImportParameterList().setValue("SFZH", SFZH);
            myfun.getImportParameterList().setValue("XM", XM);

            //提前实例化一个空的表结构出来
            myfun.execute(destination);//执行
            log.info(fuName + "myfun.Invoke succeed");
            JCoTable rfcReturn = myfun.getTableParameterList().getTable("T_TYDJB");
            if (rfcReturn == null)
                log.info("rfcReturn is null");
            // log.info(String.format("rfcReturn.Count is %s", rfcReturn.getNumRows()));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < rfcReturn.getNumRows(); i++) {
                rfcReturn.setRow(i);
                log.info(String.format("index is %s", i));
                YbTYDJB pur = new YbTYDJB();
                pur.setId(UUID.randomUUID().toString());
                pur.setXm(rfcReturn.getString("XM"));//姓名
                pur.setBz(rfcReturn.getString("BZ"));//备注
                pur.setBasj(rfcReturn.getString("BASJ"));//	备案时间
                pur.setXb(rfcReturn.getString("XB"));//性别
                pur.setYbkh(rfcReturn.getString("YBKH"));//医保卡号
                pur.setQzsj(rfcReturn.getString("QZSJ"));//确诊时间
                pur.setSfzh(rfcReturn.getString("SFZH"));//身份证号
                pur.setLxdh(rfcReturn.getString("LXDH"));//联系电话
                pur.setZd(rfcReturn.getString("ZD"));//诊断
                pur.setTymc(rfcReturn.getString("TYMC"));//特药名称
                pur.setSpm(rfcReturn.getString("SPM"));//商品名
                pur.setYyyj(rfcReturn.getString("YYYJ"));//用药依据
                pur.setYfyl(rfcReturn.getString("YFYL"));//用法用量
                pur.setKs(rfcReturn.getString("KS"));//科室
                pur.setZrys(rfcReturn.getString("ZRYS"));//责任医生
                pur.setPgrq(rfcReturn.getString("PGRQ"));//评估日期

                list.add(pur);
            }
            log.info("list fill succeed ,getTyDjBList end");

        } catch (Exception ex) {
            message = ex.getMessage();
            isError = true;
        } finally {
            destination = null;
        }

        if (isError)
            throw new Exception(message);
        return list;
    }

//    public List<ScmBPurcharseorder> GetPurcharseList(String userID, String LIFNR, String LIFNRNAME, String startDate, String endDate) {
//        String fuName = "ZMM00_FM_SCMPURCHORDGET";
//        log.info("GetPurcharseList begin");
//        List<ScmBPurcharseorder> list = new ArrayList<>();
//        JCoDestination destination;
//        try {
//
//            destination = RfcNOC.GetDestination();
//            if (destination == null) {
//                log.error("配置信息出错");
//                return null;
//            }
//
//            // JCoRepository rfcrep = destination.getRepository();
//            JCoFunction myfun = null;
//            myfun = destination.getRepository().getFunction(fuName);
//            //  myfun.SetValue("IS_SELCOND", "0");//SAP里面的传入参数
//            if (myfun == null)
//                log.info(fuName + " is null");
//            JCoStructure IrfStruct = myfun.getImportParameterList().getStructure("IS_SELCOND");
//            if (IrfStruct == null)
//                log.info("IrfStruct is null");
//            IrfStruct.setValue("LIFNR", LIFNR);
//            IrfStruct.setValue("ZBEDATL", startDate);
//            IrfStruct.setValue("ZBEDATH", endDate);
//            //  myfun.setValue("IS_SELCOND", IrfStruct);
//            //提前实例化一个空的表结构出来
//            myfun.execute(destination);//执行
//            log.info(fuName + "myfun.Invoke succeed");
//            JCoTable rfcReturn = myfun.getTableParameterList().getTable("OT_PURCHINFO"); //此处返回类型为Structure 如果是Single类型 则直接调用myfun.GetString("RETURN");
//            if (rfcReturn == null)
//                log.info("rfcReturn is null");
//            // log.info(String.format("rfcReturn.Count is %s", rfcReturn.getNumRows()));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            for (int i = 0; i < rfcReturn.getNumRows(); i++) {
//                rfcReturn.setRow(i);
//                log.info(String.format("index is %s", i));
//                ScmBPurcharseorder pur = new ScmBPurcharseorder();
//
//
//                pur.setBedat(sdf.parse(rfcReturn.getString("BEDAT")));
//                pur.setEbelp(rfcReturn.getString("EBELP"));
//                pur.setEbeln(rfcReturn.getString("EBELN"));
//                pur.setEindt(sdf.parse(rfcReturn.getString("EINDT")));
//                pur.setLgort(rfcReturn.getString("LGORT"));
//                pur.setLifnr(rfcReturn.getString("LIFNR"));
//                pur.setMatnr(rfcReturn.getString("MATNR"));
//                pur.setMseht(rfcReturn.getString("MSEHT"));
//                pur.setMeins(rfcReturn.getString("MEINS"));
//                pur.setMenge(new BigDecimal(rfcReturn.getString("MENGE")));
//                // NAME1 = rfcReturn.getString("NAME1"),
//                pur.setNetpr(new BigDecimal(rfcReturn.getString("NETPR")));
//                pur.setTxz01(rfcReturn.getString("TXZ01"));
//                pur.setWerks(rfcReturn.getString("WERKS"));
//                pur.setWerkst(rfcReturn.getString("WERKST"));
//                pur.setId(rfcReturn.getString("EBELN") + rfcReturn.getString("EBELP"));
//
//                list.add(pur);
//            }
//            log.info("list fill succeed ,GetPurcharseList end");
//        } catch (Exception ex) {
//
//            log.error(ex.getMessage());
//        } finally {
//            destination = null;
//        }
//
//        return list;
//    }
//
//    /**
//     *  发送供应计划给sap
//     * @param userID
//     * @param listEntitys
//     * @param Lifnr
//     * @param NAME1
//     * @param ZPSTA
//     * @param ZUPFG C 是新增 D 是一条删除  U是更改  X是所有数据必须同时更新，一条出错，不进行操作
//     * @return
//     */
//    public List<BackFromSAP_SubPlan> SendSupplyPlan_RFC(String userID, List<ViewSupplyplan> listEntitys, String Lifnr, String NAME1, String ZPSTA, String ZUPFG) {
//        String fuName = "ZMM00_FM_SCMSUPLANSEND";
//        log.info("SendSupplyPlan(发送计划) begin", 1);
//        List<BackFromSAP_SubPlan> list = new ArrayList<>();
//        JCoDestination destination;
//        try {
//            destination = RfcNOC.GetDestination();
//            if (destination == null) {
//                log.error("配置信息出错");
//                BackFromSAP_SubPlan pur2 = new BackFromSAP_SubPlan();
//                pur2.setMESS("无法连接SAP，或者配置出错");
//                pur2.setMSTYPE("H");
//                pur2.setZGYJH("");
//                list.add(pur2);
//                return list;
//            }
//            JCoRepository rfcrep = destination.getRepository();
//            JCoFunction myfun = null;
//            myfun = rfcrep.getFunction(fuName);
//            //  myfun.SetValue("IS_SELCOND", "0");//SAP里面的传入参数
//            if (myfun == null) {
//                log.info("ZMM00_FM_SCMSUPLANSEND is NULL");
//                return list;
//            }
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            JCoTable IrfTable_IT_SUPLAN = myfun.getTableParameterList().getTable("IT_SUPLAN");
//            for (ViewSupplyplan entity : listEntitys) {
//                IrfTable_IT_SUPLAN.appendRow();
//                IrfTable_IT_SUPLAN.setValue("ZGYJH", entity.getId().toString());//供应计划号 对应 CODE字段
//                IrfTable_IT_SUPLAN.setValue("EBELN", entity.getEbeln());
//                IrfTable_IT_SUPLAN.setValue("EBELP", entity.getEbelp());
//
//                IrfTable_IT_SUPLAN.setValue("MATNR", entity.getMatnr());
//                IrfTable_IT_SUPLAN.setValue("TXZ01", entity.getTxz01());
//                IrfTable_IT_SUPLAN.setValue("LIFNR", entity.getGysaccount());
//                IrfTable_IT_SUPLAN.setValue("NAME1", entity.getGysname());
//                IrfTable_IT_SUPLAN.setValue("WERKS", entity.getWerks());
//                IrfTable_IT_SUPLAN.setValue("LGORT", entity.getLgort());
//
//                IrfTable_IT_SUPLAN.setValue("MENGE", entity.getgMenge().toString());
//                IrfTable_IT_SUPLAN.setValue("MENGE_S", entity.getDoneMenge()==null?"":entity.getDoneMenge().toString());
//                IrfTable_IT_SUPLAN.setValue("MEINS", entity.getMeins());
//
//                IrfTable_IT_SUPLAN.setValue("CHARG", entity.getCharge());
//                IrfTable_IT_SUPLAN.setValue("ZHSDAT", entity.getHsdat() == null ? "" : sdf.format(entity.getHsdat()));
//
//                IrfTable_IT_SUPLAN.setValue("ZVFDAT", entity.getVfdat()==null?"":sdf.format(entity.getVfdat()));
//                IrfTable_IT_SUPLAN.setValue("ZFPHM", entity.getFphm());
//                IrfTable_IT_SUPLAN.setValue("BARCODE", entity.getFpbm());
//                IrfTable_IT_SUPLAN.setValue("ZFPJR", entity.getFpjr().toString());
//                log.error("我到这里啦666");
//                IrfTable_IT_SUPLAN.setValue("ZFPRQ", entity.getFprq()==null?"":sdf.format(entity.getFprq()));
//
//                IrfTable_IT_SUPLAN.setValue("ZPSTA", ZPSTA);
//                IrfTable_IT_SUPLAN.setValue("ZUPFG", ZUPFG);
//                IrfTable_IT_SUPLAN.setValue("ZBTXT", "");
//                IrfTable_IT_SUPLAN.setValue("ZJYR", ZPSTA == "1" ? userID : "");
//                //总务
//                IrfTable_IT_SUPLAN.setValue("ZLXR", entity.getLinkPerson());//联系人
//                IrfTable_IT_SUPLAN.setValue("KOSTL", entity.getSendDepart());//科室编码
//                IrfTable_IT_SUPLAN.setValue("ZSPTM", entity.getMatnr());
//                IrfTable_IT_SUPLAN.setValue("ZTEL", entity.getLinkTelephone());
////送货清单
//                IrfTable_IT_SUPLAN.setValue("ZSHQD", entity.getSendOrderCode());
//
//            }
//
//            // myfun.SetValue("IT_SUPLAN", IrfTable_IT_SUPLAN);
//            //提前实例化一个空的表结构出来
//            myfun.execute(destination);//执行
//
//            JCoTable rfcReturn = myfun.getTableParameterList().getTable("OT_RETURN"); //此处返回类型为Structure 如果是Single类型 则直接调用myfun.GetString("RETURN");
//            if (rfcReturn == null) {
//                log.info("OT_RETURN is NULL");
//                return list;
//            }
//            log.info("上传SAP，调用成功。");
//            for (int i = 0; i < rfcReturn.getNumRows(); i++) {
//                rfcReturn.setRow(i);
//                BackFromSAP_SubPlan pur = new BackFromSAP_SubPlan();
//
//                pur.setMESS(rfcReturn.getString("MESS"));
//                pur.setMSTYPE(rfcReturn.getString("MSTYP"));
//                pur.setZGYJH(rfcReturn.getString("ZGYJH"));
//
//
//                list.add(pur);
//            }
//
//            log.info("SendSupplyPlan(发送计划) END SUCCESS!", 1);
//        } catch (Exception ex) {
//            log.info("SendSupplyPlan(发送计划)出现问题：" + ex.getMessage(), 1);
//
//            BackFromSAP_SubPlan pur = new BackFromSAP_SubPlan();
//            pur.setMESS(ex.getMessage());
//            pur.setMSTYPE("H");
//            pur.setZGYJH("");
//            list.add(pur);
//
//        } finally {
//            destination = null;
//        }
//
//        return list;
//    }
//
//    public static Boolean SendUploadInfo_RFC(String GysName, String matnr, String charge, String serverName, String I_Type)
//    {
//        log.info("SendUploadInfo_RFC(发送附件信息) begin");
//        List<BackFromSAP_SubPlan> list = new ArrayList<>();
//        JCoDestination destination;
//        String fuName = "ZMM00_FM_SCM003";
//        try
//        {
//            destination =RfcNOC.GetDestination();
//            if(destination==null)
//            {
//                log.error("SAP 链接失败");
//                return  false;
//            }
//
//            JCoRepository rfcrep = destination.getRepository();
//            JCoFunction myfun = null;
//            myfun = rfcrep.getFunction(fuName);
//            //  myfun.SetValue("IS_SELCOND", "0");//SAP里面的传入参数
//            if (myfun == null)
//            {
//                log.info("ZMM00_FM_SCM003 is NULL");
//            }
//
//
//            myfun.getImportParameterList().setValue("I_OBJECT", GysName.trim() + matnr.trim() + charge.trim());
//            myfun.getImportParameterList().setValue("I_FILENAME", serverName);
//            myfun.getImportParameterList().setValue("I_MODE", I_Type);
//            //提前实例化一个空的表结构出来
//            myfun.execute(destination);//执行
//
//
//            log.info("上传文件，调用成功。");
//
//            log.info("SendUploadInfo_RFC(发送附件信息) END SUCCESS!", 1);
//            return  true;
//        }
//        catch (Exception ex)
//        {
//            log.error("SendUploadInfo_RFC(发送附件信息)出现问题：" + ex.getMessage());
//            return  false;
//        }
//        finally
//        {
//            destination = null;
//        }
//
//    }
//
//    public List<BackInfo> GetInfoList(String url) {
//        String fuName = "ZHR00_FM_HSLIST";
//        log.info("ZHR00_FM_HSLIST begin");
//        List<BackInfo> list = new ArrayList<>();
//        JCoDestination destination;
//        try {
//
//            destination = RfcNOC.GetDestination();
//            if (destination == null) {
//                log.error("配置信息出错");
//                return null;
//            }
//
//            // JCoRepository rfcrep = destination.getRepository();
//            JCoFunction myfun = null;
//            myfun = destination.getRepository().getFunction(fuName);
//            //  myfun.SetValue("IS_SELCOND", "0");//SAP里面的传入参数
//            if (myfun == null)
//                log.info(fuName + " is null");
////            JCoStructure IrfStruct = myfun.getImportParameterList().getStructure("IS_SELCOND");
////            if (IrfStruct == null)
////                log.info("IrfStruct is null");
////            IrfStruct.setValue("URL", url);
//
//
//            JCoParameterList paramList = myfun.getImportParameterList();
//            paramList.setValue("URL", url);
//
//            //  myfun.setValue("IS_SELCOND", IrfStruct);
//            //提前实例化一个空的表结构出来
//            myfun.execute(destination);//执行
//            log.info(fuName + "myfun.Invoke succeed");
//            JCoTable rfcReturn = myfun.getTableParameterList().getTable("T_LIST"); //此处返回类型为Structure 如果是Single类型 则直接调用myfun.GetString("RETURN");
//            if (rfcReturn == null)
//                log.info("rfcReturn is null");
//            // log.info(String.format("rfcReturn.Count is %s", rfcReturn.getNumRows()));
//
//            for (int i = 0; i < rfcReturn.getNumRows(); i++) {
//                rfcReturn.setRow(i);
//
//                BackInfo backInfo =new BackInfo();
//
//
//                backInfo.setGESCH(rfcReturn.getString("GESCH"));
//                backInfo.setSERNO(rfcReturn.getString("SERNO"));
//                backInfo.setKS(rfcReturn.getString("KS"));
//                backInfo.setNACHN(rfcReturn.getString("NACHN"));
//                backInfo.setRYXZ(rfcReturn.getString("RYXZ"));
//
//                list.add(backInfo);
//            }
//            log.info("list fill succeed ,GetPurcharseList end");
//        } catch (Exception ex) {
//
//            log.error(ex.getMessage());
//        } finally {
//            destination = null;
//        }
//
//        return list;
//    }
}
