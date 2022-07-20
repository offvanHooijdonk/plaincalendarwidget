package by.offvanhooijdonk.plaincalendar.widget.ui.configure.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.ConfigureViewModel
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.Result.Empty
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.Result.Error
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.Result.Progress
import by.offvanhooijdonk.plaincalendar.widget.ui.configure.Result.Success
import kotlin.math.roundToInt

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
            CalendarsForm(widget.calendars) {/*todo*/ }
            DaysNumberForm(widget.days) {/*todo*/ }
        }
    }
}

@Composable
private fun CalendarsForm(list: List<CalendarModel>, onChangeBtnClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (rowList, btn) = createRefs()

        LazyRow(modifier = Modifier.constrainAs(rowList) {
            start.linkTo(parent.start); end.linkTo(btn.start)
        }) {
            items(items = list, key = { it.id }) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = it.displayName.substring(0..1), fontSize = 18.sp
                )
            }
        }
        IconButton(
            modifier = Modifier.constrainAs(btn) { end.linkTo(parent.end) },
            onClick = onChangeBtnClick,
        ) {
            Icon(painter = painterResource(R.drawable.ic_edit_calendar_24), null)
        }
    }
}

@Composable
private fun DaysNumberForm(daySelected: Int, onDaysChange: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val daysPick = remember(daySelected) { mutableStateOf(daySelected.toFloat()) }
        Text(text = daysPick.value.roundToInt().toString(), fontSize = 24.sp)
        Slider(
            value = daysPick.value,
            valueRange = DAYS_RANGE_MIN.toFloat()..DAYS_RANGE_MAX.toFloat(),
            steps = DAYS_RANGE_STEPS,
            onValueChange = { daysPick.value = it },
            onValueChangeFinished = { onDaysChange(daysPick.value.roundToInt()) },
        )
    }
}

private const val DAYS_RANGE_MIN = 1
private const val DAYS_RANGE_MAX = 31
private const val DAYS_RANGE_STEPS = DAYS_RANGE_MAX - DAYS_RANGE_MIN - 1

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
