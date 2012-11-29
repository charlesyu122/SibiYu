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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;

public class ContactsActivity extends ListActivity {

	private ContactDataSource datasource;
	Contact selectedContact;
	List<Contact> contactsToDisplay;
	TextView tvTitle, tvNoContact;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        
        // Initialize attributes
		datasource = new ContactDataSource(this);
		datasource.open();
		selectedContact = new Contact();
		contactsToDisplay = new ArrayList<Contact>();
		
        setup();
    }
    
    private void setup(){
    	// Setup views
    	tvTitle = (TextView)findViewById(R.id.tvListHeaderTitle);
    	tvNoContact = (TextView)findViewById(R.id.tvNoContacts);
    	tvTitle.setText("Contacts");
    	
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
        displayContactsFromSQLite();
    	
    	// Set up Quick Action
    	ActionItem editAction = new ActionItem();
    	editAction.setIcon(getResources().getDrawable(R.drawable.ic_add));
    	editAction.setTitle("Edit");
    	ActionItem deleteAction = new ActionItem();
    	deleteAction.setIcon(getResources().getDrawable(R.drawable.ic_delete));
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
					Intent i = new Intent();
					Bundle b = new Bundle();
					b.putSerializable("contactToEdit", selectedContact);
					i.putExtras(b);
					i.setClass(ContactsActivity.this, EditContact.class);
					startActivityForResult(i, 1);
				}else if(pos == 1){
					// Delete contact
					datasource.deleteContact(selectedContact);
					contactsToDisplay.remove(selectedContact);
					if(contactsToDisplay.size() == 0)
						tvNoContact.setVisibility(View.VISIBLE);
					getListView().setAdapter(new ContactAdapter(ContactsActivity.this, (ArrayList<Contact>)contactsToDisplay,'d'));
					Toast.makeText(ContactsActivity.this, "Contact Successfully Deleted!", Toast.LENGTH_SHORT).show();
				}
			}
		});
    }

	private Cursor getContacts() {
		// TODO Auto-generated method stub
		// Run query
	    Uri uri = ContactsContract.Contacts.CONTENT_URI;
	    String[] projection = new String[] {ContactsContract.Contacts._ID, 
	    									ContactsContract.Contacts.DISPLAY_NAME, };
	    return getContentResolver().query(uri, projection, null, null, null);
	}
	
	private void displayContactsFromSQLite(){
        contactsToDisplay = datasource.getAllContacts();
        
    	if(contactsToDisplay.size() > 0){
    		tvNoContact.setVisibility(View.GONE);
	    	// Setup list
	    	getListView().setAdapter(new ContactAdapter(ContactsActivity.this, (ArrayList<Contact>)contactsToDisplay,'n'));
    	}
	}
	
	private String getContactPhone(String contactID) {
	    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	    String[] projection = null;
	    String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?";
	    String[] selectionArgs = new String[] { contactID };
	    String sortOrder = null;
	    Cursor result = getContentResolver().query(uri, projection, where, selectionArgs, sortOrder);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == RESULT_OK){
			displayContactsFromSQLite();
		}
	}
}
