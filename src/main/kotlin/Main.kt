import java.io.File

fun calcQualityNegatives(instance: MutableList<Oligonucleotide>, sequence: Sequence): Float {
    return (sequence.getSize().toFloat() / (instance.size.toFloat()))
}
fun calcQualityPositives(errors: Int, instance: MutableList<Oligonucleotide>, sequence: Sequence): Float {
    return (sequence.getSize().toFloat() / (instance.size.toFloat() - errors))
}

fun getFilenames(): MutableList<String?> {
    val files = File("instances").listFiles()
    val fileNames = arrayOfNulls<String>(files.size)
    files?.mapIndexed { index, item ->
        fileNames[index] = item?.name
    }
    return fileNames.toMutableList()
}

fun getInstanceProperties(filename: String?): List<String> {
    return if (filename!!.contains("+")) {
        filename.split(".", "+")
    } else {
        filename.split(".", "-")
    }
}

fun hasNegatives(filename: String?): Boolean {
    if (filename!!.contains("+"))
        return false
    return true
}

fun main() {
    val files = getFilenames()
    val outs = mutableListOf<String>()
    for (file in files) {
        println(file)
        val props = getInstanceProperties(file)
        val ts = file?.let { TabuSearch(it, (props[1].toInt() + 9)) }
        val output = ts!!.search()
        if (hasNegatives(file)) {
            println("quality: ${calcQualityNegatives(ts.getInstance(), output)}")
            outs.add("$file \t ${calcQualityNegatives(ts.getInstance(), output)} \t ${output.getSize()} \t ${output.getLength()}")
        } else {
            println("quality: ${calcQualityPositives(props[2].toInt(), ts.getInstance(), output)}")
            outs.add("$file \t ${calcQualityPositives(props[2].toInt(), ts.getInstance(), output)} \t ${output.getSize()} \t ${output.getLength()}")
        }
    }

    File("output.txt").printWriter().use { out ->
        outs.forEach {
            out.println(it)
        }
    }
}
