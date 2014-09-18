package dyl.anjon.es.traintrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import dyl.anjon.es.traintrack.models.Journey;
import dyl.anjon.es.traintrack.models.JourneyLeg;
import dyl.anjon.es.traintrack.models.Location;
import dyl.anjon.es.traintrack.models.ScheduleLocation;

public class JourneyLegActivity extends Activity {

	private JourneyLeg journeyLeg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		final Context context = getApplicationContext();
		final int journeyLegId = intent.getIntExtra("journey_leg_id", 0);
		final int journeyId = intent.getIntExtra("journey_id", 0);

		if (journeyLegId != 0) {

			setContentView(R.layout.activity_journey_leg);
			journeyLeg = JourneyLeg.get(journeyLegId);

			TextView departureStation = (TextView) findViewById(R.id.departure_station);
			departureStation.setText(journeyLeg.getOrigin().toString());

			TextView departureTime = (TextView) findViewById(R.id.departure_time);
			departureTime.setText(journeyLeg.getDepartureTime());

			TextView departurePlatform = (TextView) findViewById(R.id.departure_platform);
			departurePlatform.setText(journeyLeg.getDeparturePlatform());

			TextView arrivalStation = (TextView) findViewById(R.id.arrival_station);
			arrivalStation.setText(journeyLeg.getDestination().toString());

			TextView arrivalTime = (TextView) findViewById(R.id.arrival_time);
			arrivalTime.setText(journeyLeg.getArrivalTime());

			TextView arrivalPlatform = (TextView) findViewById(R.id.arrival_platform);
			arrivalPlatform.setText(journeyLeg.getArrivalPlatform());

		} else {

			setContentView(R.layout.activity_journey_leg_form);
			final Button saveButton = (Button) findViewById(R.id.save);
			final int scheduleId = intent.getIntExtra("schedule_id", 0);

			int originScheduleLocationId = intent.getIntExtra(
					"origin_schedule_location_id", 0);
			final ScheduleLocation originScheduleLocation = ScheduleLocation
					.get(originScheduleLocationId);
			final Location origin = originScheduleLocation.getLocation();

			final TextView departureStation = (TextView) findViewById(R.id.departure_station);
			departureStation.setText(origin.toString());

			final TimePicker departureTime = (TimePicker) findViewById(R.id.departure_time);
			int departureHour = Integer.valueOf(originScheduleLocation
					.getTime().split(":")[0]);
			departureTime.setCurrentHour(departureHour);
			int departureMinute = Integer.valueOf(originScheduleLocation
					.getTime().split(":")[1]);
			departureTime.setCurrentMinute(departureMinute);

			final EditText departurePlatform = (EditText) findViewById(R.id.departure_platform);
			departurePlatform.setText(originScheduleLocation.getPlatform());

			final int destinationScheduleLocationId = intent.getIntExtra(
					"destination_schedule_location_id", 0);
			final ScheduleLocation destinationScheduleLocation = ScheduleLocation
					.get(destinationScheduleLocationId);
			final Location destination = destinationScheduleLocation
					.getLocation();

			final TextView arrivalStation = (TextView) findViewById(R.id.arrival_station);
			arrivalStation.setText(destination.toString());

			final TimePicker arrivalTime = (TimePicker) findViewById(R.id.arrival_time);
			int arrivalHour = Integer.valueOf(destinationScheduleLocation
					.getTime().split(":")[0]);
			arrivalTime.setCurrentHour(arrivalHour);
			int arrivalMinute = Integer.valueOf(destinationScheduleLocation
					.getTime().split(":")[1]);
			arrivalTime.setCurrentMinute(arrivalMinute);

			final EditText arrivalPlatform = (EditText) findViewById(R.id.arrival_platform);
			arrivalPlatform.setText(destinationScheduleLocation.getPlatform());

			saveButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Journey journey = new Journey();
					if (journeyId == 0) {
						journey = journey.save(context);
					} else {
						journey = Journey.get(context, journeyId);
					}

					JourneyLeg journeyLeg = new JourneyLeg();
					journeyLeg.setJourneyId(journey.getId());
					journeyLeg.setScheduleId(scheduleId);

					journeyLeg.setOriginId(origin.getId());
					journeyLeg.setOrigin(origin);
					journeyLeg.setDepartureTime(departureTime.getCurrentHour()
							+ ":" + departureTime.getCurrentMinute());
					journeyLeg.setDeparturePlatform(departurePlatform.getText()
							.toString());

					journeyLeg.setDestinationId(destination
							.getId());
					journeyLeg.setDestination(destination);
					journeyLeg.setArrivalTime(arrivalTime.getCurrentHour()
							+ ":" + arrivalTime.getCurrentMinute());
					journeyLeg.setArrivalPlatform(arrivalPlatform.getText()
							.toString());

					journeyLeg = journeyLeg.save(context);

					if (journeyId == 0) {
						Intent intent = new Intent().setClass(
								getApplicationContext(), JourneyActivity.class);
						intent.putExtra("journey_id", journey.getId());
						startActivity(intent);
					}

					if (getParent() == null) {
						setResult(Activity.RESULT_OK);
					} else {
						getParent().setResult(Activity.RESULT_OK);
					}
					finish();

				}
			});

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.journey_leg, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.delete_journey_leg:
			boolean success = journeyLeg.delete(getApplicationContext());
			if (success) {
				Toast.makeText(getApplicationContext(),
						"Journey leg was deleted", Toast.LENGTH_SHORT).show();
				Journey journey = Journey.get(getApplicationContext(), journeyLeg.getJourneyId());
				if (journey.getJourneyLegs().size() == 0) {
					journey.delete(getApplicationContext());
				}
				finish();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
