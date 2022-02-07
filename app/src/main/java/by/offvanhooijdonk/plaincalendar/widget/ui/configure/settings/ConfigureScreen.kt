package by.offvanhooijdonk.plaincalendar.widget.ui.configure.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.ConfigureViewModel
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.Result.Empty
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.Result.Error
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.Result.Progress
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.Result.Success

@Composable
fun MainScreen(viewModel: ConfigureViewModel) {
    val title = when (val result = viewModel.widgetResponse.observeAsState().value) {
        is Success -> "Widget #${result.data.id}"
        Empty -> "New widget"
        else -> "..."
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text(title) }) }
    ) {
        ConfigureScreenWrap(viewModel)
    }
}

@Composable
private fun ConfigureScreenWrap(viewModel: ConfigureViewModel) {
    when (val result = viewModel.widgetResponse.observeAsState().value) {
        is Success -> ConfigureScreen(result.data)
        Empty -> ConfigureScreen(WidgetModel())
        is Error -> ErrorScreen(result.msg ?: "Default error")
        Progress -> LoadingScreen()
        else -> Unit
    }
}

@Composable
private fun ConfigureScreen(widget: WidgetModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .background(color = MaterialTheme.colors.surface)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (widget.calendars.isEmpty()) {
                    Text("No calendars")
                } else {
                    widget.calendars.forEach { c ->
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = c.displayName.substring(0..1), fontSize = 18.sp,
                        )
                    }
                }
                Row(modifier = Modifier.weight(1.0f), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.ic_edit_calendar_24), null)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Days")
                Row(modifier = Modifier.weight(1.0f), horizontalArrangement = Arrangement.End) {
                    TextField(
                        modifier = Modifier.width(64.dp),
                        value = "",
                        onValueChange = {},
                    )
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = "+1")
                    }
                    OutlinedButton(onClick = { /*TODO*/ }) {
                        Text(text = "-1")
                    }
                }
            }
        }

    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(strokeWidth = 4.dp)
    }
}

@Composable
private fun ErrorScreen(msg: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = msg, color = MaterialTheme.colors.error, fontSize = 24.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview_ConfigureNew() {
    ConfigureScreen(WidgetModel())
}
