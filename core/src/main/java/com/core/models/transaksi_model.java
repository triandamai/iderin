package com.core.models;

import lombok.Data;

@Data
public class transaksi_model {
    String idtransaksi,
            idpenjual,
            idpembeli,
            status,
            metode_pembayaran;
    long tanggal;
    double total;
}
