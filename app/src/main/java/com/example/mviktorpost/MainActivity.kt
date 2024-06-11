package com.example.mviktorpost

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mviktorpost.ui.theme.MviKtorPostTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<PostViewModel>()

        enableEdgeToEdge()
        setContent {
            MviKtorPostTheme {
                val state by viewModel.collectAsState()
                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        items(state.posts) { post ->
                            Column {
                                Text(text = post.title, style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp))
                                Text(text = post.body, style = MaterialTheme.typography.titleSmall.copy(fontSize = 10.sp))
                            }
                        }
                    }

                    if (state.progressBar) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    viewModel.collectSideEffect { uiComponent ->
                        when(uiComponent) {
                            is UIComponent.Toast -> {
                                Toast.makeText(context, uiComponent.text, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}