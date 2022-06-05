import java.util.*

class Sequence {
    var data: MutableList<Oligonucleotide>

    constructor(list: MutableList<Oligonucleotide>, calcOffsets: Boolean =false) {
        this.data = list.map{ deepCopy(it) }.toMutableList()
        if (calcOffsets) calcAllOffsets()
    }

    constructor() {
        this.data = mutableListOf()
    }

    fun getSequence(): MutableList<Oligonucleotide> {
        return this.data.map { deepCopy(it) }.toMutableList()
    }

    fun addElement(element: Oligonucleotide) {
        this.data.add(deepCopy(element))
        calcOffsetAt(this.data.size - 1)
    }

    fun insertElement(index: Int, element: Oligonucleotide) {
        this.data.add(index, deepCopy(element))
    }

    fun swapElements(index1: Int, index2: Int) {
        Collections.swap(this.data, index1, index2)
        calcOffsetAt(index1)
        calcOffsetAt(index1 + 1)
        calcOffsetAt(index2)
        calcOffsetAt(index2 + 1)
    }

    fun replaceElement(index: Int, element: Oligonucleotide) {
        this.data[index] = deepCopy(element)
        calcOffsetAt(index)
        calcOffsetAt(index + 1)
    }

    fun removeElementAt(index: Int) {
        this.data.removeAt(index)
    }

    override fun toString(): String {
        var seqString = data[0].getValue()
        for (i in 1 until data.size) {
            val offset = calcOffsetBetween(data[i-1], data[i])
            seqString += data[i].getValue().subSequence(data[i].getValue().length - offset, data[i].getValue().length)
        }
        return seqString
    }

    fun getLength(): Int {
        return toString().length
    }

    fun getSize(): Int {
        return this.data.size
    }

    fun calcOffsetBetween(elem1: Oligonucleotide, elem2: Oligonucleotide): Int {
        val elem1Value = elem1.getValue()
        val elem2Value = elem2.getValue()
        var offset = elem1Value.length
        for (i in 0..elem2Value.length - 2) {
            val substr1 = elem2Value.subSequence(0, elem2Value.length - i - 1)
            val substr2 = elem1Value.subSequence(i + 1, elem1Value.length)
            if (substr1 == substr2) {
                offset = elem1Value.length - substr1.length
                break
            }
        }
        return offset
    }

    private fun calcOffsetAt(index: Int) {
        if (index > this.data.size - 1) return
        if (index == 0) {
            data[index].setOffset(0)
        } else {
            data[index].setOffset(calcOffsetBetween(data[index-1], data[index]))
        }
    }

    fun calcAllOffsets() {
        for (i in data.indices) {
            data[i].setOffset(0)
            if (i != 0) {
                data[i].setOffset(calcOffsetBetween(data[i-1], data[i]))
            }
        }
    }

    fun refreshPositions() {
        for (i in this.data.indices) {
            this.data[i].setPosition(i)
        }
    }

    fun getAverageOffset(): Double {
        calcAllOffsets()
        var offsetSum = 0
        for (i in data.indices) {
            offsetSum += data[i].getOffset()
        }
        return offsetSum.toDouble() / data.size.toDouble()
    }

    private fun deepCopy(oligonucleotide: Oligonucleotide): Oligonucleotide {
        val new = Oligonucleotide(oligonucleotide.getValue())
        new.setPosition(oligonucleotide.getPosition())
        new.setOffset(oligonucleotide.getOffset())
        return new
    }
}
