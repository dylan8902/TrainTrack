package dyl.anjon.es.traintrack.models;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import dyl.anjon.es.traintrack.db.DatabaseHandler;

public class Schedule {

	private int id;
	private String trainOperatingCompany;

	public Schedule() {
	}

	/**
	 * @return the schedule id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the schedule id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name of the company operating the train
	 */
	public String getTrainOperatingCompany() {
		return trainOperatingCompany;
	}

	/**
	 * @param trainOperatingCompany
	 *            the name of the company running the train
	 */
	public void setTrainOperatingCompany(String trainOperatingCompany) {
		this.trainOperatingCompany = trainOperatingCompany;
	}

	/**
	 * @return the origin
	 */
	public ScheduleLocation getOrigin(Context context) {
		if (this.getScheduleLocations(context).isEmpty()) {
			Station station = new Station("", "Unknown");
			ScheduleLocation scheduleLocation = new ScheduleLocation();
			scheduleLocation.setStation(station);
			scheduleLocation.setTime("?");
			scheduleLocation.setPlatform("?");
			return scheduleLocation;
		}

		return this.getScheduleLocations(context).get(0);
	}

	/**
	 * @return the destination
	 */
	public ScheduleLocation getDestination(Context context) {
		if (this.getScheduleLocations(context).isEmpty()) {
			Station station = new Station("", "Unknown");
			ScheduleLocation scheduleLocation = new ScheduleLocation();
			scheduleLocation.setStation(station);
			scheduleLocation.setTime("?");
			scheduleLocation.setPlatform("?");
			return scheduleLocation;
		}

		int last = this.getScheduleLocations(context).size();
		return this.getScheduleLocations(context).get(last - 1);
	}

	/**
	 * @param station
	 * @return ScheduleLocation where the station is the station provided
	 */
	public ScheduleLocation at(Context context, Station station) {
		for (int i = 0; i < this.getScheduleLocations(context).size(); i++) {
			if (this.getScheduleLocations(context).get(i).getStation()
					.equals(station))
				return this.getScheduleLocations(context).get(i);
		}
		return null;
	}

	/**
	 * @return the schedule locations
	 */
	public ArrayList<ScheduleLocation> getScheduleLocations(Context context) {
		ArrayList<ScheduleLocation> scheduleLocations = new ArrayList<ScheduleLocation>();

		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getWritableDatabase();

		Cursor cursor = db.query("schedule_locations", new String[] { "id",
				"schedule_id", "station_id", "time", "platform" },
				"schedule_id = ?", new String[] { String.valueOf(this.id) },
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				ScheduleLocation scheduleLocation = new ScheduleLocation();
				scheduleLocation.setId(cursor.getInt(0));
				scheduleLocation.setScheduleId(cursor.getInt(1));
				scheduleLocation.setStationId(cursor.getInt(2));
				scheduleLocation.setStation(Station.get(context,
						cursor.getInt(2)));
				scheduleLocation.setTime(cursor.getString(3));
				scheduleLocation.setPlatform(cursor.getString(4));
				scheduleLocations.add(scheduleLocation);
			} while (cursor.moveToNext());
		}
		return scheduleLocations;
	}

	/**
	 * @param context
	 * @param id
	 * @return the schedule selected
	 */
	public static Schedule get(Context context, int id) {
		DatabaseHandler dbh = new DatabaseHandler(context);
		SQLiteDatabase db = dbh.getWritableDatabase();

		Cursor cursor = db.query("schedules",
				new String[] { "id", "toc_name" }, "id = ?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		} else {
			return null;
		}

		Schedule schedule = new Schedule();
		schedule.setId(cursor.getInt(0));
		schedule.setTrainOperatingCompany(cursor.getString(1));

		return schedule;
	}

}
