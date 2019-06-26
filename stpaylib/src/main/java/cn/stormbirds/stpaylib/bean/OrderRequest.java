package cn.stormbirds.stpaylib.bean;

import java.math.BigDecimal;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name    cn.stormbirds.stpaylib
 * @ Author         stormbirds
 * @ Email          xbaojun@gmail.com
 * @ Created At     2019/6/25 09:36
 * @ Description    客户端订单
 */


public class OrderRequest {
    /**
     * 订单支付账号（服务端颁发的支付账号）
     */
    private Integer payId;
    /**
     * 订单物品标题
     */
    private String title;
    /**
     * 订单物品描述
     */
    private String body;
    /**
     * 订单金额
     */
    private BigDecimal price;
    /**
     * 订单发起对象
     */
    private String userId;
    /**
     * 订单超时时间
     */
    private Long timeExpire;

    public OrderRequest() {
    }

    public OrderRequest(OrderRequest order){
        this.payId = order.getPayId();
        this.title = order.getTitle();
        this.body = order.getBody();
        this.price = order.getPrice();
        this.userId = order.getUserId();
        this.timeExpire = order.getTimeExpire();
    }

    public OrderRequest(Integer payId, String title, String body, BigDecimal price, String userId, Long timeExpire) {
        this.payId = payId;
        this.title = title;
        this.body = body;
        this.price = price;
        this.userId = userId;
        this.timeExpire = timeExpire;
    }

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(Long timeExpire) {
        this.timeExpire = timeExpire;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "payId=" + payId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", price=" + price +
                ", userId='" + userId + '\'' +
                ", timeExpire=" + timeExpire +
                '}';
    }
}
