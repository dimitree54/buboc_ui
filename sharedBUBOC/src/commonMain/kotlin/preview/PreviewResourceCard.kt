package preview

import RESOURCE_ICON
import androidx.compose.runtime.Composable
import cuboc_core.cuboc.database.UserResource


@Composable
internal fun PreviewResourceCard(resource: UserResource) {
    PreviewCard(
        resource.ingredient.name,
        resource.amount.toString() + " " + resource.ingredient.measureUnit.name.name,
        RESOURCE_ICON
    )
}
