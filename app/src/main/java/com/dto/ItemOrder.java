package com.dto;

import java.io.Serializable;

public class ItemOrder implements Serializable
{
    private int id;
    private String orderDate;
    private String deliveryAddress;
    private int quantity;
    private int amount;
    private String orderDesc;
    private boolean orderStatus;
    private String paymentMode;
    private String customerID;
    private boolean cancelled;

    public ItemOrder() {
    }

    public ItemOrder(int id, String orderDate, String deliveryAddress, int quantity, int amount, String orderDesc, boolean orderStatus, String paymentMode, String customerID, boolean cancelled) {
        this.id = id;
        this.orderDate = orderDate;
        this.deliveryAddress = deliveryAddress;
        this.quantity = quantity;
        this.amount = amount;
        this.orderDesc = orderDesc;
        this.orderStatus = orderStatus;
        this.paymentMode = paymentMode;
        this.customerID = customerID;
        this.cancelled = cancelled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
}
