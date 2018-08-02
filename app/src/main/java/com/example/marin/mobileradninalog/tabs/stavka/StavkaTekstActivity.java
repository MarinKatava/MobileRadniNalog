package com.example.marin.mobileradninalog.tabs.stavka;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.marin.mobileradninalog.R;

public class StavkaTekstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_text);

        Toolbar toolbar = findViewById(R.id.itemTextToolbar);
        TextView itemText = findViewById(R.id.itemText);

        Bundle extra = getIntent().getExtras();
        itemText.setText(extra.get("stavkaTekst").toString());

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StavkaTekstActivity.this.finish();
            }
        });
    }

}
