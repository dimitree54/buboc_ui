package preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
internal fun PreviewCard(name: String, info: String, icon: ImageVector) {
    Row {
        Icon(
            modifier = Modifier.size(50.dp),
            imageVector = icon,
            contentDescription = "Result icon",
            tint = Color.Black
        )
        Column {
            Text(
                text = name, style = MaterialTheme.typography.h5
            )
            Text(
                text = info
            )
        }
    }
}
