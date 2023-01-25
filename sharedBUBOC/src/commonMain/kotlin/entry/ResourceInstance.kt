package entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import cuboc.ingredient.Resource
import kotlin.random.Random

class ResourceInstance(
    resource: Resource
) : Instance {
    override val obj = resource
    override val id = Random.nextLong().toString()
    override val name = resource.ingredient.name
    override val info = "${resource.amount} ${resource.ingredient.measureUnit.name}"
    override val color = Color.Blue
    override val icon = Icons.Default.ShoppingCart
}