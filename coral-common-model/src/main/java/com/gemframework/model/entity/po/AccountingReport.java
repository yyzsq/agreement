
package com.gemframework.model.entity.po;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: AccountingReport
 * @Date: 2020-05-13 09:51:44
 * @Version: v1.0
 * @Description: 会计工作报告实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_accounting_report")
@Data
public class AccountingReport extends BaseEntityPo {

										/**
		 * 公司名称
		 */
		private String customerId;
							/**
		 * 会计编号
		 */
		private String accountingId;
							/**
		 * 户数
		 */
		private Long households;
							/**
		 * 公司性质
		 */
		private String nature;
							/**
		 * 送票通知
		 */
		private String notice;
							/**
		 * 抄税/清卡
		 */
		private String cardClearing;
							/**
		 * 凭证
		 */
		private String voucher;
							/**
		 * 报表
		 */
		private String reportForm;
							/**
		 * 月收入
		 */
		private BigDecimal monthlyIncome;
							/**
		 * 累计年收入
		 */
		private BigDecimal annualIncome;
							/**
		 * 累计已有成本费用
		 */
		private BigDecimal cumulativeCost;
							/**
		 * 增值税
		 */
		private String incrementTax;
							/**
		 * 期末留底
		 */
		private String retentiveBase;
							/**
		 * 企业所得税
		 */
		private String incomeTax;
							/**
		 * 附加税
		 */
		private String surtax;
							/**
		 * 通用申报表
		 */
		private String declarationForm;
							/**
		 * 印花税
		 */
		private String stampDuty;
							/**
		 * 个税
		 */
		private String personalIncomeTax;
							/**
		 * 社保
		 */
		private String socialSecurity;
							/**
		 * 银行
		 */
		private String bank;
							/**
		 * 库存
		 */
		private String stock;
																												
}

