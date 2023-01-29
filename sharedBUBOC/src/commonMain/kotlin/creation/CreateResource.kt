package creation

import RESOURCE_ICON
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cuboc.ingredient.Ingredient
import logic.IngredientSearchResult
import logic.SearchRequest
import logic.SearchResult
import logic.SearchType
import search.SearchField
import search.SearchResultsList

enum class ResourceCreationState {
    FILLING_FORM,
    INGREDIENT_SEARCH,
    INGREDIENT_CREATION
}

@Composable
internal fun CreateResourceForm(
    ingredient: Ingredient?,
    amountText: MutableState<String>,
    onIngredientSearch: () -> Unit,
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = RESOURCE_ICON,
                contentDescription = null,
                tint = Color.Black
            )
            Text(
                text = "Create resource", style = MaterialTheme.typography.h4
            )
        }
        Text(
            text = "Ingredient: ",
            style = MaterialTheme.typography.h5
        )
        if (ingredient != null) {
            Button(onClick = onIngredientSearch) {
                Row {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Text(ingredient.name + " (measured in " + ingredient.measureUnit.name + ")")
                }
            }
        } else {
            Button(onClick = onIngredientSearch) {
                Row {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Text(text = "search")
                }
            }
        }
        Row {
            Text(
                text = "Amount: ",
                style = MaterialTheme.typography.h5
            )
            TextField(
                amountText.value,
                onValueChange = { amountText.value = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
internal fun DynamicSaveButton(amount: Double?, ingredient: Ingredient?, onCreation: (Ingredient, Double) -> Unit) {
    if (amount != null && ingredient != null) {
        Button(onClick = {
            onCreation(ingredient, amount)
        }) {
            Text("Save")
        }
    }
}

@Composable
internal fun CreateResource(
    searchForIngredient: (SearchRequest) -> List<SearchResult>,
    onCancel: () -> Unit,
    onCreation: (Ingredient, Double) -> Unit,
) {
    val state = remember { mutableStateOf(ResourceCreationState.FILLING_FORM) }
    val amountText = remember { mutableStateOf("") }
    val chosenIngredient = remember { mutableStateOf<Ingredient?>(null) }

    val searchResults = remember { mutableStateListOf<SearchResult>() }
    Column(modifier = Modifier.fillMaxSize()) {
        when (state.value) {
            ResourceCreationState.FILLING_FORM -> {
                Button(onClick = onCancel) {
                    Text("Back")
                }
                CreateResourceForm(chosenIngredient.value, amountText) {
                    state.value = ResourceCreationState.INGREDIENT_SEARCH
                }
                DynamicSaveButton(amountText.value.toDoubleOrNull(), chosenIngredient.value, onCreation)
            }

            ResourceCreationState.INGREDIENT_SEARCH -> {
                Button(onClick = { state.value = ResourceCreationState.FILLING_FORM }) {
                    Text("Back")
                }
                Button(onClick = {
                    state.value = ResourceCreationState.INGREDIENT_CREATION
                }) {
                    Text("Create ingredient")
                }
                SearchField(SearchType.Ingredients) {
                    searchResults.clear()
                    searchResults.addAll(searchForIngredient(it))
                }
                SearchResultsList(searchResults) {
                    require(it is IngredientSearchResult)
                    chosenIngredient.value = it.ingredient
                    state.value = ResourceCreationState.FILLING_FORM
                    searchResults.clear()
                }
            }

            ResourceCreationState.INGREDIENT_CREATION -> {
                Button(onClick = { state.value = ResourceCreationState.FILLING_FORM }) {
                    Text("Back")
                }
                CreateIngredient {
                    chosenIngredient.value = it
                    state.value = ResourceCreationState.FILLING_FORM
                }
            }
        }
    }
}
