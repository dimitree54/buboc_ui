package preview

import RECIPE_OUTPUT_ICON
import androidx.compose.runtime.Composable
import cuboc.ingredient.RecipeOutput


@Composable
internal fun PreviewRecipeOutputCard(recipeOutput: RecipeOutput) {
    PreviewCard(
        recipeOutput.ingredient.name,
        recipeOutput.amount.toString() + " " + recipeOutput.ingredient.measureUnit,
        RECIPE_OUTPUT_ICON
    )
}
