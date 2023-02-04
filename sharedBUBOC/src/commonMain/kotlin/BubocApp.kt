import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import creation.CreateRecipe
import creation.CreateResource
import cuboc.database.CUBOCDatabase
import cuboc_core.cuboc.database.firebase.CUBOCFirebase
import cuboc_core.cuboc.database.search.RecipeSearchResult
import cuboc_core.cuboc.database.search.ResourceSearchResult
import cuboc_core.cuboc.database.search.SearchResult
import cuboc_core.cuboc.database.search.SearchType
import kotlinx.coroutines.launch
import search.SearchField
import search.SearchResultsList
import view.ViewRecipe
import view.ViewResource

@Composable
internal fun BubocApp() {
    val database = CUBOCFirebase()
    MaterialTheme {
        Main(database)
    }
}

enum class BubocState {
    SEARCH, CREATE, VIEW
}

enum class CreateState {
    Resource, Recipe
}

@Composable
internal fun ActionButtons(
    onCreateRecipe: () -> Unit,
    onCreateResource: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Button(
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1f),
            onClick = onCreateRecipe
        ) {
            Text("New recipe")
        }
        Button(
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1f),
            onClick = onCreateResource
        ) {
            Text("New resource")
        }
    }
}

@Composable
internal fun Main(database: CUBOCDatabase) {
    val state = remember { mutableStateOf(BubocState.SEARCH) }
    val viewItem = remember { mutableStateOf<SearchResult?>(null) }
    val createState = remember { mutableStateOf<CreateState?>(null) }
    val searchResults = remember { mutableStateListOf<SearchResult>() }
    val searchResultsListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        when (state.value) {
            BubocState.SEARCH -> {
                ActionButtons(
                    onCreateRecipe = {
                        createState.value = CreateState.Recipe
                        state.value = BubocState.CREATE
                    },
                    onCreateResource = {
                        createState.value = CreateState.Resource
                        state.value = BubocState.CREATE
                    }
                )
                SearchField(SearchType.All) {
                    searchResults.clear()
                    coroutineScope.launch {
                        searchResults.addAll(database.search(it))
                    }
                }
                SearchResultsList(searchResults, searchResultsListState) {
                    viewItem.value = it
                    state.value = BubocState.VIEW
                }
            }

            BubocState.CREATE -> {
                when (createState.value) {
                    CreateState.Resource -> CreateResource(
                        searchForIngredient = database::search,
                        onCancel = { state.value = BubocState.SEARCH }
                    ) { newResource ->
                        searchResults.clear()
                        state.value = BubocState.SEARCH
                        coroutineScope.launch {
                            database.addResource(newResource)
                        }
                    }

                    CreateState.Recipe -> CreateRecipe(
                        searchForIngredient = database::search,
                        onCancel = { state.value = BubocState.SEARCH }
                    ) { recipe ->
                        searchResults.clear()
                        state.value = BubocState.SEARCH
                        coroutineScope.launch {
                            database.addRecipe(recipe)
                        }
                    }

                    else -> throw IllegalStateException("Unsupported by creator search result type")
                }
            }

            BubocState.VIEW -> {
                BackButton { state.value = BubocState.SEARCH }
                when (val item = viewItem.value) {
                    is ResourceSearchResult -> ViewResource(item.resource) {
                        searchResults.clear()
                        state.value = BubocState.SEARCH
                        coroutineScope.launch {
                            database.removeResource(item.resource)
                        }
                    }

                    is RecipeSearchResult -> ViewRecipe(item.recipe) {
                        searchResults.clear()
                        state.value = BubocState.SEARCH
                        coroutineScope.launch {
                            database.removeRecipe(item.recipe)
                        }
                    }

                    else -> throw IllegalStateException("Unsupported by viewer search result type")
                }
            }
        }

    }
}
