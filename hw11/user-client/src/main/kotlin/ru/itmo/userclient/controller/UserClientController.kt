package ru.itmo.userclient.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.itmo.userclient.model.AddMoneyRequest
import ru.itmo.userclient.model.BuyStockRequest
import ru.itmo.userclient.model.SellStocksRequest
import ru.itmo.userclient.model.Stocks
import ru.itmo.userclient.service.UserClientEngine

@RestController
class UserClientController(
    private val userClientEngine: UserClientEngine,
) {
    @PostMapping("/register")
    fun onRegister(@RequestBody name: String): ResponseEntity<String> = ResponseEntity.ok(
        userClientEngine.registerUser(name),
    )

    @PostMapping("/add/money")
    fun onAddMoney(@RequestBody addMoneyRequest: AddMoneyRequest): ResponseEntity<Unit> {
        userClientEngine.addMoney(addMoneyRequest)

        return ResponseEntity.ok().build()
    }

    @GetMapping("/get/stocks")
    fun onGetStocks(@RequestBody id: String): ResponseEntity<List<Stocks>> = ResponseEntity.ok(
        userClientEngine.getUserStocks(id),
    )

    @GetMapping("/get/user/money")
    fun onGetUserMoney(@RequestBody id: String): ResponseEntity<Int> = ResponseEntity.ok(
        userClientEngine.getUserMoney(id),
    )

    @PostMapping("/buy/stocks")
    fun onBuyStocks(@RequestBody buyStockRequest: BuyStockRequest): ResponseEntity<Unit> {
        userClientEngine.buyStocks(buyStockRequest)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/sell/stocks")
    fun onSellStocks(@RequestBody sellStocksRequest: SellStocksRequest): ResponseEntity<Unit> {
        userClientEngine.sellStock(sellStocksRequest)

        return ResponseEntity.ok().build()
    }
}
