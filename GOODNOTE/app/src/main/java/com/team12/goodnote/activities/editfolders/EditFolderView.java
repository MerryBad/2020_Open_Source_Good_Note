package com.team12.goodnote.activities.editfolders;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.team12.goodnote.R;
import com.team12.goodnote.events.FolderDeletedEvent;
import com.team12.goodnote.models.Folder;
import com.team12.goodnote.utils.Utils;


class EditFolderView extends RecyclerView.ViewHolder implements OpenCloseable{
	private final Adapter adapter;
	Folder folder;
	@BindView(R.id.left_button) AppCompatImageButton leftButton;
	@BindView(R.id.folder_name_text) TextView folderName;
	@BindView(R.id.right_button) AppCompatImageButton rightButton;
	@BindView(R.id.folder_name_til) TextInputLayout folderNameTIL;

	public EditFolderView(final View itemView, Adapter adapter){
		super(itemView);
		ButterKnife.bind(this, itemView);
		this.adapter = adapter;
		folderName.setOnFocusChangeListener(new View.OnFocusChangeListener(){
			@Override public void onFocusChange(View v, boolean hasFocus){
				if (hasFocus){
					open();
				}
			}
		});
		folderName.setOnEditorActionListener(new TextView.OnEditorActionListener(){
			@Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
				if (actionId == EditorInfo.IME_ACTION_DONE){
					apply();
					close();
					return true;
				}
				return false;
			}
		});
	}

	@OnClick(R.id.left_button) void clickLeftButton(View view){
		if (isOpen()){
			close();
			delete();
		}
	}

	@OnClick(R.id.right_button) void clickRightButton(View view){
		if (isOpen()){
			apply();
			close();
		}else{
			open();
		}
	}

	@Override public void open(){
		leftButton.setImageResource(R.drawable.ic_delete_24dp);
		rightButton.setImageResource(R.drawable.ic_done_24dp);
		itemView.setBackgroundResource(R.color.md_white_1000);
		if (adapter.getLastOpened() != null)
			adapter.getLastOpened().close();
		adapter.setLastOpened(this);
	}

	@Override public boolean isOpen(){
		return folderName.hasFocus();
	}

	@Override public void close(){
		Utils.hideSoftKeyboard(itemView);
		leftButton.setImageResource(R.drawable.ic_folder_24dp);
		folderName.setText( folder.getName());
		folderName.clearFocus();
		rightButton.setImageResource(R.drawable.ic_edit_24dp);
		itemView.setBackgroundResource(0);
		if (adapter.getLastOpened() == this) adapter.setLastOpened(null);
	}

	private void apply(){
		if (TextUtils.isEmpty(folderName.getText())){
			folderNameTIL.setError("Enter a folder name");
			return;
		}
		folder.setName(folderName.getText().toString());
		folder.save();
	}

	private void delete(){
		new AlertDialog.Builder(itemView.getContext(), R.style.DialogTheme)
				.setCancelable(true)
				.setTitle("Delete folder?")
				.setMessage("Folder '" + folder.getName() + "' will be deleted however notes in this folder will remain safe")
				.setPositiveButton("Delete Folder", new DialogInterface.OnClickListener(){
					@Override public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
						folder.delete();
						EventBus.getDefault().post(new FolderDeletedEvent( folder ));
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
					@Override public void onClick(DialogInterface dialog, int which){
						dialog.dismiss();
					}
				})
				.show();
	}

	public void setFolder(Folder folder){
		this.folder = folder;
		folderName.setText( folder.getName());
		close();
	}
}
