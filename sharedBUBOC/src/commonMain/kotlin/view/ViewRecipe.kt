package view

import RECIPE_ICON
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cuboc.recipe.Recipe
import preview.PreviewRecipeInputCard
import preview.PreviewRecipeOutputCard


@Composable
internal fun ViewRecipe(recipe: Recipe) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = RECIPE_ICON,
                contentDescription = null,
                tint = Color.Black
            )
            Text(
                text = "Recipe", style = MaterialTheme.typography.h4
            )
        }
        Text(
            text = recipe.name, style = MaterialTheme.typography.h5
        )
        Text(text = "Inputs:")
        recipe.inputs.forEach {
            PreviewRecipeInputCard(it)
        }
        Text(text = "Instructions:")
        Text(text = recipe.instruction.text)
        Text(text = "Duration:")
        Text(text = recipe.instruction.durationMinutes.toString() + " minutes")
        Text(text = "Outputs:")
        recipe.outputs.forEach {
            PreviewRecipeOutputCard(it)
        }
    }
}
