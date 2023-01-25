package card

import InstancePreview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cuboc.ingredient.RecipeOutput
import entry.IngredientInstance


@Composable
internal fun RecipeOutputCard(recipeOutput: RecipeOutput, state: MutableList<Any>) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { state.removeLast() }) {
            Text("Back")
        }
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Result icon",
                tint = Color.Black
            )
            Text(
                text = "Recipe output", style = MaterialTheme.typography.h4
            )
        }
        Text(
            text = recipeOutput.ingredient.name, style = MaterialTheme.typography.h5
        )
        Text(recipeOutput.amount.toString() + " " + recipeOutput.ingredient.measureUnit.name)
        Text("Ingredient: ")
        InstancePreview(IngredientInstance(recipeOutput.ingredient)) {
            state.add(recipeOutput.ingredient)
        }
    }
}