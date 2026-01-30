package com.aipasa;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainBab extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bab);


        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        FloatingActionButton fab = findViewById(R.id.floating_action_button);


        // FAB
        fab.setOnClickListener(v ->
                Toast.makeText(this, "FAB Clicked", Toast.LENGTH_SHORT).show()
        );


        // Hamburguesa → BottomSheet
        bottomAppBar.setNavigationOnClickListener(v -> showBottomSheetDialog());


        // Menú del BottomAppBar
        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.heart) {
                Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
                return true;
//            } else if (item.getItemId() == R.id.search) {
//                Toast.makeText(this, "Beginning search", Toast.LENGTH_SHORT).show();
//                return true;
            }
            return false;
        });
    }


    // BottomSheet
    private void showBottomSheetDialog() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.activity_bab, null);


        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();


//        TextView option1 = view.findViewById(R.id.option1);
//        TextView option2 = view.findViewById(R.id.option2);
//        TextView option3 = view.findViewById(R.id.option3);
//
//        option1.setOnClickListener(v -> {
//            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        option2.setOnClickListener(v -> {
//            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
//
//        option3.setOnClickListener(v -> {
//            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
//            dialog.dismiss();
//        });
    }
}