package com.example.englishnotebook.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.englishnotebook.ui.theme.Blue
import com.example.englishnotebook.ui.theme.DarkGreen
import com.example.englishnotebook.ui.theme.LightBlue
import com.example.englishnotebook.ui.theme.LightOrange
import com.example.englishnotebook.ui.theme.LightPurple
import com.example.englishnotebook.ui.theme.LightYellow
import com.example.englishnotebook.ui.theme.Orange
import com.example.englishnotebook.ui.theme.PastelPink
import com.example.englishnotebook.ui.theme.PastelYellow
import com.example.englishnotebook.ui.theme.Pink
import com.example.englishnotebook.ui.theme.Purple
import com.example.englishnotebook.ui.theme.SoftBlue
import com.example.englishnotebook.ui.theme.SoftGreen
import com.example.englishnotebook.ui.theme.SoftPink
import com.example.englishnotebook.ui.theme.addStoryButtonColor
import com.example.englishnotebook.ui.theme.addStoryCardColor
import com.example.englishnotebook.viewmodel.FeedViewModel

@Composable
fun AddStoryScreen(navController: NavController, words: List<String>, viewModel: FeedViewModel = hiltViewModel()) {
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var storyContent by remember { mutableStateOf(TextFieldValue("")) }

    val postState by viewModel.postState.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.resetState() // Ekran açıldığında state'i sıfırla
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            border = BorderStroke(2.dp, addStoryCardColor),
            shape = RoundedCornerShape(25.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(addStoryCardColor)
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(words) { word ->
                        Text(
                            text = word,
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, addStoryCardColor),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.Transparent)
                ) { innerTextField ->
                    if (title.text.isEmpty()) {
                        Text(
                            text = "Enter Story Title",
                            color = Color.Gray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    innerTextField()
                }

                // İçerik LazyColumn
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        BasicTextField(
                            value = storyContent,
                            onValueChange = { storyContent = it },
                            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(Color.Transparent)
                        ) { innerTextField ->
                            if (storyContent.text.isEmpty()) {
                                Text(
                                    text = "Write your story...",
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            innerTextField()
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            viewModel.savePost(
                                title.text,
                                storyContent.text,
                                words
                            )
                            navController.navigate("feed")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(45.dp),
                        modifier = Modifier
                            .wrapContentWidth()
                            .background( addStoryButtonColor, shape = RoundedCornerShape(45.dp))
                    ) {
                        Text(
                            text = "Save Story",
                            color = Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(postState) {
        when (postState) {
            is FeedViewModel.PostState.Success -> {
                viewModel.resetState() // Önce state'i sıfırla
            }
            is FeedViewModel.PostState.Error -> {
                // Hata mesajı göstermek için Snackbar veya başka bir UI bileşeni ekleyebilirsiniz
            }
            else -> Unit
        }
    }

}
