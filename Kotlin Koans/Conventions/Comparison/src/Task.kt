import java.util.*

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    override fun compareTo(other: MyDate): Int{
        if(year != other.year) return year - other.year
        if(month != other.month) return month - other.month
        return dayOfMonth - other.dayOfMonth
    }

}

fun compare(date1: MyDate, date2: MyDate) = date1 < date2
fun main() {

    println(  compare(MyDate(2017,2,2),MyDate(2017,3,2)))
}
