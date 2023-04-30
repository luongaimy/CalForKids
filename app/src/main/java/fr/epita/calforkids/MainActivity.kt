package fr.epita.calforkids

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var operand1: TextView
    private lateinit var operand2: TextView
    private lateinit var selectedOperation: ImageView
    private lateinit var result: TextView
    private lateinit var addButton: ImageView
    private lateinit var subtractButton: ImageView
    private lateinit var multiplyButton: ImageView
    private lateinit var divideButton: ImageView
    private lateinit var equalsButton: Button
    private lateinit var numberButtons: List<Button>
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handleOrientation()
    }

    private fun handleOrientation() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Initialize your calculator components here
            initializeCalculatorComponents()
            setNumberButtonClickListeners()
            setOperationClickListeners()
            setEqualsClickListener()
            setResetClickListener()
            //fetchAndDisplayAd()
        }
    }

    private fun initializeCalculatorComponents() {
        this.operand1 = findViewById(R.id.operand1)
        this.operand2 = findViewById(R.id.operand2)
        this.selectedOperation = findViewById(R.id.selected_operation)
        this.result = findViewById(R.id.result)
        this.addButton = findViewById(R.id.add_operation)
        this.subtractButton = findViewById(R.id.minus_operation)
        this.multiplyButton = findViewById(R.id.multiple_operation)
        this.divideButton = findViewById(R.id.div_operation)
        this.equalsButton = findViewById(R.id.equals_button)
        this.resetButton = findViewById(R.id.reset_button)

        numberButtons = listOf(
            findViewById(R.id.button0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9)
        )
    }

    private fun setNumberButtonClickListeners() {
        this.numberButtons.forEach { button ->
            button.setOnClickListener { handleNumberButtonClick(it) }
        }
    }

    private fun setOperationClickListeners() {
        addButton.setOnClickListener { updateSelectedOperation(R.drawable.img_plus, '+') }
        subtractButton.setOnClickListener { updateSelectedOperation(R.drawable.img_minus, '-') }
        multiplyButton.setOnClickListener { updateSelectedOperation(R.drawable.img_times, '*') }
        divideButton.setOnClickListener { updateSelectedOperation(R.drawable.img_div, '/') }
    }

    private fun updateSelectedOperation(operationDrawable: Int, operationSymbol: Char) {
        selectedOperation.setImageResource(operationDrawable)
        selectedOperation.tag = operationSymbol
    }

    private fun setEqualsClickListener() {
        equalsButton.setOnClickListener {
            val op1 = operand1.text.toString().toDoubleOrNull()
            val op2 = operand2.text.toString().toDoubleOrNull()
            if (op1 != null && op2 != null) {
                when (selectedOperation.tag) {
                    '+' -> result.text = (op1 + op2).toString()
                    '-' -> result.text = (op1 - op2).toString()
                    '*' -> result.text = (op1 * op2).toString()
                    '/' -> if (op2 != 0.0) {
                        result.text = (op1 / op2).toString()
                    } else {
                        result.text = getString(R.string.divide_by_zero)
                    }
                }
            } else {
                result.text = getString(R.string.invalid_input)
            }
        }
    }

    private fun handleNumberButtonClick(view: View) {
        if (view is Button) {
            val number = view.text.toString()
            val currentOperand: TextView = if (selectedOperation.tag == null) operand1 else operand2
            currentOperand.text =
                if (currentOperand.text == null) number else currentOperand.text.toString() + number
        }
    }

    private fun setResetClickListener() {
        resetButton.setOnClickListener {
            resetCalculator()
        }
    }

    private fun resetCalculator() {
        operand1.text = null
        operand2.text = null
        result.text = null
        selectedOperation.setImageResource(R.drawable.img_qmark)
        selectedOperation.tag = null
    }

//  Try to implement Advertisement

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://mocki.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val ApiAds = retrofit.create(ApiAds::class.java)

    private fun fetchAndDisplayAd() {
        lifecycleScope.launch {
            try {
                val response = ApiAds.getAds()
                val activeAds = response.body()?.filter { it.active } ?: emptyList()
                if (activeAds.isNotEmpty()) {
                    val randomAd = activeAds[Random.nextInt(activeAds.size)].content
                    displayAd(randomAd)
                }
            } catch (exception: Exception) {
            }
        }
    }

    private fun displayAd(adContent: String) {
        val adTextView = findViewById<TextView>(R.id.adTextView)
        adTextView.text = adContent
    }
}