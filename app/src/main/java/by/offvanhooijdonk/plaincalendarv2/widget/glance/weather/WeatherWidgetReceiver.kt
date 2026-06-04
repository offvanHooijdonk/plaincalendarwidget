package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import androidx.glance.appwidget.GlanceAppWidgetReceiver
import org.koin.core.component.KoinComponent

class WeatherWidgetReceiver :GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget: WeatherGlanceWidget = WeatherGlanceWidget()



}