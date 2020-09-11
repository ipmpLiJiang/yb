package cc.mrbird.febs.scm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;

/**
 * <p>
 * 
 * </p>
 *
 * @author viki
 * @since 2019-10-23
 */

@Excel("scm_d_plan")
public class ScmDPlan implements Serializable{

private static final long serialVersionUID=1L;

    /**
     * 主键
     */
                    @TableId("ID")
                    @ExcelField(value ="主键")
private String id;
    /**
     * 编码
     */
    @TableField("CODE")
            @ExcelField(value ="编码")
private String code;
    @TableField("NAME")
        private String name;
    /**
     * 订单号
     */
    @TableField("EBELN")
            @ExcelField(value ="订单号")
private String ebeln;
    /**
     * 项目号
     */
    @TableField("EBELP")
            @ExcelField(value ="项目号")
private String ebelp;
    /**
     * 供应商账号
     */
    @TableField("LIFNR")
            @ExcelField(value ="供应商账号")
private String lifnr;
    /**
     * 药品编码
     */
    @TableField("MATNR")
            @ExcelField(value ="药品编码")
private String matnr;
    /**
     * 药品名称
     */
    @TableField("TXZ01")
            @ExcelField(value ="药品名称")
private String txz01;
    /**
     * 院区编码
     */
    @TableField("WERKS")
            @ExcelField(value ="院区编码")
private String werks;
    /**
     * 院区
     */
    @TableField("WERKST")
            @ExcelField(value ="院区")
private String werkst;
    /**
     * 药房
     */
    @TableField("LGORT")
            @ExcelField(value ="药房")
private String lgort;
    /**
     * 订单数量
     */
    @TableField("MENGE")
            @ExcelField(value ="订单数量")
private BigDecimal menge;
    /**
     * 单位编码
     */
    @TableField("MEINS")
            @ExcelField(value ="单位编码")
private String meins;
    /**
     * 单位
     */
    @TableField("MSEHT")
            @ExcelField(value ="单位")
private String mseht;
    @TableField("NETPR")
        private String netpr;
    @TableField("EINDT")
        private String eindt;
    @TableField("BEDAT")
        private String bedat;
    @TableField("STATUS")
        private String status;
    @TableField("FREEUSE1")
        private String freeuse1;
    @TableField("FREEUSE2")
        private String freeuse2;
    @TableField("FREEUSE3")
        private String freeuse3;
    @TableField("COMMENTS")
        private String comments;
    @TableField("STATE")
        private BigDecimal state;
    @TableField("CREATE_USER_ID")
        private String createUserId;
    @TableField("CREATE_TIME")
        private LocalDate createTime;
    @TableField("CREATE_DEPART_ID")
        private String createDepartId;
    @TableField("MODIFY_USER_ID")
        private String modifyUserId;
    @TableField("MODIFY_TIME")
        private LocalDate modifyTime;
    @TableField("MODIFY_DEPART_ID")
        private String modifyDepartId;
    @TableField("DATA_TIMESTAMP")
        private LocalDateTime dataTimestamp;
    @TableField("DATA_ORGANISE_ID")
        private String dataOrganiseId;
    @TableField("ALLMENGE")
        private BigDecimal allmenge;
    @TableField("SUREMENGE")
        private BigDecimal suremenge;
    /**
     * 订单类型
     */
    @TableField("BSART")
            @ExcelField(value ="订单类型")
private String bsart;
    /**
     * 送货单位ID
     */
    @TableField("SEND_DEAPRT_ID")
            @ExcelField(value ="送货单位ID")
private String sendDeaprtId;
    /**
     * 送货单位
     */
    @TableField("SEND_DEAPRT_NAME")
            @ExcelField(value ="送货单位")
private String sendDeaprtName;
    /**
     * 联系方式
     */
    @TableField("SEND_DEAPRT_CONTACT")
            @ExcelField(value ="联系方式")
private String sendDeaprtContact;
    /**
     * 电话号码
     */
    @TableField("SEND_DEAPRT_PHONE")
            @ExcelField(value ="电话号码")
private String sendDeaprtPhone;


    public String getId(){
            return id;
            }

        public void setId(String id) {
            this.id = id;
            }

    public String getCode(){
            return code;
            }

        public void setCode(String code) {
            this.code = code;
            }

    public String getName(){
            return name;
            }

        public void setName(String name) {
            this.name = name;
            }

    public String getEbeln(){
            return ebeln;
            }

        public void setEbeln(String ebeln) {
            this.ebeln = ebeln;
            }

    public String getEbelp(){
            return ebelp;
            }

