package logic

data class SearchRequest(
    val query: String?, val type: SearchType, val allowQueryChange: Boolean = true
)