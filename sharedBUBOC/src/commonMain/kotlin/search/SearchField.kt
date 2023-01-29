package search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import logic.SearchRequest
import logic.SearchType

@Composable
internal fun SearchField(searchType: SearchType, search: (SearchRequest) -> Unit) {
    val inputText = remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
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
            inputText.value = it
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable {
                    val searchRequest = SearchRequest(inputText.value, searchType)
                    search(searchRequest)
                }, imageVector = Icons.Default.Search, contentDescription = null
            )
        })
}
