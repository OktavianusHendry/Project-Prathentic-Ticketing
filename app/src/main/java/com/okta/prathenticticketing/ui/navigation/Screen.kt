package com.okta.prathenticticketing.ui.navigation

sealed class Screen(val route: String) {
    object Scan : Screen("scan")
    object Home : Screen("home")
    object Detail : Screen("detail/{detailName}") {
        fun createRoute(detailName: String) = "detail/$detailName"
    }
    object ScanResult : Screen("scanResult/{qrCode}") {
        fun createRoute(qrCode: String) = "scanResult/$qrCode"
    }
}