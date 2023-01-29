import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import creation.CreateIngredient
import cuboc.ingredient.Ingredient
import logic.IngredientSearchResult
import logic.SearchRequest
import logic.SearchResult
import logic.SearchType
import search.SearchField
import search.SearchResultsList

@Composable
internal fun BackButton(onClick: () -> Unit) {
    Button(
        shape = RoundedCornerShape(50),
        onClick = onClick
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
            )
            Text("Back", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
internal fun SaveButton(enables: Boolean, onClick: () -> Unit) {
    Button(
        shape = RoundedCornerShape(50),
        onClick = onClick, enabled = enables
    ) {
        Text("Save", style = MaterialTheme.typography.h6)
    }
}

enum class SearchOrCreateIngredientState {
    Search,
    Create
}

@Composable
internal fun SearchOrCreateIngredient(
    searchForIngredient: (SearchRequest) -> List<SearchResult>,
    onCancel: () -> Unit,
    onFinish: (Ingredient) -> Unit,
) {
    val state = remember { mutableStateOf(SearchOrCreateIngredientState.Search) }
    Column {
        BackButton(onCancel)
        when (state.value) {
            SearchOrCreateIngredientState.Search -> {
                Button(
                    shape = RoundedCornerShape(50),
                    onClick = {
                        state.value = SearchOrCreateIngredientState.Create
                    }) {
                    Text("Create new ingredient", style = MaterialTheme.typography.h6)
                }
                Text("Or search for existing ingredient: ")
                val searchResults = remember { mutableStateListOf<SearchResult>() }
                SearchField(SearchType.Ingredients) {
                    searchResults.clear()
                    searchResults.addAll(searchForIngredient(it))
                }
                SearchResultsList(searchResults) {
                    require(it is IngredientSearchResult)
                    searchResults.clear()
                    onFinish(it.ingredient)
                }
            }

            SearchOrCreateIngredientState.Create -> CreateIngredient(onFinish)
        }
    }
}

@Composable
internal fun DeleteButton(
    onDelete: () -> Unit
) {
    Button(
        modifier = Modifier.size(50.dp),
        shape = RoundedCornerShape(50),
        onClick = onDelete,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}
