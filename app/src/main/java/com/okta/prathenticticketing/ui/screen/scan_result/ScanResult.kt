package com.okta.prathenticticketing.ui.screen.scan_result

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.okta.prathenticticketing.R
import com.okta.prathenticticketing.TicketViewModel
import com.okta.prathenticticketing.ui.component.PieChartView
import com.okta.prathenticticketing.ui.navigation.Screen
import com.okta.prathenticticketing.ui.theme.PrathenticRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    navController: NavHostController = rememberNavController(),
    qrCode: String,
) {
    val viewModel: TicketViewModel = viewModel()
    val transaction =  if(qrCode.contains("|")) {
        viewModel.findTransactionByTextQr(qrCode).observeAsState().value
    } else{
        viewModel.findTransactionByNomorTransaksi(qrCode).observeAsState().value
    }
    var isFirstScan by remember { mutableStateOf(true) }
    val currentTime = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault()).format(Date())

    if (transaction != null && transaction.waktuMasuk == null) {
        viewModel.updateWaktuMasuk(transaction.nomorTransaksi, currentTime)
        isFirstScan = false
    } else if (transaction != null) {
        Log.d("ScanResultScreen",
            "Transaction found: ${transaction.nomorTransaksi}, ${transaction.nomorTiket}, ${transaction.nama}, ${transaction.tipeTiket}")
    } else {
        Log.d("ScanResultScreen", "Transaction not found")
    }

    if (transaction != null) {
        Log.d("ScanResultScreen", "Valid 'Nomor Transaksi'")
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Scan Result") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Continue",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    actions = {
                        // Add action icons here
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary)
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                if (isFirstScan) {
                    // The 'Nomor Transaksi' is valid and it's the first scan.
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = PrathenticRed
                        )
                    ) {
                        Text(
                            text = "Already Scanned",
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Box(contentAlignment = if (transaction.tipeTiket in listOf("VIP", "Panitia", "Talent", "Juara Talenthic", "Siswa Undangan", "Tenant"))Alignment.BottomCenter else Alignment.TopCenter) {
                        Image(
                            painter = painterResource(id =if (transaction.tipeTiket in listOf("VIP", "Panitia", "Talent", "Juara Talenthic", "Siswa Undangan", "Tenant")) R.drawable.background_tiket_free else R.drawable.background_tiket_paid),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
//                                .alpha(0.3f) // Adjust the alpha to make the background image less prominent
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 52.dp)
                                .padding(bottom = 16.dp, top = if (transaction.tipeTiket in listOf("VIP", "Panitia", "Talent", "Juara Talenthic", "Siswa Undangan", "Tenant")) 0.dp else 160.dp)
                                .fillMaxWidth(),
                        ) {
                            Row{
                                Text(
                                    text = "No Transaksi:",
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(top = 8.dp,),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "No Tiket:",
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(top = 8.dp,),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row {
                                Text(
                                    text = transaction.nomorTransaksi,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )

                                Text(
                                    text = transaction.nomorTiket,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                            }
                            Row{
                                Text(
                                    text = "Nama:",
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(top = 8.dp,),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Tipe Tiket:",
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(top = 8.dp,),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row {
                                Text(
                                    text = transaction.nama,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )

                                Text(
                                    text = transaction.tipeTiket,
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                            }
                            Row{
                                Text(
                                    text = "Tempat:",
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(top = 8.dp,),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Waktu Masuk:",
                                    modifier = Modifier
                                        .weight(0.5f)
                                        .padding(top = 8.dp,),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row {
                                Text(
                                    text = "Pradita University",
                                    modifier = Modifier
                                        .weight(0.5f)
                                )

                                Text(
                                    text = transaction.waktuMasuk ?: (currentTime),
                                    modifier = Modifier
                                        .weight(0.5f)
                                )
                            }
                        }
                    }

                }
                Text(
                    text = "Ticket yang Sudah Masuk dan Belum Masuk",
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(300.dp),
                ) {
                    PieChartView(viewModel)
                }
            }
//        if (transaction.waktuMasuk == null) {
//            // The 'Nomor Transaksi' is valid and it's the first scan.
////            isFirstScan = false
//
//            }
//        } else {
//            Text(text = "Tiket sudah masukk", modifier = Modifier.padding(16.dp))
        }
    } else {
        // The 'Nomor Transaksi' is not valid.
        Log.d("ScanResultScreen", "Invalid 'Nomor Transaksi'")
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                .fillMaxWidth()
                ) {
                Image(painter = painterResource(id = R.drawable.banner_2024), contentDescription = null)
                Text(
                    text = "Ticket Transaction ID is not found",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }

    }

    // Add UI here

}