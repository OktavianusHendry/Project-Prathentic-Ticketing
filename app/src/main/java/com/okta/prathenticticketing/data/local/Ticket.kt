package com.okta.prathenticticketing.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ticket(
    @PrimaryKey val nomorTransaksi: String,
    val nomorTiket: Int,
    val nama: String,
    val tipeTiket: String,
    val waktuMasuk: String? = null
)

data class TicketTypeCount(
    val tipeTiket: String,
    val count: Int
)