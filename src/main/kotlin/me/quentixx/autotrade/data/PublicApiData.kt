package me.quentixx.autotrade.data

import kotlinx.serialization.Serializable
import me.quentixx.autotrade.enums.OrderType
import me.quentixx.autotrade.serializer.OrderTypeSerializer

/**
 * Represents information about a market.
 *
 * @param initialprice The initial price of the market.
 * @param price The current price of the market.
 * @param high The highest price of the market.
 * @param low The lowest price of the market.
 * @param volume The trading volume of the market.
 * @param bid The bid price in the market.
 * @param ask The ask price in the market.
 */
@Serializable
data class MarketInfoResponse(
    val initialprice: String,
    val price: String,
    val high: String,
    val low: String,
    val volume: String,
    val bid: String,
    val ask: String
)

/**
 * Represents the order book for a market.
 *
 * @param success A string indicating the success status of the request.
 * @param buy A map containing buy orders with price as keys and quantity as values.
 * @param sell A map containing sell orders with price as keys and quantity as values.
 */
@Serializable
data class OrderBookResponse(
    private val success: String,
    val buy: Map<String, String>,
    val sell: Map<String, String>
)

/**
 * Represents ticker information for a market.
 *
 * @param success true if the ticker request was successful, false otherwise.
 * @param initialprice The initial price of the market.
 * @param price The current price of the market.
 * @param high The highest price of the market.
 * @param low The lowest price of the market.
 * @param volume The trading volume of the market.
 * @param bid The bid price in the market.
 * @param ask The ask price in the market.
 */
@Serializable
data class TickerResponse(
    private val success: Boolean,
    val initialprice: String,
    val price: String,
    val high: String,
    val low: String,
    val volume: String,
    val bid: String,
    val ask: String
)

/**
 * Represents trade history information for a market.
 *
 * @param date Unix UTC timestamp representing the date of the trade.
 * @param type The [OrderType] of the trade (buy or sell).
 * @param price The price at which the trade occurred.
 * @param quantity The quantity of the asset traded in the trade.
 */
@Serializable
data class TradeHistoryResponse(
    val date: Long,
    @Serializable(with = OrderTypeSerializer::class)
    val type: OrderType,
    val price: String,
    val quantity: String
)