        public void setEbelp(String ebelp) {
            this.ebelp = ebelp;
            }

    public String getLifnr(){
            return lifnr;
            }

        public void setLifnr(String lifnr) {
            this.lifnr = lifnr;
            }

    public String getMatnr(){
            return matnr;
            }

        public void setMatnr(String matnr) {
            this.matnr = matnr;
            }

    public String getTxz01(){
            return txz01;
            }

        public void setTxz01(String txz01) {
            this.txz01 = txz01;
            }

    public String getWerks(){
            return werks;
            }

        public void setWerks(String werks) {
            this.werks = werks;
            }

    public String getWerkst(){
            return werkst;
            }

        public void setWerkst(String werkst) {
            this.werkst = werkst;
            }

    public String getLgort(){
            return lgort;
            }

        public void setLgort(String lgort) {
            this.lgort = lgort;
            }

    public BigDecimal getMenge(){
            return menge;
            }

        public void setMenge(BigDecimal menge) {
            this.menge = menge;
            }

    public String getMeins(){
            return meins;
            }

        public void setMeins(String meins) {
            this.meins = meins;
            }

    public String getMseht(){
            return mseht;
            }

        public void setMseht(String mseht) {
            this.mseht = mseht;
            }

    public String getNetpr(){
            return netpr;
            }

        public void setNetpr(String netpr) {
            this.netpr = netpr;
            }

    public String getEindt(){
            return eindt;
            }

        public void setEindt(String eindt) {
            this.eindt = eindt;
            }

    public String getBedat(){
            return bedat;
            }

        public void setBedat(String bedat) {
            this.bedat = bedat;
            }

    public String getStatus(){
            return status;
            }

        public void setStatus(String status) {
            this.status = status;
            }

    public String getFreeuse1(){
            return freeuse1;
            }

        public void setFreeuse1(String freeuse1) {
            this.freeuse1 = freeuse1;
            }

    public String getFreeuse2(){
            return freeuse2;
            }

        public void setFreeuse2(String freeuse2) {
            this.freeuse2 = freeuse2;
            }

    public String getFreeuse3(){
            return freeuse3;
            }

        public void setFreeuse3(String freeuse3) {
            this.freeuse3 = freeuse3;
            }

    public String getComments(){
            return comments;
            }

        public void setComments(String comments) {
            this.comments = comments;
            }

    public BigDecimal getState(){
            return state;
            }

        public void setState(BigDecimal state) {
            this.state = state;
            }

