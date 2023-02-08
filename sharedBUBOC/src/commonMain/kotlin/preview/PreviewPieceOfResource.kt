package preview

import RECIPE_INPUT_ICON
import androidx.compose.runtime.Composable
import cuboc.ingredient.PieceOfUserResource


@Composable
internal fun PreviewPieceOfResource(pieceOfResource: PieceOfUserResource) {
    PreviewCard(
        pieceOfResource.resource.resource.ingredient.name.toString(),
        pieceOfResource.amount.toString() + " " + pieceOfResource.resource.resource.ingredient.measureUnit,
        RECIPE_INPUT_ICON
    )
}
