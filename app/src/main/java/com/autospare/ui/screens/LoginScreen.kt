package com.autospare.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autospare.common.google.GoogleUser
import com.autospare.common.google.SignInWithGoogle
import com.autospare.common.google.getUserFromTokenId
import com.autospare.common.google.rememberSignInState
import com.autospare.data.User
import com.autospare.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

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
    val isLoading by viewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    val gsc = GoogleSignIn.getClient(context, gso)
    val signInIntent = remember { gsc.signInIntent }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            onSignInResult(task, viewModel, openAndPopUp)
        }

    LaunchedEffect(
        key1 = null, block = {
            viewModel.getUser()
        })

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
                    if (userData != null && !userData?.email.isNullOrEmpty() && !userData?.name.isNullOrEmpty()) {
                        viewModel.onSignInClick(
                            User(
                                name = userData!!.name,
                                email = userData!!.email
                            ),
                            openAndPopUp
                        )
                    } else {
                        Button(
                            onClick = { launcher.launch(signInIntent) },
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

private fun onSignInResult(
    result: Task<GoogleSignInAccount>,
    viewModel: LoginViewModel,
    openAndPopUp: (String, String) -> Unit,
) {
    try {
        val account = result.result
        if (result.isSuccessful && account != null) {
            Log.i("account", "account ${account.email} , ${account.displayName}")
            viewModel.onSignInClick(
                User(
                    name = account.displayName.toString(),
                    email = account.email.toString()
                ), openAndPopUp
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
