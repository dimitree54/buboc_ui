package entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.Color
import cuboc.recipe.Recipe
import kotlin.random.Random

class RecipeInstance(
    recipe: Recipe
) : Instance {
    override val obj = recipe
    override val id = Random.nextLong().toString()
    override val name = recipe.name
    override val info = "${recipe.instruction.durationMinutes} min"
    override val color = Color.Red
    override val icon = Icons.Default.List
}