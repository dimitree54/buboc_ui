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
import cuboc.ingredient.Resource
import cuboc.scenario.Scenario
import preview.PreviewRecipeCard
import preview.PreviewResourceCard


@Composable
internal fun ViewRequestAndScenario(
    request: Resource,
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
            text = "Requesting ${request.amount} ${request.ingredient.measureUnit} of ${request.ingredient.name}",
            style = MaterialTheme.typography.h5
        )
        Text(text = "Input resources:", style = MaterialTheme.typography.h5)
        for (resource in scenario.externalResourcesRequired.values.flatten()) {
            PreviewResourceCard(resource)
        }
        Text(text = "Scenario:", style = MaterialTheme.typography.h5)
        for (stage in scenario.stages) {
            PreviewRecipeCard(stage.recipe)
        }
        Button(
            shape = RoundedCornerShape(50),
            onClick = onRequest,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
        ) {
            Text("Request")
        }
    }
}
