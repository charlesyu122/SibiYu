package com.actone.sibij.yuc.act01;

import java.util.ArrayList;
import java.util.List;

import com.actone.quickaction.ActionItem;
import com.actone.quickaction.QuickAction;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.app.ListActivity;
import android.database.Cursor;

public class ContactsActivity extends ListActivity {

	private ContactDataSource datasource;
	Contact selectedContact;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        
        // Initialize attributes
		datasource = new ContactDataSource(this);
		datasource.open();
		selectedContact = new Contact();
		
        setup();
    }
    
    private void setup(){
    	// Setup views
    	TextView tvTitle = (TextView)findViewById(R.id.tvListHeaderTitle);
    	tvTitle.setText("Contacts");
    	TextView tvNoContact = (TextView)findViewById(R.id.tvNoContacts);
    	
    	// Retrieve contents from Contact Application
    	Cursor cursor = getContacts();
    	
    	// Update sqlite table Contacts
        while(cursor.moveToNext()){
        	String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
        	String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Data._ID));
        	String contactNumber = getContactPhone(contactId);
        	datasource.insertIfNewContact(new Contact(contactId, contactName, contactNumber));
        }
        
        // Retrieve contacts from sqlite
        List<Contact> contactsToDisplay = datasource.getAllContacts();
  
    	if(contactsToDisplay.size() > 0){
    		tvNoContact.setVisibility(View.GONE);
	    	// Setup list
	    	getListView().setAdapter(new ContactAdapter(ContactsActivity.this, (ArrayList<Contact>)contactsToDisplay));
    	}
    	
    	// Set up Quick Action
    	ActionItem editAction = new ActionItem();
    	editAction.setIcon(getResources().getDrawable(R.drawable.ic_add));
    	editAction.setTitle("Edit");
    	ActionItem deleteAction = new ActionItem();
    	deleteAction.setIcon(getResources().getDrawable(R.drawable.ic_accept));
    	deleteAction.setTitle("Delete");
    	
    	final QuickAction myQA = new QuickAction(this);
    	myQA.addActionItem(editAction);
    	myQA.addActionItem(deleteAction);
    	
    	getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				selectedContact = (Contact) v.getTag();
				myQA.show(v);
			}
		});
    	
    	myQA.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				// TODO Auto-generated method stub
				if(pos == 0){
					// Edit contact
					
				}else if(pos == 1){
					// Delete contact
					
				}
			}
		});
    }

	@SuppressWarnings("deprecation")
	private Cursor getContacts() {
		// TODO Auto-generated method stub
		// Run query
	    Uri uri = ContactsContract.Contacts.CONTENT_URI;
	    String[] projection = new String[] {ContactsContract.Contacts._ID, 
	    									ContactsContract.Contacts.DISPLAY_NAME, 
	    };
	    return managedQuery(uri, projection, null, null, null);
	}
	
	@SuppressWarnings("deprecation")
	private String getContactPhone(String contactID) {
	    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	    String[] projection = null;
	    String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?";
	    String[] selectionArgs = new String[] { contactID };
	    String sortOrder = null;
	    Cursor result = managedQuery(uri, projection, where, selectionArgs, sortOrder);
	    if (result.moveToFirst()) {
	        String phone = result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	        if (phone != null) {
	            result.close();
	            return phone;
	        }
	    }
	    result.close();
	    return null;
	}
}
