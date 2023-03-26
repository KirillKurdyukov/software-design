package ru.itmo.userclient.model

import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.atomic.AtomicInteger

class User(
    val name: String,
    var amountMoney: AtomicInteger,
    val stocks: ConcurrentLinkedDeque<Stocks> = ConcurrentLinkedDeque(),
)

data class Stocks(
    @Volatile
    var count: Int,
    val companyName: String,
    val cost: Int,
)
