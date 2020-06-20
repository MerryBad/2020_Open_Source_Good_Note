package com.team12.goodnote.database;


import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import com.team12.goodnote.models.Folder;
import com.team12.goodnote.models.Note;
import com.team12.goodnote.models.Note_Table;

public class NotesDatabaseAccess {
	public static List<Note> getLatestNotes(Folder folder, boolean sort ){
		if (folder == null)
			if (sort == false)
				return SQLite.select().from( Note.class).orderBy(Note_Table.createdAt, false).queryList();
			else
				return SQLite.select().from( Note.class).orderBy(Note_Table.title, true).queryList();

		else
			return FolderNoteDatabaseAccess.getLatestNotes( folder );
	}

	public static Note getNote(int noteId){
		return SQLite.select().from( Note.class).where(Note_Table.id.is(noteId)).querySingle();
	}
}
