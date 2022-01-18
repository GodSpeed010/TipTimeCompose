package com.example.tipcalccompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tipcalccompose.ui.theme.TipCalcComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalcComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    CalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun CalculatorLayout() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        var costState by remember {
            mutableStateOf("")
        }
        val percentOptions = listOf("Amazing (20%)", "Good (18%)", "OK (15%)")
        var selectedOption by remember {
            mutableStateOf(percentOptions[0])
        }
        var tipOutput: String by remember {
            mutableStateOf("")
        }

        //Input Field for Total Cost
        TextField(
            value = costState,
            label = { Text("Enter the cost")},
            onValueChange = { costState = it }
        )

        //Displays text with question
        ServiceQuestion()

        //Tip Percent Radio Buttons
        TipPercentages(percentOptions, selectedOption) {
            //update value of selectedOption
            selectedOption = it
        }


        //Calculate Button
        Button(
            onClick = {
                val tipPercent: Double = selectedOption.filter { it.isDigit() }.toDouble()
                tipOutput = calculateTip(costState.toDouble(), tipPercent)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Calculate", fontWeight = FontWeight.Bold)
        }

        //Tip Amount Output
        Text(text = "$tipOutput", modifier = Modifier.align(Alignment.End))
    }
}

@Composable
fun ServiceQuestion() {
    Spacer(modifier = Modifier.height(16.dp))
    Text("How was the service?")
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun TipPercentages(
    percentOptions: List<String>,
    selectedOption: String,
    updateSelectedOption: (String) -> Unit
) {
    Column {
        percentOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { updateSelectedOption(text) }
                    )
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { updateSelectedOption(text) }
                )

                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

fun calculateTip(cost: Double, tipPercent: Double): String {
    return (cost * (tipPercent / 100)).toString()
}
