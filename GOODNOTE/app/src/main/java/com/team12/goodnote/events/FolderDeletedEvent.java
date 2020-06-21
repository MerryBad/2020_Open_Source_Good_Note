package com.team12.goodnote.events;

import com.team12.goodnote.models.Folder;


public class FolderDeletedEvent{
	Folder folder;

	public FolderDeletedEvent(Folder folder){
		this.folder = folder;
	}

	public Folder getFolder(){
		return folder;
	}
}
