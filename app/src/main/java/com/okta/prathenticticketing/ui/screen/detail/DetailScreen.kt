package com.okta.prathenticticketing.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.okta.prathenticticketing.R
import com.okta.prathenticticketing.TicketViewModel
import com.okta.prathenticticketing.ui.component.PieChartTipeTiket
import com.okta.prathenticticketing.ui.component.PieChartView
import com.okta.prathenticticketing.ui.component.TicketTable
import com.okta.prathenticticketing.ui.navigation.Screen
import com.okta.prathenticticketing.ui.theme.AliceBlue
import com.okta.prathenticticketing.ui.theme.PrathenticRed
import com.okta.prathenticticketing.ui.theme.PrathenticYellow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    detailName: String,
    navController: NavHostController = rememberNavController(),
) {
    val viewModel: TicketViewModel = viewModel()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(detailName ?: "") },
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
        BottomSheetScaffold(
            sheetContent = {
                SheetContent(navController, bottomSheetScaffoldState)
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetShadowElevation = 16.dp,
            sheetContainerColor = AliceBlue,
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(185.dp)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Button(
                            onClick = {
                                navController.navigate(Screen.Scan.route) {
                                    popUpTo(Screen.Detail.route) {
                                        saveState = true
                                    }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrathenticRed,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                            ) {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                                        contentDescription = null,
                                        Modifier.size(50.dp)
                                    )
                                    Text(
                                        text = "Scan QR Code",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Column(modifier = Modifier.weight(1f)) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrathenticYellow,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                            ) {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_edit_note_24),
                                        contentDescription = null,
                                        Modifier.size(50.dp)
                                    )
                                    Text("Add Manual", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }
                }
                Text(
                    text = "Statistik Tiket",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .size(300.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Ticket yang Sudah Masuk dan Belum Masuk",
                                    modifier = Modifier
                                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                PieChartView(viewModel)
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .size(300.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Jenis Tiket",
                                    modifier = Modifier
                                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                PieChartTipeTiket(viewModel)
                            }
                        }
                    }
                }

                Text(
                    text = "List Tiket",
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                TicketTable(viewModel)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetContent(
    navController: NavHostController = rememberNavController(),
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    val ticketCode = remember { mutableStateOf("") }

    Column() {
        Text(
            text = "Tambah Tiket",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        OutlinedTextField(
            value = ticketCode.value,
            onValueChange = { newTicketCode ->
                ticketCode.value = newTicketCode
            },
            placeholder = { Text("Ex: PRT-xxxxxxxx") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Characters
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Button(
            onClick = {
                // Add your code here
                MainScope().launch {
                    navController.navigate(Screen.ScanResult.createRoute(ticketCode.value)) {
                        popUpTo(Screen.Home.route) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrathenticYellow,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            enabled = ticketCode.value.isNotEmpty()
        ) {
            Text("Tambah Tiket", style = MaterialTheme.typography.titleMedium)
        }
    }
}