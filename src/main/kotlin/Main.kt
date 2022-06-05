fun calcQualityNegatives(instance: MutableList<Oligonucleotide>, sequence: Sequence): Float {
    return (sequence.getSize().toFloat() / (instance.size.toFloat()))
}
fun calcQualityPositives(errors: Int, instance: MutableList<Oligonucleotide>, sequence: Sequence): Float {
    return (sequence.getSize().toFloat() / (instance.size.toFloat() - errors))
}

fun main() {
    val ts = TabuSearch("53.500+50.txt", 509)
    ts.search()
}
