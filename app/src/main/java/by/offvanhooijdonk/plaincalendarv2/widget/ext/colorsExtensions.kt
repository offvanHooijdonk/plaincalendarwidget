package by.offvanhooijdonk.plaincalendarv2.widget.ext

import androidx.compose.ui.graphics.Color

fun Long.toColor(): Color = Color(this.toULong())

fun Color.toLong(): Long = this.value.toLong()
