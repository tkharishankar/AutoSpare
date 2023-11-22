package com.autospare.ui.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.autospare.viewmodel.TaskViewModel

/**
 * Author: Hari K
 * Date: 21/11/2023.
 */
@Composable
@ExperimentalMaterialApi
fun TaskScreen(
    openScreen: (String) -> Unit,
    viewModel: TaskViewModel = hiltViewModel(),
) {

}