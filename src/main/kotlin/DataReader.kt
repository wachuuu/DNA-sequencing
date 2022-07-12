import java.io.File

class DataReader {
    private val set = mutableListOf<Oligonucleotide>();

    fun readInstance(fileName: String) {
        val read = File("instances/$fileName").bufferedReader().readLines()
        for (i in read.indices) {
            set += Oligonucleotide(read[i])
        }
    }
    fun getSet(): MutableList<Oligonucleotide> {
        return set
    }
}