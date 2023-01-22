import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal inline fun SearchResults(results: List<SearchResult>) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState,
    ) {
        results.forEach { result ->
            item(key = result.id) {
                SearchResultCard(result)
            }
        }
    }
}

@Composable
private fun SearchResultCard(result: SearchResult) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.padding(4.dp).fillMaxWidth(), shape = RoundedCornerShape(size = 20.dp), elevation = 8.dp
        ) {
            Box(
                Modifier.background(brush = Brush.horizontalGradient(listOf(Color.White, result.color))).padding(10.dp),
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = result.icon,
                        contentDescription = "Result icon",
                        tint = Color.Black
                    )
                    Spacer(Modifier.size(8.dp))
                    Column {
                        Text(
                            text = result.name, style = MaterialTheme.typography.h5
                        )
                        Text(
                            text = result.info
                        )
                    }
                }
            }
        }
    }
}
