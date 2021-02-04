package cc.mrbird.febs.yb.entity;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author viki
 * @since 2020-07-30
 */

@Data
public  class YbDefaultValue {

    //申诉状态
    /**
     * 申诉状态 0 待接收
     */
    public static final Integer ACCEPTSTATE_0 = 0;
    /**
     * 申诉状态 1 接受
     */
    public static final Integer ACCEPTSTATE_1 = 1;
    /**
     * 申诉状态 2 不接受
     */
    public static final Integer ACCEPTSTATE_2 = 2;
    /**
     * 申诉状态 3 管理员更改
     */
    public static final Integer ACCEPTSTATE_3 = 3;
    /**
     * 申诉状态 4 医保拒绝
     */
    public static final Integer ACCEPTSTATE_4 = 4;
    /**
     * 申诉状态 6 已上传(已申诉)
     */
    public static final Integer ACCEPTSTATE_6 = 6;
    /**
     * 申诉状态 7 未申诉
     */
    public static final Integer ACCEPTSTATE_7 = 7;


    /**
     * 来源类型 0 正常流程
     */
    public static final Integer SOURCETYPE_0 = 0;
    /**
     * 来源类型 1 剔除业务
     */
    public static final Integer SOURCETYPE_1 = 1;


    /**
     * 扣款类型  0 明细扣款
     */
    public static final Integer DATATYPE_0 = 0;
    /**
     * 扣款类型  1 主单扣款
     */
    public static final Integer DATATYPE_1 = 1;


    /**
     * 审批状态  0 正常
     */
    public static final Integer APPROVALSTATE_0 = 0;
    /**
     * 审批状态  1 同意
     */
    public static final Integer APPROVALSTATE_1 = 1;
    /**
     * 审批状态  2 拒绝
     */
    public static final Integer APPROVALSTATE_2 = 2;


    /**
     * 审核版本 1 第一版
     */
    public static final Integer TYPENO_1 = 1;
    /**
     * 审核版本 2 第二版
     */
    public static final Integer TYPENO_2 = 2;

    //1待复议 2上传一 3申述一 4上传二 5申述二 6已剔除 7还款中
    /**
     * 复议申请 1 待复议
     */
    public static final Integer APPLYSTATE_1 = 1;
    /**
     * 复议申请 2 上传一
     */
    public static final Integer APPLYSTATE_2 = 2;
    /**
     * 复议申请 3 申述一
     */
    public static final Integer APPLYSTATE_3 = 3;
    /**
     * 复议申请 4 上传二
     */
    public static final Integer APPLYSTATE_4 = 4;
    /**
     * 复议申请 5 申述二
     */
    public static final Integer APPLYSTATE_5 = 5;
    /**
     * 复议申请 6 已剔除
     */
    public static final Integer APPLYSTATE_6 = 6;
    /**
     * 复议申请 7 还款中
     */
    public static final Integer APPLYSTATE_7 = 7;

    /**
     * 匹配状态 0 未匹配
     */
    public static final Integer SEEKSTATE_0 = 0;
    /**
     * 匹配状态 1 已匹配
     */
    public static final Integer SEEKSTATE_1 = 1;


    /**
     * 提醒状态 0 上传
     */
    public static final Integer WARNTYPE_0 = 0;
    /**
     * 提醒状态 1 序号正常
     */
    public static final Integer WARNTYPE_1 = 1;
    /**
     * 提醒状态 2 新序号单 字段匹配
     */
    public static final Integer WARNTYPE_2 = 2;
    /**
     * 提醒状态 3 新序号多 字段匹配多个
     */
    public static final Integer WARNTYPE_3 = 3;
    /**
     * 提醒状态 4 全无匹配
     */
    public static final Integer WARNTYPE_4 = 4;
    /**
     * 提醒状态 5 异常匹配
     */
    public static final Integer WARNTYPE_5 = 5;

    /**
     * 更新类型 0 序号更新
     */
    public static final Integer UPDATETYPE_0 = 0;
    /**
     * 更新类型 1 规则更新
     */
    public static final Integer UPDATETYPE_1 = 1;


    /**
     * 还款管理 明细状态 0 上传
     */
    public static final Integer REPAYDATA_STATE_0 = 0;
    /**
     * 还款管理 明细状态 1 一对多
     */
    public static final Integer REPAYDATA_STATE_1 = 1;
    /**
     * 还款管理 明细状态 2 未知
     */
    public static final Integer REPAYDATA_STATE_2 = 2;

    /**
     * 上传 还款状态 1 成功
     */
    public static final Integer RESULTREPAYSTATE_1 = 1;
    /**
     * 上传 还款状态 2 失败
     */
    public static final Integer RESULTREPAYSTATE_2 = 2;
    /**
     * 上传 还款状态 3 部分调整
     */
    public static final Integer RESULTREPAYSTATE_3 = 3;

    /**
     * 上传 状态 1 成功
     */
    public static final Integer RESULTSTATE_1 = 1;
    /**
     * 上传 状态 2 失败
     */
    public static final Integer RESULTSTATE_2 = 2;


    /**
     * 核对申请 状态 1 待审核
     */
    public static final Integer VERIFYSTATE_1 = 1;

    /**
     * 核对申请 状态 2 已审核
     */
    public static final Integer VERIFYSTATE_2 = 2;

    /**
     * 核对申请 状态 3 已发送
     */
    public static final Integer VERIFYSTATE_3 = 3;

    /**
     * 人工复议 状态 1 待审核
     */
    public static final Integer VERIFYDATASTATE_1 = 1;

    /**
     * 人工复议 状态 2 已审核
     */
    public static final Integer VERIFYDATASTATE_2 = 2;

    /**
     * 人工复议 状态 3 已发送
     */
    public static final Integer VERIFYDATASTATE_3 = 3;

    /**
     * 还款管理 保险类型 0 居保
     */
    public static final Integer REPAYTYPE_0 = 0;
    /**
     * 还款管理 保险类型 1 职保
     */
    public static final Integer REPAYTYPE_1 = 1;


    /**
     * 数据剔除 明细状态 0 一对一
     */
    public static final Integer RESETDATA_STATE_0 = 0;
    /**
     * 数据剔除 明细状态 1 一对多
     */
    public static final Integer RESETDATA_STATE_1 = 1;
    /**
     * 数据剔除 明细状态 2 未知
     */
    public static final Integer RESETDATA_STATE_2 = 2;


    /**
     * 扣款落实  0 个人分摊
     */
    public static final Integer SHARESTATE_0 = 0;
    /**
     * 扣款落实 1 科室分摊
     */
    public static final Integer SHARESTATE_1 = 1;

    /**
     * 扣款落实 2 其他分摊
     */
    public static final Integer SHARESTATE_2 = 2;

    /**
     * 剔除类型  1 自动剔除
     */
    public static final Integer RESETTYPE_1 = 1;
    /**
     * 剔除类型 2 手动剔除
     */
    public static final Integer RESETTYPE_2 = 2;

}