package fr.quentixx.tradeogre.wrapper.data

import kotlinx.serialization.Serializable
import fr.quentixx.tradeogre.wrapper.enums.OrderType
import fr.quentixx.tradeogre.wrapper.serializer.OrderTypeSerializer

/**
 * Represents the response of a buy/sell order request.
 *
 * @param success true if the order was successfully submitted, false otherwise.
 * @param uuid The unique identifier of the new order.
 * @param bnewbalavail The new balance available for buying.
 * @param snewbalavail The new balance available for selling.
 */
@Serializable
data class SubmitOrderResponse(
    val success: Boolean,
    val uuid: String,
    val bnewbalavail: String,
    val snewbalavail: String
)

/**
 * Represents the response of a cancel order request.
 *
 * @param success true if the order was successfully canceled, false otherwise.
 */
@Serializable
data class CancelResponse(
    val success: Boolean
)

/**
 * Represents an order details.
 *
 * @param uuid The unique identifier of the order.
 * @param date Unix UTC timestamp representing the date of the order.
 * @param type The type of the order (buy or sell).
 * @param price The price at which the order was executed.
 * @param quantity The quantity of the asset traded in the order.
 * @param market The market in which the order was placed.
 */
@Serializable
data class OrderResponse(
    val uuid: String,
    val date: Long,
    @Serializable(with = OrderTypeSerializer::class)
    val type: OrderType,
    val price: String,
    val quantity: String,
    val market: String
)

/**
 * Represents the response of a balance request for a single asset.
 *
 * @param success true if the balance request was successful, false otherwise.
 * @param balance The total balance of the asset.
 * @param available The available balance for trading.
 */
@Serializable
data class BalanceResponse(
    val success: Boolean,
    val balance: String,
    val available: String
)

/**
 * Represents the response of a balance request for multiple assets.
 *
 * @param success true if the balance request was successful, false otherwise.
 * @param balances A map containing asset symbols as keys and their corresponding balances as values.
 */
@Serializable
data class BalancesResponse(
    val success: Boolean,
    val balances: Map<String, String>
)
