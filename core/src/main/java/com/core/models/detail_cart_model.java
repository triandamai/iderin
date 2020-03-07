package com.core.models;

import lombok.Data;

@Data
public class detail_cart_model {
    String iddetail;
    String idBarang;
    int jml;
    String satuan;
    double subtotal;
}
