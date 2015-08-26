package com.allnotes.app;

import com.allnotes.bean.User;

import android.app.Application;

public class App extends Application {
	
	private User user;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
