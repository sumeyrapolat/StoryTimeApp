package com.example.englishnotebook.ui.components

import android.graphics.Paint.Align
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.englishnotebook.ui.theme.DarkGreen
import com.example.englishnotebook.ui.theme.DarkOrange
import com.example.englishnotebook.ui.theme.LightYellow
import com.example.englishnotebook.ui.theme.Orange
import com.example.englishnotebook.ui.theme.Pink
import com.example.englishnotebook.viewmodel.AuthViewModel
import com.example.englishnotebook.viewmodel.PasswordResetState
import com.example.englishnotebook.viewmodel.UserDataState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@Composable
fun DrawerContent(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel() // ViewModel'i Hilt ile enjekte ediyoruz
) {
    val context = LocalContext.current // Toast mesajı için gerekli context

    val passwordResetState by viewModel.passwordResetState.collectAsState()
    val userDataState by viewModel.userDataState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Tüm içerikleri tam ortada hizala
    ) {
        when (val state = userDataState) {
            is UserDataState.Loading -> {
                // Kullanıcı verileri yüklenirken gösterilecek bir yükleme simgesi
                CircularProgressIndicator()
            }
            is UserDataState.Success -> {
                val userFullName = "${state.firstName} ${state.lastName}"
                val userEmail = state.email

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally // İçerikleri yatayda ortala
                ) {
                    // Profil fotoğrafı
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background), // Burada profil resminizi ekleyin
                        contentDescription = "User Profile Picture",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(100.dp)) // Yuvarlak profil fotoğrafı için
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    // Kullanıcı Adı ve Soyadı
                    Text(
                        text = userFullName,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.fjallaoneregular, FontWeight.Normal)),
                        color = DarkOrange,
                    )

                    Spacer(modifier = Modifier.padding(5.dp))

                    // Kullanıcı E-postası
                    Text(
                        text = userEmail,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.fjallaoneregular, FontWeight.Normal)),
                        color = DarkOrange,
                    )

                    Spacer(modifier = Modifier.height(64.dp)) // Buton ve metinler arasında boşluk

                    Button(
                        onClick = {
                            // Firebase log out işlemi
                            viewModel.signOut()
                            navController.navigate("signUp")
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

                    Spacer(modifier = Modifier.height(5.dp)) // Buton ve metinler arasında boşluk

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
                            text = "Restart Password",
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.fjallaoneregular, FontWeight.Normal)),
                            color = DarkOrange,
                        )
                    }

                    // Toast mesajı için LaunchedEffect kullanarak `passwordResetState` durumunu kontrol edin
                    LaunchedEffect(passwordResetState) {
                        when (val resetState = passwordResetState) {
                            is PasswordResetState.Success -> {
                                Toast.makeText(context, resetState.message, Toast.LENGTH_LONG).show()
                                viewModel.resetPasswordResetState() // Durumu Idle'a geri döndür
                            }
                            is PasswordResetState.Error -> {
                                Toast.makeText(context, resetState.message, Toast.LENGTH_LONG).show()
                                viewModel.resetPasswordResetState() // Durumu Idle'a geri döndür
                            }
                            PasswordResetState.Loading -> {
                                // Yükleme sırasında bir şey yapmak isterseniz buraya ekleyebilirsiniz
                            }
                            PasswordResetState.Idle -> {
                                // Idle durumu için herhangi bir işlem yapılmasına gerek yok
                            }
                        }
                    }
                }
            }
            is UserDataState.Error -> {
                // Kullanıcı verilerini çekerken hata oluşursa gösterilecek mesaj
                Text(text = state.message, color = Color.Red)
            }
        }
    }
}
