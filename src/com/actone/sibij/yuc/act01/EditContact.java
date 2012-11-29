package com.actone.sibij.yuc.act01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditContact extends Activity{
	
	TextView tvTitle;
	EditText etName, etNumber;
	Button btnSave, btnCancel;
	Contact contactToEdit;
	private ContactDataSource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);
		
		datasource = new ContactDataSource(this);
		datasource.open();
		setup();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	private void setup(){
		// Retrieve data 
		Bundle b = this.getIntent().getExtras();
		if(b!=null){
			contactToEdit = (Contact) b.getSerializable("contactToEdit");
		}
		
		// Setup views
		tvTitle = (TextView)findViewById(R.id.tvListHeaderTitle);
		etName = (EditText)findViewById(R.id.etContactName);
		etNumber = (EditText)findViewById(R.id.etContactNumber);
		btnSave = (Button)findViewById(R.id.btnSaveChanges);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		
		tvTitle.setText("Edit Contact");
		etName.setText(contactToEdit.name);
		etNumber.setText(contactToEdit.number);
		
		// Setup buttons
		btnSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = etName.getText().toString();
				String number = etNumber.getText().toString();
				datasource.editContact(contactToEdit.id, name, number);
				Toast.makeText(EditContact.this, "Contact Successfully Edited.", Toast.LENGTH_SHORT).show();
				Intent i = getIntent();
				setResult(RESULT_OK, i);
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = getIntent();
				setResult(RESULT_CANCELED, i);
				finish();
			}
		});
		
	}
	

}
