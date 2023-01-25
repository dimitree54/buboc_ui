package entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.ui.graphics.Color
import cuboc.ingredient.RecipeInput
import kotlin.random.Random

class RecipeInputInstance(
    recipeInput: RecipeInput
) : Instance {
    override val obj = recipeInput
    override val id = Random.nextLong().toString()
    override val name = recipeInput.ingredient.name
    override val info = "${recipeInput.amount} ${recipeInput.ingredient.measureUnit.name}"
    override val color = Color.Blue
    override val icon = Icons.Default.KeyboardArrowDown
}