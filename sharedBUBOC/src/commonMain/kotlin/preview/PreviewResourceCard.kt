package preview

import RESOURCE_ICON
import androidx.compose.runtime.Composable
import cuboc.ingredient.UserResource


@Composable
internal fun PreviewResourceCard(resource: UserResource) {
    PreviewCard(
        resource.resource.ingredient.name.toString(),
        resource.resource.amount.toString() + " " + resource.resource.ingredient.measureUnit,
        RESOURCE_ICON
    )
}
