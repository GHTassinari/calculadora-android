package br.edu.ifsuldeminas.mch.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ifsuldeminas.mch.calc";

    private StringBuilder expressaoAritmetica = new StringBuilder();

    private TextView textViewResultado;
    private TextView textViewUltimaExpressao;

    private Button buttonReset;
    private Button buttonDelete;

    private Button buttonIgual;
    private Button[] botoesNumericos;

    private boolean houveResultado = false;

    private Button buttonPorcento;
    private Button buttonDivisao;
    private Button buttonMultiplicacao;
    private Button buttonSubtracao;
    private Button buttonSoma;

    private Button buttonVirgula;

    private final int[] idsOperadores = {
            R.id.buttonDivisaoID,
            R.id.buttonMultiplicacaoID,
            R.id.buttonSubtracaoID,
            R.id.buttonSomaID
    };

    private int[] idsBotoes = {
            R.id.buttonZeroID,
            R.id.buttonUmID,
            R.id.buttonDoisID,
            R.id.buttonTresID,
            R.id.buttonQuatroID,
            R.id.buttonCincoID,
            R.id.buttonSeisID,
            R.id.buttonSeteID,
            R.id.buttonOitoID,
            R.id.buttonNoveID
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewUltimaExpressao = findViewById(R.id.textViewUltimaExpressaoID);
        textViewResultado = findViewById(R.id.textViewResultadoID);

        botoesNumericos = new Button[idsBotoes.length];
        for (int i = 0; i < idsBotoes.length; i++) {
            botoesNumericos[i] = findViewById(idsBotoes[i]);
            botoesNumericos[i].setOnClickListener(listenerNumerico);
        }

        buttonDivisao = findViewById(R.id.buttonDivisaoID);
        buttonMultiplicacao = findViewById(R.id.buttonMultiplicacaoID);
        buttonSubtracao = findViewById(R.id.buttonSubtracaoID);
        buttonSoma = findViewById(R.id.buttonSomaID);

        buttonDivisao.setOnClickListener(listenerOperador);
        buttonMultiplicacao.setOnClickListener(listenerOperador);
        buttonSubtracao.setOnClickListener(listenerOperador);
        buttonSoma.setOnClickListener(listenerOperador);

        buttonReset = findViewById(R.id.buttonResetID);
        buttonDelete = findViewById(R.id.buttonDeleteID);

        buttonReset.setOnClickListener(view -> {
            expressaoAritmetica.setLength(0);
            textViewResultado.setText("0");

            if (!houveResultado) {
                textViewUltimaExpressao.setText("");
            }

            houveResultado = false;
        });

        buttonDelete.setOnClickListener(view -> {
            if (expressaoAritmetica.length() > 0) {
                expressaoAritmetica.deleteCharAt(expressaoAritmetica.length() - 1);
                textViewResultado.setText(expressaoAritmetica.toString());
            }
        });

        buttonVirgula = findViewById(R.id.buttonVirgulaID);
        buttonVirgula.setOnClickListener(view -> {
            expressaoAritmetica.append(",");
            textViewResultado.setText(expressaoAritmetica.toString());
        });

        buttonPorcento = findViewById(R.id.buttonPorcentoID);
        buttonPorcento.setOnClickListener(view -> {
            expressaoAritmetica.append("%");
            textViewResultado.setText(expressaoAritmetica.toString());
        });

        buttonIgual = findViewById(R.id.buttonIgualID);
        buttonIgual.setOnClickListener(view -> {
            try {
                String expressaoCalculada = expressaoAritmetica.toString()
                        .replace("÷", "/")
                        .replace("×", "*");


                String expressaoCalculadaComPorcentagem = expressaoCalculada.replace("%", "/100*");

                Calculable avaliadorExpressao = new ExpressionBuilder(expressaoCalculadaComPorcentagem).build();
                Double resultado = avaliadorExpressao.calculate();

                String resultadoFormatado = String.format(Locale.US, "%.10f", resultado);
                resultadoFormatado = resultadoFormatado.replaceAll("\\.?0+$", "");

                textViewUltimaExpressao.setText(expressaoAritmetica.toString());
                textViewResultado.setText(resultadoFormatado);

                expressaoAritmetica.setLength(0);
                expressaoAritmetica.append(resultadoFormatado);

                houveResultado = true;
            } catch (Exception e) {
                Log.d(TAG, "Erro na expressão: " + e.getMessage());
                Toast.makeText(MainActivity.this, "Expressão inválida", Toast.LENGTH_SHORT).show();
                expressaoAritmetica.setLength(0);
                expressaoAritmetica.append("0");
                textViewResultado.setText("0");
                textViewUltimaExpressao.setText("");
            }
        });



    }

    private final View.OnClickListener listenerNumerico = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button botaoClicado = (Button) view;
            String valor = botaoClicado.getText().toString();

            if (valor.equals("0") && expressaoAritmetica.length() > 0 && expressaoAritmetica.charAt(expressaoAritmetica.length() - 1) == '0') {
                return;
            }

            expressaoAritmetica.append(valor);
            textViewResultado.setText(expressaoAritmetica.toString());
        }
    };

    private final View.OnClickListener listenerOperador = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button botao = (Button) view;
            String operador = botao.getText().toString();

            expressaoAritmetica.append(operador);
            textViewResultado.setText(expressaoAritmetica.toString());
        }
    };
}
