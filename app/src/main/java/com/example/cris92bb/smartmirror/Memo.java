package com.example.cris92bb.smartmirror;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.System.in;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Memo extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private notesListAdapter adapter;
	AsymmetricGridViewAdapter asymmetricAdapter;
	private TextView text_month;
	private AsymmetricGridView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memo);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
	    text_month = (TextView)findViewById(R.id.text_month);

        listView = (AsymmetricGridView) findViewById(R.id.noteList);

	    Log.v("MEMO","Lista");

        // Choose your own preferred column width
        listView.setRequestedColumnWidth(Utils.dpToPx(this.getApplicationContext(), 120));
        final ArrayList<Nota> items = new ArrayList<>();

        adapter = new notesListAdapter(this.getApplicationContext(), items);
        asymmetricAdapter = new AsymmetricGridViewAdapter<>(this, listView, adapter);
        listView.setAdapter(asymmetricAdapter);
	    listView.setAllowReordering(true);

        final String TAG = "Memo";

        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.calendarView);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.WHITE, 1433701251000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.WHITE, 1433704251000L, "asdasdasda asdasdasd");
        compactCalendarView.addEvent(ev2);

	    Event ev3 = new Event(Color.WHITE, getDateForCalendar("23 03 2017 14:23:00"), "Appuntamento medico");
	    compactCalendarView.addEvent(ev3);

	    Event ev4 = new Event(Color.WHITE, getDateForCalendar("12 03 2017 14:00:00"), "Fare la spesa");
	    compactCalendarView.addEvent(ev4);

	    Event ev5 = new Event(Color.WHITE, getDateForCalendar("12 04 2017 14:00:00"), "prova2 la spesa");
	    compactCalendarView.addEvent(ev5);

	    Event ev6 = new Event(Color.WHITE, getDateForCalendar("19 04 2017 14:00:00"), "prova3 la spesa");
	    compactCalendarView.addEvent(ev6);


	    List<Event> monthEvents = compactCalendarView.getEventsForMonth(getMonthForEvents(compactCalendarView.getFirstDayOfCurrentMonth().getMonth(), compactCalendarView.getFirstDayOfCurrentMonth().getYear()));
        for (Event event : monthEvents){
			Nota n = new Nota(getDateStringFromMillisec(event.getTimeInMillis()),((String)event.getData()).split(" ")[0],(String)event.getData());
	        items.add(n);
	    }

	    setMonthText(compactCalendarView.getFirstDayOfCurrentMonth());

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
	            setMonthText(firstDayOfNewMonth);
	            items.clear();
				//asymmetricAdapter.notifyDataSetChanged();

	            List<Event> monthEvents = compactCalendarView.getEventsForMonth(getMonthForEvents(firstDayOfNewMonth.getMonth(),firstDayOfNewMonth.getYear()));
	            for (Event event : monthEvents){
		            Nota n = new Nota(getDateStringFromMillisec(event.getTimeInMillis()),((String)event.getData()).split(" ")[0],(String)event.getData());
		            items.add(n);
	            }
	            Log.v("MEMO", asymmetricAdapter.getCount()+" Count of adapter");

	            asymmetricAdapter.notifyDataSetChanged();

            }
        });

    }

    private Date getMonthForEvents(int month, int year){//Formato data: MM yyyy
	    /*Calendar c = Calendar.getInstance();
	    int year = c.get(Calendar.YEAR);*/

	    String strDate = "0"+(month+1)+" "+(1900+year);
	    SimpleDateFormat sdf = new SimpleDateFormat("MM yyyy");
	    Date mDate = null;
	    try {
		    mDate = sdf.parse(strDate);
	    } catch (ParseException e) {
		    e.printStackTrace();
	    }

	    return mDate;
    }

    private String getDateStringFromMillisec(long millisec){
	    Date mDate = new Date(millisec);
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	    return sdf.format(mDate);
    }

    private long getDateForCalendar(String date){//Formato data: dd MM yyyy HH:mm:ss
		long timeInMilliseconds = 0;
	    date = date+" GMT+01:00";
	    SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss z");
	    try {
		    Date mDate = sdf.parse(date);
		    timeInMilliseconds = mDate.getTime();
	    } catch (ParseException e) {
		    e.printStackTrace();
	    }

	    return timeInMilliseconds;
    }

    private void setMonthText(Date data){
	    switch(data.getMonth()){
		    case 0:
		    	text_month.setText("Gennaio "+(1900+data.getYear()));
		    	break;
		    case 1:
			    text_month.setText("Febbraio "+(1900+data.getYear()));
			    break;
		    case 2:
			    text_month.setText("Marzo "+(1900+data.getYear()));
			    break;
		    case 3:
			    text_month.setText("Aprile "+(1900+data.getYear()));
			    break;
		    case 4:
			    text_month.setText("Maggio "+(1900+data.getYear()));
			    break;
		    case 5:
			    text_month.setText("Giugno "+(1900+data.getYear()));
			    break;
		    case 6:
			    text_month.setText("Luglio "+(1900+data.getYear()));
			    break;
		    case 7:
			    text_month.setText("Agosto "+(1900+data.getYear()));
			    break;
		    case 8:
			    text_month.setText("Settembre "+(1900+data.getYear()));
			    break;
		    case 9:
			    text_month.setText("Ottobre "+(1900+data.getYear()));
			    break;
		    case 10:
			    text_month.setText("Novembre "+(1900+data.getYear()));
			    break;
		    case 11:
			    text_month.setText("Dicembre "+(1900+data.getYear()));
			    break;
	    }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
