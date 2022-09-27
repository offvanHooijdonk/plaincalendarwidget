@file:OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.CalendarsPickDialog
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.SettingsScreen
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts.LayoutsPickPanel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview.WidgetPreview
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs.StylesTabsPanel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.intro.*
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.ExtendedFAB
import com.canopas.lib.showcase.IntroShowCaseScaffold
import com.canopas.lib.showcase.IntroShowCaseScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlin.math.roundToInt

@Composable
fun MainScreen(viewModel: ConfigureViewModel) {
    val widget = viewModel.widgetModel.observeAsState(DummyWidget).value
    val title = when (viewModel.loadResult.observeAsState().value) {
        is Result.Widget.Success -> stringResource(R.string.toolbar_title_widget_number, widget.id)
        Result.Widget.Empty -> "Plain Calendar Widget"
        Result.Widget.New -> stringResource(R.string.toolbar_title_new_widget)
        else -> stringResource(R.string.toolbar_title_empty)
    }

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val isShowSettings = viewModel.showSettingsSheet.observeAsState().value
    LaunchedEffect(key1 = isShowSettings) {
        if (isShowSettings?.value == true) {
            sheetState.show()
        }
    }

    IntroShowCaseScaffold(
        showIntroShowCase = !viewModel.isIntroPassed.observeAsState(true).value,
        onShowCaseCompleted = { viewModel.onIntroPassed() }
    ) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = {
                SettingsScreen(viewModel.widgetModel.observeAsState(DummyWidget).value, viewModel::onWidgetChange)
            },
        ) {
            Scaffold(
                backgroundColor = Color.Transparent,
                topBar = { TopAppBar(title = { Text(title) }) },
            ) {
                ConfigureScreenWrap(viewModel)
            }
        }
    }

    if (viewModel.showExitConfirmation.observeAsState(false).value) {
        AlertDialog(
            onDismissRequest = { viewModel.onExitCanceled() },
            title = { Text(stringResource(R.string.exit_confirmation_title)) },
            text = { Text(stringResource(R.string.exit_confirmation_text)) },
            confirmButton = {
                TextButton(onClick = { viewModel.onExitConfirmed() }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onExitCanceled() }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun IntroShowCaseScope.ConfigureScreenWrap(viewModel: ConfigureViewModel) {
    val widget = viewModel.widgetModel.observeAsState(DummyWidget)
    when (val result = viewModel.loadResult.observeAsState().value) {
        Result.Widget.New, Result.Widget.Success, Result.Widget.Empty -> ConfigureScreen(
            widget.value,
            viewModel.calendarsResponse.observeAsState().value ?: Result.Idle,
            onCalendarsRequested = viewModel::loadCalendars,
            onWidgetChange = viewModel::onWidgetChange,
            onSaveChanges = viewModel::updateWidget,
            onSettingsClick = viewModel::onSettingsClick,
            viewModel.isIntroPassed.observeAsState(true).value,
        )
        is Result.Error -> ErrorScreen(result.msg ?: "Default error")
        Result.Progress, Result.Idle -> LoadingScreen()
        else -> Unit
    }
}

@Composable
private fun IntroShowCaseScope.ConfigureScreen(
    widget: WidgetModel,
    allCalendars: Result, // todo don't like it
    onCalendarsRequested: () -> Unit,
    onWidgetChange: (WidgetModel) -> Unit,
    onSaveChanges: () -> Unit,
    onSettingsClick: () -> Unit,
    isIntroPassed: Boolean,
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topSettings, layouts, preview, previewIntro, btnSave, btnSettings, bottomSettingsIntro, bottomSettings) = createRefs()
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.surface)
                    .padding(D.spacingL)
                    .constrainAs(topSettings) { top.linkTo(parent.top) }
            ) {
                //val calendarsList = remember(widget) { mutableStateOf(widget.calendars) }
                CalendarsForm(
                    widget.calendars,
                    allCalendars,
                    onChangeBtnClick = { onCalendarsRequested() },
                    onCalendarsSelected = { list ->
                        onWidgetChange(widget.copy(calendars = list, calendarIds = list.map { it.id }))
                    },
                )
                Spacer(modifier = Modifier.height(D.spacingL))

                DaysNumberForm(widget.days) { onWidgetChange(widget.copy(days = it)) }
            }
            LayoutsPickPanel(
                modifier = Modifier.constrainAs(layouts) {
                    top.linkTo(topSettings.bottom)
                },
                widget = widget,
                onLayoutPick = { onWidgetChange(widget.copy(layoutType = it)) },
            )

            WidgetPreview(
                modifier = Modifier
                    .constrainAs(preview) {
                        top.linkTo(layouts.bottom, D.spacingL)
                        bottom.linkTo(btnSettings.top, D.spacingM)
                        start.linkTo(parent.start, D.spacingXXL)
                        end.linkTo(parent.end, D.spacingXXL)
                        width = Dimension.fillToConstraints
                    },
                widget = widget,
            )

            Box(modifier = Modifier
                .constrainAs(previewIntro) {
                    start.linkTo(preview.start, 32.dp)
                    top.linkTo(preview.top, 16.dp)
                    width = Dimension.value(100.dp)
                    height = Dimension.value(132.dp)
                }
                .introShowCaseTarget(IntroTargets.PREVIEW.ordinal, IntroStylePreview) {
                    IntroPreview()
                }) {}

            val isSaveEnabled = !isIntroPassed || (widget.id != 0L && widget.calendars.isNotEmpty())
            ExtendedFAB(
                modifier = Modifier
                    .constrainAs(btnSave) {
                        top.linkTo(btnSettings.top)
                        bottom.linkTo(btnSettings.bottom)
                        end.linkTo(preview.end)
                    }
                    .introShowCaseTarget(IntroTargets.APPLY.ordinal, IntroStyleApplyButton) { IntroApplyButton() },
                onClick = { onSaveChanges() },
                enabled = isSaveEnabled,
            ) {
                Text(text = stringResource(R.string.btn_save_widget_settings))
            }

            FloatingActionButton(
                modifier = Modifier
                    .constrainAs(btnSettings) {
                        bottom.linkTo(bottomSettings.top, D.spacingL)
                        start.linkTo(preview.start)
                    }
                    .introShowCaseTarget(IntroTargets.SETTINGS.ordinal, IntroStyleSettings) { IntroSettings() },
                onClick = { onSettingsClick() },
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.primary,
            ) {
                Icon(painterResource(R.drawable.ic_settings), contentDescription = null)
            }


            Box(modifier = Modifier
                .background(color = MaterialTheme.colors.surface)
                .fillMaxWidth()
                .constrainAs(bottomSettings) {
                    bottom.linkTo(parent.bottom)
                }) {
                StylesTabsPanel(widget) { onWidgetChange(it) }
            }
            Box(modifier = Modifier
                .constrainAs(bottomSettingsIntro) {
                    top.linkTo(bottomSettings.top, 0.dp)
                    start.linkTo(bottomSettings.start, 26.dp)
                    height = Dimension.value(44.dp)
                    width = Dimension.value(44.dp)
                }
                .introShowCaseTarget(IntroTargets.COLOR_TABS.ordinal, IntroStyleColorsTabs) { IntroColorsTabs() }
            )
        }
    }
}

