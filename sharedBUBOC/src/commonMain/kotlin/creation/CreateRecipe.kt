package creation

import RECIPE_ICON
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cuboc.ingredient.Ingredient
import cuboc.ingredient.RecipeInput
import cuboc.ingredient.RecipeOutput
import cuboc.recipe.Instruction
import cuboc.recipe.Recipe
import logic.IngredientSearchResult
import logic.SearchRequest
import logic.SearchResult
import logic.SearchType
import search.SearchField
import search.SearchResultsList

enum class RecipeCreationState {
    FILLING_FORM,
    INPUT_INGREDIENT_SEARCH,
    INPUT_INGREDIENT_CREATION,
    OUTPUT_INGREDIENT_SEARCH,
    OUTPUT_INGREDIENT_CREATION
}


@Composable
internal fun CreateRecipeForm(
    recipeName: MutableState<String>,
    recipeInputPrototypes: MutableList<RecipeInputPrototype>,
    recipeOutputPrototypes: MutableList<RecipeOutputPrototype>,
    durationMinutesText: MutableState<String>,
    instructionsText: MutableState<String>,
    onInputAdd: () -> Unit,
    onOutputAdd: () -> Unit,
) {
    Row {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = RECIPE_ICON,
            contentDescription = null,
            tint = Color.Black
        )
        Text(
            text = "Create recipe", style = MaterialTheme.typography.h4
        )
    }
    Row {
        Text(
            text = "Name: ",
            style = MaterialTheme.typography.h5
        )
        TextField(
            recipeName.value,
            onValueChange = { recipeName.value = it }
        )
    }
    Text(text = "Inputs:")
    recipeInputPrototypes.forEachIndexed { index, recipeInputPrototype ->
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { recipeInputPrototypes.remove(recipeInputPrototype) }) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            Text(recipeInputPrototype.ingredient.name)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                recipeInputPrototype.amountText,
                onValueChange = {
                    val newPrototype = RecipeInputPrototype(
                        recipeInputPrototype.ingredient,
                        it,
                        recipeInputPrototype.scalable
                    )
                    recipeInputPrototypes[index] = newPrototype
                })
            Text(recipeInputPrototype.ingredient.measureUnit.name)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Scalable: ")
            Checkbox(
                checked = recipeInputPrototype.scalable,
                onCheckedChange = {
                    val newPrototype = RecipeInputPrototype(
                        recipeInputPrototype.ingredient,
                        recipeInputPrototype.amountText,
                        it
                    )
                    recipeInputPrototypes[index] = newPrototype
                }
            )
        }
    }
    Button(onClick = onInputAdd) {
        Text("Add input")
    }
    Row {
        Text(
            text = "Duration in minutes: ",
            style = MaterialTheme.typography.h5
        )
        TextField(
            durationMinutesText.value,
            onValueChange = { durationMinutesText.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
    Row {
        Text(
            text = "Instructions: ",
            style = MaterialTheme.typography.h5
        )
        TextField(
            instructionsText.value,
            onValueChange = { instructionsText.value = it }
        )
    }
    Text(text = "Outputs:")
    recipeOutputPrototypes.forEachIndexed { index, recipeOutputPrototype ->
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { recipeOutputPrototypes.remove(recipeOutputPrototype) }) {
                Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            Text(recipeOutputPrototype.ingredient.name)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                recipeOutputPrototype.amountText,
                onValueChange = {
                    val newPrototype = RecipeOutputPrototype(
                        recipeOutputPrototype.ingredient,
                        it,
                        recipeOutputPrototype.scalable
                    )
                    recipeOutputPrototypes[index] = newPrototype
                })
            Text(recipeOutputPrototype.ingredient.measureUnit.name)
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Scalable: ")
            Checkbox(
                checked = recipeOutputPrototype.scalable,
                onCheckedChange = {
                    val newPrototype = RecipeOutputPrototype(
                        recipeOutputPrototype.ingredient,
                        recipeOutputPrototype.amountText,
                        it
                    )
                    recipeOutputPrototypes[index] = newPrototype
                }
            )
        }
    }
    Button(onClick = onOutputAdd) {
        Text("Add output")
    }
}

data class RecipeInputPrototype(
    val ingredient: Ingredient,
    val amountText: String = "",
    val scalable: Boolean = false
) {

    fun toRecipeInput(): RecipeInput? {
        return RecipeInput(
            ingredient,
            amountText.toDoubleOrNull() ?: return null,
            scalable
        )
    }
}

data class RecipeOutputPrototype(
    val ingredient: Ingredient,
    val amountText: String = "",
    val scalable: Boolean = false
) {

    fun toRecipeOutput(): RecipeOutput? {
        return RecipeOutput(
            ingredient,
            amountText.toDoubleOrNull() ?: return null,
            scalable
        )
    }
}

