import com.google.gson.Gson
import java.time.LocalDateTime
import java.math.BigDecimal

fun main() {
    Gson().fromJson(orders, Array<OrdersAnalyzer.Order>::class.java).also {
        OrdersAnalyzer().totalDailySales(it.asList()).let { sales ->
            sales.toSortedMap().map(::println)
        }
    }
}

main()

class OrdersAnalyzer {

    data class Order(val orderId: Int, val creationDate: String, val orderLines: List<OrderLine>)

    data class OrderLine(val productId: Int, val name: String, val quantity: Int, val unitPrice: BigDecimal)

    fun totalDailySales(orders: List<Order>) =
        orders.groupBy { it.creationDateTime.dayOfWeek }.mapValues {
            it.value.sumBy { it.orderLines.sumBy { it.quantity } }
        }

    val Order.creationDateTime get() = LocalDateTime.parse(creationDate)
}

val orders get() = """
    [
        {
            "orderId": 554,
            "creationDate": "2017-03-25T10:35:20",
            "orderLines": [
                { "productId": 9872, "name": "Pencil", "quantity": 3, "unitPrice": 3.00}
            ]
        },
        {
            "orderId": 555,
            "creationDate": "2017-03-25T11:24:20",
            "orderLines": [
                {"productId": 9872, "name": "Pencil", "quantity": 2, "unitPrice": 3.00},
                {"productId": 1746, "name": "Eraser", "quantity": 1, "unitPrice": 1.00}
            ]
        },
        {
            "orderId": 453,
            "creationDate": "2017-03-27T14:53:12",
            "orderLines": [
                {"productId": 5723, "name": "Pen", "quantity": 4, "unitPrice": 4.22},
                {"productId": 9872, "name": "Pencil", "quantity": 3, "unitPrice": 3.12},
                {"productId": 3433, "name": "Erasers Set", "quantity": 1, "unitPrice": 6.15}
            ]
        },
        {
            "orderId": 431,
            "creationDate": "2017-03-20T12:15:02",
            "orderLines": [
                {"productId": 5723, "name": "Pen", "quantity": 7, "unitPrice": 4.22},
                {"productId": 3433, "name": "Erasers Set", "quantity": 2, "unitPrice": 6.15}
            ]
        },
        {
            "orderId": 690,
            "creationDate": "2017-03-26T11:14:00",
            "orderLines": [
                {"productId": 9872, "name": "Pencil", "quantity": 4, "unitPrice": 3.12},
                {"productId": 4098, "name": "Marker", "quantity": 5, "unitPrice": 4.50}
            ]
        }
    ]
""".trimIndent()
