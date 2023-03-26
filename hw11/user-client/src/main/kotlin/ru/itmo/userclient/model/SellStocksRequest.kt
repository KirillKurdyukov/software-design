package ru.itmo.userclient.model

data class SellStocksRequest(
    val id: String,
    val companyName: String,
    val count: Int,
)