package ru.itmo.userclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserClientApplication

fun main(args: Array<String>) {
    runApplication<UserClientApplication>(*args)
}
