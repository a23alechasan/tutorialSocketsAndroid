package com.exemple.tutorialsocketsandroid

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ControladorApp(socket: Socket) {
    var estat by remember { mutableStateOf("Esperant...") }
    var imatgeUrl by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        socket.on("resposta") { args ->
            coroutineScope.launch(Dispatchers.Main) {
                if (args.isNotEmpty()) {
                    val resposta = args[0].toString()
                    if (resposta.startsWith("/Imatges/")) {
                        imatgeUrl = "http://tutorialsockets.dam.inspedralbes.cat:29888$resposta"
                    } else {
                        imatgeUrl = null
                        estat = when (resposta) {
                            "1" -> "Encès"
                            "0" -> "Apagat"
                            else -> "Resposta: $resposta"
                        }
                    }
                }
            }
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { socket.emit("missatge", "encendre") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Encendre", color = Color.White)
            }

            Button(
                onClick = { socket.emit("missatge", "apagar") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Apagar", color = Color.White)
            }

            Button(
                onClick = { socket.emit("missatge", "imatge") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text("Nova imatge", color = Color.White)
            }

            Text(estat, style = MaterialTheme.typography.headlineSmall)

            imatgeUrl?.let { url ->
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = "Imatge del servidor",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}
