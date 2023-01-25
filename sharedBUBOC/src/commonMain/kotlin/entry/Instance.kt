package entry

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

interface Instance {
    val obj: Any
    val id: String
    val name: String
    val info: String
    val color: Color
    val icon: ImageVector
}

