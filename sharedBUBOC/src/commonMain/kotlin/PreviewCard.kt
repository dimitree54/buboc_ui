import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import entry.Instance


@Composable
internal fun InstancePreview(result: Instance, onClick: () -> Unit) {
    Button(
        onClick,
        Modifier.fillMaxWidth(),
    ) {
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = result.icon,
                contentDescription = "Result icon",
                tint = Color.Black
            )
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