package com.okta.prathenticticketing.ui.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.data.PieEntry
import com.okta.prathenticticketing.data.local.Ticket
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.okta.prathenticticketing.TicketViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.okta.prathenticticketing.ui.theme.PrathenticBeige
import com.okta.prathenticticketing.ui.theme.PrathenticBlue
import com.okta.prathenticticketing.ui.theme.PrathenticGreen
import com.okta.prathenticticketing.ui.theme.PrathenticPink
import com.okta.prathenticticketing.ui.theme.PrathenticPurple
import com.okta.prathenticticketing.ui.theme.PrathenticRed
import com.okta.prathenticticketing.ui.theme.PrathenticYellow


@Composable
fun PieChartView(viewModel: TicketViewModel) {
    val ticketsWithWaktuMasuk by viewModel.ticketsWithWaktuMasuk.observeAsState(0)
    val ticketsWithoutWaktuMasuk by viewModel.ticketsWithoutWaktuMasuk.observeAsState(0)

    Log.d("PieChartView", "Tickets with WaktuMasuk: $ticketsWithWaktuMasuk")
    Log.d("PieChartView", "Tickets without WaktuMasuk: $ticketsWithoutWaktuMasuk")

    val entries = listOf(
        PieEntry(ticketsWithWaktuMasuk.toFloat(), "Sudah Masuk"),
        PieEntry(ticketsWithoutWaktuMasuk.toFloat(), "Belum Masuk")
    )

    val pieData = PieData(PieDataSet(entries, "").apply {
        colors = listOf(PrathenticYellow.toArgb(), PrathenticRed.toArgb())
        valueTextSize = 16f
        valueTextColor = Color.Black.toArgb()
        valueLineColor = Color.Black.toArgb()

        valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }
    })

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                data = pieData
                setDrawEntryLabels(true)
                description.isEnabled = false
                setDrawHoleEnabled(false)
                legend.textSize = 12f
                setEntryLabelColor(Color.Black.toArgb())
                invalidate()
            }
        },
        update = { view ->
            view.data = pieData
            view.invalidate()
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    )
}

@Composable
fun PieChartTipeTiket(viewModel: TicketViewModel) {
    val vipTicket by viewModel.vipTickets.observeAsState(0)
    val regularTicket by viewModel.regularTickets.observeAsState(0)
    val siswaUndanganTicket by viewModel.siswaUndanganTickets.observeAsState(0)
//    val dosenTicket by viewModel.dosenTickets.observeAsState(0)
    val expoTicket by viewModel.expoTickets.observeAsState(0)
    val mentorTicket by viewModel.mentorTickets.observeAsState(0)
    val talentTicket by viewModel.talentTickets.observeAsState(0)
    val tenantTicket by viewModel.tenantTickets.observeAsState(0)
    val juaraTicket by viewModel.juaraTickets.observeAsState(0)

    val entries = listOf(
        PieEntry(vipTicket.toFloat(), "VIP"),
        PieEntry(regularTicket.toFloat(), "Regular"),
        PieEntry(siswaUndanganTicket.toFloat(), "Siswa Undangan"),
//        PieEntry(dosenTicket.toFloat(), "Dosen"),
        PieEntry(expoTicket.toFloat(), "Expo"),
        PieEntry(mentorTicket.toFloat(), "Mentor"),
        PieEntry(talentTicket.toFloat(), "Talent"),
        PieEntry(tenantTicket.toFloat(), "Tenant"),
        PieEntry(juaraTicket.toFloat(), "Juara Talenthic")
    )

    val pieData = PieData(PieDataSet(entries, "").apply {
        colors = listOf(
            PrathenticPink.toArgb(),
            Color.White.toArgb(),
            PrathenticGreen.toArgb(),
            PrathenticPurple.toArgb(),
            PrathenticYellow.toArgb(),
            PrathenticRed.toArgb(),
            PrathenticBlue.toArgb(),
            PrathenticBeige.toArgb()
        )
        valueTextSize =
            16f
        valueTextColor = Color.Black.toArgb()
        valueLineColor = Color.Black.toArgb()
    })

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                data = pieData
                setDrawEntryLabels(true)
                description.isEnabled = false
                setDrawHoleEnabled(false)
                legend.textSize = 12f
                setEntryLabelColor(Color.Black.toArgb())
                invalidate()
            }
        },
        update = { view ->
            view.data = pieData
            view.invalidate()
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    )
}