package ru.itmo.stockmarket.model

data class SetCostRequest(
    val companyName: String,
    val cost: Int,
)
