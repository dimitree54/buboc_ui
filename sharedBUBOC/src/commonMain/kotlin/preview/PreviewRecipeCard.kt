package preview

import RECIPE_ICON
import androidx.compose.runtime.Composable
import cuboc.recipe.Recipe


@Composable
internal fun PreviewRecipeCard(recipe: Recipe) {
    PreviewCard(
        recipe.name.toString(),
        "duration: " + recipe.instruction.durationMinutes + " minutes",
        RECIPE_ICON
    )
}
