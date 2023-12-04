package com.autospare.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autospare.common.google.GoogleUser
import com.autospare.common.google.SignInWithGoogle
import com.autospare.common.google.getUserFromTokenId
import com.autospare.common.google.rememberSignInState
import com.autospare.viewmodel.LoginViewModel

/**
 * Author: Senthil
 * Date: 21/11/2023.
 */
@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state = rememberSignInState()
    val userData by viewModel.user.collectAsState()
    var user: GoogleUser? by remember { mutableStateOf(null) }
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(
        key1 = null, block = {
            viewModel.getUser()
        })

    SignInWithGoogle(
        state = state,
        clientId = "508397395105-lu2o6ddkvehkprcifcd9pcd21i0bgr4i.apps.googleusercontent.com",
        onTokenIdReceived = {
            user = getUserFromTokenId(tokenId = it)
            Log.d("MainActivity", user.toString())
            viewModel.onSignInClick(user, openAndPopUp)
        },
        onDialogDismissed = {
            Log.d("MainActivity", it)
        }
    )

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.weight(2f),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Blue,
                        strokeWidth = 4.dp
                    )
                } else {
                    if (!userData?.email.isNullOrEmpty()) {
                        viewModel.onSignInClick(
                            GoogleUser(
                                fullName = userData?.name,
                                email = userData?.email
                            ),
                            openAndPopUp
                        )
                    } else {
                        Button(
                            onClick = { state.open() },
                            enabled = !state.opened
                        ) {
                            Text(text = "Google Sign in")
                        }
                    }
                }
            }
        }
    }
}