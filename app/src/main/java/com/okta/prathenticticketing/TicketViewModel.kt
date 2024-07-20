package com.okta.prathenticticketing

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.okta.prathenticticketing.data.local.Ticket
import com.okta.prathenticticketing.data.local.TicketDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class TicketViewModel(application: Application) : AndroidViewModel(application) {
    private val db: TicketDatabase = Room.databaseBuilder(
        application,
        TicketDatabase::class.java, "database-ticket"
    ).fallbackToDestructiveMigration().build()

    val ticketsWithWaktuMasuk: LiveData<Int> = db.ticketDao().countTicketsWithWaktuMasuk()
    val ticketsWithoutWaktuMasuk: LiveData<Int> = db.ticketDao().countTicketsWithoutWaktuMasuk()

    val vipTickets: LiveData<Int> = db.ticketDao().countVIPTickets()
    val regularTickets: LiveData<Int> = db.ticketDao().countRegularTickets()
    val siswaUndanganTickets: LiveData<Int> = db.ticketDao().countSiswaUndanganTickets()
    val dosenTickets: LiveData<Int> = db.ticketDao().countDosenTickets()
    val expoTickets: LiveData<Int> = db.ticketDao().countExpoTickets()
    val mentorTickets: LiveData<Int> = db.ticketDao().countMentorTickets()
    val talentTickets: LiveData<Int> = db.ticketDao().countTalentTickets()
    val tenantTickets: LiveData<Int> = db.ticketDao().countTenantTickets()

    val allTickets: LiveData<List<Ticket>> = db.ticketDao().getAllTickets()

    fun findTransactionByNomorTransaksi(qrCodeContent: String): LiveData<Ticket?> {
        return liveData(Dispatchers.IO) {
            emit(db.ticketDao().findTransactionByNomorTransaksi(qrCodeContent))
        }
    }

    fun updateWaktuMasuk(nomorTransaksi: String, waktuMasuk: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.ticketDao().updateWaktuMasuk(nomorTransaksi, waktuMasuk)
        }
    }

    fun prepopulateDatabase(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val inputStream: InputStream = context.assets.open("tiket_prathentic.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val ticketList = mutableListOf<Ticket>()

            try {
                reader.readLine() // Skip header line
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val tokens = line!!.split(',')
                    if (tokens.size >= 5) { // Ensure there are enough tokens
                        val nomorTransaksi = tokens[0]
                        val nomorTiket = tokens[1].toInt()
                        val nama = tokens[2]
                        val tipeTiket = tokens[3]
                        val waktuMasuk = if (tokens[4] == "-") null else tokens[4]
                        ticketList.add(Ticket(nomorTransaksi, nomorTiket, nama,tipeTiket, waktuMasuk))
                    }
                }
                db.ticketDao().insertAll(ticketList)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                reader.close()
            }
        }
    }
}
