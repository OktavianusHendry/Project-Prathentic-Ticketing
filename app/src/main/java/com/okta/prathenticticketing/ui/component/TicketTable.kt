package com.okta.prathenticticketing.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.okta.prathenticticketing.TicketViewModel
import com.okta.prathenticticketing.ui.theme.PrathenticBeige
import com.okta.prathenticticketing.ui.theme.PrathenticBlue
import com.okta.prathenticticketing.ui.theme.PrathenticGreen
import com.okta.prathenticticketing.ui.theme.PrathenticPink
import com.okta.prathenticticketing.ui.theme.PrathenticPurple
import com.okta.prathenticticketing.ui.theme.PrathenticRed
import com.okta.prathenticticketing.ui.theme.PrathenticYellow

@Composable
fun TicketTable(viewModel: TicketViewModel) {
    // Observe the tickets from the ViewModel
    val tickets by viewModel.allTickets.observeAsState(initial = emptyList())

    Row(
        modifier = Modifier
            .width(1000.dp)
            .padding(horizontal = 16.dp)
            .border(1.dp, color = Color.Gray)
            .horizontalScroll(rememberScrollState())
    ) {
        Text(
            text = "Nomor Transaksi",
            modifier = Modifier
                .weight(1f)
                .padding(top = 4.dp, start = 4.dp, bottom = 4.dp)
        )
        Text(
            text = "Nama",
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        )
        Text(
            text = "Tipe Tiket",
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        )
        Text(
            text = "Waktu Masuk",
            modifier = Modifier
                .weight(1f)
                .padding(top = 4.dp, end = 4.dp, bottom = 4.dp)
        )
    }
    LazyColumn(
        modifier = Modifier
            .height(300.dp)
//            .fillMaxWidth()
    ) {
        items(tickets.size) { index ->
            val ticket = tickets[index]
            // Each ticket is represented as a row in the table
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, color = Color.Gray)
                    .horizontalScroll(rememberScrollState())
                    .background(when (ticket.tipeTiket){
                        "Mentor" -> PrathenticRed
                        "Expo" -> PrathenticYellow
                        "Dosen" -> PrathenticPurple
                        "Talent" -> PrathenticBlue
                        "Reguler" -> Color.White
                        "VIP" -> PrathenticPink
                        "Tenant" -> PrathenticBeige
                        "Siswa Undangan" -> PrathenticGreen
                        else -> Color.White

                    })
            ) {
                Text(
                    text = ticket.nomorTransaksi,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp, start = 4.dp, bottom = 4.dp)
                )
                Text(
                    text = ticket.nama,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = ticket.tipeTiket,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = ticket.waktuMasuk ?: "-",
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp, end = 4.dp, bottom = 4.dp)
                )
                // Add more columns for other ticket properties
            }
        }
    }
}
