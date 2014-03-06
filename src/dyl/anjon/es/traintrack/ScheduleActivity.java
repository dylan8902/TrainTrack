package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import dyl.anjon.es.traintrack.adapters.ScheduleLocationRowAdapter;
import dyl.anjon.es.traintrack.models.Schedule;
import dyl.anjon.es.traintrack.models.ScheduleLocation;
import dyl.anjon.es.traintrack.models.Station;

public class ScheduleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);

		final Intent intent = getIntent();

		final int scheduleId = intent.getIntExtra("schedule_id", 0);
		Schedule schedule = Schedule.get(this, scheduleId);

		final int stationId = intent.getIntExtra("station_id", 0);
		Station station = Station.get(this, stationId);

		final TextView name = (TextView) findViewById(R.id.name);
		name.setText(schedule.getOrigin(this).getTime() + " "
				+ schedule.getOrigin(this).toString() + " to "
				+ schedule.getDestination(this).toString());

		final TextView toc = (TextView) findViewById(R.id.toc);
		toc.setText(schedule.getTrainOperatingCompany());

		final ScheduleLocationRowAdapter adapter = new ScheduleLocationRowAdapter(
				LayoutInflater.from(this), schedule.getScheduleLocations(this),
				station);
		final ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long x) {
				ScheduleLocation scheduleLocation = (ScheduleLocation) adapter
						.getItem(index);

				Intent intent = new Intent().setClass(getApplicationContext(),
						JourneyLegActivity.class);
				intent.putExtra("schedule_id", scheduleId);
				intent.putExtra("origin_station_id", stationId);
				intent.putExtra("destination_station_id",
						scheduleLocation.getStationId());
				startActivity(intent);
				return;
			}

		});

	}

}
