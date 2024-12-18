package com.example.pleasebeyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LetterActivity extends AppCompatActivity {
    private List<Letter> letters = new ArrayList<>();
    private boolean flashcardOrderRandom;
    private int alphabeticalOrderCounter;
    private boolean cardShowingBack = false;
    private Random randomInt = new Random();
    private Bundle bundle = new Bundle();
    private ProgressDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);
        new DownloadLetters().execute("rizh9xqu9ijm");

        // Make layout clickable
        ConstraintLayout layout = findViewById(R.id.activity_letter);
        layout.setClickable(true);

        // Flip card when layout is clicked
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCard();
            }
        });

        // 'nextButton' will load a new 'letterFrontFragment'
        FloatingActionButton nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardShowingBack = false;
                Fragment fragment = new LetterFrontFragment();
                fragment.setArguments(bundle);

                // Check 'flashcardOrderRandom' to determine new letter with data to be loaded into bundle
                // Load a random letter if 'flashcardOrderRandom' is true
                if(flashcardOrderRandom) {
                    loadLetter(randomInt.nextInt(letters.size()));
                }
                // Load the next letter if 'flashcardOrderRandom' is false
                else {
                    // If flashcard displays "Zz", then load letter "Aa"
                    if(alphabeticalOrderCounter == 25) {
                        alphabeticalOrderCounter = 0;
                    }
                    else {
                        alphabeticalOrderCounter++;
                    }
                    loadLetter(alphabeticalOrderCounter);
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.card_fragment_container, fragment)
                        .commit();
            }
        });

        // 'nextButton' will load a new 'letterFrontFragment'
        FloatingActionButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardShowingBack = false;
                Fragment fragment = new LetterFrontFragment();
                fragment.setArguments(bundle);

                // Check 'flashcardOrderRandom' to determine new letter with data to be loaded into bundle
                // Load a random letter if 'flashcardOrderRandom' is true
                if(flashcardOrderRandom) {
                    loadLetter(randomInt.nextInt(letters.size()));
                }
                // Load the previous letter if 'flashcardOrderRandom' is false
                else {
                    // If flashcard displays "Aa", then load letter "Zz"
                    if(alphabeticalOrderCounter == 0) {
                        alphabeticalOrderCounter = 25;
                    }
                    else {
                        alphabeticalOrderCounter--;
                    }
                    loadLetter(alphabeticalOrderCounter);
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.card_fragment_container, fragment)
                        .commit();
            }
        });
    }

    // Trigger confirmation dialog if user presses the back button
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Leaving Flashcards")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }

    // Replace front / back fragment with new back / front fragment with existing bundle
    // Modified from https://www.androidauthority.com/how-to-add-flip-animations-to-your-android-app-814902/
    private void flipCard() {
        Fragment fragment;
        int enterAnimation;
        int exitAnimation;

        // Initialize 'fragment' as a new 'LetterFrontFragment()' with existing bundle if 'cardShowingBack' is true
        if (cardShowingBack) {
            fragment = new LetterFrontFragment();
            fragment.setArguments(bundle);
            enterAnimation = R.animator.card_flip_left_in;
            exitAnimation = R.animator.card_flip_left_out;
            cardShowingBack = false;
        }
        // Initialize 'fragment' as a new 'LetterBackFragment()' with existing bundle if 'cardShowingBack' is false
        else {
            fragment = new LetterBackFragment();
            fragment.setArguments(bundle);
            enterAnimation = R.animator.card_flip_right_in;
            exitAnimation = R.animator.card_flip_right_out;
            cardShowingBack = true;
        }

        // Replace existing fragment by animating in 'fragment'
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(enterAnimation, exitAnimation)
                .replace(R.id.card_fragment_container, fragment)
                .commit();
    }

    // Download Letters from Contentful and load them into 'letters[]'
    private class DownloadLetters extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            // Retrieve flashcard order from intent from 'MainActivity'
            flashcardOrderRandom = getIntent().getBooleanExtra("FLASH_CARD_ORDER", true);

            // Establish link with Contentful account
            CDAClient client = CDAClient.builder().setSpace(strings[0])
                    .setToken("0ePzUovJqDXjTpILwdySbtoEy_nA8o34ln1s97EdJbk")
                    .build();

            // Download a CDAArray of objects representing 'Letter' objects
            CDAArray entries = client
                    .fetch(CDAEntry.class)
                    .where("content_type", "alphabetModule")
                    .where("order", "fields.id")
                    .all();

            // Copy elements from CDAArray 'entries' to List 'letters' of 'Letter' objects
            for (int i = 0; i < entries.total(); i++) {
                CDAEntry entry = (CDAEntry) entries.items().get(i);
                String entryLetter = entry.getField("letter");
                String entryWord = entry.getField("word");
                String entryDescription = entry.getField("description");
                CDAAsset entryImage = entry.getField("image");
                String entryImageUrl = "https:" + entryImage.url();
                letters.add(new Letter(entryLetter, entryWord, entryDescription, entryImageUrl));
            }

            // Return index of first flashcard to be loaded based on 'flashcardOrderRandom'
            if (flashcardOrderRandom) {
                return randomInt.nextInt(letters.size());
            }
            else {
                alphabeticalOrderCounter = 0;
                return alphabeticalOrderCounter;
            }
        }

        // Display loading screen before 'doInBackground' executes
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = new ProgressDialog(LetterActivity.this);
            loadingDialog.setMessage("Loading");
            loadingDialog.setIndeterminate(false);
            loadingDialog.setCancelable(false);
            loadingDialog.show();

        }

        // Load content for first flashcard at 'letters' index passed by 'doInBackground'
        @Override
        protected void onPostExecute(Integer integer) {
            loadLetter(integer);

            // Create initial fragment
            Fragment fragment = new LetterFrontFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.card_fragment_container, fragment)
                    .commit();

            loadingDialog.dismiss();
        }
    }

    // Retrieves a random Letter and loads its data into a new bundle to be passed to letter fragments
    private void loadLetter(int i) {
        // Retrieve random Letter from 'letters'
        Letter newLetter = letters.get(i);

        // Add letter, word, description, and image url to bundle to send to letter fragments
        bundle.putString("letter", newLetter.getLetter());
        bundle.putString("word", newLetter.getWord());
        bundle.putString("description", newLetter.getDescription());
        bundle.putString("imageUrl", newLetter.getImageUrl());
    }
}