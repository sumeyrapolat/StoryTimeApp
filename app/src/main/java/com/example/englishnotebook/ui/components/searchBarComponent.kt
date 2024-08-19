package com.example.englishnotebook.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishnotebook.ui.theme.MediumPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(
    searchText: MutableState<String>,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchText.value,
        onValueChange = { newText -> searchText.value = newText },
        placeholder = {
            Text(
                "Search...",
                color = Color.Gray,
                fontSize = 16.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.Gray)
        },
        trailingIcon = {
            if (searchText.value.isNotEmpty()) {
                IconButton(onClick = { searchText.value = "" }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear", tint = Color.Gray)
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search // Klavyede 'Search' eylemi için
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() } // Klavyede 'Search' butonuna basıldığında tetiklenir
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MediumPurple,
            unfocusedBorderColor = MediumPurple.copy(alpha = 0.5f),
            cursorColor = Color.Black,
        )
    )
}
