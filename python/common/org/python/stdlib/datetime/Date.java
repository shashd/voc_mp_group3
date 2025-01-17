package org.python.stdlib.datetime;

import org.python.types.Bool;

import java.util.Collections;

public class Date extends org.python.types.Object {

    @org.python.Attribute
    public org.python.Object year = __year__();

    @org.python.Attribute
    public org.python.Object month = __month__();

    @org.python.Attribute
    public org.python.Object day = __day__();

    @org.python.Attribute
    public static final org.python.Object min = __min__();

    @org.python.Attribute
    public static final org.python.Object max = __max__();

    @org.python.Method(__doc__ = "")
    public Date(org.python.Object[] args, java.util.Map<java.lang.String, org.python.Object> kwargs) {


        super();

        if (args.length + kwargs.size() > 3) {
            int val = args.length + kwargs.size();
            throw new org.python.exceptions.TypeError("function takes at most 3 arguments (" + val + " given)");
        }

        if (args.length + kwargs.size() == 3) {
            if (kwargs.get("year") != null && args.length == 2) {
                throw new org.python.exceptions.SyntaxError("positional argument follows keyword argument");
            } else if (kwargs.get("month") != null && args.length == 2) {
                throw new org.python.exceptions.SyntaxError("positional argument follows keyword argument");
            }
            if (kwargs.get("year") != null) {
                this.year = kwargs.get("year");
            } else {
                this.year = args[0];
            }

            if (kwargs.get("month") != null) {
                this.month = kwargs.get("month");
            } else {
                this.month = args[1];
            }

            if (kwargs.get("day") != null) {
                this.day = kwargs.get("day");
            } else {
                this.day = args[2];
            }

            if ((this.year instanceof org.python.types.Int) && (this.month instanceof org.python.types.Int) && (this.day instanceof org.python.types.Int)) {
                if (1 <= ((org.python.types.Int) this.year).value && ((org.python.types.Int) this.year).value <= 9999) {
                    if (1d <= ((org.python.types.Int) this.month).value && ((org.python.types.Int) this.month).value <= 12d) {
                        if (1d <= ((org.python.types.Int) this.day).value && ((org.python.types.Int) this.day).value <= 31d) {
                        } else {
                            throw new org.python.exceptions.ValueError("day is out of range for month");
                        }
                    } else {
                        throw new org.python.exceptions.ValueError("month must be in 1..12");
                    }
                } else {
                    throw new org.python.exceptions.ValueError("year " + this.year + " is out of range");
                }
            } else {
                if (!(this.year instanceof org.python.types.Int)) {
                    throw new org.python.exceptions.TypeError("integer argument expected, got " + this.year.typeName());
                }
                if (!(this.month instanceof org.python.types.Int)) {
                    throw new org.python.exceptions.TypeError("integer argument expected, got " + this.month.typeName());
                }
                if (!(this.day instanceof org.python.types.Int)) {
                    throw new org.python.exceptions.TypeError("integer argument expected, got " + this.day.typeName());
                }
            }
        }

        if (args.length + kwargs.size() == 2) {
            if (args.length == 2) {
                this.year = args[0];
                this.month = args[1];
            }

            if (kwargs.get("year") != null) {
                this.year = kwargs.get("year");
            } else if (args.length > 0) {
                this.year = args[0];
            }

            if (kwargs.get("month") != null) {
                this.month = kwargs.get("month");
            }
            if (kwargs.get("day") != null) {
                this.day = kwargs.get("day");
            }

            String y = this.year + "";
            String m = this.month + "";
            String d = this.day + "";

            if (!y.equals("null") && !(this.year instanceof org.python.types.Int)) {
                throw new org.python.exceptions.TypeError("integer argument expected, got " + this.year.typeName());
            }
            if (kwargs.get("year") != null && args.length > 0) {
                throw new org.python.exceptions.SyntaxError("positional argument follows keyword argument");
            }

            if (!(this.month instanceof org.python.types.Int) && !m.equals("null")) {
                throw new org.python.exceptions.TypeError("integer argument expected, got " + this.month.typeName());
            }

            if (y.equals("null")) {
                throw new org.python.exceptions.TypeError("function missing required argument 'year' (pos 1)");
            }

            if (m.equals("null")) {
                throw new org.python.exceptions.TypeError("function missing required argument 'month' (pos 2)");
            }
            if (d.equals("null")) {
                throw new org.python.exceptions.TypeError("function missing required argument 'day' (pos 3)");
            }
        }

        if (args.length + kwargs.size() == 1) {
            if (kwargs.get("year") != null) {
                this.year = kwargs.get("year");
            } else if (args.length > 0) {
                this.year = args[0];
            }
            if (kwargs.get("month") != null) {
                this.month = kwargs.get("month");
            }

            if (kwargs.get("day") != null) {
                this.day = kwargs.get("day");
            }

            String y = this.year + "";
            String m = this.month + "";
            String d = this.day + "";

            if (!(this.year instanceof org.python.types.Int) && !y.equals("null")) {
                throw new org.python.exceptions.TypeError("integer argument expected, got " + this.year.typeName());
            }
            if (!y.equals("null")) {
                throw new org.python.exceptions.TypeError("function missing required argument 'month' (pos 2)");
            }
            if (!m.equals("null") || !d.equals("null")) {
                throw new org.python.exceptions.TypeError("function missing required argument 'year' (pos 1)");
            }
        }

        if (args.length + kwargs.size() == 0) {
            throw new org.python.exceptions.TypeError("function missing required argument 'year' (pos 1)");
        }
    }

