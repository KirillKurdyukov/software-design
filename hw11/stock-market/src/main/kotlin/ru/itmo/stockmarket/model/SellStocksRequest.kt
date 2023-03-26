package ru.itmo.stockmarket.model

data class SellStocksRequest(
    val companyName: String,
    val count: Int,
)