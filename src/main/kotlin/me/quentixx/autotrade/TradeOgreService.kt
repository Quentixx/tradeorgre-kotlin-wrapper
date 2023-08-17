package me.quentixx.autotrade

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import me.quentixx.autotrade.data.MarketInfoResponse
import me.quentixx.autotrade.data.OrderBookResponse
import me.quentixx.autotrade.data.TickerResponse
import me.quentixx.autotrade.data.TradeHistoryResponse

/**
 * Represents public service for interacting with the TradeOgre API.
 *
 * @param apiUrl The target API url.
 */
class TradeOgreService(
    val apiUrl: String = "https://tradeogre.com/api/v1/",
) {

    companion object {
        /**
         * A json instance to decode responses and convert them into data objects.
         */
        val json = Json {
            ignoreUnknownKeys = true
        }
    }

    // Initialize the HTTP client with JSON support
    val client = HttpClient(CIO) {

    }

    /**
     * Access the private TradeOgre API service for authenticated operations.
     *
     * @param apiKey The API key for authentication.
     * @param apiSecret The API secret for authentication.
     * @return An instance of [TradeOgrePrivateService].
     */
    fun privateApiService(
        apiKey: String,
        apiSecret: String
    ) : TradeOgrePrivateService {
        return TradeOgrePrivateService(apiKey, apiSecret, apiUrl, client)
    }

    /**
     * Retrieve a listing of all markets and basic information including current price, volume, high, low, bid and ask.
     *
     * @return [List] of market mapping.
     */
    suspend fun listMarkets(): List<Map<String, MarketInfoResponse>> {
        return json.decodeFromString<List<Map<String, MarketInfoResponse>>>(
            client.get("${apiUrl}markets").bodyAsText()
        )
    }

    /**
     * Retrieve the current order book for {market} such as XMR-BTC.
     *
     * @param market The market (like "XMR-BTC") to retrieve order book.
     * @return [OrderBookResponse] for the provided market.
     */
    suspend fun getOrderBook(market: String): OrderBookResponse {
        return json.decodeFromString<OrderBookResponse>(
            client.get("${apiUrl}orders/$market").bodyAsText()
        )
    }

    /**
     * Retrieve the ticker for {market}, volume, high, and low are in the last 24 hours, initialprice is the price from 24 hours ago.
     *
     * @param market The market (like "XMR-BTC") to retrieve the ticker.
     * @return [TickerResponse] for the provided market.
     */
    suspend fun getTicker(market: String): TickerResponse {
        return json.decodeFromString<TickerResponse>(
            client.get("${apiUrl}ticker/$market").bodyAsText()
        )
    }

    /**
     * Retrieve the history of the last trades on {market} limited to 100 of the most recent trades.
     * The date is a Unix UTC timestamp.
     *
     * @param market The market (like "XMR-BTC") to retrieve trade history.
     * @return List of [TradeHistoryResponse].
     */
    suspend fun getTradeHistory(market: String): List<TradeHistoryResponse> {
        return json.decodeFromString(
            client.get("${apiUrl}history/$market").bodyAsText()
        )
    }
}
