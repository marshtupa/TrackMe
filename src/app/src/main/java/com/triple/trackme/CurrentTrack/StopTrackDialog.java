package com.triple.trackme.CurrentTrack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.triple.trackme.R;

class StopTrackDialog {

    Dialog create(final Context context,
                  final CurrentTrackView.CurrentTrackState trackStateBeforeDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.stopTrackDialogMessage)
                .setPositiveButton(R.string.stopTrackDialogPositiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (trackStateBeforeDialog == CurrentTrackView.CurrentTrackState.START) {
                            CurrentTrackView.startTrack();
                        }
                    }
                })
                .setNegativeButton(R.string.stopTrackDialogNegativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CurrentTrackView.endTrackAndSave();
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (trackStateBeforeDialog == CurrentTrackView.CurrentTrackState.START) {
                    CurrentTrackView.startTrack();
                }
            }
        });

        return builder.create();
    }
}
