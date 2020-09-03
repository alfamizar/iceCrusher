//package com.complementarycode.icecrusher;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.app.FragmentManager;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatDialogFragment;
//import android.view.View;
//
//public class GameResult extends DialogFragment {
//
//    @Override
//    public Dialog onCreateDialog(Bundle bundle) {
//        // create dialog displaying String resource for messageId
//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(getActivity());
//        builder.setTitle(getResources().getString(getArguments().getInt("messageId")));
//
//        // display number of shots fired and total time elapsed
//        builder.setMessage(getResources().getString(
//                R.string.results_format, getArguments().getInt("shotsFired"),
//                getArguments().getDouble("totalElapsedTime")));
//        builder.setPositiveButton(R.string.reset_game,
//                new DialogInterface.OnClickListener() {
//                    // called when "Reset Game" Button is pressed
//                    @Override
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        GameActivity.cannonView.setDialogIsDisplayed(false);
//                        GameActivity.cannonView.newGame();
//
//                    }
//                }
//        );
//        return builder.create(); // return the AlertDialog
//    }
//}