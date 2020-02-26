package com.pmo.iderin.models;

import lombok.Data;

@Data
public class barang_model {
    String id, nama,
			idkategori,
			 idtoko,
			 deskripsi,
            foto;
    double harga;
    long created_at,updated_at;


}
