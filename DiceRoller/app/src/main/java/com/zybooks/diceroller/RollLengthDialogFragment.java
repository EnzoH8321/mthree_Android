package com.zybooks.diceroller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RollLengthDialogFragment extends DialogFragment {

    public interface onRollLengthSelectedListener {
        void onRollLengthClick(int which);
    }

    private onRollLengthSelectedListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.pick_roll_lengthj);
        builder.setItems(R.array.length_array, (dialog, which) -> {
                //which is the index position chosen
            mListener.onRollLengthClick(which);

        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        mListener = (onRollLengthSelectedListener) context;
    }
}
