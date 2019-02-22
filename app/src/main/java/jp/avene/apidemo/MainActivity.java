package jp.avene.apidemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.language_translator.v3.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslationResult;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

public class MainActivity extends AppCompatActivity {
    private TextView outputTextView;
    private EditText input;
    private IamOptions options;
    private LanguageTranslator languageTranslator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputTextView = findViewById(R.id.textView);
        input = findViewById(R.id.input);
    }

    public void EJButtonClick(View view) {
        translate("en-ja");
    }

    public void EZButtonClick(View view) {
        translate("en-zh");
    }

    private void translate(String model) {
        if (options == null) {
            outputTextView.setText("Set API Key first");
            return;
        }

        TranslateOptions translateOptions = new TranslateOptions.Builder()
                .addText(input.getText().toString())
                .modelId(model)
                .build();

        new TranslateTask().execute(translateOptions);
        outputTextView.setText("Translating...");
    }

    public void APIKeyButtonClick(View view) {
        String key = ((EditText) findViewById(R.id.apiKey)).getText().toString().trim();
        if (key.isEmpty()) {
            return;
        }

        options = new IamOptions.Builder().apiKey(key).build();

        languageTranslator = new LanguageTranslator("2018-05-01", options);
        languageTranslator.setEndPoint("https://gateway-tok.watsonplatform.net/language-translator/api");
        ((Button)findViewById(R.id.APIKeyButton)).setEnabled(false);
    }

    private class TranslateTask extends AsyncTask<TranslateOptions, Void, Void> {
        @Override
        protected Void doInBackground(TranslateOptions... translateOptions) {

            final TranslationResult response = languageTranslator.translate(translateOptions[0]).execute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    outputTextView.setText(response.getTranslations().get(0).getTranslationOutput());
                }
            });
            return null;
        }
    }
}
