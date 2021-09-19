package org.python.stdlib.datetime;

import org.python.types.Object;

import java.util.Collections;
import java.util.HashMap;

public class DateTime extends org.python.types.Object {
    private final int YEAR_INDEX = 0;
    private final int MONTH_INDEX = 1;
    private final int DAY_INDEX = 2;
    private final int HOUR_INDEX = 3;
    private final int MINUTE_INDEX = 4;
    private final int SECOND_INDEX = 5;
    private final int MICROSECOND_INDEX = 6;

    private final int MIN_YEAR = 1;
    private final int MAX_YEAR = 9999;

    private Long[] timeUnits = { 0l, 0l, 0l, 0l, 0l, 0l, 0l };

    @org.python.Attribute
    public final org.python.Object year;

    @org.python.Attribute
    public final org.python.Object month;

    @org.python.Attribute
    public final org.python.Object day;

    @org.python.Attribute
    public final org.python.Object hour;

    @org.python.Attribute
    public final org.python.Object minute;

    @org.python.Attribute
    public final org.python.Object second;

    @org.python.Attribute
    public final org.python.Object microsecond;

    @org.python.Attribute
    public static final org.python.Object min = __min__();

    @org.python.Attribute
    public static final org.python.Object max = __max__();

    public DateTime(org.python.Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs) {
	super();
	String[] keys = { "year", "month", "day", "hour", "minute", "second", "microsecond" };
	boolean kwargsIsUsed = false;
	int keyIndex = 0;
	int argIndex = 0;

	for (String key : keys) {
	    if (kwargs.get(key) != null) {
            if (kwargs.get(key) instanceof org.python.types.Str){
                throw new org.python.exceptions.TypeError("an integer is required (got type str)");
            }
            if (kwargs.get(key) instanceof org.python.types.Float){
                throw new org.python.exceptions.TypeError("integer argument expected, got float");
            }
		    this.timeUnits[keyIndex] = ((org.python.types.Int) kwargs.get(key)).value;
		    kwargsIsUsed = true;
	    } else if (args.length > argIndex) {
		    if (kwargsIsUsed) {
                throw new org.python.exceptions.SyntaxError("positional argument follows keyword argument");
            }
            if (args[argIndex] instanceof org.python.types.Str){
                throw new org.python.exceptions.TypeError("an integer is required (got type str)");
            }
            if (args[argIndex] instanceof org.python.types.Float) {
                throw new org.python.exceptions.TypeError("integer argument expected, got float");
            }
		    this.timeUnits[keyIndex] = ((org.python.types.Int) args[argIndex]).value;
		    argIndex++;
	    } else if (keyIndex < 3) {
		    throw new org.python.exceptions.TypeError("Required argument '" + keys[keyIndex] + "' (pos " + (keyIndex + 1) + ") not found");
	    }
	    keyIndex++;
	}

	if (this.timeUnits[YEAR_INDEX] < MIN_YEAR || this.timeUnits[YEAR_INDEX] > MAX_YEAR) {
	    throw new org.python.exceptions.ValueError("year " + this.timeUnits[YEAR_INDEX] + "is out of range");
	}

	if (this.timeUnits[MONTH_INDEX] < 1 || this.timeUnits[MONTH_INDEX] > 12) {
	    throw new org.python.exceptions.ValueError("month " + this.timeUnits[MONTH_INDEX] + "is out of range");
	}
	if (this.timeUnits[DAY_INDEX] < 1 || this.timeUnits[DAY_INDEX] > 31) {
	    throw new org.python.exceptions.ValueError("day " + this.timeUnits[DAY_INDEX] + "is out of range");
	}

	if (this.timeUnits[HOUR_INDEX] < 0 || this.timeUnits[HOUR_INDEX] > 24) {
	    throw new org.python.exceptions.ValueError("hour " + this.timeUnits[HOUR_INDEX] + "is out of range");
	}

	if (this.timeUnits[MINUTE_INDEX] < 0 || this.timeUnits[MINUTE_INDEX] > 60) {
	    throw new org.python.exceptions.ValueError("minute " + this.timeUnits[MINUTE_INDEX] + "is out of range");
	}

	if (this.timeUnits[SECOND_INDEX] < 0 || this.timeUnits[SECOND_INDEX] > 60) {
	    throw new org.python.exceptions.ValueError("second " + this.timeUnits[SECOND_INDEX] + "is out of range");
	}

	if (this.timeUnits[MICROSECOND_INDEX] < 0 || this.timeUnits[MICROSECOND_INDEX] > 999999) {
	    throw new org.python.exceptions.ValueError("microsecond " + this.timeUnits[MICROSECOND_INDEX] + "is out of range");
	}

	this.year = __year__();
	this.month = __month__();
	this.day = __day__();
	this.hour = __hour__();
	this.minute = __minute__();
	this.second = __second__();
	this.microsecond = __microsecond__();
    }

