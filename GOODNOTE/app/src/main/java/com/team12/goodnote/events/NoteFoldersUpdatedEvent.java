package com.team12.goodnote.events;

import com.team12.goodnote.database.NotesDatabaseAccess;
import com.team12.goodnote.models.Note;

public class NoteFoldersUpdatedEvent{

	int noteId;

	public NoteFoldersUpdatedEvent(int noteId){
		this.noteId = noteId;
	}

	public int getNoteId(){
		return noteId;
	}

	public Note getNote(){
		return NotesDatabaseAccess.getNote(noteId);
	}
}
