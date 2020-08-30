package com.quirozhernandez.digitoverificador;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DigitoVerificador extends AppCompatActivity {
    String rutcopiar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button btn = (Button) findViewById(R.id.calcular);
        final EditText rut = (EditText) findViewById(R.id.rut);
        final TextView dv = (TextView) findViewById(R.id.dv);
        final TextView msg = (TextView) findViewById(R.id.msg);
        msg.setVisibility(View.INVISIBLE);
        final TextView texto = (TextView) findViewById(R.id.texto);
        texto.setVisibility(View.INVISIBLE);
        final TextView resultado = (TextView) findViewById(R.id.resultado);
        final FloatingActionButton botoncompartir = (FloatingActionButton) findViewById(R.id.compartir);
        botoncompartir.setVisibility(View.INVISIBLE);


        rut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {
                    btn.setEnabled(true);
                } else {
                    btn.setEnabled(false);
                    texto.setVisibility(View.GONE);
                    dv.setVisibility(View.INVISIBLE);
                    resultado.setVisibility(View.INVISIBLE);
                    botoncompartir.setVisibility(View.INVISIBLE);
                    msg.setVisibility(View.INVISIBLE);

                }
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rut.onEditorAction(EditorInfo.IME_ACTION_DONE);
                texto.setVisibility(View.VISIBLE);
                String resultadofinal;
                int resultadonumero;
                resultadonumero = calculaDigitoVerificador(rut.getText().toString());
                resultadofinal = String.valueOf(resultadonumero);
                if (resultadonumero == 10) {
                    resultadofinal = "K";
                }
                dv.setText(resultadofinal);
                dv.setVisibility(View.VISIBLE);
                botoncompartir.setVisibility(View.VISIBLE);
                msg.setVisibility(View.VISIBLE);

                DecimalFormat decim = new DecimalFormat("#,###,###", DecimalFormatSymbols.getInstance(new Locale("es", "ES")));
                String formattedString = decim.format(Long.valueOf(rut.getText().toString()));
                rutcopiar = (formattedString + "-" + resultadofinal);
                resultado.setVisibility(View.VISIBLE);
                resultado.setText(rutcopiar);
            }
        });
        botoncompartir.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Obtuve el RUT: \n" + rutcopiar + "\nCon la App Dígito Verificador RUT (Chile)");
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent, "Compartir a través de:"));
                    }
                }
        );

        resultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("RUT", rutcopiar);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "RUT Copiado",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }


    public int calculaDigitoVerificador(String original) {
        int total;
        int n, b, c, d, e, f, g, h, i;
        int a = Integer.valueOf(original);
        //descomponer el cuerpo del rut, para luego contener cada numero en una va&lt;riable.
        int A = a / 10000000; //se guarda el primer numero
        n = a % 10000000;
        int B = n / 1000000;
        n = n % 1000000;
        int C = n / 100000;
        n = n % 100000;
        int D = n / 10000;
        n = n % 10000;
        int E = n / 1000;
        n = n % 1000;
        int F = n / 100;
        n = n % 100;
        int G = n / 10;
        n = n % 10;
        // Multiplicar cada digito,
        b = A * 3;
        c = B * 2;
        d = C * 7;
        e = D * 6;
        f = E * 5;
        g = F * 4;
        h = G * 3;
        i = n * 2;
        //sumar las multiplicaciones
        int suma = b + c + d + e + f + g + h + i;
        //dividir el total de la suma en 11
        int division = suma / 11;
        //sacar el resto ya que con eso se trabaja
        int resto = suma % 11;
        //a 11 se le quita el resto
        total = 11 - resto;
        if (total == 11) {
            total = 0;
        }
        return total;
    }

}
