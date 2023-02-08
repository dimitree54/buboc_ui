package preview

import RESOURCE_ICON
import androidx.compose.runtime.Composable
import cuboc_core.cuboc.ingredient.UserResource


@Composable
internal fun PreviewResourceCard(resource: UserResource) {
    PreviewCard(
        resource.ingredient.name.toString(),
        resource.amount.toString() + " " + resource.ingredient.measureUnit,
        RESOURCE_ICON
    )
}
