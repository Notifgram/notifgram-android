package com.notifgram.core.presentation_core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@Composable
fun SearchBarComp(
    onValueChange: (searchText: String) -> Unit,
    leadingIcon: ImageVector = Icons.Filled.Search,
    placeholderText: String
) {
    val text = rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onValueChange(it)
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        placeholder = { Text(placeholderText) },
        maxLines = 1,
        singleLine = true,
        shape = RoundedCornerShape(55.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        leadingIcon = {
            Icon(
                leadingIcon,
                contentDescription = "leadingIcon",
            )
        },
        trailingIcon = {
            Icon(
                Icons.Filled.Clear,
                contentDescription = "trailingIcon",
                Modifier
                    .size(AssistChipDefaults.IconSize)
                    .clickable {
                        text.value = ""
                        onValueChange(text.value)
                    }
            )
        },
    )// end OutlinedTextField

}

@Preview("dark theme")
@Preview("light theme", showBackground = true)
@Composable
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
private fun SearchBarCompPreview() {
    MaterialTheme {
        SearchBarComp({}, placeholderText = "text")
    }
}
