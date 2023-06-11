package com.arifwidayana.stockmarket.data.mapper

import com.arifwidayana.stockmarket.data.network.model.IntraDayInfo
import com.arifwidayana.stockmarket.data.network.model.dto.IntraDayInfoDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntraDayInfoDto.toIntraDayInfo(): IntraDayInfo {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timestamp, formatter)

    return IntraDayInfo(
        date = localDateTime,
        close = close
    )
}