    @org.python.Method(__doc__ = "")
    public org.python.types.Str __repr__() {

        String year = this.year + "";
        while (year.length() < 4)
            year = "0" + year;

        String month = this.month + "";
        while (month.length() < 2)
            month = "0" + month;

        String day = this.day + "";
        while (day.length() < 2)
            day = "0" + day;

        return new org.python.types.Str(year + "-" + month + "-" + day);
    }

    public static org.python.Object constant_4() {
        return org.python.types.Int.getInt(4);
    }

    @org.python.Method(__doc__ = "")
    public org.python.types.Str __year__() {
        return new org.python.types.Str(this.year + "");
    }

    @org.python.Method(__doc__ = "")
    public org.python.types.Str __month__() {
        return new org.python.types.Str(this.month + "");
    }

    @org.python.Method(__doc__ = "")
    public org.python.types.Str __day__() {
        return new org.python.types.Str(this.day + "");
    }

    @org.python.Method(__doc__ = "")
    private static org.python.Object __max__() {
        org.python.types.Int day = org.python.types.Int.getInt(31);
        org.python.types.Int month = org.python.types.Int.getInt(12);
        org.python.types.Int year = org.python.types.Int.getInt(9999);
        org.python.Object[] args = { year, month, day };
        return new Date(args, Collections.emptyMap());
    }

    @org.python.Method(__doc__ = "")
    private static org.python.Object __min__() {
        org.python.types.Int day = org.python.types.Int.getInt(1);
        org.python.types.Int month = org.python.types.Int.getInt(1);
        org.python.types.Int year = org.python.types.Int.getInt(1);
        org.python.Object[] args = { year, month, day };
        return new Date(args, Collections.emptyMap());
    }

    @org.python.Method(__doc__ = "")
    public static org.python.Object today() {
        java.time.LocalDateTime today = java.time.LocalDateTime.now();
        int y = today.getYear();
        int m = today.getMonthValue();
        int d = today.getDayOfMonth();
        org.python.Object[] args = { org.python.types.Int.getInt(y), org.python.types.Int.getInt(m), org.python.types.Int.getInt(d) };
        return new Date(args, Collections.emptyMap());
    }

    @org.python.Method(__doc__ = "")
    public org.python.Object ctime() {
        String[] month_list = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        double month_num = ((org.python.types.Int) this.month).value;
        String month_str = month_list[(int) month_num - 1];

        String[] weekday_list = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
        double weekday_num = ((org.python.types.Int) weekday()).value;
        String weekday_str = weekday_list[(int) weekday_num];

        return new org.python.types.Str(weekday_str + " " + month_str + "  " + this.day + " 00:00:00 " + this.year);
    }

    @org.python.Method(__doc__ = "")
    public org.python.Object weekday() {
        double y = ((org.python.types.Int) this.year).value;
        double m = ((org.python.types.Int) this.month).value;
        double d = ((org.python.types.Int) this.day).value;

        java.util.Date myCalendar = new java.util.GregorianCalendar((int) y, (int) m - 1, (int) d).getTime();
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(myCalendar);
        int day = c.get(java.util.Calendar.DAY_OF_WEEK);
        int[] convertToPython = { 6, 0, 1, 2, 3, 4, 5 };
        return org.python.types.Int.getInt(convertToPython[day - 1]);
    }

    @org.python.Method(__doc__ = "")
    public static Date fromisoformat(org.python.types.Str date_string) {
        java.lang.String str = ((org.python.types.Str) date_string).value;
        java.lang.String[] str_split = str.split("-");

        if (str_split.length != 3) {
            throw new org.python.exceptions.TypeError("Need YYYY-MM-DD");
        }
        if (str_split[0].length() != 4) {
            throw new org.python.exceptions.TypeError("Year format is error");
        }
        if (str_split[1].length() != 2) {
            throw new org.python.exceptions.TypeError("Month format is error");
        }
        if (str_split[2].length() != 2) {
            throw new org.python.exceptions.TypeError("Day format is error");
        }

        int y, m, d;
        try {
            y = java.lang.Integer.parseInt(str_split[0]);
            m = java.lang.Integer.parseInt(str_split[1]);
            d = java.lang.Integer.parseInt(str_split[2]);
        } catch(Exception e) {
            throw new org.python.exceptions.TypeError("YYYY, MM and DD in YYYY-MM-DD need to be integers");
        }
        org.python.Object[] args = {org.python.types.Int.getInt(y), org.python.types.Int.getInt(m), org.python.types.Int.getInt(d)};
        return new Date(args, Collections.emptyMap());
    }

