package ru.itmo.hw4.model

data class ListWorks(
    val listId: Int,
    val listName: String,
)

data class Work(
    val workId: Int,
    val workName: String,
    val isReady: Boolean,
)