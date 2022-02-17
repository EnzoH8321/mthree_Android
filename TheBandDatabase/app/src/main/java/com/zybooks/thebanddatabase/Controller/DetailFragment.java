package com.zybooks.thebanddatabase.Controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zybooks.thebanddatabase.Model.Band;
import com.zybooks.thebanddatabase.Model.BandRepository;
import com.zybooks.thebanddatabase.R;


public class DetailFragment extends Fragment {

    private Band mBand;
    public static final String ARG_BAND_ID = "band_id";
    TextView descriptionTextView;

    public DetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int bandId = 1;

        //Get the band ID from the fragment arguments
        Bundle args = getArguments();
        if (args != null) {
            bandId = args.getInt(ARG_BAND_ID);
        }

        //Get the selected band
        mBand = BandRepository.getInstance(requireContext()).getBand(bandId);
        System.out.println(mBand);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        if (mBand != null) {
            TextView nameTextView = rootView.findViewById(R.id.band_name);
            nameTextView.setText(mBand.getName());

            TextView descriptionTextView = rootView.findViewById(R.id.band_description);
            descriptionTextView.setText(mBand.getDescription());
            descriptionTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String url = "";


                    switch (mBand.getId()) {
                        case 0:
                            url = "https://en.wikipedia.org/wiki/The_Beatles";

                        case 1:
                            url = "https://en.wikipedia.org/wiki/Nirvana";

                        case 2:
                            url = "https://en.wikipedia.org/wiki/U2";

                        case 3:
                            url = "https://en.wikipedia.org/wiki/Red_Hot_Chili_Peppers";

                        case 4:
                            url = "https://en.wikipedia.org/wiki/Rolling_Stone";

                    }

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            ImageView bandImageView = rootView.findViewById(R.id.band_image);

            switch (mBand.getId()) {
                case 0:
                    bandImageView.setImageResource(R.drawable.beatles);
                case 1:
                    bandImageView.setImageResource(R.drawable.nirvana);
                case 2:
                    bandImageView.setImageResource(R.drawable.u2);
                case 3:
                    bandImageView.setImageResource(R.drawable.chilipeppers);
                case 4:
                    bandImageView.setImageResource(R.drawable.chilipeppers);
            }
        }

        return rootView;

    }
}