package dyl.anjon.es.traintrack.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.traintrack.R;
import dyl.anjon.es.traintrack.api.ServiceItem;
import dyl.anjon.es.traintrack.models.Station;

public class ServiceItemRowAdapter extends BaseAdapter {

	private ArrayList<ServiceItem> serviceItems;
	private Station station;
	private Context context;

	/**
	 * @param inflater
	 * @param schedules
	 *            to be displayed
	 * @param station
	 *            the service is being seen from
	 */
	public ServiceItemRowAdapter(ArrayList<ServiceItem> serviceItems,
			Station station, Context context) {
		this.serviceItems = serviceItems;
		this.station = station;
		this.context = context;
	}

	public int getCount() {
		return serviceItems.size();
	}

	public ServiceItem getItem(int position) {
		return serviceItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.row_service, null);
			holder = new ViewHolder();
			holder.scheduledTime = (TextView) convertView
					.findViewById(R.id.scheduled_time);
			holder.estimatedTime = (TextView) convertView
					.findViewById(R.id.estimated_time);
			holder.destination = (TextView) convertView
					.findViewById(R.id.destination);
			holder.origin = (TextView) convertView.findViewById(R.id.origin);
			holder.platform = (TextView) convertView
					.findViewById(R.id.platform);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ServiceItem serviceItem = serviceItems.get(position);
		holder.scheduledTime.setText(serviceItem.getScheduledTime());
		holder.estimatedTime.setText(serviceItem.getEstimatedTime());
		if (serviceItem.terminatesHere()) {
			holder.destination.setText("Terminates Here");
			if (serviceItem.isDelayedArriving()) {
				holder.estimatedTime.setTextColor(Color.RED);
				holder.scheduledTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			} else {
				holder.estimatedTime.setTextColor(Color.BLACK);
				holder.scheduledTime.setPaintFlags(0);
			}
		} else {
			Station destination = serviceItem.getDestination();
			if (destination != null) {
				holder.destination.setText(destination.toString());
			}
			if (serviceItem.isDelayedDeparting()) {
				holder.estimatedTime.setTextColor(Color.RED);
				holder.scheduledTime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			} else {
				holder.estimatedTime.setTextColor(Color.BLACK);
				holder.scheduledTime.setPaintFlags(0);
			}
		}
		if (serviceItem.startsHere()) {
			holder.origin.setText("Starts Here");
		} else if (serviceItem.getOrigin() != null) {
			holder.origin.setText("From " + serviceItem.getOrigin().toString());
		}
		holder.origin.setText(holder.origin.getText() + " - "
				+ serviceItem.getOperator());
		holder.platform.setText(serviceItem.getPlatform());

		return convertView;
	}

	static class ViewHolder {
		TextView scheduledTime;
		TextView estimatedTime;
		TextView destination;
		TextView origin;
		TextView platform;
	}
}
