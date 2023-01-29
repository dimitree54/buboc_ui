package view

import RECIPE_OUTPUT_ICON
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
import cuboc.ingredient.RecipeOutput
import preview.PreviewIngredientCard


@Composable
internal fun ViewRecipeOutput(recipeOutput: RecipeOutput) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = RECIPE_OUTPUT_ICON,
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
        PreviewIngredientCard(recipeOutput.ingredient)
    }
}