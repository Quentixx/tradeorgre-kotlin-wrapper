package fr.quentixx.tradeogre.wrapper.serializer

import fr.quentixx.tradeogre.wrapper.enums.OrderType

/**
 * Serializer for [OrderType] enumeration.
 */
object OrderTypeSerializer : EnumSerializer<OrderType>("order", OrderType.entries)
