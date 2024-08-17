package com.example.englishnotebook.ui.components

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.englishnotebook.R
import com.example.englishnotebook.viewmodel.AuthViewModel
import java.io.InputStream

import coil.compose.rememberImagePainter
import com.example.englishnotebook.ui.theme.DarkerLightPurple
import com.example.englishnotebook.ui.theme.DarkerPurple

@Composable
fun DrawerContent(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val passwordResetState by viewModel.passwordResetState.collectAsState()
    val cachedUserData = viewModel.userDataState.collectAsState().value
    val profileImageUri by viewModel.userProfilePhotoUri.collectAsState()

    // Call `onViewInitialized()` here since it's a side effect
    LaunchedEffect(Unit) {
        viewModel.onViewInitialized()
    }

    // Fotoğrafı seçmek için kullanıcının galerisini açacak bir launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri = result.data?.data
            selectedImageUri?.let { uri ->
                viewModel.updateUserProfilePhoto(uri) // Profil fotoğrafını ViewModel'de güncelle
            }
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
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    // Profil fotoğrafı yüklenirken Coil kullanılır
                    SubcomposeAsyncImage(
                        model = profileImageUri,
                        contentDescription = "User Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .border(4.dp, Color.White, CircleShape),
                        loading = {
                            CircularProgressIndicator() // Yükleme sırasında gösterilecek ProgressBar
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.user),
                                contentDescription = "Default User Profile Picture",
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape)
                                    .border(4.dp, Color.White, CircleShape)
                                )
                        }
                    )
                    // Kamera ikonu
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.type = "image/*"
                            galleryLauncher.launch(intent)
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.6f), CircleShape)
                            .clip(CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Profile Picture",
                            tint = DarkerPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = userFullName,
                    fontSize = 18.sp,
                    color = DarkerLightPurple,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = userEmail,
                    fontSize = 18.sp,
                    color = DarkerLightPurple,
                    fontWeight = FontWeight.Medium

                )

                Spacer(modifier = Modifier.height(82.dp))

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
                        containerColor = Color.White.copy(0.6f),
                        contentColor = DarkerLightPurple
                    ),
                    modifier = Modifier.padding(vertical = 2.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
                ) {
                    Text(
                        text = "Sign Out",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        viewModel.sendPasswordResetEmail(userEmail)
                    },
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(0.6f),
                        contentColor = DarkerLightPurple
                    ),
                    modifier = Modifier.padding(vertical = 2.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(8.dp)
                ) {
                    Text(
                        text = "Reset Password",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
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
            CircularProgressIndicator()
        }
    }
}
