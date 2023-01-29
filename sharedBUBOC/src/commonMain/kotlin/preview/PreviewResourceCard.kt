package preview

import RESOURCE_ICON
import androidx.compose.runtime.Composable
import cuboc.ingredient.Resource


@Composable
internal fun PreviewResourceCard(resource: Resource) {
    PreviewCard(
        resource.ingredient.name,
        resource.amount.toString() + " " + resource.ingredient.measureUnit.name,
        RESOURCE_ICON
    )
}