    public String getCreateUserId(){
            return createUserId;
            }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
            }

    public LocalDate getCreateTime(){
            return createTime;
            }

        public void setCreateTime(LocalDate createTime) {
            this.createTime = createTime;
            }

    public String getCreateDepartId(){
            return createDepartId;
            }

        public void setCreateDepartId(String createDepartId) {
            this.createDepartId = createDepartId;
            }

    public String getModifyUserId(){
            return modifyUserId;
            }

        public void setModifyUserId(String modifyUserId) {
            this.modifyUserId = modifyUserId;
            }

    public LocalDate getModifyTime(){
            return modifyTime;
            }

        public void setModifyTime(LocalDate modifyTime) {
            this.modifyTime = modifyTime;
            }

    public String getModifyDepartId(){
            return modifyDepartId;
            }

        public void setModifyDepartId(String modifyDepartId) {
            this.modifyDepartId = modifyDepartId;
            }

    public LocalDateTime getDataTimestamp(){
            return dataTimestamp;
            }

        public void setDataTimestamp(LocalDateTime dataTimestamp) {
            this.dataTimestamp = dataTimestamp;
            }

    public String getDataOrganiseId(){
            return dataOrganiseId;
            }

        public void setDataOrganiseId(String dataOrganiseId) {
            this.dataOrganiseId = dataOrganiseId;
            }

    public BigDecimal getAllmenge(){
            return allmenge;
            }

        public void setAllmenge(BigDecimal allmenge) {
            this.allmenge = allmenge;
            }

    public BigDecimal getSuremenge(){
            return suremenge;
            }

        public void setSuremenge(BigDecimal suremenge) {
            this.suremenge = suremenge;
            }

    public String getBsart(){
            return bsart;
            }

        public void setBsart(String bsart) {
            this.bsart = bsart;
            }

    public String getSendDeaprtId(){
            return sendDeaprtId;
            }

        public void setSendDeaprtId(String sendDeaprtId) {
            this.sendDeaprtId = sendDeaprtId;
            }

    public String getSendDeaprtName(){
            return sendDeaprtName;
            }

        public void setSendDeaprtName(String sendDeaprtName) {
            this.sendDeaprtName = sendDeaprtName;
            }

    public String getSendDeaprtContact(){
            return sendDeaprtContact;
            }

        public void setSendDeaprtContact(String sendDeaprtContact) {
            this.sendDeaprtContact = sendDeaprtContact;
            }

    public String getSendDeaprtPhone(){
            return sendDeaprtPhone;
            }

        public void setSendDeaprtPhone(String sendDeaprtPhone) {
            this.sendDeaprtPhone = sendDeaprtPhone;
            }

    public static final String ID ="ID" ;

    public static final String CODE ="CODE" ;

    public static final String NAME ="NAME" ;

    public static final String EBELN ="EBELN" ;

    public static final String EBELP ="EBELP" ;

    public static final String LIFNR ="LIFNR" ;

    public static final String MATNR ="MATNR" ;

    public static final String TXZ01 ="TXZ01" ;

    public static final String WERKS ="WERKS" ;

    public static final String WERKST ="WERKST" ;

    public static final String LGORT ="LGORT" ;

    public static final String MENGE ="MENGE" ;

    public static final String MEINS ="MEINS" ;

    public static final String MSEHT ="MSEHT" ;

    public static final String NETPR ="NETPR" ;

    public static final String EINDT ="EINDT" ;

    public static final String BEDAT ="BEDAT" ;

    public static final String STATUS ="STATUS" ;

    public static final String FREEUSE1 ="FREEUSE1" ;

    public static final String FREEUSE2 ="FREEUSE2" ;

    public static final String FREEUSE3 ="FREEUSE3" ;

    public static final String COMMENTS ="COMMENTS" ;

    public static final String STATE ="STATE" ;

    public static final String CREATE_USER_ID ="CREATE_USER_ID" ;

    public static final String CREATE_TIME ="CREATE_TIME" ;

    public static final String CREATE_DEPART_ID ="CREATE_DEPART_ID" ;

    public static final String MODIFY_USER_ID ="MODIFY_USER_ID" ;

    public static final String MODIFY_TIME ="MODIFY_TIME" ;

    public static final String MODIFY_DEPART_ID ="MODIFY_DEPART_ID" ;

    public static final String DATA_TIMESTAMP ="DATA_TIMESTAMP" ;

    public static final String DATA_ORGANISE_ID ="DATA_ORGANISE_ID" ;

    public static final String ALLMENGE ="ALLMENGE" ;

    public static final String SUREMENGE ="SUREMENGE" ;

    public static final String BSART ="BSART" ;

    public static final String SEND_DEAPRT_ID ="SEND_DEAPRT_ID" ;

    public static final String SEND_DEAPRT_NAME ="SEND_DEAPRT_NAME" ;

    public static final String SEND_DEAPRT_CONTACT ="SEND_DEAPRT_CONTACT" ;

    public static final String SEND_DEAPRT_PHONE ="SEND_DEAPRT_PHONE" ;

@Override
public String toString() {
        return "ScmDPlan{" +
                ", id=" + id +
                ", code=" + code +
                ", name=" + name +
                ", ebeln=" + ebeln +
                ", ebelp=" + ebelp +
                ", lifnr=" + lifnr +
                ", matnr=" + matnr +
                ", txz01=" + txz01 +
                ", werks=" + werks +
                ", werkst=" + werkst +
                ", lgort=" + lgort +
                ", menge=" + menge +
                ", meins=" + meins +
                ", mseht=" + mseht +
                ", netpr=" + netpr +
                ", eindt=" + eindt +
                ", bedat=" + bedat +
                ", status=" + status +
                ", freeuse1=" + freeuse1 +
                ", freeuse2=" + freeuse2 +
                ", freeuse3=" + freeuse3 +
                ", comments=" + comments +
                ", state=" + state +
                ", createUserId=" + createUserId +
                ", createTime=" + createTime +
                ", createDepartId=" + createDepartId +
                ", modifyUserId=" + modifyUserId +
                ", modifyTime=" + modifyTime +
                ", modifyDepartId=" + modifyDepartId +
                ", dataTimestamp=" + dataTimestamp +
                ", dataOrganiseId=" + dataOrganiseId +
                ", allmenge=" + allmenge +
                ", suremenge=" + suremenge +
                ", bsart=" + bsart +
                ", sendDeaprtId=" + sendDeaprtId +
                ", sendDeaprtName=" + sendDeaprtName +
                ", sendDeaprtContact=" + sendDeaprtContact +
                ", sendDeaprtPhone=" + sendDeaprtPhone +
        "}";
        }
        }