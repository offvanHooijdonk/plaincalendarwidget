@file:OptIn(ExperimentalFoundationApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs.*
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.ConfigureViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.Result
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview.WidgetPreview
import kotlin.math.roundToInt

@Composable
fun MainScreen(viewModel: ConfigureViewModel) {
    val title = when (val result = viewModel.widgetResponse.observeAsState().value) {
        is Result.Success -> "Widget #${result.data.id}"
        Result.Empty -> "New widget"
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
        is Result.Success -> ConfigureScreen(result.data)
        Result.Empty -> ConfigureScreen(WidgetModel())
        is Result.Error -> ErrorScreen(result.msg ?: "Default error")
        Result.Progress -> LoadingScreen()
        else -> Unit
    }
}

@Composable
private fun ConfigureScreen(widget: WidgetModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topSettings, preview, bottomSettings) = createRefs()
            Column(
                Modifier
                    .background(color = MaterialTheme.colors.surface)
                    .padding(16.dp)
                    .constrainAs(topSettings) {
                        top.linkTo(parent.top)
                    }
            ) {
                CalendarsForm(widget.calendars) {/*todo*/ }
                DaysNumberForm(widget.days) {/*todo*/ }
            }

            WidgetPreview(
                modifier = Modifier.constrainAs(preview) {
                    top.linkTo(topSettings.bottom)
                    bottom.linkTo(bottomSettings.top)
                },
                widget = WidgetModel(opacity = 0.5f),
            )

            Box(modifier = Modifier
                .background(color = MaterialTheme.colors.surface)
                .fillMaxWidth()
                .constrainAs(bottomSettings) {
                    bottom.linkTo(parent.bottom)
                }) {
                SettingsBottomPanel(widget)
            }
        }
    }
}

@Composable
private fun CalendarsForm(list: List<CalendarModel>, onChangeBtnClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (rowList, btn) = createRefs()

        LazyRow(modifier = Modifier.constrainAs(rowList) {
            start.linkTo(parent.start); end.linkTo(btn.start)
            this.width = Dimension.fillToConstraints
            centerVerticallyTo(parent)
        }) {
            stickyHeader {
                Text(text = "Calendars:")
            }
            items(items = list, key = { it.id }) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = it.displayName.substring(0..1), fontSize = 18.sp
                )
            }
        }
        IconButton(
            modifier = Modifier.constrainAs(btn) { end.linkTo(parent.end); centerVerticallyTo(parent) },
            onClick = onChangeBtnClick,
        ) {
            Icon(painter = painterResource(R.drawable.ic_edit_calendar_24), null)
        }
    }
}

@Composable
private fun DaysNumberForm(daySelected: Int, onDaysChange: (Int) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth().systemGestureExclusion()) {
        val daysPick = remember(daySelected) { mutableStateOf(daySelected.toFloat()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Days to show:")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = daysPick.value.roundToInt().toString(), fontSize = 18.sp)
        }
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
private fun SettingsBottomPanel(widget: WidgetModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val selectedIndex = remember { mutableStateOf(0) }
        TabRow(selectedTabIndex = selectedIndex.value) {
            SettingTabsList.forEachIndexed { index, tab ->
                LeadingIconTab(
                    selected = index == selectedIndex.value,
                    onClick = { selectedIndex.value = index },
                    text = { },
                    icon = {
                        Icon(painterResource(tab.iconRes), contentDescription = null)
                    }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            // todo add some animation, like CrossFade ?
            when (SettingTabsList[selectedIndex.value]) {
                SettingTab.ColorTab -> ColorTab()
                SettingTab.OpacityTab -> OpacityTab()
                SettingTab.TextColorTab -> TextColorTab()
                SettingTab.TextSizeTab -> TextSizeTab()
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
    ConfigureScreen(WidgetModel(days = 5))
}
