package com.autospare.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCartCheckout
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
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.autospare.data.Product
import com.autospare.ui.theme.Green40
import com.autospare.viewmodel.ProductViewModel

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterialApi
fun ProductScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val userId: String? by viewModel.userId.collectAsStateWithLifecycle()

    val productsState by viewModel.products.collectAsState()

    LaunchedEffect(key1 = null, block = {
        viewModel.getProducts()
        userId?.let { userId ->
            Log.i("Tag", "username: $userId")
        } ?: run {
            Log.i("Tag", "No saved data")
            // Handle the case where userIdState is null ("No saved data")
        }
    })

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Product List")
                },
                actions = {
//                    if (userId == "tkharishankar@gmail.com") {
                    IconButton(onClick = {
                        viewModel.openAddProduct(openAndPopUp)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Localized description"
                        )
                    }
//                    }
                },
            )
        },
        floatingActionButton = {
            if (productsState.find { it.isSelected } != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.createOrder()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.ShoppingCartCheckout,
                            "Extended floating action button."
                        )
                    },
                    text = { Text(text = "Place the order", fontWeight = FontWeight.Bold) },
                )
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
                items(productsState.size) { i ->
                    ProviderItem(productsState[i]) {
                        viewModel.toggleProductSelection(productsState[i])
                    }
                }
            }
        }
    }

}

@Composable
fun ProviderItem(product: Product, onProductSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onProductSelect()
            },
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
                        text = product.price + " INR",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            if (product.isSelected) {
                                Green40
                            } else {
                                Color.LightGray
                            }, shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "selected",
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center)
                    )
                }

            }
        }
    }
}

