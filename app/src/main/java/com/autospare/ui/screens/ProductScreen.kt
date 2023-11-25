package com.autospare.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.autospare.data.Product
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

    val products = viewModel.products.collectAsStateWithLifecycle(emptyList())

    LaunchedEffect(key1 = null, block = {
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
                    if (userId == "tkharishankar@gmail.com") {
                        IconButton(onClick = {
                            viewModel.openAddProduct(openAndPopUp)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Localized description"
                            )
                        }
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
            ) {
                items(products.value) { product ->
                    ProviderItem(product)
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
            .padding(8.dp)
            .clickable {},
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "stringResource(R.string.description)",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(300.dp)
                    .clip(RectangleShape)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color(0xFFE9E8E9)),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    text = product.name,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Start,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Icon(
//                        Icons.Default.LocationOn,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.padding(4.dp),
//                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, bottom = 8.dp),
                        text = product.price + " INR",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}