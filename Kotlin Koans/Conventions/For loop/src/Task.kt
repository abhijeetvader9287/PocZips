class DateRange(val start: MyDate, val end: MyDate): Iterable<MyDate> {
    override fun iterator(): Iterator<MyDate>  =DateIterator(this)
}
class DateIterator(val dateRange:DateRange):Iterator<MyDate> {
    var currentDate=dateRange.start
    override fun hasNext(): Boolean {
        return  currentDate<=dateRange.end
          }

    override fun next(): MyDate {
        val resultDate=currentDate
        currentDate=currentDate.nextDay()
        return resultDate

    }
}

fun iterateOverDateRange(firstDate: MyDate, secondDate: MyDate, handler: (MyDate) -> Unit) {
    for (date in firstDate..secondDate) {
        handler(date)
    }
}
