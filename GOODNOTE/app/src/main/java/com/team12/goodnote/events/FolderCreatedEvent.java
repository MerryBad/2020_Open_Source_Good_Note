package com.team12.goodnote.events;

import com.team12.goodnote.models.Folder;


public class FolderCreatedEvent{
	private Folder folder;

	public FolderCreatedEvent(Folder folder){
		this.folder = folder;
	}

	public Folder getFolder(){
		return folder;
	}
}
