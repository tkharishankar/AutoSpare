package com.autospare.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.autospare.data.Product
import com.autospare.data.Status
import com.autospare.viewmodel.OrderViewModel

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterialApi
fun OrderDetailScreen(
    popUp: (String) -> Unit,
    viewModel: OrderViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsState()

    val order by viewModel.order.collectAsState()

    val products by viewModel.products.collectAsState()

    LaunchedEffect(
        key1 = null, block = {
            viewModel.getUser()
            viewModel.getOrders()
        })

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Order Detail ${order?.orderId ?: ""}")
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Status: " + order?.status.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Total Amount : " + products.sumOf { it.price.toInt() }
                            .toString() + " $",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (user?.isAdmin == true && order?.status == Status.CREATED) {
                    Button(
                        onClick = {
                            viewModel.updateOrderStatus(order?.orderId, Status.DELIVERED)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(text = "Mark Completed")
                    }
                }
            }

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(products.size) { i ->
                    ProviderItem(products[i])
                }
            }
        }
    }

}


@Composable
fun ProviderItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "stringResource(R.string.description)",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp)
                    .clip(RectangleShape)
            )

            Column(
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = product.name,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Start,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .background(Color.Black, shape = RoundedCornerShape(25.dp))
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = product.price + " $",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