    public org.python.types.Str __str__() {
	String year = Long.toString(this.timeUnits[YEAR_INDEX]);
	while (year.length() < 4)
	    year = "0" + year;

	String month = Long.toString(this.timeUnits[MONTH_INDEX]);
	while (month.length() < 2)
	    month = "0" + month;

	String day = Long.toString(this.timeUnits[DAY_INDEX]);
	while (day.length() < 2)
	    day = "0" + day;

	String hour = this.timeUnits[HOUR_INDEX] != 0 ? Long.toString(this.timeUnits[HOUR_INDEX]) : "00";
	while (hour.length() < 2)
	    hour = "0" + hour;

	String minute = this.timeUnits[MINUTE_INDEX] != 0 ? Long.toString(this.timeUnits[MINUTE_INDEX]) : "00";
	while (minute.length() < 2)
	    minute = "0" + minute;

	String second = this.timeUnits[SECOND_INDEX] != 0 ? Long.toString(this.timeUnits[SECOND_INDEX]) : "00";
	while (second.length() < 2)
	    second = "0" + second;

	String microsecond = this.timeUnits[MICROSECOND_INDEX] != 0 ? Long.toString(this.timeUnits[MICROSECOND_INDEX]) : "";
	while (microsecond.length() < 6 && microsecond.length() != 0)
	    microsecond = "0" + microsecond;

	String returnStr = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;

	returnStr += microsecond.length() > 0 ? "." + microsecond : "";
	return new org.python.types.Str(returnStr);
    }

    @org.python.Method(__doc__ = "")
    public org.python.Object date() {
	org.python.Object[] args = { org.python.types.Int.getInt(this.timeUnits[YEAR_INDEX]), org.python.types.Int.getInt(this.timeUnits[MONTH_INDEX]),
		org.python.types.Int.getInt(this.timeUnits[DAY_INDEX]) };
	return new Date(args, Collections.emptyMap());
    }

    @org.python.Method(__doc__ = "")
    public static org.python.Object today() {
	java.time.LocalDateTime today = java.time.LocalDateTime.now();
	org.python.Object[] args = { org.python.types.Int.getInt(today.getYear()), org.python.types.Int.getInt(today.getMonth().getValue()),
		org.python.types.Int.getInt(today.getDayOfMonth()), org.python.types.Int.getInt(today.getHour()), org.python.types.Int.getInt(today.getMinute()),
		org.python.types.Int.getInt(today.getSecond()), org.python.types.Int.getInt(today.getNano() / 1000) };
	return new DateTime(args, Collections.emptyMap());
    }

    @org.python.Method(__doc__ = "returns year")
    public org.python.types.Str __year__() {
	return new org.python.types.Str(this.timeUnits[YEAR_INDEX] + "");
    }

    @org.python.Method(__doc__ = "returns month")
    public org.python.types.Str __month__() {
	return new org.python.types.Str(this.timeUnits[MONTH_INDEX] + "");
    }

    @org.python.Method(__doc__ = "returns day")
    public org.python.types.Str __day__() {
	return new org.python.types.Str(this.timeUnits[DAY_INDEX] + "");
    }

