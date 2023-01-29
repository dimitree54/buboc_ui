import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import creation.CreateRecipe
import creation.CreateResource
import logic.*
import search.SearchField
import search.SearchResultsList
import view.ViewRecipe
import view.ViewResource

@Composable
internal fun BubocApp() {
    val database = FakeDatabase()
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
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().padding(8.dp)) {
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
internal fun Main(database: FakeDatabase) {
    val state = remember { mutableStateOf(BubocState.SEARCH) }
    val viewItem = remember { mutableStateOf<SearchResult?>(null) }
    val createState = remember { mutableStateOf<CreateState?>(null) }
    val searchResults = remember { mutableStateListOf<SearchResult>() }
    val searchResultsListState = rememberLazyListState()
    Column(
        modifier = Modifier.fillMaxSize()
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
                    searchResults.addAll(database.search(it))
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
                        onCancel = { state.value = BubocState.SEARCH },
                        onCreation = { ingredient, amount ->
                            database.addResource(ingredient, amount)
                            state.value = BubocState.SEARCH
                        }
                    )

                    CreateState.Recipe -> CreateRecipe(
                        searchForIngredient = database::search,
                        onCancel = { state.value = BubocState.SEARCH },
                        onCreation = { recipe ->
                            database.addRecipe(recipe)
                            state.value = BubocState.SEARCH
                        }
                    )

                    else -> throw IllegalStateException("Unsupported by creator search result type")
                }
            }

            BubocState.VIEW -> {
                Button(onClick = {
                    state.value = BubocState.SEARCH
                }) {
                    Text("Back")
                }
                when (val item = viewItem.value) {
                    is ResourceSearchResult -> ViewResource(item.resource)
                    is RecipeSearchResult -> ViewRecipe(item.recipe)
                    else -> throw IllegalStateException("Unsupported by viewer search result type")
                }
            }
        }

    }
}
