package preview

import INGREDIENT_ICON
import androidx.compose.runtime.Composable
import cuboc.ingredient.Ingredient


@Composable
internal fun PreviewIngredientCard(ingredient: Ingredient) {
    PreviewCard(ingredient.name, "measure unit: " + ingredient.measureUnit, INGREDIENT_ICON)
}
