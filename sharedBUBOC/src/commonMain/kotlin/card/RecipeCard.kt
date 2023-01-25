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
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cuboc.recipe.Recipe
import entry.RecipeInputInstance
import entry.RecipeOutputInstance


@Composable
internal fun RecipeCard(recipe: Recipe, state: MutableList<Any>) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { state.removeLast() }) {
            Text("Back")
        }
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Default.List,
                contentDescription = "Result icon",
                tint = Color.Black
            )
            Text(
                text = "Recipe", style = MaterialTheme.typography.h4
            )
        }
        Text(
            text = recipe.name, style = MaterialTheme.typography.h5
        )
        Text(text = "Duration:")
        Text(text = recipe.instruction.durationMinutes.toString() + " minutes")
        Text(text = "Inputs:")
        recipe.inputs.forEach {
            InstancePreview(RecipeInputInstance(it)) {
                state.add(it)
            }
        }
        Text(text = "Instructions:")
        Text(text = recipe.instruction.text)
        Text(text = "Outputs:")
        recipe.outputs.forEach {
            InstancePreview(RecipeOutputInstance(it)) {
                state.add(it)
            }
        }
    }
}