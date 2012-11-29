package com.actone.sibij.yuc.act01;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContactDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_APPID, 
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_NUMBER};
	
	public ContactDataSource(Context context){
		dbHelper = new MySQLiteHelper(context);
	}
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void insertIfNewContact(Contact contact){
		// Check if present
		Cursor cursor = database.rawQuery("select * from contact where app_id = "+contact.appContactsId, null);
		if(cursor.getCount() == 0){
			// Insert Contact
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.COLUMN_APPID, contact.appContactsId);
			values.put(MySQLiteHelper.COLUMN_NAME, contact.name);
			values.put(MySQLiteHelper.COLUMN_NUMBER, contact.number);
			long insertid = database.insert(MySQLiteHelper.TABLE_CONTACTS, null, values);
			Log.d("Insertedid:", ""+insertid);
		}
	}
	
	public List<Contact> getAllContacts(){
		List<Contact> contacts = new ArrayList<Contact>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACTS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Contact contact = cursorToContact(cursor);
			Log.d("RETRIEVEDID", ""+contact.id);
			contacts.add(contact);
			cursor.moveToNext();
		}
		// Close the cursor
		cursor.close();
		return contacts;
	}
	
	public void deleteContact(Contact contact){
		long id = contact.id;
		database.delete(MySQLiteHelper.TABLE_CONTACTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public void editContact(long id, String name, String number){
		// Edit contact
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_NUMBER, number);
		database.update(MySQLiteHelper.TABLE_CONTACTS, values, MySQLiteHelper.COLUMN_ID+"="+id , null);
	}
	
	private Contact cursorToContact(Cursor cursor){
		Contact contact = new Contact();
		contact.setId(cursor.getLong(0));
		contact.setAppId(cursor.getString(1));
		contact.setName(cursor.getString(2));
		contact.setNumber(cursor.getString(3));
		return contact;
	}
}
