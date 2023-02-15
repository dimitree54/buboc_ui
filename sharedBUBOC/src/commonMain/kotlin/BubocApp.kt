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
import cuboc.database.CUBOCDatabaseClient
import cuboc.database.firebase.CUBOCFirebaseAdmin
import cuboc.database.firebase.CUBOCFirebaseClient
import cuboc.ingredient.Resource
import cuboc.scenario.Scenario
import cuboc.scenario.ScenarioBuilder
import cuboc_core.cuboc.database.search.RecipeSearchResult
import cuboc_core.cuboc.database.search.ResourceSearchResult
import cuboc_core.cuboc.database.search.SearchResult
import cuboc_core.cuboc.database.search.SearchType
import cuboc_core.utility.IdGenerator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import search.SearchField
import search.SearchResultsList
import view.ViewRecipe
import view.ViewRequestAndScenario
import view.ViewResource

@Composable
internal fun BubocApp() {
    val databaseClient = CUBOCFirebaseClient()
    val databaseAdmin = CUBOCFirebaseAdmin()
    val scenariosBuilder = ScenarioBuilder(databaseClient, 5, IdGenerator())
    MaterialTheme {
        Main(databaseClient, databaseAdmin, scenariosBuilder)
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
internal fun Main(
    databaseClient: CUBOCDatabaseClient,
    databaseAdmin: CUBOCFirebaseAdmin,
    scenariosBuilder: ScenarioBuilder
) {
    val state = remember { mutableStateOf(BubocState.SEARCH) }
    val viewItem = remember { mutableStateOf<SearchResult?>(null) }
    val requestAndScenario = remember { mutableStateOf<Pair<Resource, Scenario>?>(null) }
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
                SearchField(SearchType.SmartAll) {
                    searchingCoroutine?.cancel()
                    searchingCoroutine = coroutineScope.launch {
                        delay(1000)
                        val found = databaseClient.search(it)
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
                        searchForIngredient = databaseClient::search,
                        onCancel = { state.value = BubocState.SEARCH }
                    ) { newResource ->
                        state.value = BubocState.LOADING
                        searchResults.clear()
                        coroutineScope.launch {
                            databaseClient.addResource(newResource)
                            state.value = BubocState.SEARCH
                        }
                    }

                    CreateState.Recipe -> CreateRecipe(
                        searchForIngredient = databaseClient::search,
                        onCancel = { state.value = BubocState.SEARCH }
                    ) { recipe ->
                        state.value = BubocState.LOADING
                        searchResults.clear()
                        coroutineScope.launch {
                            databaseClient.addRecipe(recipe)
                            state.value = BubocState.SEARCH
                        }
                    }

                    CreateState.Request -> CreateRequest(
                        searchForIngredient = databaseClient::search,
                        onCancel = { state.value = BubocState.SEARCH }
                    ) { request ->
                        coroutineScope.launch {
                            val scenario = scenariosBuilder.searchForBestScenario(request)
                            if (scenario != null) {
                                requestAndScenario.value = request to scenario
                            } else {
                                requestAndScenario.value = null
                            }
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
                    is ResourceSearchResult -> ViewResource(item.pieceOfUserResource) {
                        searchResults.clear()
                        state.value = BubocState.SEARCH
                        coroutineScope.launch {
                            databaseAdmin.removeResource(item.pieceOfUserResource.userResource)
                        }
                    }

                    is RecipeSearchResult -> ViewRecipe(item.userRecipe) {
                        searchResults.clear()
                        state.value = BubocState.SEARCH
                        coroutineScope.launch {
                            databaseAdmin.removeRecipe(item.userRecipe)
                        }
                    }

                    else -> throw IllegalStateException("Unsupported by viewer search result type")
                }
            }

            BubocState.REQUEST -> {
                BackButton {
                    requestAndScenario.value = null
                    state.value = BubocState.SEARCH
                }
                if (requestAndScenario.value == null) {
                    Text("Request can not be produced")
                } else {
                    val (request, scenario) = requestAndScenario.value!!
                    ViewRequestAndScenario(request, scenario) {
                        searchResults.clear()
                        state.value = BubocState.SEARCH
                        coroutineScope.launch {
                            databaseAdmin.launchScenario(listOf(request), scenario)
                            requestAndScenario.value = null
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
