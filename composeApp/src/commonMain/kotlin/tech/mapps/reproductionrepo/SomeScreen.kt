package tech.mapps.reproductionrepo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import tech.mapps.reproductionrepo.parcelize.CommonParcelize

@CommonParcelize
object SomeScreen : Screen {
    sealed interface Event : CircuitUiEvent

    data class State(val eventSink: (Event) -> Unit) : CircuitUiState
}

@Inject
@CircuitInject(SomeScreen::class, AppScope::class)
class ScreenAPresenter : Presenter<SomeScreen.State> {

    @Composable
    override fun present(): SomeScreen.State = SomeScreen.State {}
}

@OptIn(ExperimentalMaterial3Api::class)
@CircuitInject(SomeScreen::class, AppScope::class)
@Composable
fun SomeScreenUi(
    state: SomeScreen.State,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Some Screen") },
            )
        },
        content = { paddingValues ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(paddingValues),
            ) {
                Text(text = state.toString())
            }
        },
        modifier = modifier,
    )
}