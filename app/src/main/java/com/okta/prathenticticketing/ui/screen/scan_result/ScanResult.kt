package com.okta.prathenticticketing.ui.screen.scan_result

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.okta.prathenticticketing.R
import com.okta.prathenticticketing.TicketViewModel
import com.okta.prathenticticketing.ui.component.PieChartView
import com.okta.prathenticticketing.ui.navigation.Screen
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
    val transaction = viewModel.findTransactionByNomorTransaksi(qrCode).observeAsState().value

    if (transaction != null) {
        // The 'Nomor Transaksi' is valid.
        Log.d("ScanResultScreen", "Valid 'Nomor Transaksi'")
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        viewModel.updateWaktuMasuk(transaction.nomorTransaksi, currentTime)
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
                                modifier = Modifier
                                    .size(24.dp)
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
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Image(painter = painterResource(id = R.drawable.banner_2024), contentDescription = null)
                    Text(
                        text = "Ticket Transaction ID: ${transaction.nomorTransaksi}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                    )
                    Text(
                        text = "Waktu Masuk: $currentTime",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
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