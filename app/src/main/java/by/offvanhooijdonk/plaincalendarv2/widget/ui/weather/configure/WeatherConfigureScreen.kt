@file:OptIn(ExperimentalMaterial3Api::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.LocationModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.tabs.StylesTabsPanel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.ExtendedFAB
import by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure.WeatherConfigureViewModel.Intent
import by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure.WeatherConfigureViewModel.UiState
import by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.preview.WeatherWidgetPreview

@Composable
fun WeatherConfigureScreen(state: UiState, onIntent: (Intent) -> Unit) {
    Scaffold(
        containerColor = Color.Transparent,
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.weather_widget_title)) }) }
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            Surface {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimens().spacingL),
                ) {
                    Spacer(Modifier.height(dimens().spacingL))
                    CountriesDropdown(
                        modifier = Modifier.fillMaxWidth(),
                        countries = state.countries,
                        expanded = state.isCountriesExpanded,
                        onDismiss = { onIntent(Intent.CountriesListDismiss) },
                        selectedCountry = state.selectedCountry,
                        onSelect = { onIntent(Intent.CountrySelect(it)) },
                    )

                    CitySearchDropDown(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = state.isSuggestionsExpanded,
                        input = state.cityInput,
                        onInput = { onIntent(Intent.CityInput(it)) },
                        suggestions = state.citySuggestions,
                        onSelect = { onIntent(Intent.CitySelect(it)) },
                        onDismiss = { onIntent(Intent.DismissSuggestions) },
                    )

                    Spacer(Modifier.height(dimens().spacingS))

                    AnimatedVisibility(state.selectedLocation != null) {
                        state.selectedLocation?.let { city ->
                            Column {
                                Spacer(Modifier.height(dimens().spacingS))

                                Text(text = city.title, style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.width(dimens().spacingSM))

                                Text(
                                    text = "${city.state?.let { "$it, " }}${city.countryCode}",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(Modifier.height(dimens().spacingM))
                            }
                        }
                    }

                    if (state.isSearchProgress) {
                        LinearProgressIndicator()
                    } else {
                        Box(Modifier.size(dimens().spacingS))
                    }
                }
            }


            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    Modifier
                        .padding(horizontal = dimens().spacingL)
                        .align(Alignment.Center)
                ) {
                    WeatherWidgetPreview(
                        modifier = Modifier
                            .width(dimens().widgetPreviewWidth)
                            .height(dimens().widgetPreviewHeight),
                        widget = state.widget,
                        weather = state.weather,
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(dimens().spacingL)
                        .align(Alignment.BottomEnd)
                ) {
                    ExtendedFAB(onClick = { onIntent(Intent.ApplyClick) }, enabled = state.isApplyEnabled) {
                        Text(text = stringResource(R.string.btn_save_widget_settings))
                    }
                }
            }

            Surface {
                StylesTabsPanel(
                    widget = state.widget,
                    onAction = { onIntent(it.toWeatherIntent()) }
                )
            }
        }
    }
}

@Composable
private fun CountriesDropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    countries: List<CountryModel>,
    onDismiss: () -> Unit,
    selectedCountry: CountryModel,
    onSelect: (CountryModel) -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            Log.d("===", "onExpandedChange $it")
        },
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable),
            value = "${selectedCountry.flag} ${selectedCountry.title}",
            onValueChange = {},
            readOnly = true,
            label = { Text("Country") },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
        ) {
            countries.forEach { country ->
                DropdownMenuItem(
                    text = { Text(text = "${country.flag} ${country.title}") },
                    onClick = { onSelect(country) },
                )
            }
        }
    }
}

@Composable
private fun CitySearchDropDown(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    input: String,
    onInput: (String) -> Unit,
    suggestions: List<LocationModel>,
    onSelect: (LocationModel) -> Unit,
    onDismiss: () -> Unit,
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {}
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryEditable),
            value = input,
            onValueChange = onInput,
            label = { Text("City") },
            placeholder = { Text("Start typing the city") },
            maxLines = 1,
            singleLine = true,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
        ) {
            suggestions.forEach { location ->
                DropdownMenuItem(
                    text = { LocationTextItem(location = location) },
                    onClick = { onSelect(location) },
                )
            }
        }
    }
}

@Composable
fun LocationTextItem(location: LocationModel) {
    Column {
        Text(text = location.title)

        Text(text = "${location.state?.let { "$it, " }}${location.countryCode}", style = MaterialTheme.typography.labelMedium)
    }
}

@Preview(device = Devices.PIXEL_9, showSystemUi = true)
@Composable
private fun Preview_WeatherConfigureScreen() {
    PlainTheme {
        WeatherConfigureScreen(
            state = UiState(
                isApplyEnabled = true,
                cityInput = "Go",
                citySuggestions = listOf(
                    LocationModel(title = "Gomel", "BY", "Homiel Region", 0.0, 0.0),
                    LocationModel(title = "Gondor", "GO", "MiddleEarth", 0.0, 0.0),
                    LocationModel(title = "Gorodetz", "Unk", "Everywhere", 0.0, 0.0),
                ),
                isSearchProgress = true,
                isSuggestionsExpanded = true,
                selectedLocation = LocationModel(
                    title = "Shchuchyn",
                    countryCode = "BY",
                    state = "Grodno Region",
                    lat = 0.0,
                    lon = 0.0,
                ),
            ),
            onIntent = {}
        )
    }
}
