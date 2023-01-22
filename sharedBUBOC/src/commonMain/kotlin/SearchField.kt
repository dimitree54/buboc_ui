import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class SearchType {
    Recipes, Resources, All
}

@Composable
internal fun SearchField(searchType: SearchType, search: (String) -> Unit) {
    var inputText by remember { mutableStateOf("") }
    TextField(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.background).padding(10.dp),
        value = inputText,
        placeholder = {
            val placeholderText = when (searchType) {
                SearchType.Recipes -> "Search for recipes"
                SearchType.Resources -> "Search for resources"
                SearchType.All -> "Search for recipes and resources"
            }
            Text(placeholderText)
        },
        onValueChange = {
            inputText = it
        },
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable { search(inputText) },
                imageVector = Icons.Default.Search,
                contentDescription = "Send",
                tint = MaterialTheme.colors.primary
            )
        })
}