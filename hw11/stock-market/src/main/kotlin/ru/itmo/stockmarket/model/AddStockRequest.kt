package ru.itmo.stockmarket.model

data class AddStockRequest(
    val companyName: String,
    val count: Int,
)