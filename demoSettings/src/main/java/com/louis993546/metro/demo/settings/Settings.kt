package com.louis993546.metro.demo.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.louis993546.metro.Pivot
import com.louis993546.metro.PivotTitle
import com.louis993546.metro.Text

@ExperimentalFoundationApi
@Composable
fun Settings(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PivotTitle(title = "Settings")
        Pivot(pageTitles = listOf("system", "applications")) { pageNumber ->
            when (pageNumber) {
                0 -> SystemPage()
                1 -> ApplicationsPage()
            }
        }
    }
}

@Composable
fun SystemPage(
    modifier: Modifier = Modifier,
) {
    Text(text = "TBD", modifier = modifier)
}

@Composable
fun ApplicationsPage(
    modifier: Modifier = Modifier,
) {
    Text(text = "TBD", modifier = modifier)
}
