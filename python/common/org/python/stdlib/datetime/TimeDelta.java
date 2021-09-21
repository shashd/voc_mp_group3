package org.python.stdlib.datetime;

import org.python.types.Int;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

        String[] argumentNames = { "days", "seconds", "microseconds", "milliseconds", "minutes", "hours", "weeks" };
        List<String> allowedList = Arrays.asList(argumentNames);
        if (!kwargs.isEmpty()) {
            for (java.lang.String key : kwargs.keySet()) {
                boolean correct = allowedList.contains(key);
                if (!correct) {
                    throw new org.python.exceptions.TypeError(key + " is an invalid keyword argument for this function");
                }
            }
            for (int i=0; i < args.length;i++) {
                // If defined by position and kwarg, throw exception
                if (kwargs.get(argumentNames[i]) != null) {
                    throw new org.python.exceptions.TypeError("argument for __new__() given by name ('" + argumentNames[i] + "') and position (" + (i+1) + ")");
                }
            }
        }
        // Make a copy of kwargs so we can add things to it
        kwargs = new HashMap<>(kwargs);

        // Add any non kwarg argument as a kwarg
        for (int i=0; i < args.length;i++) {
            kwargs.put(argumentNames[i], args[i]);
        }

        if (kwargs.get("days") != null) {
            this.days = kwargs.get("days");
        }
        if (kwargs.get("seconds") != null) {
            this.seconds = kwargs.get("seconds");
        }
        if (kwargs.get("microseconds") != null) {
            this.microseconds = kwargs.get("microseconds");
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

        normalize();
    }

    @org.python.Method(__doc__ = "returns days")
    public org.python.types.Str __days__() {
	return new org.python.types.Str(this.days + "");
    }

    @org.python.Method(__doc__ = "returns seconds")
    public org.python.types.Str __seconds__() {
	return new org.python.types.Str(this.seconds + "");
    }

    @org.python.Method(__doc__ = "returns microseconds")
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

    /**
     * Keeps numbers well defined in their range
     */
    private void normalize() {
        long microSeconds = ((Int)this.microseconds).value;
        long seconds = ((Int)this.seconds).value;
        long days = ((Int)this.days).value;

        seconds += microSeconds / 1_000_000;
        microSeconds = microSeconds % 1_000_000;

        /// 86400 seconds in a day
        days += seconds / 86400;
        seconds = seconds % 86400;

        this.microseconds = Int.getInt(microSeconds);
        this.seconds = Int.getInt(seconds);
        this.days = Int.getInt(days);
    }
}
