package ru.itmo

interface EventsStatistics {
    /**
     * инкрементит число событий name
     */
    fun incEvent(eventName: String)

    /**
     * выдает rpm (request per minute)
     * события name за последний час
     */
    fun getEventStatisticByName(eventName: String): Double?

    /**
     * выдает rpm всех произошедших событий за последний час
     */
    fun getAllEventStatistic(): Map<String, Double>

    /**
     * выводит в консоль rpm всех произошедших событий за последний час
     */
    fun printStatistic()
}