    public Date replace(org.python.types.Dict kwargs) {
        long y = ((org.python.types.Int) this.year).value;
        long m = ((org.python.types.Int) this.month).value;
        long d = ((org.python.types.Int) this.day).value;
        java.util.Map<org.python.Object,org.python.Object> kwargsMap = kwargs.value;

        for (org.python.Object key: kwargsMap.keySet()) {
            org.python.types.Str keyString = (org.python.types.Str) key;
            org.python.Object obj = kwargsMap.get(keyString);
            if (!(obj instanceof org.python.types.Int)) {
                throw new org.python.exceptions.TypeError("an integer is required (got type " + obj.typeName() + ")");
            }
            long key_value = ((org.python.types.Int) obj).value;
            if (keyString.value.equals("year")) {
                y = key_value;
            } else if (keyString.value.equals("month")) {
                m = key_value;
            } else if (keyString.value.equals("day")) {
                d = key_value;
            }
        }
        org.python.types.Int year = org.python.types.Int.getInt(y);
        org.python.types.Int month = org.python.types.Int.getInt(m);
        org.python.types.Int day = org.python.types.Int.getInt(d);
        org.python.Object[] args = { year, month, day };
        return new Date(args, Collections.emptyMap());
    }

    @org.python.Method(__doc__ = "return self less than other")
    public org.python.Object __lt__(org.python.Object other) {
        if (other instanceof Date) {
            Date other_date = (Date) other;
            if (((Bool) this.year.__lt__(other_date.year)).value) {
                return Bool.getBool(true);
            } else if (((Bool) this.year.__eq__(other_date.year)).value) {
                if (((Bool) this.month.__lt__(other_date.month)).value) {
                    return Bool.getBool(true);
                } else if (((Bool) this.month.__eq__(other_date.month)).value) {
                    if (((Bool) this.day.__lt__(other_date.day)).value) {
                        return Bool.getBool(true);
                    }
                    return Bool.getBool(false);
                }
                return Bool.getBool(false);
            }
            return Bool.getBool(false);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(__doc__ = "return self equal to other")
    public org.python.Object __eq__(org.python.Object other) {
        if (other instanceof Date) {
            Date other_date = (Date) other;
            boolean year_is_equal =  ((Bool) this.year.__eq__(other_date.year)).value;
            boolean month_is_equal = ((Bool) this.month.__eq__(other_date.month)).value;
            boolean day_is_equal = ((Bool) this.day.__eq__(other_date.day)).value;
            if (year_is_equal && month_is_equal && day_is_equal) {
                return Bool.getBool(true);
            }
            return Bool.getBool(false);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(__doc__ = "return self less or equal to other")
    public org.python.Object __le__(org.python.Object other) {
        if (other instanceof Date) {
            Date other_date = (Date) other;
            if (((Bool) this.__lt__(other_date)).value || ((Bool) this.__eq__(other_date)).value) {
                return Bool.getBool(true);
            } else {
                return Bool.getBool(false);
            }
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(__doc__ = "return self greater than other")
    public org.python.Object __gt__(org.python.Object other) {
        if (other instanceof Date) {
            Date other_date = (Date) other;
            if (((Bool) this.year.__gt__(other_date.year)).value) {
                return Bool.getBool(true);
            } else if (((Bool) this.year.__eq__(other_date.year)).value) {
                if (((Bool) this.month.__gt__(other_date.month)).value) {
                    return Bool.getBool(true);
                } else if (((Bool) this.month.__eq__(other_date.month)).value) {
                    if (((Bool) this.day.__gt__(other_date.day)).value) {
                        return Bool.getBool(true);
                    }
                    return Bool.getBool(false);
                }
                return Bool.getBool(false);
            }
            return Bool.getBool(false);
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }

    @org.python.Method(__doc__ = "return self greater than or equal other")
    public org.python.Object __ge__(org.python.Object other) {
        if (other instanceof Date) {
            Date other_date = (Date) other;
            if (((Bool) this.__gt__(other_date)).value || ((Bool) this.__eq__(other_date)).value) {
                return Bool.getBool(true);
            } else {
                return Bool.getBool(false);
            }
        }
        return org.python.types.NotImplementedType.NOT_IMPLEMENTED;
    }
}
