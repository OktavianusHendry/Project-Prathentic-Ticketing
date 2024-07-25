package com.okta.prathenticticketing

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
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
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class TicketViewModel(application: Application) : AndroidViewModel(application) {
    private val db: TicketDatabase = TicketDatabase.getDatabase(application)

    val ticketsWithWaktuMasuk: LiveData<Int> = db.ticketDao().countTicketsWithWaktuMasuk()
    val ticketsWithoutWaktuMasuk: LiveData<Int> = db.ticketDao().countTicketsWithoutWaktuMasuk()

    val vipTickets: LiveData<Int> = db.ticketDao().countVIPTickets()
    val regularTickets: LiveData<Int> = db.ticketDao().countRegularTickets()
    val siswaUndanganTickets: LiveData<Int> = db.ticketDao().countSiswaUndanganTickets()
//    val dosenTickets: LiveData<Int> = db.ticketDao().countDosenTickets()
    val expoTickets: LiveData<Int> = db.ticketDao().countExpoTickets()
    val mentorTickets: LiveData<Int> = db.ticketDao().countMentorTickets()
    val talentTickets: LiveData<Int> = db.ticketDao().countTalentTickets()
    val tenantTickets: LiveData<Int> = db.ticketDao().countTenantTickets()
    val juaraTickets: LiveData<Int> = db.ticketDao().countJuaraTickets()

    val allTickets: LiveData<List<Ticket>> = db.ticketDao().getAllTickets()

    fun findTransactionByTextQr(qrCodeContent: String): LiveData<Ticket?> {
        return liveData(Dispatchers.IO) {
            emit(db.ticketDao().findTransactionByTextQr(qrCodeContent))
        }
    }

    fun findTransactionByNomorTransaksi(inputManual: String): LiveData<Ticket?> {
        return liveData(Dispatchers.IO) {
            emit(db.ticketDao().findTransactionByNomorTransaksi(inputManual))
        }
    }

    fun updateWaktuMasuk(nomorTransaksi: String, waktuMasuk: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.ticketDao().updateWaktuMasuk(nomorTransaksi, waktuMasuk)
        }
    }

    fun prepopulateDatabase(context: Context) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isDatabasePrepopulated = sharedPreferences.getBoolean("isDatabasePrepopulated", false)

        if (!isDatabasePrepopulated) {
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
                            val nomorTiket = tokens[1]
                            val nama = tokens[2]
                            val tipeTiket = tokens[3]
                            val textBarcode = tokens[4]
                            val waktuMasuk = if (tokens[5] == "-") null else tokens[5]
                            ticketList.add(Ticket(nomorTransaksi, nomorTiket, nama, tipeTiket, textBarcode, waktuMasuk))
                        }
                    }
                    db.ticketDao().insertAll(ticketList)
                    sharedPreferences.edit().putBoolean("isDatabasePrepopulated", true).apply()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    reader.close()
                }
            }
        }
    }

    fun exportDatabaseToCsv(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = db.ticketDao().getAllTicketsSuspend()

            Log.d("DataSize", "Size of data list: ${data.size}")

            val csvContent = buildString {
                append("nomorTransaksi;nomorTiket;nama;tipeTiket;textBarcode;waktuMasuk\n") // CSV header

                data.forEach { ticket ->
                    append("${ticket.nomorTransaksi};${ticket.nomorTiket};${ticket.nama};${ticket.tipeTiket};${ticket.textBarcode};${ticket.waktuMasuk}\n")
                }
            }

            // Save the CSV content to a file (e.g., in external storage)
            val fileName = "tickets.csv"
            val exportDir = File(context.getExternalFilesDir(null), "CSV")
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }
            val file = File(exportDir, fileName)
            file.createNewFile()
            file.writeText(csvContent)

            // Log the existence of the file
            Log.d("FileExistence", "Does the file exist? ${file.exists()}")

            // Create a content URI for the file using FileProvider
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

            // Now create an Intent to share the file
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/csv"
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            // Show the share sheet to let the user choose an app to share the file
            context.startActivity(Intent.createChooser(shareIntent, "Share CSV via"))

            // Note: Handle exceptions related to file creation and sharing.
        }
    }







//    fun prepopulateDatabase(context: Context) {
//        viewModelScope.launch(Dispatchers.IO) {
//            if (db.ticketDao().getAllTickets().value.isNullOrEmpty()) {
//                val inputStream: InputStream = context.assets.open("tiket_prathentic.csv")
//                val reader = BufferedReader(InputStreamReader(inputStream))
//                val ticketList = mutableListOf<Ticket>()
//
//                try {
//                    reader.readLine() // Skip header line
//                    var line: String?
//                    while (reader.readLine().also { line = it } != null) {
//                        val tokens = line!!.split(',')
//                        if (tokens.size >= 5) { // Ensure there are enough tokens
//                            val nomorTransaksi = tokens[0]
//                            val nomorTiket = tokens[1]
//                            val nama = tokens[2]
//                            val tipeTiket = tokens[3]
//                            val textBarcode = tokens[4]
//                            val waktuMasuk = if (tokens[5] == "-") null else tokens[5]
//                            ticketList.add(Ticket(nomorTransaksi, nomorTiket, nama, tipeTiket, textBarcode, waktuMasuk))
//                        }
//                    }
//                    db.ticketDao().insertAll(ticketList)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                } finally {
//                    reader.close()
//                }
//            }
//        }
//    }
}
