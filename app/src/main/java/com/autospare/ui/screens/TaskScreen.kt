package com.autospare.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.autospare.R
import com.autospare.viewmodel.TaskViewModel
import com.google.android.gms.analytics.ecommerce.Product

/**
 * Author: Hari K
 * Date: 21/11/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterialApi
fun TaskScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
) {
    val userId: String? by viewModel.userId.collectAsStateWithLifecycle()

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
                    Text("Task List")
                },
                actions = {
                    if (userId == "tkharishankar@gmail.com") {
                        IconButton(onClick = {
                            viewModel.openAddTask(openAndPopUp)
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }

}