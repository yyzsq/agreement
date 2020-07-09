
package com.gemframework.model.entity.po;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gemframework.model.common.BaseEntityPo;
import lombok.Data;

/**
 * @Title: MonthlyReport
 * @Date: 2020-06-15 19:41:37
 * @Version: v1.0
 * @Description: 月报表实体
 * @Author: yuanrise
 * @Email: 1649951967@qq.com
 * @Copyright: Copyright (c) 2020 wanyong
 * @Company: www.gemframework.com
 */

@TableName("rise_monthly_report")
@Data
public class MonthlyReport extends BaseEntityPo {

										/**
		 * 会计编号
		 */
		private String userId;
							/**
		 * 客户编号
		 */
		private String customerId;
							/**
		 * 客户名称
		 */
		private String customerName;
							/**
		 * 资产
		 */
		private String assets;
							/**
		 * 负债
		 */
		private String liabilities;
							/**
		 * 法人负债
		 */
		private String corporationLiabilities;
							/**
		 * 存货商品
		 */
		private String shangpch;
							/**
		 * 本月营业收入
		 */
		private String businessIncome;
							/**
		 * 本月营业成本
		 */
		private String operatingCosts;
							/**
		 * 本月利润
		 */
		private String monthProfit;
							/**
		 * 季度收入
		 */
		private String quarterIncome;
							/**
		 * 季度成本
		 */
		private String quarterCosts;
							/**
		 * 季度利润
		 */
		private String quarterProfit;
							/**
		 * 年度收入
		 */
		private String yearIncome;
							/**
		 * 年度成本
		 */
		private String yearCosts;
							/**
		 * 年度利润
		 */
		private String yearProfit;
							/**
		 * 增值税
		 */
		private String incrementTax;
							/**
		 * 附加税
		 */
		private String surtax;
							/**
		 * 所得税
		 */
		private String incomeTax;
							/**
		 * 水利基金
		 */
		private String waterConservancyFund;
							/**
		 * 工会经费
		 */
		private String laborUnionFunds;
							/**
		 * 期末留底
		 */
		private String retentiveBase;
							/**
		 * 文化建设
		 */
		private String wenhuajs;
							/**
		 * 印花税
		 */
		private String yinhuaTax;
							/**
		 * 土地使用税
		 */
		private String landUseTax;
							/**
		 * 房产税
		 */
		private String propertyTax;
							/**
		 * 其他费用
		 */
		private String otherTax;
							/**
		 * 残保金
		 */
		private String canbaoJin;
							/**
		 * 个人所得
		 */
		private String gerenSuode;
							/**
		 * 社保
		 */
		private String shebao;
							/**
		 * 经营所得
		 */
		private String jingyingSuode;
							/**
		 * 发票
		 */
		private String invoice;
							/**
		 * 商品
		 */
		private String commodity;
							/**
		 * 服务费
		 */
		private String serviceCharge;
							/**
		 * 审核状态 0待审核 1审核通过 2审核不通过
		 */
		private String status;
							/**
		 * 审核人员编号
		 */
		private String shenheId;
							/**
		 * 
		 */
		private String other1;
							/**
		 * 
		 */
		private String other2;
																												
}

