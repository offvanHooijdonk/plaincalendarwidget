@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.calendar.CalendarModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.calendar.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.calendar.CalendarWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.CalendarsPickDialog
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.SettingsScreen
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.layouts.LayoutsPickPanel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.preview.WidgetPreview
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.tabs.StylesTabsPanel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroApplyButton
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroColorsTabs
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroConfigureCalendars
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroDays
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroPreview
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroSettings
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroStyleApplyButton
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroStyleColorsTabs
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroStyleConfigureCalendars
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroStyleDays
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroStylePreview
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroStyleSettings
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroTargets
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.ExtendedFAB
import com.canopas.lib.showcase.IntroShowCaseScaffold
import com.canopas.lib.showcase.IntroShowCaseScope
import com.google.accompanist.permissions.*
import kotlin.math.roundToInt

@Composable
fun CalendarConfigureScreen(viewModel: ConfigureViewModel) {
    val widget = viewModel.widgetModel.collectAsState().value
    val state = viewModel.uiState.collectAsState().value
    val title = when (state.loadState) {
        is LoadState.Widget.Success -> stringResource(R.string.toolbar_title_widget_number, widget.id)
        LoadState.Widget.Empty -> stringResource(R.string.calendar_widget_title)
        LoadState.Widget.New -> stringResource(R.string.toolbar_title_new_widget)
        else -> stringResource(R.string.toolbar_title_empty)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isShowSettings = viewModel.showSettingsSheet.collectAsState().value

    val widgetIds = viewModel.widgetIdsList.collectAsState()
    IntroShowCaseScaffold(
        showIntroShowCase = !state.isIntroPassed,
        onShowCaseCompleted = { viewModel.onAction(ConfigureViewModel.Action.OnIntroPassed) }
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(title = { Text(title) }, actions = {
                    widgetIds.value.takeIf { it.size > 1 }?.let { list ->
                        WidgetsDropDown(list) { viewModel.onAction(ConfigureViewModel.Action.OnWidgetPick(it)) }
                    }
                    IconButton(onClick = { viewModel.onAction(ConfigureViewModel.Action.OnIntroductionRequested) }) {
                        Icon(painter = painterResource(R.drawable.ic_help), contentDescription = "Introduction")
                    }
                })
            },
        ) { innerPaddings ->
            Box(Modifier.padding(top = innerPaddings.calculateTopPadding())) {
                ConfigureScreenWrap(viewModel)

                if (isShowSettings) {
                    ModalBottomSheet(sheetState = sheetState, onDismissRequest = { viewModel.onAction(ConfigureViewModel.Action.OnSettingsClick) }) {
                        SettingsScreen(viewModel.widgetModel.collectAsState().value, viewModel::onAction)
                    }
                }
            }
        }
    }

    if (state.isShowExitConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.onAction(ConfigureViewModel.Action.OnExitCanceled) },
            title = { Text(stringResource(R.string.exit_confirmation_title)) },
            text = { Text(stringResource(R.string.exit_confirmation_text)) },
            confirmButton = {
                TextButton(onClick = { viewModel.onAction(ConfigureViewModel.Action.OnExitConfirmed) }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onAction(ConfigureViewModel.Action.OnExitCanceled) }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun IntroShowCaseScope.ConfigureScreenWrap(viewModel: ConfigureViewModel) { // added to calm compiler
    val widget = viewModel.widgetModel.collectAsState(DummyWidget).value
    val state = viewModel.uiState.collectAsState().value
    when (val result = state.loadState) {
        LoadState.Widget.New, LoadState.Widget.Success, LoadState.Widget.Empty -> ConfigureScreen(
            widget,
            viewModel.calendarsResponse.collectAsState().value,
            onAction = viewModel::onAction,
            state.isIntroPassed,
        )
        is LoadState.Error -> ErrorScreen(result.msg ?: "Default error")
        LoadState.Progress, LoadState.Idle -> LoadingScreen()
        else -> Unit
    }
}

@Composable
private fun IntroShowCaseScope.ConfigureScreen(
    widget: CalendarWidgetModel,
    allCalendars: LoadState, // todo don't like it
    onAction: (ConfigureViewModel.Action) -> Unit,
    isIntroPassed: Boolean,
) {
    val dimens = dimens() // constraints do not take composable functions
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topSettings, layouts, preview, previewIntro, btnSave, btnSettings, bottomSettingsIntro, bottomSettings) = createRefs()
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(dimens().spacingL)
                    .constrainAs(topSettings) { top.linkTo(parent.top) }
            ) {
                //val calendarsList = remember(widget) { mutableStateOf(widget.calendars) }
                CalendarsForm(
                    widget.calendars,
                    allCalendars,
                    onChangeBtnClick = { onAction(ConfigureViewModel.Action.OnCalendarsRequested) },
                    onCalendarsSelected = { list ->
                        onAction(ConfigureViewModel.Action.OnCalendarsPicked(list))
                        // todo logic to VM (widget.copy(calendars = list, calendarIds = list.map { it.id }))
                    },
                )
                Spacer(modifier = Modifier.height(dimens().spacingL))

                DaysNumberForm(widget.days, onAction)
            }
            LayoutsPickPanel(
                modifier = Modifier.constrainAs(layouts) {
                    top.linkTo(topSettings.bottom)
                },
                widget = widget,
                onLayoutPick = { onAction(ConfigureViewModel.Action.OnLayoutPick(it)) },
            )

            WidgetPreview(
                modifier = Modifier
                    .constrainAs(preview) {
                        top.linkTo(layouts.bottom, dimens.spacingL)
                        bottom.linkTo(btnSettings.top, dimens.spacingM)
                        start.linkTo(parent.start, dimens.spacingXXL)
                        end.linkTo(parent.end, dimens.spacingXXL)
                        width = Dimension.fillToConstraints
                    },
                widget = widget,
            )

            Box(
                modifier = Modifier
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
                onClick = { onAction(ConfigureViewModel.Action.OnSaveChanges) },
                enabled = isSaveEnabled,
            ) {
                Text(text = stringResource(R.string.btn_save_widget_settings))
            }

            FloatingActionButton(
                modifier = Modifier
                    .constrainAs(btnSettings) {
                        bottom.linkTo(bottomSettings.top, dimens.spacingL)
                        start.linkTo(preview.start)
                    }
                    .introShowCaseTarget(IntroTargets.SETTINGS.ordinal, IntroStyleSettings) { IntroSettings() },
                onClick = { onAction(ConfigureViewModel.Action.OnSettingsClick) },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(painterResource(R.drawable.ic_settings), contentDescription = null)
            }

            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(bottom = dimens().spacingL)
                    .fillMaxWidth()
                    .constrainAs(bottomSettings) {
                        bottom.linkTo(parent.bottom)
                    }) {
                StylesTabsPanel(widget = widget, onAction = { onAction(it.toCalendarAction()) })
            }
            Box(
                modifier = Modifier
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
    allCalendars: LoadState,
    onChangeBtnClick: () -> Unit,
    onCalendarsSelected: (List<CalendarModel>) -> Unit,
) {
    val dimens = dimens()
    val isDialogCanShow = remember { mutableStateOf(false) }

    val isPreview = LocalInspectionMode.current
    val permissionCalendar = if (isPreview) DummyPermissionState else rememberPermissionState(
        Manifest.permission.READ_CALENDAR
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
            color = MaterialTheme.colorScheme.primary,
        )
        LazyRow(
            modifier = Modifier.constrainAs(rowList) {
                width = Dimension.matchParent
                top.linkTo(caption.bottom, dimens.spacingM)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pickedCalendars.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.width(dimens().spacingM))
                    Text(text = stringResource(R.string.calendars_picked_empty), fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(dimens().spacingL))
                }
            }
            item(key = "configure") {
                val context = LocalContext.current
                val rationaleText = stringResource(R.string.calendar_permissions_rationale)
                FilterChip(
                    modifier = Modifier.introShowCaseTarget(IntroTargets.CONFIGURE_CALENDARS.ordinal, IntroStyleConfigureCalendars) {
                        IntroConfigureCalendars()
                    },
                    onClick = { // todo move to a function
                        if (permissionCalendar.status != PermissionStatus.Granted) {
                            if (permissionCalendar.status.shouldShowRationale) {
                                Toast.makeText(context, rationaleText, Toast.LENGTH_LONG).show()
                            }
                            if (!isPreview) permissionCalendar.launchPermissionRequest()
                        } else {
                            onChangeBtnClick()
                            isDialogCanShow.value = true
                        }
                    },
                    shape = RoundedCornerShape(percent = 50),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        labelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = true,
                    selected = false,
                    label = {
                        Spacer(modifier = Modifier.width(dimens().spacingS))
                        Icon(
                            modifier = Modifier.size(dimens().spacingL),
                            painter = painterResource(R.drawable.ic_edit_calendar_24),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(dimens().spacingS))

                    }
                )
                Spacer(modifier = Modifier.width(dimens().spacingM))
            }
            if (pickedCalendars.isNotEmpty()) {
                items(items = pickedCalendars, key = { it.id }) {
                    FilterChip(
                        selected = false,
                        enabled = true,
                        // todo to separate fun
                        onClick = { },
                        shape = RoundedCornerShape(percent = 50),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = it.color?.let { color -> Color(color) } ?: MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        label = {

                            Text(text = it.displayName.calendarName, fontSize = 14.sp)
                        }
                    )
                    Spacer(modifier = Modifier.width(dimens().spacingS))
                }
            }
        }
    }

    if (allCalendars is LoadState.Calendars.Success && isDialogCanShow.value) {
        CalendarsPickDialog(
            pickedCalendars = pickedCalendars,
            allCalendars = allCalendars.list,
            onDismissRequest = { isDialogCanShow.value = false },
            onSelectionSave = { list ->
                isDialogCanShow.value = false; onCalendarsSelected(allCalendars.list.filter { it in list })
            }, // filter here to preserve sorting order
        )
    }
}

