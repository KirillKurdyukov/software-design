package ru.itmo.userclient.model

data class BuyStockRequest(
    val id: String,
    val companyName: String,
    val count: Int,
)
