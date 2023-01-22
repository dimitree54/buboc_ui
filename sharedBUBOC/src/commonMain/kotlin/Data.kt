import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import cuboc.ingredient.Resource
import cuboc.recipe.Recipe
import kotlin.random.Random

sealed interface SearchResult{
    val id: String
    val name: String
    val info: String
    val color: Color
    val icon: ImageVector
}

class ResourceSearchResult(
    resource: Resource
) : SearchResult{
    override val id = Random.nextLong().toString()
    override val name = resource.ingredient.name
    override val info = "${resource.amount} ${resource.ingredient.measureUnit.name}"
    override val color = Color.Blue
    override val icon = Icons.Default.ShoppingCart
}


class RecipeSearchResult(
    recipe: Recipe
) : SearchResult{
    override val id = Random.nextLong().toString()
    override val name = recipe.name
    override val info = "${recipe.instruction.durationMinutes} min"
    override val color = Color.Red
    override val icon = Icons.Default.List
}
