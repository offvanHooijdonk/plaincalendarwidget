package by.offvanhooijdonk.plaincalendarv2.widget.ui.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D

@Composable
fun ExtendedFAB(modifier: Modifier = Modifier, onClick: () -> Unit, enabled: Boolean = true, text: @Composable () -> Unit) {
    Button(
        modifier = Modifier
            .height(ExtFABHeight)
            .then(modifier),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 12.dp,
            hoveredElevation = 8.dp,
            focusedElevation = 8.dp
        ),
        shape = RoundedCornerShape(percent = 50),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = Color.White,
        ),
        onClick = { onClick() },
        enabled = enabled
    ) {
        Spacer(modifier = Modifier.width(D.spacingS))
        text()
        Spacer(modifier = Modifier.width(D.spacingS))
    }
}

val ExtFABHeight = 48.dp
