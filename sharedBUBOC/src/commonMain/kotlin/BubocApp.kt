import entry.RecipeInstance
import entry.ResourceInstance
import entry.Instance
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import card.*
import cuboc.ingredient.Ingredient
import cuboc.ingredient.RecipeInput
import cuboc.ingredient.RecipeOutput
import cuboc.ingredient.Resource
import cuboc.recipe.Instruction
import cuboc.recipe.Recipe
import search.SearchField
import search.SearchResultsList
import search.SearchType
import utility.MeasureUnit

data class SearchRequest(
    val query: String, val type: SearchType
)

@Composable
internal fun BubocApp() {
    val potato = Resource("", Ingredient("Potato", MeasureUnit("kg")), 5.0)
    val oil = Resource("", Ingredient("Oil", MeasureUnit("l")), 3.0)
    val kitchen = Resource("", Ingredient("Kitchen", MeasureUnit("unit")), 1.0)
    val pan = Resource("", Ingredient("Pan", MeasureUnit("unit")), 1.0)
    val chief = Resource("", Ingredient("Chief", MeasureUnit("person")), 1.0)
    val friedPotato = Resource("", Ingredient("Fried potato", MeasureUnit("kg")), 5.0)

    val inputs = listOf(potato, oil, kitchen, pan, chief).map { RecipeInput(it.ingredient, it.amount, true) }
    val outputs = listOf(friedPotato, kitchen, pan, chief).map { RecipeOutput(it.ingredient, it.amount, true) }
    val recipe = Recipe("Fried potato", inputs.toSet(), outputs.toSet(), Instruction(30, "Fry the potato"))
    val searchResults = listOf(
        ResourceInstance(friedPotato),
        RecipeInstance(recipe),
        ResourceInstance(potato),
    )

    val cardsStackState = remember { mutableStateListOf<Any>() }
    val searchResultsState = remember { mutableStateListOf<Instance>().apply { addAll(searchResults) } }
    val searchRequestState = remember { mutableStateOf<SearchRequest?>(null) }
    MaterialTheme {
        Main(cardsStackState, searchResultsState, searchRequestState)
    }
}


@Composable
internal fun Main(
    stackState: MutableList<Any>, searchResults: MutableList<Instance>, searchRequest: MutableState<SearchRequest?>
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (stackState.isEmpty()) {
            SearchField(SearchType.All) {
                searchRequest.value = SearchRequest(it, SearchType.All)
            }
            ActionButtons()
            SearchResultsList(searchResults, stackState)
        } else {
            when (val obj = stackState.last()) {
                is Resource -> ResourceCard(obj, stackState)
                is Recipe -> RecipeCard(obj, stackState)
                is Ingredient -> IngredientCard(obj, stackState)
                is RecipeInput -> RecipeInputCard(obj, stackState)
                is RecipeOutput -> RecipeOutputCard(obj, stackState)
                else -> throw Exception("Unknown object type")
            }
        }
    }
}

@Composable
internal fun ActionButtons() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(modifier = Modifier.padding(10.dp), onClick = { /*TODO*/ }) {
            Text("Create new")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(modifier = Modifier.padding(10.dp), onClick = { /*TODO*/ }) {
            Text("My requests")
        }
    }
}
