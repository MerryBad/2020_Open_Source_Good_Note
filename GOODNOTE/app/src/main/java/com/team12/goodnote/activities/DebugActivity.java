package com.team12.goodnote.activities;

import android.os.Bundle;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.team12.goodnote.R;
import com.team12.goodnote.database.AppDatabase;
import com.team12.goodnote.database.FoldersDatabaseAccess;
import com.team12.goodnote.models.Folder;
import com.team12.goodnote.models.FolderNoteRelation;
import com.team12.goodnote.models.Note;
import com.team12.goodnote.models.Note_Table;
public class DebugActivity extends AppCompatActivity{
	@Override protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.assign_to_folders) void assignToFolders(){
		Note note = SQLite.select().from( Note.class).orderBy(Note_Table.createdAt, false).querySingle();
		List<Folder> folders = FoldersDatabaseAccess.getLatestFolders();
		for (Folder folder : folders){
			FolderNoteRelation fnr = new FolderNoteRelation();
			fnr.setFolder( folder );
			fnr.setNote( note );
			fnr.save();
		}
	}

	@OnClick(R.id.create_5_notes) void create5Notes(){
		AppDatabase.Utils.createSomeNotes(5);
	}
}
