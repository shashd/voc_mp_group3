package org.python.stdlib.datetime;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimeDelta extends org.python.types.Object {

    @org.python.Attribute
    public org.python.Object days = __days__();

    @org.python.Attribute
    public org.python.Object seconds = __seconds__();

    @org.python.Attribute
    public org.python.Object microseconds = __microseconds__();

    @org.python.Attribute
    public org.python.Object min = __min__();
    @org.python.Attribute
    public org.python.Object max = __max__();
    @org.python.Attribute
    public org.python.Object resolution = __resolution__();

    @org.python.Method(__doc__ = "")
    public TimeDelta(org.python.Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs) {
	super();

	this.days = org.python.types.Int.getInt(0);
	this.seconds = org.python.types.Int.getInt(0);
	this.microseconds = org.python.types.Int.getInt(0);

	if (args.length > 7) {
	    throw new org.python.exceptions.TypeError("__new__() takes at most 7 arguments (" + args.length + " given)");
	}

	String[] allowed = { "days", "seconds", "microseconds", "milliseconds", "minutes", "hours", "weeks" };
	List<String> allowedList = Arrays.asList(allowed);
	if (!kwargs.isEmpty()) {
	    boolean correct = true;
	    for (java.lang.String key : kwargs.keySet()) {
		correct = allowedList.contains(key);
		if (!correct) {
		    throw new org.python.exceptions.TypeError(key + " is an invalid keuword argument for this function");

		}
	    }
	    if (args.length > 0) {
		if (kwargs.get("days") != null && args.length >= 1) {
		    throw new org.python.exceptions.TypeError("Argument given by name ('days') and position (1)");
		}

		if (kwargs.get("seconds") != null && args.length >= 2) {
		    throw new org.python.exceptions.TypeError("Argument given by name ('seconds') and position (2)");
		}

		if (kwargs.get("microseconds") != null && args.length >= 3) {
		    throw new org.python.exceptions.TypeError("Argument given by name ('microseconds') and position (3)");
		}
	    }
	}

	if (args.length == 3) {
	    this.days = args[0];
	    this.seconds = args[1];
	    this.microseconds = args[2];
	} else if (args.length == 2) {
	    this.days = args[0];
	    this.seconds = args[1];
	    this.microseconds = org.python.types.Int.getInt(0);

	} else if (args.length == 1) {
	    this.days = args[0];
	    this.seconds = org.python.types.Int.getInt(0);
	    this.microseconds = org.python.types.Int.getInt(0);

	}

	if (kwargs.get("weeks") != null) {
	    long weeks = ((org.python.types.Int) kwargs.get("weeks")).value;
	    long day = ((org.python.types.Int) this.days).value;
	    day = day + weeks * 7;
	    this.days = org.python.types.Int.getInt(day);
	}

	if (kwargs.get("hours") != null) {
	    long hours = ((org.python.types.Int) kwargs.get("hours")).value;
	    long second = ((org.python.types.Int) this.seconds).value;
	    second = second + hours * 3600;
	    this.seconds = org.python.types.Int.getInt(second);
	}

	if (kwargs.get("minutes") != null) {
	    long minutes = ((org.python.types.Int) kwargs.get("minutes")).value;
	    long minute = ((org.python.types.Int) this.seconds).value;
	    minute = minute + minutes * 60;
	    this.seconds = org.python.types.Int.getInt(minute);
	}

	if (kwargs.get("milliseconds") != null) {
	    long millisecond = ((org.python.types.Int) kwargs.get("milliseconds")).value;
	    long mili = ((org.python.types.Int) this.microseconds).value;
	    mili = mili + millisecond * 100;
	    this.microseconds = org.python.types.Int.getInt(mili);
	}
    }

    @org.python.Method(__doc__ = "returns days")
    public org.python.types.Str __days__() {
	return new org.python.types.Str(this.days + "");
    }

    @org.python.Method(__doc__ = "returns month")
    public org.python.types.Str __seconds__() {
	return new org.python.types.Str(this.seconds + "");
    }

    @org.python.Method(__doc__ = "returns day")
    public org.python.types.Str __microseconds__() {
	return new org.python.types.Str(this.microseconds + "");
    }

    @org.python.Method()
    public org.python.Object __min__() {

	return new org.python.types.Str("-999999 days, 0:00:00");
    }

    @org.python.Method()
    public org.python.Object __max__() {
	return new org.python.types.Str("9999999 days, 23:59:59.999999");
    }

    @org.python.Method()
    public org.python.Object __resolution__() {
	return new org.python.types.Str("0:00:00.000001");
    }

    @org.python.Method()
    public org.python.types.Str total_seconds() {
	long days = (((org.python.types.Int) this.days).value) * 24 * 3600;
	long sum_seconds = days + (((org.python.types.Int) this.seconds).value);
	long microseconds = (((org.python.types.Int) this.microseconds).value);
	String micro = "";
	if (microseconds == 0) {
	    micro = "0";
	} else if (microseconds < 10) {
	    micro = "00000" + microseconds;
	} else if (microseconds < 100) {
	    micro = "0000" + microseconds;
	} else if (microseconds < 1000) {
	    micro = "000" + microseconds;
	} else if (microseconds < 10000) {
	    micro = "00" + microseconds;
	} else if (microseconds < 100000) {
	    micro = "0" + microseconds;
	} else {
	    micro = "" + microseconds;
	}
	String returnStr = ("" + sum_seconds + "." + micro);
	return new org.python.types.Str(returnStr);
    }

    @org.python.Method(__doc__ = "", args = { "other" })
    public org.python.Object __add__(org.python.Object other) {
	long thisDays = ((org.python.types.Int) this.days).value;
	TimeDelta otherObject = (org.python.stdlib.datetime.TimeDelta) other;
	long otherDays = ((org.python.types.Int) otherObject.days).value;
	long thisSeconds = ((org.python.types.Int) this.seconds).value;
	long otherSeconds = ((org.python.types.Int) otherObject.seconds).value;
	long thisMicroseconds = ((org.python.types.Int) this.microseconds).value;
	long otherMicroSeconds = ((org.python.types.Int) otherObject.microseconds).value;
	long sumDays = thisDays + otherDays;
	long sumSeconds = thisSeconds + otherSeconds;
	long sumMicroseconds = thisMicroseconds + otherMicroSeconds;
	org.python.Object[] args = { org.python.types.Int.getInt(sumDays), org.python.types.Int.getInt(sumSeconds), org.python.types.Int.getInt(sumMicroseconds) };
	TimeDelta TD = new TimeDelta(args, Collections.EMPTY_MAP);
	return TD;
    }

    public org.python.Object __pos__() {
	long otherSeconds = ((org.python.types.Int) this.seconds).value;
	long otherMicroSeconds = ((org.python.types.Int) this.microseconds).value;
	long otherDays = ((org.python.types.Int) this.days).value;
	org.python.Object[] args = { org.python.types.Int.getInt(otherDays), org.python.types.Int.getInt(otherSeconds), org.python.types.Int.getInt(otherMicroSeconds) };
	TimeDelta TD = new TimeDelta(args, Collections.EMPTY_MAP);
	return TD;
    }

    public org.python.types.Str __str__() {
	long dayslong = ((org.python.types.Int) this.days).value;
	String days = Long.toString(dayslong);
	long seconds = ((org.python.types.Int) this.seconds).value;
	long microseconds = ((org.python.types.Int) this.microseconds).value;
	String returnStr = days + " days, " + "seconds: " + seconds + ", microseconds: " + microseconds;
	return new org.python.types.Str(returnStr);
    }

    @org.python.Method(__doc__ = "")
    public static org.python.Object constant_4() {
	return org.python.types.Int.getInt(4);
    }
}
