# tradeorgre-kotlin-wrapper
Kotlin wrapper of TradeOgre Rest API.

### Public API
___
These API calls do not require any authentication. Instantiates the service as follows:
````kt
val service = TradeOgreService(/*Optional URL, already set by default*/)
````

___
#### List Markets
Retrieve a listing of all markets and basic information including current price, volume, high, low, bid and ask.
````kt
val markets: List<Map<String, MarketInfoResponse>> = service.listMarkets()
println(markets)
````
```kt
// Output
[
    {AAVE-USDT=MarketInfoResponse(initialprice=0.00000000, price=0.00000000, high=0.00000000, low=0.00000000, volume=0.00000000, bid=5.00000000, ask=0.00000000)}, 
    {ACM-BTC=MarketInfoResponse(initialprice=0.00000005, price=0.00000005, high=0.00000005, low=0.00000005, volume=0.00073916, bid=0.00000005, ask=0.00000006)}, 
    {ADA-BTC=MarketInfoResponse(initialprice=0.00000969, price=0.00000998, high=0.00000998, low=0.00000930, volume=0.04807046, bid=0.00001000, ask=0.00001008)}, 
    // ...
]
```
___
#### Get Order Book
Retrieve the current order book for {market} such as XMR-BTC.
````kt
val orderBook: OrderBookResponse = service.getOrderBook("XMR-BTC")
println(orderBook)
````
```kt
// Output
OrderBookResponse(success=true, 
    buy={
        0.00420000=25.73237526, // Buy ~25.7 XMR for (when price in BTC reach) 0,0042 BTC 
        0.00425000=0.08314867, 
        0.00429990=1.00000000, 
        0.00430000=1.33643861, 
        // ...
    }
    sell={
        0.00548373=14.98784837, // Sell ~14.9 XMR for (when price in BTC reach) 0.00548373 BTC
        0.00548374=1.00000000, 
        0.00548425=0.02192780, 
        0.00548446=0.02192744, 
        // ...
    }
)
```
___
#### Get Ticker
Retrieve the ticker for {market}, volume, high, and low are in the last 24 hours, initialprice is the price from 24 hours ago.
````kt
val ticker: TickerResponse = service.getTicker("XMR-BTC")
println(ticker)
````
```kt
// Output
TickerResponse(success=true, 
    initialprice=0.00538696, 
    price=0.00549299, 
    high=0.00549300, 
    low=0.00531637, 
    volume=25.93213774, 
    bid=0.00546170, 
    ask=0.00549273
)
```
___
#### Get Trade History
Retrieve the history of the last trades on {market} limited to 100 of the most recent trades. The date is a Unix UTC timestamp.
````kt
val tradeHistory: List<TradeHistoryResponse> = service.getTradeHistory("XMR-BTC")
println(tradeHistory)
````
```kt
// Output
[
    TradeHistoryResponse(date=1692360866, type=BUY, price=0.00549238, quantity=0.10841407), 
    TradeHistoryResponse(date=1692360927, type=BUY, price=0.00549257, quantity=0.34187021), 
    TradeHistoryResponse(date=1692361320, type=SELL, price=0.00546246, quantity=0.20515308), 
    TradeHistoryResponse(date=1692361451, type=SELL, price=0.00546600, quantity=6.07520000), 
    // ...
]
```
___
### Private API
These API calls require authentication. Instantiates this service as follows:
````kt
// If you have previously used the Public API.
val privateService = service.privateApiService(youApiKey, yourApiSecret)

// Otherwise:
val privateService = TradeOgrePrivateService(yourApiKey, yourApiSecret, HttpClient())
````
To generate an API key, go into your account settings and click on Generate New API Keys.
___
#### Submit Buy Order
Submit a buy order to the order book for a market. The success status will be false if there is an error, and error will contain the error message. Your available buy and sell balance for the market will be returned if successful. If your order is successful but not fully fulfilled, the order is placed onto the order book and you will receive a uuid for the order.
````kt
// Buy 1 XMR when his price reaches 0.0042 BTC
val submitBuy: SubmitOrderResponse = privateService.submitBuyOrder("XMR-BTC", "1", "0.0042")
println(submitBuy)
````
````kt
// Output:
SubmitOrderResponse(success=true, 
    uuid=ae19d981-7341-1d2a-54e8-e32b4f33b4f6, 
    bnewbalavail=0.00009034, // The new available BTC balance after this operation.
    snewbalavail=0.39230000 // The new available XMR balance after this operation.
)
````
___
#### Submit Sell Order
Submit a sell order to the order book for a market. The success status will be false if there is an error, and error will contain the error message. Your available buy and sell balance for the market will be returned if successful. If your order is successful but not fully fulfilled, the order is placed onto the order book and you will receive a uuid for the order.
````kt
// Sell 1 XMR when his price reaches 0.5 BTC
val submitSell: SubmitOrderResponse = privateService.submitSellOrder("XMR-BTC", "1", "0.5")
println(submitSell)
````
````kt
// Output:
SubmitOrderResponse(success=true, 
    uuid=55c36339-da4c-1b10-2f56-e52f41092b02, 
    bnewbalavail=0.00009034, // The new available BTC balance after this operation.
    snewbalavail=0.39230000 // The new available XMR balance after this operation.
)
````
___
#### Cancel Order
Cancel an order on the order book based on the order uuid. The uuid parameter can also be set to all and all of your orders will be cancelled across all markets.
````kt
// If you want to cancel one order by unique id
val submitCancel: CancelResponse = privateService.cancelOrder("55c36339-da4c-1b10-2f56-e52f41092b02")

// If you want to cancel all you active orders
val submitCancel: CancelResponse = privateService.cancelOrder("all")
````
````kt
// Output:
CancelResponse(success=true)
````
___
#### Get Orders
Retrieve the active orders under your account. The market field is optional, and leaving it out will return all orders in every market. date is a Unix UTC timestamp.
````kt
// If you want to retrieve all your active orders
val orders: List<OrderResponse> = privateService.getOrders()

// If you want to retrieve orders for a specific market
val orders: List<OrderResponse> = privateService.getOrders("XMR-BTC")
````
````kt
// Output:
[
    OrderResponse(uuid=ae19d981-7341-1d2a-54e8-e32b4f33b4f6, date=1692372889, type=BUY, price=0.00420000, quantity=1.00000000, market=XMR-BTC), 
    OrderResponse(uuid=55c36339-da4c-1b10-2f56-e52f41092b02, date=1692204956, type=SELL, price=0.5, quantity=1.00000000, market=XMR-BTC)
]
````
___
#### Get Balance
Get the balance of a specific currency for you account. The currency field is required, such as BTC. The total balance is returned and the available balance is what can be used in orders or withdrawn.
````kt
val balance: BalanceResponse = privateService.getBalance("BTC")
println(balance)
````
````kt
// Output:
BalanceResponse(success=true, 
    balance=0.00015033, // The BTC balance including that placed in orders
    available=0.00009034 // The available BTC balance to trade
)
````
#### Get Balances
Retrieve all balances for your account.
````kt
val balances: BalancesResponse = privateService.getBalances()
println(balances)
````
````kt
// Output:
BalancesResponse(success=true, 
    balances={
        BTC=0.00015033, 
        XMR=1.00000000, 
        LTC=21.00000000,
        // ...
    }
)
````
___
### // TODO
- getOrder(uuid)
- Serializer for BigDecimal 
- Serializer for Date 
