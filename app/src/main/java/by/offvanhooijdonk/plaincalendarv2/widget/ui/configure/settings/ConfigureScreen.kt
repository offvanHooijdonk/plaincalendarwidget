@file:OptIn(ExperimentalMaterialApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import by.offvanhooijdonk.plaincalendarv2.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.ConfigureViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.Result
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts.LayoutsPickPanel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview.WidgetPreview
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs.StylesTabsPanel
import kotlin.math.roundToInt

@Composable
fun MainScreen(viewModel: ConfigureViewModel) {
    val title = when (val result = viewModel.widgetResponse.observeAsState().value) {
        is Result.Widget.Success -> "Widget #${result.data.id}"
        Result.Widget.New -> "New widget"
        else -> "..."
    }
    Scaffold(
        backgroundColor = Color.Transparent,
        topBar = { TopAppBar(title = { Text(title) }) },
    ) {
        ConfigureScreenWrap(viewModel)
    }
}

@Composable
private fun ConfigureScreenWrap(viewModel: ConfigureViewModel) {
    when (val result = viewModel.widgetResponse.observeAsState().value) {
        is Result.Widget.Success -> ConfigureScreen(
            result.data,
            viewModel.calendarsResponse.observeAsState().value ?: Result.Idle,
            { viewModel.loadCalendars()/*todo calendars permissions*/ },
            { viewModel.updateWidget(it) },
        )
        Result.Widget.New -> ConfigureScreen(
            WidgetModel.createDefault().copy(id = viewModel.widgetId?.toLong() ?: 0),
            viewModel.calendarsResponse.observeAsState().value ?: Result.Idle,
            { viewModel.loadCalendars() },
            { viewModel.updateWidget(it) },
        )
        is Result.Error -> ErrorScreen(result.msg ?: "Default error")
        Result.Progress -> LoadingScreen()
        else -> Unit
    }
}

@Composable
private fun ConfigureScreen(
    widget: WidgetModel,
    allCalendars: Result, // todo don't like it
    onCalendarsRequested: () -> Unit,
    onSaveChanges: (WidgetModel) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val widgetPreview = remember(widget) { mutableStateOf(widget) }
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topSettings, layouts, preview, btn, bottomSettings) = createRefs()
            Column(
                Modifier
                    .background(color = MaterialTheme.colors.surface)
                    .padding(16.dp)
                    .constrainAs(topSettings) {
                        top.linkTo(parent.top)
                    }
            ) {
                //val calendarsList = remember(widget) { mutableStateOf(widget.calendars) }
                CalendarsForm(
                    widgetPreview.value.calendars,
                    allCalendars,
                    onChangeBtnClick = { onCalendarsRequested() },
                    onCalendarsSelected = { list ->
                        widgetPreview.value = widgetPreview.value.copy(calendars = list, calendarIds = list.map { it.id })
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))

                DaysNumberForm(widgetPreview.value.days) { widgetPreview.value = widgetPreview.value.copy(days = it) }
            }
            LayoutsPickPanel(
                modifier = Modifier.constrainAs(layouts) {
                    top.linkTo(topSettings.bottom)
                },
                widget = widgetPreview.value,
                onLayoutPick = { widgetPreview.value = widgetPreview.value.copy(layoutType = it) },
            )

            WidgetPreview(
                modifier = Modifier.constrainAs(preview) {
                    top.linkTo(layouts.bottom)
                    bottom.linkTo(bottomSettings.top)
                },
                widget = widgetPreview.value,
            )
            if (widget.id != 0L) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.constrainAs(btn) {
                        bottom.linkTo(bottomSettings.top, 16.dp)
                        centerHorizontallyTo(parent)
                    },
                    text = { Text(text = stringResource(R.string.btn_save_widget_settings), color = Color.White) },
                    onClick = { onSaveChanges(widgetPreview.value) },
                )
            }

            Box(modifier = Modifier
                .background(color = MaterialTheme.colors.surface)
                .fillMaxWidth()
                .constrainAs(bottomSettings) {
                    bottom.linkTo(parent.bottom)
                }) {
                StylesTabsPanel(widgetPreview.value) { widgetPreview.value = it }
            }
        }
    }
}

@Composable
private fun CalendarsForm(
    pickedCalendars: List<CalendarModel>,
    allCalendars: Result,
    onChangeBtnClick: () -> Unit,
    onCalendarsSelected: (List<CalendarModel>) -> Unit,
) {
    val isDialogCanShow = remember { mutableStateOf(false) }

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (caption, rowList, btn) = createRefs()

        Text(
            modifier = Modifier.constrainAs(caption) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            },
            text = "Calendars".uppercase() + pickedCalendars.size.let { if (it == 0) "" else ": $it" },
            color = MaterialTheme.colors.primary,
        )
        LazyRow(modifier = Modifier.constrainAs(rowList) {
            //start.linkTo(parent.start); end.linkTo(parent.end)
            width = Dimension.matchParent
            //this.width = Dimension.fillToConstraints
            top.linkTo(caption.bottom, 8.dp)
        }) {
            if (pickedCalendars.isEmpty()) {
                item { Text(modifier = Modifier.padding(6.dp), text = "No calendars picked") }
            } else {
                items(items = pickedCalendars, key = { it.id }) {
                    Chip(
                        onClick = { },
                        shape = RoundedCornerShape(percent = 50),
                        colors = ChipDefaults.chipColors(
                            backgroundColor = it.color?.let { color -> Color(color) } ?: MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        ),
                    ) {
                        Text(text = it.displayName.calendarName/*.uppercase()*/, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
        }
        val showPermissionCheck = remember { mutableStateOf(false) }
        if (showPermissionCheck.value) {
            permissionCheck(
                ifGranted = {
                    onChangeBtnClick()
                    isDialogCanShow.value = true
                },
                ifDenied = { /*todo*/ },
                ifShowRationale = { /*todo*/ },
            )
            showPermissionCheck.value = false
        }
        IconButton(
            modifier = Modifier.constrainAs(btn) { end.linkTo(parent.end); centerVerticallyTo(caption) },
            onClick = { showPermissionCheck.value = true },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_edit_calendar_24),
                tint = MaterialTheme.colors.primary,
                contentDescription = null
            )
        }
    }

    if (allCalendars is Result.Calendars.Success && isDialogCanShow.value) {
        CalendarsPickDialog(
            pickedCalendars = pickedCalendars,
            allCalendars = allCalendars.list,
            onDismissRequest = { isDialogCanShow.value = false },
            onSelectionSave = { list -> isDialogCanShow.value = false; onCalendarsSelected(list) },
        )
    }
}

@Composable
private fun DaysNumberForm(daySelected: Int, onDaysChange: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Days to show".uppercase(), color = MaterialTheme.colors.primary)

        val daysPick = remember(daySelected) { mutableStateOf(daySelected.toFloat()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.width(28.dp),
                text = daysPick.value.roundToInt().toString(),
                fontSize = 20.sp,
                textAlign = TextAlign.End,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                modifier = Modifier.systemGestureExclusion(),
                value = daysPick.value,
                valueRange = DAYS_RANGE_MIN.toFloat()..DAYS_RANGE_MAX.toFloat(),
                steps = DAYS_RANGE_STEPS,
                onValueChange = { daysPick.value = it },
                onValueChangeFinished = { onDaysChange(daysPick.value.roundToInt()) },
            )
        }
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

private val String.calendarName: String
    get() = this.substringBefore('@')

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
    ConfigureScreen(WidgetModel.createDefault().copy(id = 1L, days = 25), Result.Idle, {}) {}
}
