package ru.itmo.stockmarket.model

data class BuyStockRequest(
    val companyName: String,
    val count: Int,
    val userAmountMoney: Int,
)
