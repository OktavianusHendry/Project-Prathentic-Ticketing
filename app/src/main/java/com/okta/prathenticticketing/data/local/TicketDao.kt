package com.okta.prathenticticketing.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TicketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tickets: List<Ticket>)

    @Query("SELECT * FROM ticket ORDER BY tipeTiket ASC, nomorTransaksi ASC")
    fun getAllTickets(): LiveData<List<Ticket>>

    @Query("SELECT * FROM ticket WHERE nomorTransaksi = :nomorTransaksi")
    fun findTransactionByNomorTransaksi(nomorTransaksi: String): Ticket?

    @Query("UPDATE ticket SET waktuMasuk = :waktuMasuk WHERE nomorTransaksi = :nomorTransaksi")
    suspend fun updateWaktuMasuk(nomorTransaksi: String, waktuMasuk: String)

    @Query("SELECT COUNT(*) FROM Ticket WHERE waktuMasuk IS NOT NULL")
    fun countTicketsWithWaktuMasuk(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE waktuMasuk IS NULL")
    fun countTicketsWithoutWaktuMasuk(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE tipeTiket = 'VIP'")
    fun countVIPTickets(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE tipeTiket = 'Reguler'")
    fun countRegularTickets(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE tipeTiket = 'Siswa Undangan'")
    fun countSiswaUndanganTickets(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE tipeTiket = 'Dosen'")
    fun countDosenTickets(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE tipeTiket = 'Expo'")
    fun countExpoTickets(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE tipeTiket = 'Mentor'")
    fun countMentorTickets(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE tipeTiket = 'Talent'")
    fun countTalentTickets(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM Ticket WHERE tipeTiket = 'Tenant'")
    fun countTenantTickets(): LiveData<Int>
}
