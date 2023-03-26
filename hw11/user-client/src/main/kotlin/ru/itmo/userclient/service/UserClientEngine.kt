package ru.itmo.userclient.service

import org.springframework.stereotype.Service
import ru.itmo.userclient.client.Client
import ru.itmo.userclient.exceptions.NotFoundException
import ru.itmo.userclient.model.*
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Service
class UserClientEngine(
    private val client: Client,
) {

    private val userRepository = ConcurrentHashMap<String, User>()

    fun registerUser(name: String): String {
        val id = UUID.randomUUID().toString()

        userRepository[id] = User(name, AtomicInteger(0))

        return id
    }

    fun addMoney(addMoneyRequest: AddMoneyRequest) {
        val user = getUser(addMoneyRequest.id)

        user.amountMoney.addAndGet(addMoneyRequest.money)
    }

    fun getUser(id: String) = userRepository[id] ?: throw NotFoundException()

    fun getUserMoney(id: String): Int {
        val user = getUser(id)

        return user.amountMoney.get() +
            getUserStocks(id)
                .map { it.count * it.cost }
                .reduce(Int::plus)
    }

    fun getUserStocks(id: String) = getUser(id)
        .stocks
        .map {
            val stocksSnapshot = client.getStock(it.companyName)

            it.copy(cost = stocksSnapshot.cost)
        }

    fun buyStocks(buyStockRequest: BuyStockRequest) {
        val user = getUser(buyStockRequest.id)
        val userMoney = user.amountMoney.get()

        val stocksSnapshot = client.buyStock(
            buyStockRequest.companyName,
            buyStockRequest.count,
            userMoney,
        )

        user.amountMoney.addAndGet(-stocksSnapshot.cost * stocksSnapshot.count)
        user.stocks.add(Stocks(stocksSnapshot.count, buyStockRequest.companyName, stocksSnapshot.cost))
    }

    fun sellStock(sellStocksRequest: SellStocksRequest) {
        val user = getUser(sellStocksRequest.id)
        val stocks = user
            .stocks
            .find { it.companyName == sellStocksRequest.companyName } ?: throw NotFoundException()

        if (stocks.count >= sellStocksRequest.count) {
            val amount = client.sellStocks(stocks.companyName, sellStocksRequest.count)

            user.amountMoney.addAndGet(amount)
            stocks.count -= sellStocksRequest.count
            if (stocks.count == 0) {
                user.stocks.remove(stocks)
            }
        } else {
            throw RuntimeException()
        }
    }
}
