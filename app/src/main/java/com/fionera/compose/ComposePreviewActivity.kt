package com.fionera.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fionera.R
import com.fionera.compose.ui.theme.BehaviorChangesTheme

class ComposePreviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BehaviorChangesTheme {
                ContentView(Message("Author", "Message"))
            }
        }
    }
}

data class Message(val author: String, val text: String)

@Composable
fun ContentView(msg: Message) {
    LazyColumn {
        items(3) {
            MessageRow(msg)
        }
    }
}

@Composable
fun MessageRow(msg: Message) {
    Row(
        modifier = Modifier.padding(8.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
        ) {
            Text(
                text = msg.author,
                color = MaterialTheme.colors.secondary,
                style = MaterialTheme.typography.caption,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 2.dp,
            ) {
                Text(
                    text = msg.text,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContentView() {
    BehaviorChangesTheme {
        ContentView(Message("Author", "Message"))
    }
}
