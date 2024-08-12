package com.example.englishnotebook.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.englishnotebook.ui.screens.AddStoryScreen
import com.example.englishnotebook.ui.screens.DetailScreen
import com.example.englishnotebook.ui.screens.FeedScreen
import com.example.englishnotebook.ui.screens.ProfileScreen
import com.example.englishnotebook.ui.screens.SignInScreen
import com.example.englishnotebook.ui.screens.SignUpScreen
import com.example.englishnotebook.ui.screens.WelcomeScreen
import com.example.englishnotebook.viewmodel.SignInViewModel
import com.example.englishnotebook.viewmodel.SignUpViewModel

@Composable
fun Router(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "feed") {
        composable("feed") { FeedScreen(navController) }
        composable("detail") { DetailScreen(navController) }
        composable("addstory") { AddStoryScreen(navController) }
        composable("signup") {
            val signUpViewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(navController, signUpViewModel)
        }
        composable("signin") {
            val signInViewModel = hiltViewModel<SignInViewModel>()
            SignInScreen(navController, signInViewModel)
        }
        composable("profile") { ProfileScreen(navController) }
        composable("welcome") { WelcomeScreen(navController) }
    }
}
