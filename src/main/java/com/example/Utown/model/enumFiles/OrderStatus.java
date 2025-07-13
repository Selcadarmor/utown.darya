package com.example.Utown.model.enumFiles;

public enum OrderStatus {
    PENDING,
    PROCESSING, //accepted by the restaurant owner
    READY_FOR_PICKUP,
    DELIVERY,
    COMPLETED, // order is completed
    CANCELED,// order canceled and refunded
    REJECTED
}
