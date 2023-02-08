package preview

import RECIPE_INPUT_ICON
import androidx.compose.runtime.Composable
import cuboc.ingredient.PieceOfResource


@Composable
internal fun PreviewPieceOfResource(pieceOfResource: PieceOfResource) {
    PreviewCard(
        pieceOfResource.resource.ingredient.name.toString(),
        pieceOfResource.amount.toString() + " " + pieceOfResource.resource.ingredient.measureUnit,
        RECIPE_INPUT_ICON
    )
}
