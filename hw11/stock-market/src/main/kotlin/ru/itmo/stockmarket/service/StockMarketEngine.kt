package ru.itmo.stockmarket.service

import org.springframework.stereotype.Service
import ru.itmo.stockmarket.exceptions.ConflictException
import ru.itmo.stockmarket.exceptions.NotFoundException
import ru.itmo.stockmarket.model.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Service
class StockMarketEngine {

    private val stocksMarket = ConcurrentHashMap<String, Stocks>()

    fun addCompany(companyName: String) {
        stocksMarket.putIfAbsent(
            companyName,
            Stocks(
                AtomicInteger(0),
                AtomicInteger(0),
            ),
        )
    }

    fun addStocks(addStockRequest: AddStockRequest) {
        val stock = getStocks(addStockRequest.companyName)
        stock.count.addAndGet(addStockRequest.count)
    }

    fun getStocks(companyName: String): Stocks = stocksMarket[companyName]
        ?: throw NotFoundException()

    fun setCost(setCostRequest: SetCostRequest) {
        val stock = getStocks(setCostRequest.companyName)
        stock.cost.set(setCostRequest.cost)
    }

    fun buyStocks(buyStockRequest: BuyStockRequest): StocksSnapshot {
        while (true) {
            val stock = getStocks(buyStockRequest.companyName)
            val stockCount = stock.count.get()
            val stockCost = stock.cost.get()

            if (stockCount >= buyStockRequest.count &&
                buyStockRequest.count * stockCost <= buyStockRequest.userAmountMoney
            ) {
                if (stock.cost.compareAndSet(stockCost, stockCost) &&
                    stock.count.compareAndSet(stockCount, stockCount - buyStockRequest.count)
                ) {
                    return StocksSnapshot(
                        buyStockRequest.count,
                        stockCost,
                    )
                }
            } else {
                throw ConflictException()
            }
        }
    }

    fun sellStocks(sellStocksRequest: SellStocksRequest): Int {
        val stock = getStocks(sellStocksRequest.companyName)

        val stockCost = stock.cost.get()

        stock.count.addAndGet(sellStocksRequest.count)
        return stockCost * sellStocksRequest.count
    }
}
