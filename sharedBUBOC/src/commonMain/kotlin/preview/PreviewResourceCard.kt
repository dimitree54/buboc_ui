package preview

import RESOURCE_ICON
import androidx.compose.runtime.Composable
import cuboc.ingredient.Resource


@Composable
internal fun PreviewResourceCard(resource: Resource) {
    PreviewCard(
        resource.ingredient.name.toString(),
        resource.amount.toString() + " " + resource.ingredient.measureUnit,
        RESOURCE_ICON
    )
}
