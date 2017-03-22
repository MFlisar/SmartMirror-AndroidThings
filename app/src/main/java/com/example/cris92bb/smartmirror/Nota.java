package com.example.cris92bb.smartmirror;

import android.annotation.SuppressLint;
import android.os.Parcel;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

/**
 * Created by Alessio on 21/03/2017.
 */

@SuppressLint("ParcelCreator")
public class Nota implements AsymmetricItem {

	private String date, title, description;

	public Nota(String date, String title, String description){
		this.date = date;
		this.title = title;
		this.description = description;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return this.getDate()+" "+this.getTitle();
	}

	@Override
	public int getColumnSpan() {
		return 1;
	}

	@Override
	public int getRowSpan() {
		return 1;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}
}
