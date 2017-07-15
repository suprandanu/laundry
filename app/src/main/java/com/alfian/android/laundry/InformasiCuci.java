package com.alfian.android.laundry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InformasiCuci extends AppCompatActivity {
    TextView tambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_cuci);

        tambah = (TextView)findViewById(R.id.tambah);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InformasiCuci.this, TambahBarang.class));
            }
        });
    }
}
