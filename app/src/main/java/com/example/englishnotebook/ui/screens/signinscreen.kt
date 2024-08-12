package com.example.englishnotebook.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.englishnotebook.ui.theme.LightBlue
import com.example.englishnotebook.ui.theme.LightPurple
import com.example.englishnotebook.ui.theme.LightYellow
import com.example.englishnotebook.ui.theme.PastelPink
import com.example.englishnotebook.ui.theme.PastelYellow
import com.example.englishnotebook.ui.theme.Pink
import com.example.englishnotebook.ui.theme.cardColor
import com.example.englishnotebook.viewmodel.SignInState
import com.example.englishnotebook.viewmodel.SignInViewModel
import com.example.englishnotebook.viewmodel.SignUpState

@Composable
fun SignInScreen(navController: NavController, viewModel: SignInViewModel = hiltViewModel()){

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val signInState = viewModel.signInState.collectAsState()

    // Klavye kontrolcüsü
    val keyboardController = LocalSoftwareKeyboardController.current

    // Ekran boyutlarını alıyoruz
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels.toFloat()
    val screenHeight = displayMetrics.heightPixels.toFloat()

    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            LightPurple,
            PastelPink,
            PastelYellow,
            PastelPink,
            LightBlue
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, screenHeight),
        tileMode = TileMode.Clamp
    )


    val radialOverlay = Brush.radialGradient(
        colors = listOf(
            Color(0xFFB8CEFF).copy(alpha = 0.7f),
            Color.Transparent
        ),
        center = Offset(screenWidth / 2, screenHeight / 8),
        radius = screenHeight / 5
    )

    val buttonBackgroundColor = Brush.linearGradient(
        colors = listOf(
            LightPurple,
            Pink,
            LightPurple
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    val cardBackgroundColor = cardColor
    val textColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(radialOverlay),
            contentAlignment = Alignment.BottomCenter
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardBackgroundColor
                )
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 60.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Sign In",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Pink,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(emailFocusRequester),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { passwordFocusRequester.requestFocus() }
                        )
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text("Password", fontSize = 16.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(passwordFocusRequester),
                        shape = RoundedCornerShape(8.dp),
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible.value) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            }
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide() // Klavyeyi kapat
                                //viewModel.signIn(email.value, password.value)
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Button(
                        onClick = {
                            viewModel.signIn(email.value, password.value)
                        },
                        shape = RoundedCornerShape(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = textColor
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(buttonBackgroundColor, shape = RoundedCornerShape(45.dp))
                    ) {
                        Text(text = "Sign In", modifier = Modifier.padding(8.dp), fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    TextButton(onClick = { navController.navigate("signup") }) {
                        Text(
                            text = "Don't have an account? Sign Up",
                            fontSize = 16.sp,
                            color = Pink
                        )
                    }
                }
            }

            when (signInState.value) {
                is SignInState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is SignInState.Success -> {
                    navController.navigate("feed")
                }
                is SignInState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (signInState.value as SignInState.Error).error,
                            color = Color.Red,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    Log.d("SignInScreen", "Unknown state: ${signInState.value}")
                }
            }
        }
    }

}
