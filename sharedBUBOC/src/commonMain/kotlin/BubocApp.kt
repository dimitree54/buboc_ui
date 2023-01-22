import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cuboc.ingredient.Ingredient
import cuboc.ingredient.RecipeInput
import cuboc.ingredient.RecipeOutput
import cuboc.ingredient.Resource
import cuboc.recipe.Instruction
import cuboc.recipe.Recipe
import utility.MeasureUnit

@Composable
internal fun BubocApp() {
    // val coroutineScope = rememberCoroutineScope()
    // val store = remember { coroutineScope.createStore() }
    // val state by store.stateFlow.collectAsState()
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
        ResourceSearchResult(friedPotato),
        RecipeSearchResult(recipe),
        ResourceSearchResult(potato),
    )

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // add button and by requests
                    SearchField(SearchType.All) { }
                    Box(Modifier.weight(1f)) {
                        SearchResults(searchResults)
                    }
                }
            }
        }
    }
}
