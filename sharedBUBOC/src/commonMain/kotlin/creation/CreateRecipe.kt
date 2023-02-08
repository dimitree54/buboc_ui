package creation

import BackButton
import DeleteButton
import RECIPE_ICON
import SaveButton
import SearchOrCreateIngredient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cuboc.ingredient.Ingredient
import cuboc.ingredient.RecipeInput
import cuboc.ingredient.RecipeOutput
import cuboc.recipe.Instruction
import cuboc.recipe.Recipe
import cuboc_core.cuboc.database.search.SearchRequest
import cuboc_core.cuboc.database.search.SearchResult
import utility.Name
import kotlin.reflect.KSuspendFunction1

enum class RecipeCreationState {
    FILLING_FORM,
    REQUEST_INPUT_INGREDIENT,
    REQUEST_OUTPUT_INGREDIENT
}

data class RecipeInputPrototype(
    val ingredient: Ingredient,
    val amountText: String = "",
    val scalable: Boolean = false
) {

    fun toRecipeInput(): RecipeInput? {
        val amount = amountText.toDoubleOrNull() ?: return null
        if (amount <= 0) return null
        return RecipeInput(
            ingredient,
            amount,
            scalable
        )
    }

    fun toRecipeOutput(): RecipeOutput? {
        val amount = amountText.toDoubleOrNull() ?: return null
        if (amount <= 0) return null
        return RecipeOutput(
            ingredient,
            amount,
            scalable
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun EditRecipeInputPrototype(
    recipeOutputPrototype: RecipeInputPrototype,
    onChange: (RecipeInputPrototype) -> Unit
) {
    Box(modifier = Modifier.border(BorderStroke(2.dp, Color.Black))) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(recipeOutputPrototype.ingredient.name.toString(), style = MaterialTheme.typography.h6)
            Text("Amount (in ${recipeOutputPrototype.ingredient.measureUnit}): ")
            val keyboardController = LocalSoftwareKeyboardController.current
            TextField(
                recipeOutputPrototype.amountText,
                onValueChange = {
                    if (it.lastOrNull() == '\n') {
                        keyboardController?.hide()
                    } else if (it.toDoubleOrNull() != null || it.isEmpty()) {
                        val newPrototype = RecipeInputPrototype(
                            recipeOutputPrototype.ingredient,
                            it,
                            recipeOutputPrototype.scalable
                        )
                        onChange(newPrototype)
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions { keyboardController?.hide() }
            )
            Row {
                Text("Scalable:")
                Checkbox(
                    checked = recipeOutputPrototype.scalable,
                    onCheckedChange = {
                        val newPrototype = RecipeInputPrototype(
                            recipeOutputPrototype.ingredient,
                            recipeOutputPrototype.amountText,
                            it
                        )
                        onChange(newPrototype)
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun CreateRecipeForm(
    recipeName: MutableState<String>,
    recipeInputPrototypes: MutableList<RecipeInputPrototype>,
    recipeOutputPrototypes: MutableList<RecipeInputPrototype>,
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
            text = "Create recipe", style = MaterialTheme.typography.h3
        )
    }
    Text(
        text = "Name: ",
        style = MaterialTheme.typography.h5
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        recipeName.value,
        singleLine = true,
        shape = RoundedCornerShape(25),
        onValueChange = {
            if (it.lastOrNull() == '\n') {
                keyboardController?.hide()
            } else {
                recipeName.value = it
            }
        },
        keyboardActions = KeyboardActions { keyboardController?.hide() }
    )
    Text(text = "Inputs:", style = MaterialTheme.typography.h5)
    recipeInputPrototypes.forEachIndexed { index, recipeInputPrototype ->
        Row(modifier = Modifier.fillMaxWidth()) {
            DeleteButton(true) {
                recipeInputPrototypes.removeAt(index)
            }
            EditRecipeInputPrototype(recipeInputPrototype) {
                recipeInputPrototypes[index] = it
            }
        }
    }
    Button(shape = RoundedCornerShape(50), onClick = onInputAdd) {
        Row {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Text("Add input", style = MaterialTheme.typography.h6)
        }
    }
    Text("Duration in minutes: ", style = MaterialTheme.typography.h5)
    TextField(
        durationMinutesText.value,
        singleLine = true,
        shape = RoundedCornerShape(25),
        onValueChange = {
            if (it.lastOrNull() == '\n') {
                keyboardController?.hide()
            } else if (it.toDoubleOrNull() != null) {
                durationMinutesText.value = it
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions { keyboardController?.hide() }
    )
    Text("Instructions: ", style = MaterialTheme.typography.h5)
    TextField(
        instructionsText.value,
        singleLine = true,
        shape = RoundedCornerShape(25),
        onValueChange = {
            if (it.lastOrNull() == '\n') {
                keyboardController?.hide()
            } else {
                instructionsText.value = it
            }
        },
        keyboardActions = KeyboardActions { keyboardController?.hide() }
    )
    Text(text = "Outputs:", style = MaterialTheme.typography.h5)
    recipeOutputPrototypes.forEachIndexed { index, recipeOutputPrototype ->
        DeleteButton(true) {
            recipeInputPrototypes.removeAt(index)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            EditRecipeInputPrototype(recipeOutputPrototype) {
                recipeOutputPrototypes[index] = it
            }
        }
    }
    Button(shape = RoundedCornerShape(50), onClick = onOutputAdd) {
        Row {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Text("Add output", style = MaterialTheme.typography.h6)
        }
    }
}

@Composable
internal fun CreateRecipe(
    searchForIngredient: KSuspendFunction1<SearchRequest, List<SearchResult>>,
    onCancel: () -> Unit,
    onCreation: (Recipe) -> Unit
) {
    val state = remember { mutableStateOf(RecipeCreationState.FILLING_FORM) }
    val recipeName = remember { mutableStateOf("") }
    val recipeInputPrototypes = remember { mutableStateListOf<RecipeInputPrototype>() }
    val recipeOutputPrototypes = remember { mutableStateListOf<RecipeInputPrototype>() }
    val durationMinutesText = remember { mutableStateOf("") }
    val instructionsText = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        when (state.value) {
            RecipeCreationState.FILLING_FORM -> {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BackButton(onClick = onCancel)
                    CreateRecipeForm(
                        recipeName,
                        recipeInputPrototypes,
                        recipeOutputPrototypes,
                        durationMinutesText,
                        instructionsText,
                        onInputAdd = { state.value = RecipeCreationState.REQUEST_INPUT_INGREDIENT },
                        onOutputAdd = {
                            state.value = RecipeCreationState.REQUEST_OUTPUT_INGREDIENT
                        }
                    )
                    val durationMinutes = durationMinutesText.value.toIntOrNull()
                    val recipeInputs =
                        recipeInputPrototypes.mapNotNull { it.toRecipeInput() }.toSet()
                    val recipeOutputs =
                        recipeOutputPrototypes.mapNotNull { it.toRecipeOutput() }.toSet()
                    val readyToSave =
                        durationMinutes != null && recipeName.value.isNotBlank() && recipeInputs.isNotEmpty() && recipeOutputs.isNotEmpty() && instructionsText.value.isNotBlank()
                    SaveButton(readyToSave) {
                        val recipe = Recipe(
                            Name(recipeName.value),
                            recipeInputs,
                            recipeOutputs,
                            Instruction(durationMinutes!!, utility.Text(instructionsText.value))
                        )
                        onCreation(recipe)
                    }
                }
            }

            RecipeCreationState.REQUEST_INPUT_INGREDIENT -> SearchOrCreateIngredient(
                true,
                searchForIngredient,
                onCancel = { state.value = RecipeCreationState.FILLING_FORM },
            ) {
                recipeInputPrototypes.add(RecipeInputPrototype(it))
                state.value = RecipeCreationState.FILLING_FORM
            }

            RecipeCreationState.REQUEST_OUTPUT_INGREDIENT -> SearchOrCreateIngredient(
                true,
                searchForIngredient,
                onCancel = { state.value = RecipeCreationState.FILLING_FORM },
            ) {
                recipeOutputPrototypes.add(RecipeInputPrototype(it))
                state.value = RecipeCreationState.FILLING_FORM
            }
        }
    }
}