@Composable
private fun IntroShowCaseScope.CalendarsForm(
    pickedCalendars: List<CalendarModel>,
    allCalendars: Result,
    onChangeBtnClick: () -> Unit,
    onCalendarsSelected: (List<CalendarModel>) -> Unit,
) {


    val isDialogCanShow = remember { mutableStateOf(false) }

    val permissionCalendar = rememberPermissionState(
        android.Manifest.permission.READ_CALENDAR
    ) { isGranted ->
        if (isGranted) {
            onChangeBtnClick()
            isDialogCanShow.value = true
        }
    }

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (caption, rowList) = createRefs()

        Text(
            modifier = Modifier.constrainAs(caption) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            },
            text = stringResource(R.string.title_calendars_picked).uppercase() + pickedCalendars.size.let { if (it == 0) "" else ": $it" },
            color = MaterialTheme.colors.primary,
        )
        LazyRow(
            modifier = Modifier.constrainAs(rowList) {
                width = Dimension.matchParent
                top.linkTo(caption.bottom, D.spacingM)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pickedCalendars.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.width(D.spacingM))
                    Text(text = stringResource(R.string.calendars_picked_empty), fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(D.spacingL))
                }
            }
            item(key = "configure") {
                val context = LocalContext.current
                val rationaleText = stringResource(R.string.calendar_permissions_rationale)
                Chip(
                    modifier = Modifier.introShowCaseTarget(IntroTargets.CONFIGURE_CALENDARS.ordinal, IntroStyleConfigureCalendars) {
                        IntroConfigureCalendars()
                    },
                    onClick = {
                        if (permissionCalendar.status != PermissionStatus.Granted) {
                            if (permissionCalendar.status.shouldShowRationale) {
                                Toast.makeText(context, rationaleText, Toast.LENGTH_LONG).show()
                            }
                            permissionCalendar.launchPermissionRequest()
                        }
                    },
                    shape = RoundedCornerShape(percent = 50),
                    colors = ChipDefaults.chipColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Spacer(modifier = Modifier.width(D.spacingS))
                    Icon(
                        modifier = Modifier.size(D.spacingL),
                        painter = painterResource(R.drawable.ic_edit_calendar_24),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(D.spacingS))
                }
                Spacer(modifier = Modifier.width(D.spacingM))
            }
            if (pickedCalendars.isNotEmpty()) {
                items(items = pickedCalendars, key = { it.id }) {
                    Chip(
                        // todo to separate fun
                        onClick = { },
                        shape = RoundedCornerShape(percent = 50),
                        colors = ChipDefaults.chipColors(
                            backgroundColor = it.color?.let { color -> Color(color) } ?: MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        ),
                    ) {
                        Text(text = it.displayName.calendarName, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.width(D.spacingS))
                }
            }
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
private fun IntroShowCaseScope.DaysNumberForm(daySelected: Int, onDaysChange: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.title_days_to_show).uppercase(), color = MaterialTheme.colors.primary)

        val daysPick = remember(daySelected) { mutableStateOf(daySelected.toFloat()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.width(D.spacingXXL),
                text = daysPick.value.roundToInt().toString(),
                fontSize = 20.sp,
                textAlign = TextAlign.End,
            )
            Spacer(modifier = Modifier.width(D.spacingM))
            Box(contentAlignment = Alignment.CenterStart) {
                Box(modifier = Modifier
                    .size(20.dp)
                    //.padding(start = 4.dp)
                    .introShowCaseTarget(IntroTargets.DAYS_NUMBER.ordinal, IntroStyleDays) { IntroDays() }) {}
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
}

private const val DAYS_RANGE_MIN = 1
private const val DAYS_RANGE_MAX = 31
private const val DAYS_RANGE_STEPS = DAYS_RANGE_MAX - DAYS_RANGE_MIN

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(strokeWidth = D.spacingS)
    }
}

private val String.calendarName: String
    get() = this.substringBefore('@')

@Composable
private fun ErrorScreen(msg: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(D.spacingL)
    ) {
        Text(text = msg, color = MaterialTheme.colors.error, fontSize = 24.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun Preview_ConfigureNew() {
    PlainTheme {
        IntroShowCaseScaffold(showIntroShowCase = true, onShowCaseCompleted = { /*TODO*/ }) {
            ConfigureScreen(DummyWidget.copy(id = 1L, days = 25), Result.Idle, {}, {}, {}, {}, true)
        }
    }
}
