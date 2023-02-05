package preview

import RECIPE_ICON
import androidx.compose.runtime.Composable
import cuboc_core.cuboc.database.UserRecipe


@Composable
internal fun PreviewRecipeCard(recipe: UserRecipe) {
    PreviewCard(recipe.name, "duration: " + recipe.instruction.durationMinutes + " minutes", RECIPE_ICON)
}
