package com.team12.goodnote.jobs;

import android.graphics.Bitmap;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.raizlabs.android.dbflow.data.Blob;
import com.team12.goodnote.database.NotesDatabaseAccess;
import com.team12.goodnote.events.NoteEditedEvent;
import com.team12.goodnote.models.Note;
import com.team12.goodnote.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class SaveImageJob extends Job{
	private static final String TAG = "SaveImageJob";
	private final Bitmap img;
	private final int noteId;

	public SaveImageJob(Bitmap img, int noteId){
		super(new Params(1));
		this.img =img;
		this.noteId = noteId;
	}

	@Override public void onAdded(){
		Log.e(TAG, "onAdded() called with: " + "");
	}

	@Override public void onRun() throws Throwable{
		Log.e(TAG, "onRun() called with: " + "");

		byte[] byteBlobTrimmed = Utils.getBytes(img);
		Blob blobTrimmed = new Blob(byteBlobTrimmed);
		Note note = NotesDatabaseAccess.getNote(noteId);
		note.setDrawingTrimmed(blobTrimmed);
		note.save();

		byte[] byteBlob = Utils.getBytes(img);
		Blob blob = new Blob(byteBlob);
		note.setDrawing(blob);
		note.save();

		EventBus.getDefault().post(new NoteEditedEvent( note.getId()));
	}

	@Override protected void onCancel(int cancelReason, @Nullable Throwable throwable){
		Log.e(TAG, "onCancel() called with: " + "cancelReason = [" + cancelReason + "], throwable = [" + throwable + "]");
	}

	@Override protected RetryConstraint shouldReRunOnThrowable(
			@NonNull Throwable throwable, int runCount, int maxRunCount
	){
		Log.e(TAG, "shouldReRunOnThrowable() called with: "
				+ "throwable = ["
				+ throwable
				+ "], runCount = ["
				+ runCount
				+ "], maxRunCount = ["
				+ maxRunCount
				+ "]");
		return RetryConstraint.CANCEL;
	}
}
