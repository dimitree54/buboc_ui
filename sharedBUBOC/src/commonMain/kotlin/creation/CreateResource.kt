package creation

import BackButton
import RESOURCE_ICON
import SaveButton
import SearchOrCreateIngredient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cuboc.ingredient.Ingredient
import cuboc.ingredient.ResourcePrototype
import cuboc_core.cuboc.database.search.SearchRequest
import cuboc_core.cuboc.database.search.SearchResult
import kotlin.reflect.KSuspendFunction1

enum class ResourceCreationState {
    FILLING_FORM,
    REQUEST_INGREDIENT
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
                text = "Create resource", style = MaterialTheme.typography.h3
            )
        }
        Text(
            text = "Ingredient: ", style = MaterialTheme.typography.h5
        )
        if (ingredient != null) {
            Button(
                shape = RoundedCornerShape(50),
                onClick = onIngredientSearch
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                    )
                    Text(ingredient.name, style = MaterialTheme.typography.h6)
                }
            }
            Text(
                text = "Amount(in ${ingredient.measureUnit.name}): ",
                style = MaterialTheme.typography.h5
            )
            TextField(
                amountText.value,
                modifier = Modifier.width(60.dp),
                singleLine = true,
                shape = RoundedCornerShape(25),
                onValueChange = { amountText.value = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        } else {
            Button(
                shape = RoundedCornerShape(50),
                onClick = onIngredientSearch
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                    Text(text = "Search", style = MaterialTheme.typography.h6)
                }
            }
        }
    }
}

@Composable
internal fun CreateResource(
    searchForIngredient: KSuspendFunction1<SearchRequest, List<SearchResult>>,
    onCancel: () -> Unit,
    onCreation: (ResourcePrototype) -> Unit,
) {
    val state = remember { mutableStateOf(ResourceCreationState.FILLING_FORM) }
    val amountText = remember { mutableStateOf("") }
    val chosenIngredient = remember { mutableStateOf<Ingredient?>(null) }
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        when (state.value) {
            ResourceCreationState.FILLING_FORM -> {
                BackButton(onCancel)
                CreateResourceForm(chosenIngredient.value, amountText) {
                    state.value = ResourceCreationState.REQUEST_INGREDIENT
                }
                val amount = amountText.value.toDoubleOrNull()
                val readyToSave = amount != null && amount > 0 && chosenIngredient.value != null
                SaveButton(readyToSave) {
                    val newResource = ResourcePrototype(chosenIngredient.value!!, amount!!)
                    onCreation(newResource)
                }
            }

            ResourceCreationState.REQUEST_INGREDIENT -> SearchOrCreateIngredient(
                searchForIngredient,
                onCancel = { state.value = ResourceCreationState.FILLING_FORM }
            ) {
                chosenIngredient.value = it
                state.value = ResourceCreationState.FILLING_FORM
            }
        }
    }
}
