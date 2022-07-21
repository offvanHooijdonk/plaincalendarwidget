package by.offvanhooijdonk.plaincalendar.widget.ui.configure.settings.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import by.offvanhooijdonk.plaincalendar.widget.model.EventModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel

@Composable
fun WidgetPreview(modifier: Modifier = Modifier, widget: WidgetModel) {
    Box(
        modifier = Modifier
            .then(modifier)
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        WidgetBlueprint(widget)
    }
}

@Composable
private fun WidgetBlueprint(widget: WidgetModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = widget.opacity))
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        
    }
}

@Composable
private fun WidgetEventItem(event: EventModel) {
    ConstraintLayout() {
        
    }
}
