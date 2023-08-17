package me.quentixx.autotrade.serializer

import me.quentixx.autotrade.enums.OrderType

/**
 * Serializer for [OrderType] enumeration.
 */
object OrderTypeSerializer : EnumSerializer<OrderType>("order", OrderType.entries)
