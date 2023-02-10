import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import creation.CreateRecipe
import creation.CreateRequest
import creation.CreateResource
import cuboc.database.CUBOCDatabase
import cuboc_core.cuboc.database.firebase.CUBOCFirebase
import cuboc_core.cuboc.database.search.RecipeSearchResult
import cuboc_core.cuboc.database.search.ResourceSearchResult
import cuboc_core.cuboc.database.search.SearchResult
import cuboc_core.cuboc.database.search.SearchType
import cuboc_core.cuboc.scenario.CraftingScenario
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import search.SearchField
import search.SearchResultsList
import view.ViewRecipe
import view.ViewResource
import view.ViewScenario

@Composable
internal fun BubocApp() {
    val database = CUBOCFirebase()
    val scenariosBuilder = ScenarioBuilder(database, 5)
    MaterialTheme {
        Main(database, scenariosBuilder)
    }
}

enum class BubocState {
    SEARCH, CREATE, VIEW, REQUEST, LOADING
}

enum class CreateState {
    Resource, Recipe, Request
}

@Composable
internal fun ActionButtons(
    onCreateRecipe: () -> Unit,
    onCreateResource: () -> Unit,
    onRequest: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Button(
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1f),
            onClick = onCreateRecipe
        ) {
            Text("+recipe")
        }
        Button(
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1f),
            onClick = onCreateResource
        ) {
            Text("+resource")
        }
        Button(
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1f),
            onClick = onRequest
        ) {
            Text("+request")
        }
    }
}

@Composable
internal fun Main(database: CUBOCDatabase, scenariosBuilder: ScenarioBuilder) {
    val state = remember { mutableStateOf(BubocState.SEARCH) }
    val viewItem = remember { mutableStateOf<SearchResult?>(null) }
    val scenario = remember { mutableStateOf<CraftingScenario?>(null) }
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
                    },
                    onRequest = {
                        createState.value = CreateState.Request
                        state.value = BubocState.CREATE
                    }
                )
                var searchingCoroutine: Job? by remember { mutableStateOf(null) }
                SearchField(SearchType.All) {
                    searchingCoroutine?.cancel()
                    searchingCoroutine = coroutineScope.launch {
                        delay(1000)
                        val found = database.search(it)
                        searchResults.clear()
                        searchResults.addAll(found)
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
                        state.value = BubocState.LOADING
                        searchResults.clear()
                        coroutineScope.launch {
                            database.addResource(newResource)
                            state.value = BubocState.SEARCH
                        }
                    }

                    CreateState.Recipe -> CreateRecipe(
                        searchForIngredient = database::search,
                        onCancel = { state.value = BubocState.SEARCH }
                    ) { recipe ->
                        state.value = BubocState.LOADING
                        searchResults.clear()
                        coroutineScope.launch {
                            database.addRecipe(recipe)
                            state.value = BubocState.SEARCH
                        }
                    }

                    CreateState.Request -> CreateRequest(
                        searchForIngredient = database::search,
                        onCancel = { state.value = BubocState.SEARCH }
                    ) { request ->
                        coroutineScope.launch {
                            scenario.value = scenariosBuilder.searchForBestScenario(request)
                            state.value = BubocState.REQUEST
                        }
                    }

                    else -> {
                        println("Unsupported create state")
                        state.value = BubocState.SEARCH
                    }
                }
            }

            BubocState.VIEW -> {
                BackButton {
                    viewItem.value = null
                    state.value = BubocState.SEARCH
                }
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

            BubocState.REQUEST -> {
                BackButton {
                    scenario.value = null
                    state.value = BubocState.SEARCH
                }
                if (scenario.value == null) {
                    Text("Request can not be produced")
                } else {
                    ViewScenario(scenario.value!!) {
                        searchResults.clear()
                        state.value = BubocState.SEARCH
                        coroutineScope.launch {
                            database.execute(scenario.value!!, "test_user")
                            scenario.value = null
                            state.value = BubocState.SEARCH
                        }
                    }
                }
            }

            BubocState.LOADING -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }

    }
}
