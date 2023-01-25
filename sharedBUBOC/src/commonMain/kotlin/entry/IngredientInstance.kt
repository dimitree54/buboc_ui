package entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import cuboc.ingredient.Ingredient
import kotlin.random.Random

class IngredientInstance(
    ingredient: Ingredient
) : Instance {
    override val obj = ingredient
    override val id = Random.nextLong().toString()
    override val name = ingredient.name
    override val info = "Measured in ${ingredient.measureUnit.name}"
    override val color = Color.Green
    override val icon = Icons.Default.Star
}