    @org.python.Method(__doc__ = "returns hour")
    public org.python.types.Str __hour__() {
	return new org.python.types.Str(this.timeUnits[HOUR_INDEX] + "");
    }

    @org.python.Method(__doc__ = "returns minute")
    public org.python.types.Str __minute__() {
	return new org.python.types.Str(this.timeUnits[MINUTE_INDEX] + "");
    }

    @org.python.Method(__doc__ = "returns second")
    public org.python.types.Str __second__() {
	return new org.python.types.Str(this.timeUnits[SECOND_INDEX] + "");
    }

    @org.python.Method(__doc__ = "returns microsecond")
    public org.python.types.Str __microsecond__() {
	return new org.python.types.Str(this.timeUnits[MICROSECOND_INDEX] + "");
    }

    @org.python.Method(__doc__ = "")
    private static org.python.Object __min__() {
	org.python.types.Int year = org.python.types.Int.getInt(1);
	org.python.types.Int month = org.python.types.Int.getInt(1);
	org.python.types.Int day = org.python.types.Int.getInt(1);

	org.python.Object[] args = { year, month, day };
	return new DateTime(args, Collections.emptyMap());
    }

    @org.python.Method(__doc__ = "")
    private static org.python.Object __max__() {
	org.python.types.Int year = org.python.types.Int.getInt(9999);
	org.python.types.Int month = org.python.types.Int.getInt(12);
	org.python.types.Int day = org.python.types.Int.getInt(31);
	org.python.types.Int hour = org.python.types.Int.getInt(23);
	org.python.types.Int minute = org.python.types.Int.getInt(59);
	org.python.types.Int second = org.python.types.Int.getInt(59);
	org.python.types.Int microsecond = org.python.types.Int.getInt(999999);

	org.python.Object[] args = { year, month, day, hour, minute, second, microsecond };
	return new DateTime(args, Collections.emptyMap());
    }

    @org.python.Method(__doc__ = "")
    public org.python.Object weekday() {
	double y = ((org.python.types.Int) this.year.__int__()).value;
	double m = ((org.python.types.Int) this.month.__int__()).value;
	double d = ((org.python.types.Int) this.day.__int__()).value;

	java.util.Date myCalendar = new java.util.GregorianCalendar((int) y, (int) m - 1, (int) d).getTime();
	java.util.Calendar c = java.util.Calendar.getInstance();
	c.setTime(myCalendar);
	int day = c.get(java.util.Calendar.DAY_OF_WEEK);
	int[] convertToPython = { 6, 0, 1, 2, 3, 4, 5 };
	return org.python.types.Int.getInt(convertToPython[day - 1]);

    }

