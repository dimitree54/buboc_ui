import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import creation.CreateIngredient
import cuboc.ingredient.Ingredient
import cuboc_core.cuboc.database.search.IngredientSearchResult
import cuboc_core.cuboc.database.search.SearchRequest
import cuboc_core.cuboc.database.search.SearchResult
import cuboc_core.cuboc.database.search.SearchType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import search.SearchField
import search.SearchResultsList
import kotlin.reflect.KSuspendFunction1

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
    allowCreation: Boolean,
    searchForIngredient: KSuspendFunction1<SearchRequest, List<SearchResult>>,
    onCancel: () -> Unit,
    onFinish: (Ingredient) -> Unit,
) {
    val state = remember { mutableStateOf(SearchOrCreateIngredientState.Search) }
    val coroutineScope = rememberCoroutineScope()
    val searchQuery = remember { mutableStateOf("") }
    val searchResults = remember { mutableStateListOf<SearchResult>() }
    Column {
        BackButton(onCancel)
        when (state.value) {
            SearchOrCreateIngredientState.Search -> {
                var searchingCoroutine: Job? by remember { mutableStateOf(null) }
                SearchField(SearchType.SmartIngredients, searchQuery) {
                    searchingCoroutine?.cancel()
                    searchingCoroutine = coroutineScope.launch {
                        delay(1000)
                        val found = searchForIngredient(it)
                        searchResults.clear()
                        searchResults.addAll(found)
                    }
                }
                if (allowCreation && searchQuery.value.isNotBlank() && searchResults.isEmpty()) {
                    Button(
                        shape = RoundedCornerShape(50),
                        onClick = {
                            state.value = SearchOrCreateIngredientState.Create
                        }) {
                        Text("Create ingredient \"${searchQuery.value}\"", style = MaterialTheme.typography.h6)
                    }
                } else {
                    SearchResultsList(searchResults) {
                        require(it is IngredientSearchResult)
                        searchResults.clear()
                        onFinish(it.ingredient)
                    }
                }
            }

            SearchOrCreateIngredientState.Create -> CreateIngredient(searchQuery.value, onFinish)
        }
    }
}

@Composable
internal fun DeleteButton(
    compact: Boolean = false,
    onDelete: () -> Unit
) {
    Button(
        modifier = if (compact) Modifier.size(50.dp) else Modifier,
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
        if (!compact) Text("Delete", style = MaterialTheme.typography.h6)
    }
}
