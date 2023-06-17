import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskListScreen()
        }
    }

    @Composable
    fun TaskListScreen() {
        val tasks = remember { mutableStateListOf<Task>() }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TaskInput(tasks)
            Spacer(modifier = Modifier.height(16.dp))
            TaskList(tasks)
        }
    }

    @Composable
    fun TaskInput(tasks: MutableList<Task>) {
        val newTaskText = remember { mutableStateOf("") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newTaskText.value,
                onValueChange = { newTaskText.value = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.weight(1f),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (newTaskText.value.isNotEmpty()) {
                            tasks.add(Task(newTaskText.value, false))
                            newTaskText.value = ""
                        }
                    }
                )
            )
            Button(
                onClick = {
                    if (newTaskText.value.isNotEmpty()) {
                        tasks.add(Task(newTaskText.value, false))
                        newTaskText.value = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Agregar")
            }

        }
        Button(
            onClick = {
                tasks.removeAll { it.completed }
            },

            ) {
            Text("Eliminar completadas")
        }
    }

    @Composable
    fun TaskList(tasks: MutableList<Task>) {
        LazyColumn {
            items(tasks) { task ->
                TaskItem(task) {
                    tasks.remove(task)
                }
            }
        }


    }

    @Composable
    fun TaskItem(task: Task, onTaskRemoved: () -> Unit) {
        var checkedState by remember { mutableStateOf(task.completed) }
        LaunchedEffect(task.completed) {
            checkedState = task.completed
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Checkbox(
                checked = checkedState,
                onCheckedChange = { isChecked ->
                    checkedState = isChecked
                    task.completed = isChecked
                },
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(text = task.text)
        }
    }

    data class Task(val text: String, var completed: Boolean)

}

@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    MainActivity().TaskListScreen()
}