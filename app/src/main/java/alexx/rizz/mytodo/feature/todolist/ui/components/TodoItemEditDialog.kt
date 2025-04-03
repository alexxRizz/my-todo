package alexx.rizz.mytodo.feature.todolist.ui.components

import alexx.rizz.mytodo.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*

@Composable
fun TodoItemEditDialog(
  title: String,
  text: String,
  isDeleteVisible: Boolean,
  onTextChanged: (String) -> Unit = {},
  onCancel: () -> Unit = {},
  onOk: (String) -> Unit = {},
  onDelete: () -> Unit = {},
) {
  Dialog(
    onDismissRequest = onCancel,
    DialogProperties(
      dismissOnClickOutside = false,
      usePlatformDefaultWidth = false,
    )
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(0.92f),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MyColors.Background)
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .fillMaxWidth(),
      ) {
        Row {
          Title(title)
          if (isDeleteVisible) {
            Spacer(Modifier.weight(1f))
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
              IconButton(onDelete, modifier = Modifier.offset(7.dp, (-10).dp)) {
                  Icon(Icons.Default.Delete, null, tint = Color.Red)
              }
            }
          }
        }
        val inputState = rememberSaveable { mutableStateOf(text) }
        DescriptionTextField(inputState, onTextChanged)
        DialogButtons(inputState, onCancel, onOk)
      }
    }
  }
}

@Composable
private fun Title(title: String) {
  Text(
    text = title,
    fontSize = 20.sp
  )
}

@Composable
private fun DescriptionTextField(input: MutableState<String>, onTextChanged: (String) -> Unit) {
  val focusRequester = remember { FocusRequester() }
  OutlinedTextField(
    value = input.value,
    onValueChange = {
      input.value = it
      onTextChanged(it)
    },
    maxLines = 3,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    label = { Text("Введите описание") },
    modifier = Modifier
      .focusRequester(focusRequester)
      .padding(top = 20.dp, bottom = 30.dp)
      .fillMaxWidth(),
  )
  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}

@Composable
private fun DialogButtons(inputState: MutableState<String>, onCancel: () -> Unit, onOk: (String) -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    val modifiers = Modifier
      .weight(1f)
    val shape = RoundedCornerShape(10.dp)
    OutlinedButton(
      onClick = onCancel,
      modifier = modifiers,
      shape = shape,
    ) {
      Text("Отмена")
    }
    Button(
      onClick = { onOk(inputState.value) },
      modifier = modifiers,
      shape = shape,
    ) {
      Text("ОК")
    }
  }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun TodoItemEditDialogPreview() {
  MyTodoTheme {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier.fillMaxSize(),
    ) {
      TodoItemEditDialog("Редактирование пункта", "", isDeleteVisible = true)
    }
  }
}
