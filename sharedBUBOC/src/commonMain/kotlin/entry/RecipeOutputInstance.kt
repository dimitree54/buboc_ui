package entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import cuboc.ingredient.RecipeInput
import cuboc.ingredient.RecipeOutput
import cuboc.ingredient.Resource
import kotlin.random.Random

class RecipeOutputInstance(
    recipeOutput: RecipeOutput
) : Instance {
    override val obj = recipeOutput
    override val id = Random.nextLong().toString()
    override val name = recipeOutput.ingredient.name
    override val info = "${recipeOutput.amount} ${recipeOutput.ingredient.measureUnit.name}"
    override val color = Color.Blue
    override val icon = Icons.Default.KeyboardArrowUp
}