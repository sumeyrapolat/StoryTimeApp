package com.example.englishnotebook.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.englishnotebook.R
import com.example.englishnotebook.ui.theme.DarkOrange
import com.example.englishnotebook.ui.theme.Pink
import com.example.englishnotebook.viewmodel.AuthViewModel

@Composable
fun DrawerContent(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val passwordResetState by viewModel.passwordResetState.collectAsState()

    // onViewInitialized() çağırıyoruz
    LaunchedEffect(Unit) {
        viewModel.onViewInitialized()
    }

    val cachedUserData = viewModel.userDataState.collectAsState().value

    // Alınan cache datanın içeriğini log ile yazdırma
    LaunchedEffect(cachedUserData) {
        if (cachedUserData is AuthViewModel.UserDataState.Success) {
            Log.d("DrawerContent", "Cached User Data: ${cachedUserData.firstName} ${cachedUserData.lastName} - ${cachedUserData.email}")
        } else if (cachedUserData is AuthViewModel.UserDataState.Error) {
            Log.d("DrawerContent", "Error loading user data: ${cachedUserData.message}")
        } else {
            Log.d("DrawerContent", "Loading user data...")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (cachedUserData is AuthViewModel.UserDataState.Success) {
            val userFullName = "${cachedUserData.firstName} ${cachedUserData.lastName}"
            val userEmail = cachedUserData.email

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(100.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = userFullName,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.fjallaoneregular, FontWeight.Normal)),
                    color = DarkOrange,
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = userEmail,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.fjallaoneregular, FontWeight.Normal)),
                    color = DarkOrange,
                )

                Spacer(modifier = Modifier.height(64.dp))

                Button(
                    onClick = {
                        viewModel.signOut()
                        navController.navigate("signUp") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    },
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Pink
                    ),
                    modifier = Modifier.padding(vertical = 2.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
                ) {
                    Text(
                        text = "Sign Out",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.fjallaoneregular, FontWeight.Normal)),
                        color = DarkOrange,
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Button(
                    onClick = {
                        viewModel.sendPasswordResetEmail(userEmail)
                    },
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Pink
                    ),
                    modifier = Modifier.padding(vertical = 2.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
                ) {
                    Text(
                        text = "Reset Password",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.fjallaoneregular, FontWeight.Normal)),
                        color = DarkOrange,
                    )
                }

                LaunchedEffect(passwordResetState) {
                    when (passwordResetState) {
                        is AuthViewModel.PasswordResetState.Success -> {
                            Toast.makeText(context, (passwordResetState as AuthViewModel.PasswordResetState.Success).message, Toast.LENGTH_LONG).show()
                            viewModel.resetPasswordResetState()
                        }
                        is AuthViewModel.PasswordResetState.Error -> {
                            Toast.makeText(context, (passwordResetState as AuthViewModel.PasswordResetState.Error).message, Toast.LENGTH_LONG).show()
                            viewModel.resetPasswordResetState()
                        }
                        else -> Unit
                    }
                }
            }
        } else if (cachedUserData is AuthViewModel.UserDataState.Error) {
            val errorMessage = cachedUserData.message ?: "An unknown error occurred"
            Text(text = errorMessage, color = Color.Red)
        } else {
            // Bu durumda hala Loading veya başka bir durumdaysa bir şey gösterebiliriz.
            CircularProgressIndicator()
        }
    }
}
