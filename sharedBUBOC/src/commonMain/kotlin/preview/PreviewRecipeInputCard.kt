package preview

import RECIPE_INPUT_ICON
import androidx.compose.runtime.Composable
import cuboc.ingredient.RecipeInput


@Composable
internal fun PreviewRecipeInputCard(recipeInput: RecipeInput) {
    PreviewCard(
        recipeInput.ingredient.name.toString(),
        recipeInput.amount.toString() + " " + recipeInput.ingredient.measureUnit,
        RECIPE_INPUT_ICON
    )
}
