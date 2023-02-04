package search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cuboc_core.cuboc.database.search.IngredientSearchResult
import cuboc_core.cuboc.database.search.RecipeSearchResult
import cuboc_core.cuboc.database.search.ResourceSearchResult
import cuboc_core.cuboc.database.search.SearchResult
import preview.PreviewIngredientCard
import preview.PreviewRecipeCard
import preview.PreviewResourceCard


@Composable
internal fun SearchResultsList(
    results: List<SearchResult>,
    state: LazyListState = rememberLazyListState(),
    resultChosen: (SearchResult) -> Unit
) {
    if (results.isEmpty()) {
        Text("No results found", modifier = Modifier.fillMaxSize().padding(8.dp))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = state,
        ) {
            results.forEachIndexed { index, result ->
                item(key = index) {
                    Button(
                        onClick = { resultChosen(result) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(25),
                    ) {
                        when (result) {
                            is IngredientSearchResult -> PreviewIngredientCard(result.ingredient)
                            is RecipeSearchResult -> PreviewRecipeCard(result.recipe)
                            is ResourceSearchResult -> PreviewResourceCard(result.resource)
                        }
                    }
                }
            }
        }
    }
}

