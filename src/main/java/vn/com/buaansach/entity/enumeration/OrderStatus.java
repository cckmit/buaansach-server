package vn.com.buaansach.entity.enumeration;

public enum OrderStatus {
    CREATED, // Customer create order
    RECEIVED, // Employee create order or accept order of customer
    PURCHASED,
    CANCELLED,
}
