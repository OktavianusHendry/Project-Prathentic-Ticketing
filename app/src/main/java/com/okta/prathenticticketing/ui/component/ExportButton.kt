package com.okta.prathenticticketing.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.okta.prathenticticketing.TicketViewModel
import com.okta.prathenticticketing.ui.theme.PrathenticBlue
import com.okta.prathenticticketing.ui.theme.PrathenticYellow

@Composable
fun ExportButton(viewModel: TicketViewModel) {
    val context = LocalContext.current

    Button(
        onClick = { viewModel.exportDatabaseToCsv(context) },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrathenticBlue,
            contentColor = Color.Black
        ),
    ) {
        Text(
            "Export Database to CSV",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
