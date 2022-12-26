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

    private Button add;
    private AlertDialog addDialog;
    private AlertDialog editDialog;
    private LinearLayout layout;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        dbHandler = DBHandler.getInstance(this);
        List<Product> products = dbHandler.getFirst(100);

        for (Product product: products) {
            addCard(product.getName(), product.getId());
        }

        buildDialogAdd();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.show();
            }
        });
    }

    private void buildDialogAdd() {
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
                        name.setText("".toUpperCase());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
        addDialog = builder.create();
    }

    private void buildDialogEdit(View v, String text, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        EditText name = view.findViewById(R.id.nameEdit);

        name.setText(text);

        builder.setView(view);

        builder.setTitle("Product name: ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        int itemIndex = layout.indexOfChild(v);
                        dbHandler.update(id, name.getText().toString());
                        layout.removeView(v);
                        TextView nameView = v.findViewById(R.id.name);
                        nameView.setText(name.getText().toString());
                        layout.addView(v, itemIndex);
                        name.setText("".toUpperCase());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
        editDialog = builder.create();
    }

    private void addCard(String name, long id) {
        View view = getLayoutInflater().inflate(R.layout.card, null);
        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);
        Button edit = view.findViewById(R.id.edit);

        nameView.setText(name);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.remove(id);
                layout.removeView(view);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildDialogEdit(view, name, id);
                editDialog.show();
            }
        });

        layout.addView(view);
    }
}