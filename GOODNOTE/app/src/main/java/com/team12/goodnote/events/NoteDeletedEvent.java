package com.team12.goodnote.events;

import com.team12.goodnote.models.Note;


public class NoteDeletedEvent{
	Note note;

	public NoteDeletedEvent(Note note){
		this.note = note;
	}

	public Note getNote(){
		return note;
	}
}
