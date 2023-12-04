package com.autospare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autospare.ORDER_DETAIL_SCREEN
import com.autospare.ORDER_ID
import com.autospare.PRODUCT_SCREEN
import com.autospare.data.Order
import com.autospare.viewmodel.OrderViewModel
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterialApi
fun OrderScreen(
    openScreen: (String) -> Unit,
    viewModel: OrderViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsState()

    val orders by viewModel.orders.collectAsState()

    LaunchedEffect(
        key1 = null, block = {
            viewModel.getUser()
            viewModel.getOrders()
        })

    println("order ${orders.map { it }}")

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Orders")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        openScreen(PRODUCT_SCREEN)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(orders.size) { i ->
                    OrderCard(orders[i]) {
                        openScreen("$ORDER_DETAIL_SCREEN?$ORDER_ID=${orders[i].orderId}")
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, onOrderDetailClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onOrderDetailClick()
            },
        elevation = CardDefaults.outlinedCardElevation(),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Order ID: ${order.orderId}", fontWeight = FontWeight.Bold)
                        Text(text = "Placed By: ${order.username}", fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "No.of Items: " + order.products.count(),
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Amount: 100 $",
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Created Date: " + date(order.createdTimestamp),
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Box(
                            modifier = Modifier
                                .background(Color.Black, shape = RoundedCornerShape(25.dp))
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = order.status.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.width(25.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowRight,
                        tint = Color.Black,
                        contentDescription = "Localized description"
                    )
                }
            }
        }
    }
}

fun date(timestamp: Long): String {
    val date = Instant.fromEpochSeconds(timestamp)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${date.dayOfMonth}-${date.monthNumber}-${date.year}"
}




