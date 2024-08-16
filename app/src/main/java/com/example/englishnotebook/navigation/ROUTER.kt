package com.example.englishnotebook.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.englishnotebook.ui.screens.AddStoryScreen
import com.example.englishnotebook.ui.screens.DetailScreen
import com.example.englishnotebook.ui.screens.FeedScreen
import com.example.englishnotebook.ui.screens.ProfileScreen
import com.example.englishnotebook.ui.screens.SignInScreen
import com.example.englishnotebook.ui.screens.SignUpScreen
import com.example.englishnotebook.ui.screens.WelcomeScreen
import com.example.englishnotebook.viewmodel.AuthViewModel
import com.example.englishnotebook.viewmodel.ProfileViewModel
import com.example.englishnotebook.viewmodel.SignInViewModel
import com.example.englishnotebook.viewmodel.SignUpViewModel

@Composable
fun Router(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel(), profileViewModel: ProfileViewModel = hiltViewModel()) {

    val userLoggedIn by authViewModel.userLoggedInState.collectAsState()

    // userLoggedIn durumuna göre başlangıç ekranını belirleyin
    val startDestination = if (userLoggedIn) "feed" else "welcome"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("feed") { FeedScreen(navController) }
        composable("detail") { DetailScreen(navController) }
        composable("signup") {
            val signUpViewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(navController, signUpViewModel)
        }
        composable("signin") {
            val signInViewModel = hiltViewModel<SignInViewModel>()
            SignInScreen(navController, signInViewModel)
        }
        composable("profile") { ProfileScreen(profileViewModel) }
        composable("welcome") { WelcomeScreen(navController) }

        composable("addstory/{words}", arguments = listOf(navArgument("words") { type = NavType.StringType })) { backStackEntry ->
            val words = backStackEntry.arguments?.getString("words")?.split(",") ?: emptyList()
            AddStoryScreen(navController = navController, words = words)
        }

    }
}
