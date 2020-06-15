package com.team12.goodnote.utils;

import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;


public class TimeUtils{
	public static final PrettyTime prettyTime;

	static{
		prettyTime = new PrettyTime();
	}

	public static String getHumanReadableTimeDiff(Date lastTime){
		if (lastTime == null) return "";
		prettyTime.setReference(new Date());
		return prettyTime.format(lastTime);
	}
}
