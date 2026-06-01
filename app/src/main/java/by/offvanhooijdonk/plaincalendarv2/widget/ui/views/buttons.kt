package by.offvanhooijdonk.plaincalendarv2.widget.ui.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens

@Composable
fun ExtendedFAB(modifier: Modifier = Modifier, onClick: () -> Unit, enabled: Boolean = true, text: @Composable () -> Unit) {
    ElevatedButton(
        modifier = Modifier
            .height(ExtFABHeight)
            .then(modifier),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 12.dp,
            hoveredElevation = 8.dp,
            focusedElevation = 8.dp
        ),
        shape = RoundedCornerShape(percent = 50),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = Color.White,
        ),
        onClick = { onClick() },
        enabled = enabled
    ) {
        Spacer(modifier = Modifier.width(dimens().spacingS))
        text()
        Spacer(modifier = Modifier.width(dimens().spacingS))
    }
}

val ExtFABHeight = 48.dp
