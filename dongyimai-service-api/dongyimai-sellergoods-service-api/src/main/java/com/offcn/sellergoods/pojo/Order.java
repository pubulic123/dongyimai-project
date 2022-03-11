package com.offcn.sellergoods.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/****
 * @Author:ujiuye
 * @Description:Order构建
 * @Date 2021/2/1 14:19
 *****/
@ApiModel(description = "Order",value = "Order")
@TableName(value="tb_order")
public class Order implements Serializable{

	@ApiModelProperty(value = "订单id",required = false)
    @TableId(type = IdType.AUTO)
    @TableField(value = "order_id")
	private Long orderId;//订单id

	@ApiModelProperty(value = "实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分",required = false)
    @TableField(value = "payment")
	private BigDecimal payment;//实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分

	@ApiModelProperty(value = "支付类型，1、在线支付，2、货到付款",required = false)
    @TableField(value = "pay_type")
	private String payType;//支付类型，1、在线支付，2、货到付款

	@ApiModelProperty(value = "邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分",required = false)
    @TableField(value = "post_fee")
	private String postFee;//邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分

	@ApiModelProperty(value = "状态：0:未完成 1:已完成 2:已退货",required = false)
    @TableField(value = "order_status")
	private String orderStatus;//状态：0:未完成 1:已完成 2:已退货

	@ApiModelProperty(value = "订单创建时间",required = false)
    @TableField(value = "create_time")
	private Date createTime;//订单创建时间

	@ApiModelProperty(value = "订单更新时间",required = false)
    @TableField(value = "update_time")
	private Date updateTime;//订单更新时间

	@ApiModelProperty(value = "付款时间",required = false)
    @TableField(value = "payment_time")
	private Date paymentTime;//付款时间

	@ApiModelProperty(value = "发货时间",required = false)
    @TableField(value = "consign_time")
	private Date consignTime;//发货时间

	@ApiModelProperty(value = "交易完成时间",required = false)
    @TableField(value = "end_time")
	private Date endTime;//交易完成时间

	@ApiModelProperty(value = "交易关闭时间",required = false)
    @TableField(value = "close_time")
	private Date closeTime;//交易关闭时间

	@ApiModelProperty(value = "物流名称",required = false)
    @TableField(value = "shipping_name")
	private String shippingName;//物流名称

	@ApiModelProperty(value = "物流单号",required = false)
    @TableField(value = "shipping_code")
	private String shippingCode;//物流单号

	@ApiModelProperty(value = "用户id",required = false)
    @TableField(value = "username")
	private String username;//用户id

	@ApiModelProperty(value = "买家留言",required = false)
    @TableField(value = "buyer_message")
	private String buyerMessage;//买家留言

	@ApiModelProperty(value = "买家昵称",required = false)
    @TableField(value = "buyer_nick")
	private String buyerNick;//买家昵称

	@ApiModelProperty(value = "买家是否已经评价",required = false)
    @TableField(value = "buyer_rate")
	private String buyerRate;//买家是否已经评价

	@ApiModelProperty(value = "收货人地区名称(省，市，县)街道",required = false)
    @TableField(value = "receiver_area_name")
	private String receiverAreaName;//收货人地区名称(省，市，县)街道

	@ApiModelProperty(value = "收货人手机",required = false)
    @TableField(value = "receiver_mobile")
	private String receiverMobile;//收货人手机

	@ApiModelProperty(value = "收货人邮编",required = false)
    @TableField(value = "receiver_zip_code")
	private String receiverZipCode;//收货人邮编

	@ApiModelProperty(value = "收货人",required = false)
    @TableField(value = "receiver_contact")
	private String receiverContact;//收货人

	@ApiModelProperty(value = "过期时间，定期清理",required = false)
    @TableField(value = "expire")
	private Date expire;//过期时间，定期清理

	@ApiModelProperty(value = "发票类型(普通发票，电子发票，增值税发票)",required = false)
    @TableField(value = "invoice_type")
	private String invoiceType;//发票类型(普通发票，电子发票，增值税发票)

	@ApiModelProperty(value = "订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端",required = false)
    @TableField(value = "source_type")
	private String sourceType;//订单来源：1:app端，2：pc端，3：M端，4：微信端，5：手机qq端

	@ApiModelProperty(value = "商家ID",required = false)
    @TableField(value = "seller_id")
	private String sellerId;//商家ID

