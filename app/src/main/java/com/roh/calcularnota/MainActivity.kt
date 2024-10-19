package com.roh.calcularnota

import android.os.Bundle
import android.text.InputFilter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.roh.calcularnota.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var notasEditTexts: Array<TextInputEditText>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_dark)

        binding.capturar.setOnClickListener {
            criarCampos()
        }

        binding.calcular.setOnClickListener {
            calcular()
        }

        binding.imageButton.setOnClickListener{
            it.animate().rotationBy(360f).setDuration(500).start()
            reset()
        }
    }

    private fun criarCampos() {
        val inputLayout = binding.textInputLayout

        val inputEdit = binding.textInputEdit.text.toString()
        if (inputEdit.isBlank()) {
            inputLayout.error = "O campo não pode estar vazio"
            return

        } else {
            inputLayout.error = null
            binding.textInputEdit.text?.clear()
        }

        val convertCampoInt = inputEdit.toIntOrNull()

        if (convertCampoInt == null || convertCampoInt <= 0) {
            inputLayout.error = "Os valores devem ser positivos"
            return
        }

        if (convertCampoInt > 10) {
            inputLayout.error = "Número máximo de notas excedido"
            return
        }


        binding.LinearLayout.removeAllViews()
        notasEditTexts = Array(convertCampoInt) { index ->
            val recebeNotas = TextInputLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 8, 16, 8)
                }
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE

            }
            val editTextLayout = TextInputEditText(recebeNotas.context).apply {
                inputType =
                    android.text.InputType.TYPE_CLASS_NUMBER or
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                setPadding(16, 30, 16, 30)
                typeface = binding.textInputEdit.typeface
                backgroundTintList = binding.textInputEdit.backgroundTintList
                filters = arrayOf(InputFilter.LengthFilter(2))
                hint = "Digite a nota ${index + 1}"
            }
            recebeNotas.addView(editTextLayout)
            binding.LinearLayout.addView(recebeNotas)
            editTextLayout
        }

    }
    private fun calcular() {

        var houveError = false
        var soma = 0.0

        notasEditTexts?.forEach { campoNota ->

            val textInput = campoNota.parent.parent as TextInputLayout

            val notaText = campoNota.text.toString()

            if (notaText.isBlank()) {
                textInput.error = "Digite todas notas primeiro"
                houveError = true

            } else {
                textInput.error = null
                val nota = notaText.toDoubleOrNull()

                if (nota == null || nota < 0 || nota > 10) {
                    textInput.error = "Nota máxima (0-10)"
                    houveError = true

                } else {
                    textInput.error = null
                    soma += nota
                    binding.textInputEdit.text?.clear()
                }
            }
        }
        if(!houveError && soma > 0.0) {
            binding.Resultado.text = getString(R.string.resultado1, soma)
            binding.LinearLayout.removeAllViews()

        } else {
            binding.Resultado.text = getString(R.string.resultado2)
        }
    }

    private fun reset() {
        val quantNotas = binding.textInputEdit.text
        if (!quantNotas.isNullOrEmpty()){
            quantNotas.clear()
            binding.textInputLayout.error= null

        }
        if (binding.LinearLayout.childCount > 0) {
            binding.LinearLayout.children.forEach { limpar ->
                if (limpar is TextInputLayout) {
                    limpar.error = null
                    limpar.editText?.text?.clear()
                }
            }

        } else {
            binding.Resultado.text = getString(R.string.resultado2) // Resultado sem soma.
            notasEditTexts?.let {
                it.forEach { campoNota -> campoNota.text?.clear() } // Limpa o array
            }
        }
    }
}


