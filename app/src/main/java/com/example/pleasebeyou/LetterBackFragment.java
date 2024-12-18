package com.example.pleasebeyou;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LetterBackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LetterBackFragment extends Fragment {
    private TextView wordTextView;
    private TextView descriptionTextView;
    private ImageView imageView;
/*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
*/
    public LetterBackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LetterBackFragment.
     */
/*
    // TODO: Rename and change types and number of parameters
    public static LetterBackFragment newInstance(String param1, String param2) {
        LetterBackFragment fragment = new LetterBackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_letter_back, container, false);

        wordTextView = view.findViewById(R.id.word_text_view);
        descriptionTextView = view.findViewById(R.id.description_text_view);
        imageView = view.findViewById(R.id.image_view);

        wordTextView.setText(getArguments().getString("word"));
        descriptionTextView.setText(getArguments().getString("description"));
        new loadImage().execute(getArguments().getString("imageUrl"));

        return view;
    }

    private class loadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                Bitmap image = BitmapFactory.decodeStream(url.openStream());
                return image;
            } catch (IOException e) {
                Log.e("cs50", "URL error", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}