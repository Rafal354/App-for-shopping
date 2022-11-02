package com.example.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class MainView extends AppCompatActivity {

    Button add;
    AlertDialog dialog;
    LinearLayout layout;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        dbHandler = new DBHandler(MainView.this);
        List<Product> products = dbHandler.getAllProducts();

        for (Product product: products) {

            View view = getLayoutInflater().inflate(R.layout.card, null);
            TextView nameView = view.findViewById(R.id.name);
            Button delete = view.findViewById(R.id.delete);

            String str = product.getName();
            long idk = product.getId();

            nameView.setText(str);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbHandler.remove(idk);
                    layout.removeView(view);
                }
            });
            layout.addView(view);
        }
        buildDialog();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);

        builder.setTitle("Product name: ")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    long id = dbHandler.addNewProduct(name.getText().toString());
                    addCard(name.getText().toString(), id);
                    name.setText("");
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {

                }
            });
        dialog = builder.create();
    }

    private void addCard(String name, long id) {
        View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);

        nameView.setText(name);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.remove(id);
                layout.removeView(view);
            }
        });
        layout.addView(view);
    }
}