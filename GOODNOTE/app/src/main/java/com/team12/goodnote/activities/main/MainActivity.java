package com.team12.goodnote.activities.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewTreeObserver;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.team12.goodnote.R;
import com.team12.goodnote.activities.editfolders.EditFoldersActivityIntentBuilder;
import com.team12.goodnote.database.FoldersDatabaseAccess;
import com.team12.goodnote.models.Folder;

import java.util.List;

public class MainActivity extends AppCompatActivity{
	private static final String TAG = "MainActivity";
	private static final int ALL_NOTES_MENU_ID = -1;
	private static final int EDIT_FOLDERS_MENU_ID = -2;
	private static final int SAVE_DATABASE_MENU_ID = -3;
	private static final int IMPORT_DATABASE_MENU_ID = -4;

	@BindView(R.id.navigation_view) NavigationView mNavigationView;
	@BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
	List<Folder> latestFolders;
	BackupRestoreDelegate backupRestoreDelegate;

	@Override protected void onCreate(@Nullable Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		mDrawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
			@Override public void onGlobalLayout(){
				mDrawerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				setFragment(null);
			}
		});
		backupRestoreDelegate = new BackupRestoreDelegate(this);
		if (getIntent().getData() != null) backupRestoreDelegate.handleFilePickedWithIntentFilter(getIntent().getData());
			mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
				@Override public boolean onNavigationItemSelected(MenuItem item){
					Log.e(TAG, "onNavigationItemSelected() called with: " + "item id = [" + item.getItemId() + "]");
					int menuId = item.getItemId();
					if (menuId == ALL_NOTES_MENU_ID){
						setFragment(null);
					}else if (menuId == EDIT_FOLDERS_MENU_ID){
						startActivity(new EditFoldersActivityIntentBuilder().build(MainActivity.this));
					}else if (menuId == SAVE_DATABASE_MENU_ID){
						backupRestoreDelegate.backupDataToFile();
					}else if (menuId == IMPORT_DATABASE_MENU_ID){
						backupRestoreDelegate.startFilePickerIntent();
					}else{
						setFragment( FoldersDatabaseAccess.getFolder(menuId));
					}
					mDrawerLayout.closeDrawer(Gravity.LEFT);
					inflateNavigationMenus(menuId);
					return true;
				}
			});
	}

	@Override protected void onStart(){
		super.onStart();
		inflateNavigationMenus(ALL_NOTES_MENU_ID);
	}

	public void inflateNavigationMenus(int checkedItemId){
		Menu menu = mNavigationView.getMenu();
		menu.clear();
		menu
				.add(Menu.NONE, ALL_NOTES_MENU_ID, Menu.NONE, "Notes")
				.setIcon(R.drawable.ic_note_24dp)
				.setChecked(checkedItemId == ALL_NOTES_MENU_ID);
		final SubMenu subMenu = menu.addSubMenu("Folders");
		latestFolders = FoldersDatabaseAccess.getLatestFolders();
		for (Folder folder : latestFolders){
			subMenu
					.add(Menu.NONE, folder.getId(), Menu.NONE, folder.getName())
					.setIcon(R.drawable.ic_folder_24dp)
					.setChecked( folder.getId() == checkedItemId);
		}
		menu
				.add(Menu.NONE, EDIT_FOLDERS_MENU_ID, Menu.NONE, "Create or edit folders")
				.setIcon(R.drawable.ic_add_24dp);
		SubMenu backupSubMenu = menu.addSubMenu("Backup and restore");
		backupSubMenu
				.add(Menu.NONE, SAVE_DATABASE_MENU_ID, Menu.NONE, "Backup data")
				.setIcon(R.drawable.ic_save_24dp);
		backupSubMenu
				.add(Menu.NONE, IMPORT_DATABASE_MENU_ID, Menu.NONE, "Restore data")
				.setIcon(R.drawable.ic_restore_24dp);
	}

	@Override public void onBackPressed(){
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}else{
			super.onBackPressed();
		}
	}

	public void setFragment(Folder folder){
		// Create a new fragment and specify the fragment to show based on nav item clicked
		Fragment fragment = new NoteListFragment();
		if (folder != null){
			Bundle bundle = new Bundle();
			bundle.putParcelable(NoteListFragment.FOLDER, folder );
			fragment.setArguments(bundle);
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == BackupRestoreDelegate.PICK_RESTORE_FILE_REQUEST_CODE){
			backupRestoreDelegate.handleFilePickedWithFilePicker(resultCode, data);
		}
	}
}
