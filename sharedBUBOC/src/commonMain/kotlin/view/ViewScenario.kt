package view

import REQUEST_ICON
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cuboc.recipe.ComplexRecipe
import cuboc.recipe.Recipe
import cuboc.recipe.Scenario
import preview.PreviewPieceOfResource
import preview.PreviewRecipeCard


@Composable
internal fun PreviewComplexRecipe(recipe: Recipe) {
    when (recipe) {
        // is TrivialRecipe -> {}
        is ComplexRecipe -> {
            for (stage in recipe.stages) {
                PreviewComplexRecipe(stage)
            }
        }

        else -> PreviewRecipeCard(recipe)
    }
}


@Composable
internal fun ViewScenario(
    scenario: Scenario,
    onRequest: () -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = REQUEST_ICON,
                contentDescription = null,
                tint = Color.Black
            )
            Text(
                text = "Request", style = MaterialTheme.typography.h3
            )
        }
        Text(
            text = "Requesting ${scenario.request.amount} ${scenario.request.ingredient.measureUnit.name} of ${scenario.request.ingredient.name}",
            style = MaterialTheme.typography.h5
        )
        Text(text = "Input resources:", style = MaterialTheme.typography.h5)
        for (pieceOfResource in scenario.resources.values.flatten()) {
            PreviewPieceOfResource(pieceOfResource)
        }
        Text(text = "Scenario:", style = MaterialTheme.typography.h5)
        PreviewComplexRecipe(scenario.recipe)
        Button(
            shape = RoundedCornerShape(50),
            onClick = onRequest,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
        ) {
            Text("Request")
        }
    }
}
