package ru.itmo.stockmarket.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.itmo.stockmarket.model.*
import ru.itmo.stockmarket.service.StockMarketEngine

@RestController
class StockMarketController(
    private val stockMarketEngine: StockMarketEngine,
) {

    @PostMapping("/add/company")
    fun addCompany(@RequestBody companyName: String): ResponseEntity<Unit> {
        stockMarketEngine.addCompany(companyName)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/add/stocks")
    fun addStocks(@RequestBody addStockRequest: AddStockRequest): ResponseEntity<Unit> {
        stockMarketEngine.addStocks(addStockRequest)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/set/cost")
    fun setCost(@RequestBody setCostRequest: SetCostRequest): ResponseEntity<Unit> {
        stockMarketEngine.setCost(setCostRequest)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/get/stocks")
    fun getStocks(@RequestBody companyName: String) = ResponseEntity.ok(
        stockMarketEngine.getStocks(companyName).apply {
            StocksSnapshot(
                count = this.count.get(),
                cost = this.cost.get(),
            )
        },
    )

    @PostMapping("/buy/stocks")
    fun buyStocks(@RequestBody buyStockRequest: BuyStockRequest) = ResponseEntity.ok(
        stockMarketEngine.buyStocks(buyStockRequest),
    )

    @PostMapping("/sell/stocks")
    fun sellStocks(@RequestBody sellStocksRequest: SellStocksRequest) = ResponseEntity.ok(
        stockMarketEngine.sellStocks(sellStocksRequest),
    )
}
