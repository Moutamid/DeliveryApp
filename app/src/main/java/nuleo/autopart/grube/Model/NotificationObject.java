package nuleo.autopart.grube.Model;

import android.graphics.Bitmap;

public class NotificationObject {
	
	private int primaryKey;
	private String title;
	private String notificationText;
	private String uniqueKey;
	private int icon;
	
	public NotificationObject(){}

	public NotificationObject(String titleData, String textData, int id, int icon) {
		this.title = titleData;
		this.notificationText = textData;
		this.primaryKey = id;
		this.icon = icon;
	}

	public int getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public String getNotificationText() {
		return notificationText;
	}

	public void setNotificationText(String notificationText) {
		this.notificationText = notificationText;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
}