    @org.python.Method(
        __doc__ = "Return self < dateTime.",
        args = {"other"}
    )
    public org.python.Object __lt__(org.python.Object other){
        if (other instanceof DateTime){

            if (((org.python.types.Bool)this.__year__().__lt__(((DateTime) other).__year__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__year__().__gt__(((DateTime) other).__year__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__month__().__lt__(((DateTime) other).__month__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__month__().__gt__(((DateTime) other).__month__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__day__().__lt__(((DateTime) other).__day__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__day__().__gt__(((DateTime) other).__day__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__hour__().__lt__(((DateTime) other).__hour__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__hour__().__gt__(((DateTime) other).__hour__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__minute__().__lt__(((DateTime) other).__minute__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__minute__().__gt__(((DateTime) other).__minute__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__second__().__lt__(((DateTime) other).__second__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__second__().__gt__(((DateTime) other).__second__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__microsecond__().__lt__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__microsecond__().__gt__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(false);
            }
            return org.python.types.Bool.getBool(false);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(
        __doc__ = "Return self <= dateTime.",
        args = {"other"}
    )
    public org.python.Object __le__(org.python.Object other){
        if (other instanceof DateTime){

            if (((org.python.types.Bool)this.__year__().__le__(((DateTime) other).__year__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__year__().__gt__(((DateTime) other).__year__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__month__().__le__(((DateTime) other).__month__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__month__().__gt__(((DateTime) other).__month__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__day__().__le__(((DateTime) other).__day__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__day__().__gt__(((DateTime) other).__day__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__hour__().__le__(((DateTime) other).__hour__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__hour__().__gt__(((DateTime) other).__hour__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__minute__().__le__(((DateTime) other).__minute__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__minute__().__gt__(((DateTime) other).__minute__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__second__().__le__(((DateTime) other).__second__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__second__().__gt__(((DateTime) other).__second__())).value){
                return org.python.types.Bool.getBool(false);
            }

            if (((org.python.types.Bool)this.__microsecond__().__le__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(true);
            } else if (((org.python.types.Bool)this.__microsecond__().__gt__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(false);
            }
            return org.python.types.Bool.getBool(false);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }


    @org.python.Method(
        __doc__ = "Return self == dateTime.",
        args = {"other"}
    )
    public org.python.Object __eq__(org.python.Object other){
        if (other instanceof DateTime){
            if (((org.python.types.Bool)this.__year__().__eq__(((DateTime) other).__year__())).value
            && ((org.python.types.Bool)this.__month__().__eq__(((DateTime) other).__month__())).value
            && ((org.python.types.Bool)this.__day__().__eq__(((DateTime) other).__day__())).value
            && ((org.python.types.Bool)this.__hour__().__eq__(((DateTime) other).__hour__())).value
            && ((org.python.types.Bool)this.__minute__().__eq__(((DateTime) other).__minute__())).value
            && ((org.python.types.Bool)this.__second__().__eq__(((DateTime) other).__second__())).value
            && ((org.python.types.Bool)this.__microsecond__().__eq__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(true);
            }
            return org.python.types.Bool.getBool(false);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(
        __doc__ = "Return self > dateTime.",
        args = {"other"}
    )
    public org.python.Object __gt__(org.python.Object other){
        if (other instanceof DateTime){

            if (((org.python.types.Bool)this.__year__().__lt__(((DateTime) other).__year__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__year__().__gt__(((DateTime) other).__year__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__month__().__lt__(((DateTime) other).__month__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__month__().__gt__(((DateTime) other).__month__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__day__().__lt__(((DateTime) other).__day__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__day__().__gt__(((DateTime) other).__day__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__hour__().__lt__(((DateTime) other).__hour__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__hour__().__gt__(((DateTime) other).__hour__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__minute__().__lt__(((DateTime) other).__minute__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__minute__().__gt__(((DateTime) other).__minute__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__second__().__lt__(((DateTime) other).__second__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__second__().__gt__(((DateTime) other).__second__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__microsecond__().__lt__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__microsecond__().__gt__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(true);
            }
            return org.python.types.Bool.getBool(false);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(
        __doc__ = "Return self >= dateTime.",
        args = {"other"}
    )
    public org.python.Object __ge__(org.python.Object other){
        if (other instanceof DateTime){

            if (((org.python.types.Bool)this.__year__().__lt__(((DateTime) other).__year__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__year__().__ge__(((DateTime) other).__year__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__month__().__lt__(((DateTime) other).__month__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__month__().__ge__(((DateTime) other).__month__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__day__().__lt__(((DateTime) other).__day__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__day__().__ge__(((DateTime) other).__day__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__hour__().__lt__(((DateTime) other).__hour__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__hour__().__ge__(((DateTime) other).__hour__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__minute__().__lt__(((DateTime) other).__minute__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__minute__().__ge__(((DateTime) other).__minute__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__second__().__lt__(((DateTime) other).__second__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__second__().__ge__(((DateTime) other).__second__())).value){
                return org.python.types.Bool.getBool(true);
            }

            if (((org.python.types.Bool)this.__microsecond__().__lt__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(false);
            } else if (((org.python.types.Bool)this.__microsecond__().__ge__(((DateTime) other).__microsecond__())).value){
                return org.python.types.Bool.getBool(true);
            }
            return org.python.types.Bool.getBool(false);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }


    @org.python.Method(
        __doc__ = "Return a datetime corresponding to a date_string in one of the formats emitted by " +
            "date.isoformat() and datetime.isoformat().",
        args = {"other"}
    )
    public static org.python.Object fromisoformat(org.python.Object other){
        java.lang.String str = ((org.python.types.Str) other).value;
        Object[] args = new Object[]{};
        if (str.split("T").length <= 2){
            str = str.replace("T"," ");
            java.lang.String[] splitStr = str.split(" ");
            // only date
            if (splitStr.length == 1){
                java.lang.String[] date = splitStr[0].split("-");
                if (date.length!=3 ){
                    throw new org.python.exceptions.ValueError("Invalid isoformat string: " + str);
                }
                long year = java.lang.Long.parseLong(date[0]);
                long month = java.lang.Long.parseLong(date[1]);
                long day = java.lang.Long.parseLong(date[2]);
                args = new Object[]{org.python.types.Int.getInt(year),
                                org.python.types.Int.getInt(month), org.python.types.Int.getInt(day)};

            }
            // both date and time
            else {
                java.lang.String[] date = splitStr[0].split("-");
                java.lang.String[] time = splitStr[1].split(":");
                if (date.length != 3 || time.length != 3){
                    throw new org.python.exceptions.ValueError("Invalid isoformat string: " + str);
                }
                long year = java.lang.Long.parseLong(date[0]);
                long month = java.lang.Long.parseLong(date[1]);
                long day = java.lang.Long.parseLong(date[2]);
                long hour = java.lang.Long.parseLong(time[0]);
                long minute = java.lang.Long.parseLong(time[1]);
                long second = 0L;
                long microsecond = 0L;
                java.lang.String[] seconds = time[2].split("\\.");
                // check whether microseconds exist
                if (seconds.length == 2){
                    second = Long.parseLong(seconds[0]);
                    microsecond = Long.parseLong(seconds[1]);
                } else{
                    second = Long.parseLong(time[2]);
                }
                args =  new Object[]{org.python.types.Int.getInt(year), org.python.types.Int.getInt(month),
                                     org.python.types.Int.getInt(day),org.python.types.Int.getInt(hour),
                                    org.python.types.Int.getInt(minute),org.python.types.Int.getInt(second),
                                    org.python.types.Int.getInt(microsecond)};
            }
        } else {
            throw new org.python.exceptions.ValueError("Invalid isoformat string: " + str);
        }
        return new DateTime(args,Collections.emptyMap());
    }

    @org.python.Method(
        __doc__ = "Return a datetime with the same attributes, except for those attributes given new values by " +
            "whichever keyword arguments are specified. Note that tzinfo=None can be specified to create a naive" +
            " datetime from an aware datetime with no conversion of date and time data.\n",
        args = {"kwargs"}
    )
    public org.python.Object replace(java.util.Map<java.lang.String, org.python.Object> kwargs) {
        String[] keys = { "year", "month", "day", "hour", "minute", "second", "microsecond" };
        java.util.Map<java.lang.String, org.python.Object> new_kwargs = new HashMap<>();
        Object[] args = new Object[0];
        for (int i = 0; i < keys.length; i++){
            new_kwargs.put(keys[i], org.python.types.Int.getInt(this.timeUnits[i]));
            if (kwargs.get(keys[i]) != null){
                if (kwargs.get(keys[i]) instanceof org.python.types.Str) {
                    throw new org.python.exceptions.TypeError("an integer is required (got type str)");
                }
                if (kwargs.get(keys[i]) instanceof org.python.types.Float) {
                    throw new org.python.exceptions.TypeError("integer argument expected, got float");
                }
                // over-write
                new_kwargs.put(keys[i],kwargs.get(keys[i]));
            }
        }
        return new DateTime(args,new_kwargs);
    }

}
