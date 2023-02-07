package search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cuboc_core.cuboc.database.search.SearchRequest
import cuboc_core.cuboc.database.search.SearchType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun SearchField(searchType: SearchType, search: (SearchRequest) -> Unit) {
    val inputText = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        shape = RoundedCornerShape(50),
        modifier = Modifier.fillMaxWidth(),
        value = inputText.value,
        placeholder = {
            val placeholderText = when (searchType) {
                SearchType.RecipesByInput -> "Search for recipes by input"
                SearchType.RecipesByOutput -> "Search for recipes by output"
                SearchType.RecipesByName -> "Search for recipes"
                SearchType.Resources -> "Search for resources"
                SearchType.Ingredients -> "Search for ingredients"
                SearchType.All -> "Search for recipes and resources"
            }
            Text(placeholderText)
        },
        onValueChange = {
            if (it.lastOrNull() == '\n') {
                keyboardController?.hide()
            } else {
                inputText.value = it
            }
            if (inputText.value.isNotBlank()) {
                search(SearchRequest(inputText.value, searchType))
            }
        },
        trailingIcon = {
            if (inputText.value.isNotBlank()) {
                Icon(
                    modifier = Modifier.clickable {
                        if (inputText.value.isNotBlank()) {
                            keyboardController?.hide()
                            search(SearchRequest(inputText.value, searchType))
                        }
                    }, imageVector = Icons.Default.Search, contentDescription = null
                )
            } else {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }

        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
            disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
        }
    )
}
