package card

import InstancePreview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cuboc.ingredient.RecipeInput
import entry.IngredientInstance


@Composable
internal fun RecipeInputCard(recipeInput: RecipeInput, state: MutableList<Any>) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { state.removeLast() }) {
            Text("Back")
        }
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Result icon",
                tint = Color.Black
            )
            Text(
                text = "Recipe input", style = MaterialTheme.typography.h4
            )
        }
        Text(
            text = recipeInput.ingredient.name, style = MaterialTheme.typography.h5
        )
        Text(recipeInput.amount.toString() + " " + recipeInput.ingredient.measureUnit.name)
        Text("Ingredient: ")
        InstancePreview(IngredientInstance(recipeInput.ingredient)) {
            state.add(recipeInput.ingredient)
        }
    }
}