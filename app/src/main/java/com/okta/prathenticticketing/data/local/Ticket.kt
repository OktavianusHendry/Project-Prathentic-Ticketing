package com.okta.prathenticticketing.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ticket(
    @PrimaryKey val nomorTransaksi: String, //kode Transaksi
    val nomorTiket: String, //kode tiket
    val nama: String,
    val tipeTiket: String,
    val textBarcode: String,
    val waktuMasuk: String? = null
)

data class TicketTypeCount(
    val tipeTiket: String,
    val count: Int
)