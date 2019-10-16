import org.w3c.dom.ranges.Range

class DateRange(val start: MyDate, val endInclusive: MyDate)
{
    operator fun contains(date: MyDate): Boolean= date>=start && date<=endInclusive



}

fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in DateRange(first, last)
}