	@ApiModelProperty(value = "总金额",required = false)
    @TableField(value = "total_money")
	private BigDecimal totalMoney;//总金额

	@ApiModelProperty(value = "实际支付金额",required = false)
    @TableField(value = "pay_money")
	private BigDecimal payMoney;//实际支付金额

	@ApiModelProperty(value = "总数量",required = false)
    @TableField(value = "total_num")
	private Integer totalNum;//总数量

	@ApiModelProperty(value = "",required = false)
    @TableField(value = "pre_money")
	private BigDecimal preMoney;//

	@ApiModelProperty(value = "支付状态 0:未支付 1:已支付 2:支付失败",required = false)
    @TableField(value = "pay_status")
	private String payStatus;//支付状态 0:未支付 1:已支付 2:支付失败



	//get方法
	public Long getOrderId() {
		return orderId;
	}

	//set方法
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	//get方法
	public BigDecimal getPayment() {
		return payment;
	}

	//set方法
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	//get方法
	public String getPayType() {
		return payType;
	}

	//set方法
	public void setPayType(String payType) {
		this.payType = payType;
	}
	//get方法
	public String getPostFee() {
		return postFee;
	}

	//set方法
	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}
	//get方法
	public String getOrderStatus() {
		return orderStatus;
	}

	//set方法
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	//get方法
	public Date getCreateTime() {
		return createTime;
	}

	//set方法
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	//get方法
	public Date getUpdateTime() {
		return updateTime;
	}

	//set方法
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	//get方法
	public Date getPaymentTime() {
		return paymentTime;
	}

	//set方法
	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}
	//get方法
	public Date getConsignTime() {
		return consignTime;
	}

	//set方法
	public void setConsignTime(Date consignTime) {
		this.consignTime = consignTime;
	}
	//get方法
	public Date getEndTime() {
		return endTime;
	}

	//set方法
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	//get方法
	public Date getCloseTime() {
		return closeTime;
	}

	//set方法
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	//get方法
	public String getShippingName() {
		return shippingName;
	}

	//set方法
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	//get方法
	public String getShippingCode() {
		return shippingCode;
	}

	//set方法
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	//get方法
	public String getUsername() {
		return username;
	}

	//set方法
	public void setUsername(String username) {
		this.username = username;
	}
	//get方法
	public String getBuyerMessage() {
		return buyerMessage;
	}

	//set方法
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}
	//get方法
	public String getBuyerNick() {
		return buyerNick;
	}

	//set方法
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	//get方法
	public String getBuyerRate() {
		return buyerRate;
	}

	//set方法
	public void setBuyerRate(String buyerRate) {
		this.buyerRate = buyerRate;
	}
	//get方法
	public String getReceiverAreaName() {
		return receiverAreaName;
	}

	//set方法
	public void setReceiverAreaName(String receiverAreaName) {
		this.receiverAreaName = receiverAreaName;
	}
	//get方法
	public String getReceiverMobile() {
		return receiverMobile;
	}

	//set方法
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	//get方法
	public String getReceiverZipCode() {
		return receiverZipCode;
	}

	//set方法
	public void setReceiverZipCode(String receiverZipCode) {
		this.receiverZipCode = receiverZipCode;
	}
	//get方法
	public String getReceiverContact() {
		return receiverContact;
	}

	//set方法
	public void setReceiverContact(String receiverContact) {
		this.receiverContact = receiverContact;
	}
	//get方法
	public Date getExpire() {
		return expire;
	}

	//set方法
	public void setExpire(Date expire) {
		this.expire = expire;
	}
	//get方法
	public String getInvoiceType() {
		return invoiceType;
	}

	//set方法
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	//get方法
	public String getSourceType() {
		return sourceType;
	}

	//set方法
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	//get方法
	public String getSellerId() {
		return sellerId;
	}

	//set方法
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	//get方法
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	//set方法
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	//get方法
	public BigDecimal getPayMoney() {
		return payMoney;
	}

	//set方法
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	//get方法
	public Integer getTotalNum() {
		return totalNum;
	}

	//set方法
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}
	//get方法
	public BigDecimal getPreMoney() {
		return preMoney;
	}

	//set方法
	public void setPreMoney(BigDecimal preMoney) {
		this.preMoney = preMoney;
	}
	//get方法
	public String getPayStatus() {
		return payStatus;
	}

	//set方法
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}


}