@Composable
internal fun DynamicSaveButton(
    name: String,
    recipeInputs: Set<RecipeInput>,
    recipeOutputs: Set<RecipeOutput>,
    durationMinutes: Int?,
    instructionsText: String,
    onCreation: (Recipe) -> Unit
) {
    if (durationMinutes != null && name.isNotBlank() && recipeInputs.isNotEmpty() && recipeOutputs.isNotEmpty() && instructionsText.isNotBlank()) {
        Button(onClick = {
            val recipe = Recipe(
                name,
                recipeInputs,
                recipeOutputs,
                Instruction(durationMinutes, instructionsText)
            )
            onCreation(recipe)
        }) {
            Text("Save")
        }
    }
}

@Composable
internal fun CreateRecipe(
    searchForIngredient: (SearchRequest) -> List<SearchResult>,
    onCancel: () -> Unit,
    onCreation: (Recipe) -> Unit
) {
    val state = remember { mutableStateOf(RecipeCreationState.FILLING_FORM) }
    val recipeName = remember { mutableStateOf("") }
    val recipeInputPrototypes = remember { mutableStateListOf<RecipeInputPrototype>() }
    val recipeOutputPrototypes = remember { mutableStateListOf<RecipeOutputPrototype>() }
    val durationMinutesText = remember { mutableStateOf("") }
    val instructionsText = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        when (state.value) {
            RecipeCreationState.FILLING_FORM -> {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = onCancel) {
                        Text("Back")
                    }
                    CreateRecipeForm(
                        recipeName,
                        recipeInputPrototypes,
                        recipeOutputPrototypes,
                        durationMinutesText,
                        instructionsText,
                        onInputAdd = { state.value = RecipeCreationState.INPUT_INGREDIENT_SEARCH },
                        onOutputAdd = { state.value = RecipeCreationState.OUTPUT_INGREDIENT_SEARCH }
                    )
                    DynamicSaveButton(
                        recipeName.value,
                        recipeInputPrototypes.mapNotNull { it.toRecipeInput() }.toSet(),
                        recipeOutputPrototypes.mapNotNull { it.toRecipeOutput() }.toSet(),
                        durationMinutesText.value.toIntOrNull(),
                        instructionsText.value,
                        onCreation
                    )
                }
            }

            RecipeCreationState.INPUT_INGREDIENT_SEARCH -> {
                Button(onClick = { state.value = RecipeCreationState.FILLING_FORM }) {
                    Text("Back")
                }
                Button(onClick = {
                    state.value = RecipeCreationState.INPUT_INGREDIENT_CREATION
                }) {
                    Text("Create ingredient")
                }
                val searchResults = remember { mutableStateListOf<SearchResult>() }
                SearchField(SearchType.Ingredients) {
                    searchResults.clear()
                    searchResults.addAll(searchForIngredient(it))
                }
                SearchResultsList(searchResults) {
                    require(it is IngredientSearchResult)
                    recipeInputPrototypes.add(RecipeInputPrototype(it.ingredient))
                    state.value = RecipeCreationState.FILLING_FORM
                }
            }

            RecipeCreationState.INPUT_INGREDIENT_CREATION -> {
                Button(onClick = { state.value = RecipeCreationState.FILLING_FORM }) {
                    Text("Back")
                }
                CreateIngredient {
                    recipeInputPrototypes.add(RecipeInputPrototype(it))
                    state.value = RecipeCreationState.FILLING_FORM
                }
            }

            RecipeCreationState.OUTPUT_INGREDIENT_SEARCH -> {
                Button(onClick = { state.value = RecipeCreationState.FILLING_FORM }) {
                    Text("Back")
                }
                Button(onClick = {
                    state.value = RecipeCreationState.OUTPUT_INGREDIENT_CREATION
                }) {
                    Text("Create ingredient")
                }
                val searchResults = remember { mutableStateListOf<SearchResult>() }
                SearchField(SearchType.Ingredients) {
                    searchResults.clear()
                    searchResults.addAll(searchForIngredient(it))
                }
                SearchResultsList(searchResults) {
                    require(it is IngredientSearchResult)
                    recipeOutputPrototypes.add(RecipeOutputPrototype(it.ingredient))
                    state.value = RecipeCreationState.FILLING_FORM
                }
            }

            RecipeCreationState.OUTPUT_INGREDIENT_CREATION -> {
                Button(onClick = { state.value = RecipeCreationState.FILLING_FORM }) {
                    Text("Back")
                }
                CreateIngredient {
                    recipeOutputPrototypes.add(RecipeOutputPrototype(it))
                    state.value = RecipeCreationState.FILLING_FORM
                }
            }
        }
    }
}
