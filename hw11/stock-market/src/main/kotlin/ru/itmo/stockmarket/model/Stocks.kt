package ru.itmo.stockmarket.model

import java.util.concurrent.atomic.AtomicInteger

data class Stocks(
    var count: AtomicInteger,
    var cost: AtomicInteger,
)

data class StocksSnapshot(
    var count: Int,
    var cost: Int,
)

