package com.example.englishnotebook

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.englishnotebook.navigation.Router
import com.example.englishnotebook.ui.components.DrawerContent
import com.example.englishnotebook.ui.theme.EnglishnotebookTheme
import com.example.englishnotebook.ui.theme.bottomGradient
import com.example.englishnotebook.ui.theme.drawerGradientColor
import com.example.englishnotebook.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnglishnotebookTheme {
                MyApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(viewModel: AuthViewModel = hiltViewModel()) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels.toFloat()



    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (currentDestination == "feed" || currentDestination == "profile" || currentDestination == "addstory") {
                // Drawer içeriği sadece menü butonuna tıklanırsa görünsün
                if (drawerState.isOpen) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(300.dp)
                            .background(drawerGradientColor, shape = RoundedCornerShape(topEnd = 25.dp, bottomEnd = 25.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DrawerContent(navController,viewModel)
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (currentDestination == "feed" || currentDestination == "profile") {
                    TopAppBar(
                        title = { Text("Story Time", fontSize = 20.sp) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu", Modifier.size(22.dp))
                            }
                        }
                    )
                }

            },
            bottomBar = {
                if (currentDestination?.startsWith("addstory") == true || currentDestination == "feed" || currentDestination == "profile") {
                    Log.d("Current Destination", "Current destination is: $currentDestination")
                    Surface(
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bottomGradient),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(onClick = { navController.navigate("feed") }) {
                                Icon(
                                    modifier = Modifier.size(26.dp),
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    tint = if (currentDestination == "feed") Color.White else Color.Gray
                                )
                            }
                            IconButton(onClick = { navController.navigate("profile") }) {
                                Icon(
                                    modifier = Modifier.size(26.dp),
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = if (currentDestination == "profile") Color.White else Color.Gray
                                )
                            }
                            IconButton(onClick = { navController.navigate("profile") }) {
                                Icon(
                                    modifier = Modifier.size(26.dp),
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Diary",
                                    tint = if (currentDestination == "profile") Color.White else Color.Gray
                                )
                            }
                        }
                    }
                }
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    Router(navController = navController)
                }
            }
        )
    }
}

