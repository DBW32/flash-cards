package com.example.pleasebeyou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView startFlashcardsTextView;
    private BottomNavigationView mainMenu;
    private Boolean flashcardOrderRandom = true;

    // Used to save flashcard order selection under "Settings"
    private int flashcardOrderSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation will serve as main menu
        mainMenu = findViewById(R.id.main_menu);

        // Set main menu text and icon colors to not indicate selection
        mainMenu.setItemIconTintList(null);
        mainMenu.setItemTextColor(null);
        startFlashcardsTextView = findViewById(R.id.start_flashcards_text_view);

        // Set actions for options in main menu
        mainMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    // Access 'AboutActivity' when "About" is selected
                    case R.id.main_menu_about:
                        Context context = MainActivity.this;
                        Intent intent = new Intent(context, AboutActivity.class);
                        context.startActivity(intent);
                        return true;
                    // Show settings option via 'showSettings()' when "Settings" is selected
                    case R.id.main_menu_settings:
                        showSettings();
                        return true;
                }
                return false;
            }
        } );

        // Go to 'LetterActivity' when "Start Flashcards" is clicked
        startFlashcardsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(view.getContext(), LetterActivity.class);
                // Pass selected flashcard order to 'LetterActivity'
                intent.putExtra("FLASH_CARD_ORDER", flashcardOrderRandom);
                context.startActivity(intent);
            }
        });
    }

    // Display AlertDialog that allows users to select "Random" or "Alphabetical" order for flashcards
    private void showSettings() {
        AlertDialog.Builder settingsDialog = new AlertDialog.Builder(MainActivity.this);
        settingsDialog.setTitle("Select Flashcard Order");
        String[] flashcardOrders = {"Random", "Alphabetical"};
        settingsDialog.setSingleChoiceItems(flashcardOrders, flashcardOrderSelection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(i) {
                    case 0:
                        flashcardOrderSelection = 0;
                        flashcardOrderRandom = true;
                        break;
                    case 1:
                        flashcardOrderSelection = 1;
                        flashcardOrderRandom = false;
                        break;
                }
            }
        });

        settingsDialog.setPositiveButton("Ok", null);
        AlertDialog alert = settingsDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}