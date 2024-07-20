package com.okta.prathenticticketing.ui.screen.scan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.Image
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun ScanScreen(
    navController: NavHostController = rememberNavController(),
    onQRCodeDetected: (String) -> Unit,
) {
    val context = LocalContext.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val previewView = remember { PreviewView(context) }

    AndroidView({ previewView }) { view ->
        if (allPermissionsGranted(context)) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(
                    cameraProvider,
                    onQRCodeDetected,
                    cameraExecutor,
                    context as LifecycleOwner,
                    view.surfaceProvider
                )
            }, ContextCompat.getMainExecutor(context))
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CAMERA),
                0
            )
        }

        if (allPermissionsGranted(context)) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(
                    cameraProvider,
                    onQRCodeDetected,
                    cameraExecutor,
                    context as LifecycleOwner,
                    view.surfaceProvider
                )
            }, ContextCompat.getMainExecutor(context))
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CAMERA),
                0
            )
        }
    }


}

@OptIn(ExperimentalGetImage::class)
private fun bindPreview(
    cameraProvider: ProcessCameraProvider,
    onQRCodeDetected: (String) -> Unit,
    cameraExecutor: ExecutorService,
    lifecycleOwner: LifecycleOwner,
    surfaceProvider: Preview.SurfaceProvider
) {
    val preview = Preview.Builder().build().also {
        it.setSurfaceProvider(surfaceProvider)
    }
    val cameraSelector =
        CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
    val imageAnalysis = ImageAnalysis.Builder()
        .setTargetResolution(Size(1280, 720))
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    imageAnalysis.setAnalyzer(cameraExecutor, { imageProxy ->
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val pixels = mediaImage.toGrayScaleIntArray()
            val source = RGBLuminanceSource(mediaImage.width, mediaImage.height, pixels)
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val reader = MultiFormatReader()
            try {
                val result = reader.decode(binaryBitmap)
                onQRCodeDetected(result.text)
            } catch (e: Exception) {
                // QR Code not found
            }
        }
        imageProxy.close()
    })

    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
}

private fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
    ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
}

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

private fun ByteBuffer.toByteArray(): ByteArray {
    rewind()    // Rewind the buffer to zero
    val data = ByteArray(remaining())
    get(data)   // Copy the buffer into a byte array
    return data // Return the byte array
}

private fun Image.toGrayScaleIntArray(): IntArray {
    val yBuffer = planes[0].buffer
    val ySize = yBuffer.remaining()
    val data = ByteArray(ySize)
    yBuffer.get(data, 0, ySize)

    val pixels = IntArray(width * height)
    for (i in pixels.indices) {
        val y = data[i].toInt() and 0xFF
        pixels[i] = 0xFF000000.toInt() or (y shl 16) or (y shl 8) or y
    }

    return pixels
}

