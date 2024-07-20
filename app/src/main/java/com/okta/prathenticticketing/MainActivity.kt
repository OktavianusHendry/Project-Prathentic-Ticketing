package com.okta.prathenticticketing

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.camera.view.PreviewView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.okta.prathenticticketing.data.local.TicketDatabase
import com.okta.prathenticticketing.ui.navigation.Screen
import com.okta.prathenticticketing.ui.screen.detail.DetailScreen
import com.okta.prathenticticketing.ui.screen.home.HomeScreen
import com.okta.prathenticticketing.ui.screen.scan.ScanScreen
import com.okta.prathenticticketing.ui.screen.scan_result.ScanResultScreen
import com.okta.prathenticticketing.ui.theme.PrathenticTicketingTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            PrathenticTicketingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val context = LocalContext.current
                    val navController = rememberNavController()

                    val ticketViewModel: TicketViewModel by viewModels()
                    ticketViewModel.prepopulateDatabase(context)


                    NavHost(navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController)
                        }
                        composable(
                            route = Screen.Detail.route,
                            arguments = listOf(navArgument("detailName") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val detailName = backStackEntry.arguments?.getString("detailName")
                            if (detailName != null) {
                                DetailScreen(detailName, navController)
                            } else {
                                // Handle the case where detailName is null
                            }
                        }
                        composable(Screen.Scan.route) {
                            ScanScreen(navController, onQRCodeDetected = { qrCode ->
                                // Handle the QR code here
                                println("QR Code detected: $qrCode")
                                Log.d("QR Code detected", qrCode)

                                // Log right before navigation
                                Log.d("Navigation", "Navigating to ScanResultScreen with QR Code: $qrCode")

                                // Ensure navigation is on the main thread
                                MainScope().launch {
                                    navController.navigate(Screen.ScanResult.createRoute(qrCode)){
                                        popUpTo(Screen.Home.route) {
                                            saveState = true
                                        }
                                        restoreState = true
                                        launchSingleTop = true
                                    }
                                }
                            })
                        }
                        composable(
                            route = Screen.ScanResult.route,
                            arguments = listOf(navArgument("qrCode") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val qrCode = backStackEntry.arguments?.getString("qrCode")
                            if (qrCode != null) {
                                ScanResultScreen(navController, qrCode)
                            } else {
                                Toast.makeText(context, "QR Code not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
            }
        }
    }


}

