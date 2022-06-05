class GreedyAlgorithm(_instance: MutableList<Oligonucleotide>) {
    private var instance: MutableList<Oligonucleotide> = _instance.map{ deepCopy(it) }.toMutableList()

    fun getBestSequence(length: Int): Sequence {
        var bestSeqSize = 0
        var best = Sequence()
        for (i in this.instance.indices) {
            val currentSeq = getSequence(length, i)
            if (bestSeqSize < currentSeq.getSize()) {
                best = currentSeq
                bestSeqSize = best.getSize()
                println("New solution, size: $bestSeqSize, avgOffset: ${best.getAverageOffset()}")
            }
        }
        return best
    }

    fun getSequence(length: Int, startIndex: Int): Sequence {
        val seq = Sequence()
        val toProcess = this.instance.toMutableList()
        var currentElem = toProcess[startIndex]
        seq.addElement(currentElem)
        toProcess.removeAt(startIndex)

        while (toProcess.isNotEmpty()) {
            var min = 1000
            var minIndex = -1
            for (i in toProcess.indices) {
                val currOffset = seq.calcOffsetBetween(currentElem, toProcess[i])
                if (currOffset < min) {
                    min = currOffset
                    minIndex = i
                }
            }
            if(seq.getLength() + seq.calcOffsetBetween(currentElem, toProcess[minIndex]) > length) {
                break
            } else {
                seq.addElement(toProcess[minIndex])
                currentElem = toProcess[minIndex]
                toProcess.remove(toProcess[minIndex])
            }
        }
        seq.refreshPositions()
        seq.calcAllOffsets()
        return seq
    }

    private fun deepCopy(oligonucleotide: Oligonucleotide): Oligonucleotide {
        val new = Oligonucleotide(oligonucleotide.getValue())
        new.setPosition(oligonucleotide.getPosition())
        new.setOffset(oligonucleotide.getOffset())
        return new
    }
}
