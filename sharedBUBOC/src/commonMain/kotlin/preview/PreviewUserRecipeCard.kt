package preview

import RECIPE_ICON
import androidx.compose.runtime.Composable
import cuboc.recipe.UserRecipe


@Composable
internal fun PreviewUserRecipeCard(userRecipe: UserRecipe) {
    PreviewCard(
        userRecipe.recipe.name.toString(),
        "duration: " + userRecipe.recipe.instruction.durationMinutes + " minutes",
        RECIPE_ICON
    )
}