@Composable
private fun IntroShowCaseScope.DaysNumberForm(daySelected: Int, onAction: (ConfigureViewModel.Action) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = stringResource(R.string.title_days_to_show).uppercase(), color = MaterialTheme.colorScheme.primary)

        val daysPick = remember(daySelected) { mutableFloatStateOf(daySelected.toFloat()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.width(dimens().spacingXXL),
                text = daysPick.value.roundToInt().toString(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.End,
            )
            Spacer(modifier = Modifier.width(dimens().spacingM))
            Box(contentAlignment = Alignment.CenterStart) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        //.padding(start = 4.dp)
                        .introShowCaseTarget(IntroTargets.DAYS_NUMBER.ordinal, IntroStyleDays) { IntroDays() }) {}
                Slider(
                    modifier = Modifier.systemGestureExclusion(),
                    value = daysPick.value,
                    valueRange = DAYS_RANGE_MIN.toFloat()..DAYS_RANGE_MAX.toFloat(),
                    steps = DAYS_RANGE_STEPS,
                    onValueChange = { daysPick.value = it },
                    onValueChangeFinished = { onAction(ConfigureViewModel.Action.OnDaysPick(daysPick.value.roundToInt())) },
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
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(strokeWidth = dimens().spacingS)
    }
}

private val String.calendarName: String
    get() = this.substringBefore('@')

@Composable
private fun ErrorScreen(msg: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimens().spacingL)
    ) {
        Text(text = msg, color = MaterialTheme.colorScheme.error, fontSize = 24.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview_ConfigureNew() {
    PlainTheme {
        IntroShowCaseScaffold(showIntroShowCase = false, onShowCaseCompleted = { /*TODO*/ }) {
            ConfigureScreen(DummyWidget.copy(id = 1L, days = 25), LoadState.Idle, {}, true)
        }
    }
}

private object DummyPermissionState : PermissionState {
    override val permission: String = ""
    override val status: PermissionStatus = PermissionStatus.Granted

    override fun launchPermissionRequest() {

    }

}
