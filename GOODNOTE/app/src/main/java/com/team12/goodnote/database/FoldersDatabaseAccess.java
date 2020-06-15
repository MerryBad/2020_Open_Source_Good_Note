package com.team12.goodnote.database;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import com.team12.goodnote.models.Folder;
import com.team12.goodnote.models.Folder_Table;

public class FoldersDatabaseAccess {
	public static List<Folder> getLatestFolders(){
		return SQLite.select().from( Folder.class).orderBy(Folder_Table.createdAt, false).queryList();
	}

	public static Folder getFolder(int id){
		return SQLite.select().from( Folder.class).where(Folder_Table.id.is(id)).querySingle();
	}
}
