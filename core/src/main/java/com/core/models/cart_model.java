package com.core.models;

import lombok.Data;

@Data
public class cart_model {
    String idcart, tanggal;
    long timestamp;
    String idPembeli;
    String status;
    double total;
}
