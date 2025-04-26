package com.example.blaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.blaze.ui.theme.BlazeTheme
import com.example.blaze.Screen
import com.example.blaze.BottomNavBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlazeTheme {
                AppNavigation()
            }
        }
    }
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tasks.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Tasks.route) { TasksScreen() }
            composable(Screen.Health.route) { HealthScreen() }
            composable(Screen.Social.route) { SocialScreen() }
        }
    }
}

// Screens

@Composable
fun TasksScreen() {
    MainTaskScreen()
}

@Composable
fun HealthScreen() {
    var tasksHealth by remember {
        mutableStateOf(
            listOf(
                Task(1, "Drink 8 glasses of water", false),
                Task(2, "Walk 10,000 steps", false)
            )
        )
    }
    var isDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogVisible = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (tasksHealth.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No Health Tasks Available")
                }
            } else {
                TaskList(
                    tasks = tasksHealth,
                    onComplete = { task ->
                        tasksHealth = tasksHealth.map {
                            if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    },
                    onDelete = { task ->
                        tasksHealth = tasksHealth.filter { it.id != task.id }
                    }
                )
            }

            if (isDialogVisible) {
                AddTaskDialog(
                    onAddTask = { taskTitle ->
                        tasksHealth = tasksHealth + Task(tasksHealth.size + 1, taskTitle, false)
                        isDialogVisible = false
                    },
                    onDismiss = { isDialogVisible = false }
                )
            }
        }
    }
}

@Composable
fun SocialScreen() {
    var tasksSoc by remember {
        mutableStateOf(
            listOf(
                Task(1, "Call Mom", false)
            )
        )
    }
    var isDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogVisible = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (tasksSoc.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No Tasks Available")
                }
            } else {
                TaskList(
                    tasks = tasksSoc,
                    onComplete = { task ->
                        tasksSoc = tasksSoc.map {
                            if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    },
                    onDelete = { task ->
                        tasksSoc = tasksSoc.filter { it.id != task.id }
                    }
                )
            }

            if (isDialogVisible) {
                AddTaskDialog(
                    onAddTask = { taskTitle ->
                        tasksSoc = tasksSoc + Task(tasksSoc.size + 1, taskTitle, false)
                        isDialogVisible = false
                    },
                    onDismiss = { isDialogVisible = false }
                )
            }
        }
    }
}


// Task-related UI

@Composable
fun TaskItem(task: Task, onComplete: (Task) -> Unit, onDelete: (Task) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = task.title,
            modifier = Modifier.weight(1f),
            color = if (task.isCompleted) Color.Gray else Color.Unspecified
        )
        IconButton(onClick = { onComplete(task) }) {
            Icon(
                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.AddCircle,
                contentDescription = "Toggle completion"
            )
        }
        IconButton(onClick = { onDelete(task) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete task"
            )
        }
    }
}

@Composable
fun TaskList(tasks: List<Task>, onComplete: (Task) -> Unit, onDelete: (Task) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks) { task ->
            TaskItem(task = task, onComplete = onComplete, onDelete = onDelete)
        }
    }
}

@Composable
fun AddTaskDialog(onAddTask: (String) -> Unit, onDismiss: () -> Unit) {
    var taskTitle by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Add New Task") },
        text = {
            TextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("Task Title") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (taskTitle.isNotBlank()) {
                        onAddTask(taskTitle)
                        taskTitle = ""
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MainTaskScreen() {
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(1, "Welcome!", false)
            )
        )
    }
    var isDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogVisible = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No Tasks Available")
                }
            } else {
                TaskList(
                    tasks = tasks,
                    onComplete = { task ->
                        tasks = tasks.map {
                            if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
                        }
                    },
                    onDelete = { task ->
                        tasks = tasks.filter { it.id != task.id }
                    }
                )
            }
        }

        if (isDialogVisible) {
            AddTaskDialog(
                onAddTask = { title ->
                    tasks = tasks + Task(tasks.size + 1, title, false)
                    isDialogVisible = false
                },
                onDismiss = { isDialogVisible = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    BlazeTheme {
        MainTaskScreen()
    }
}
