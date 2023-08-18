package fr.quentixx.tradeogre.wrapper

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import fr.quentixx.tradeogre.wrapper.TradeOgreService.Companion.json
import fr.quentixx.tradeogre.wrapper.data.*

/**
 * Represents the private TradeOgre API service for authenticated operations.
 *
 * @param apiKey The API key for authentication.
 * @param apiSecret The API secret for authentication.
 * @param apiUrl The target API URL.
 * @param client The HTTP client instance to use for making API requests.
 */
class TradeOgrePrivateService(
    private val apiKey: String,
    private val apiSecret: String,
    private val apiUrl: String = "https://tradeogre.com/api/v1/",
    private val client: HttpClient = HttpClient()
) {

    /**
     * Submit a buy order to the order book for a market.
     * The success status will be false if there is an error,
     * and error will contain the error message.
     * Your available buy and sell balance for the market will be returned if successful.
     * If your order is successful but not fully fulfilled, the order is placed onto the
     * order book, and you will receive a uuid for the order.
     *
     * @param market The market (like "XMR-BTC") to submit order.
     * @param quantity The quantity of tokens you want to buy.
     * @param price The price at which the buy order will be executed.
     * @return [SubmitOrderResponse] for this request.
     */
    suspend fun submitBuyOrder(market: String, quantity: String, price: String): SubmitOrderResponse {
        return json.decodeFromString<SubmitOrderResponse>(
            client.post("${apiUrl}order/buy") {
                basicAuth(apiKey, apiSecret)
                setBody(
                    FormDataContent(Parameters.build {
                        append("market", market)
                        append("quantity", quantity)
                        append("price", price)
                    })
                )
            }.bodyAsText()
        )
    }

    /**
     * Submit a sell order to the order book for a market.
     * The success status will be false if there is an error,
     * and error will contain the error message.
     * Your available buy and sell balance for the market will be returned if successful.
     * If your order is successful but not fully fulfilled, the order is placed onto the
     * order book, and you will receive a uuid for the order.
     *
     * @param market The market (like "XMR-BTC") to submit order.
     * @param quantity The quantity of tokens you want to sell.
     * @param price The price at which the sell order will be executed.
     * @return [SubmitOrderResponse] for this request.
     */
    suspend fun submitSellOrder(market: String, quantity: String, price: String): SubmitOrderResponse {
        return json.decodeFromString<SubmitOrderResponse>(
            client.post("${apiUrl}order/sell") {
                basicAuth(apiKey, apiSecret)
                setBody(
                    FormDataContent(Parameters.build {
                        append("market", market)
                        append("quantity", quantity)
                        append("price", price)
                    })
                )
            }.bodyAsText()
        )
    }

    /**
     * Cancel an order on the order book based on the order uuid.
     * The uuid parameter can also be set to all and all of
     * your orders will be cancelled across all markets.
     *
     * @param uuid The uuid of the active order you want to cancel.
     * @return [CancelResponse] for this request.
     */
    suspend fun cancelOrder(uuid: String): CancelResponse {
        return json.decodeFromString<CancelResponse>(
            client.post("${apiUrl}order/cancel") {
                basicAuth(apiKey, apiSecret)
                setBody(
                    FormDataContent(Parameters.build {
                        append("uuid", uuid)
                    })
                )
            }.bodyAsText()
        )
    }

    /**
     * Retrieve the active orders under your account.
     * The market field is optional, and leaving it
     * out will return all orders in every market.
     *
     * @param market The market (like "XMR-BTC") to retrieve specific orders. Optional.
     * @return [List] of retrieved orders.
     */
    suspend fun getOrders(market: String? = null): List<OrderResponse> {
        return json.decodeFromString<List<OrderResponse>>(
            client.post("${apiUrl}account/orders") {
                basicAuth(apiKey, apiSecret)
                if (market != null) {
                    setBody(
                        FormDataContent(Parameters.build {
                            append("market", market)
                        })
                    )
                }
            }.bodyAsText()
        )
    }

    /**
     * Get the balance of a specific currency for you account.
     * The currency field is required, such as BTC.
     * The total balance is returned and the available balance
     * is what can be used in orders or withdrawn.
     *
     * @param currency The currency to retrieve the balance.
     * @return [BalanceResponse] for this request.
     */
    suspend fun getBalance(currency: String): BalanceResponse {

        return json.decodeFromString<BalanceResponse>(
            client.post("${apiUrl}account/balance") {
                basicAuth(apiKey, apiSecret)
                setBody(
                    FormDataContent(Parameters.build {
                        append("currency", currency)
                    })
                )
            }.bodyAsText()
        )
    }

    /**
     * Retrieve all balances for your account.
     *
     * @return [BalancesResponse] for this request.
     */
    suspend fun getBalances(): BalancesResponse {
        return json.decodeFromString<BalancesResponse>(
            client.get("${apiUrl}account/balances") {
                basicAuth(apiKey, apiSecret)
            }.bodyAsText()
        )
    }
}
