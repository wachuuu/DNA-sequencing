import kotlin.math.max

class TabuSearch(filename: String, maxLength: Int) {
    private var maxLength = 0
    private val debug = true
    private val maxIterations = 50
    private var iterations = 0
    private var tabuList = mutableListOf<String>()
    private var tabuLength = 5

    private lateinit var instance: MutableList<Oligonucleotide>

    private lateinit var bestSolution: Sequence
    private var bestObjectFunction = 99999.0

    init {
        loadInstance(filename)
        setMaxLength(maxLength)
    }

    fun search():Sequence {
        this.bestSolution = getInitSolution()
        this.bestObjectFunction = getObjectFunction(this.bestSolution)

        while (iterations < maxIterations) {
            val neighbourSolutions = getNeighbourSolutions(this.bestSolution)
            if (neighbourSolutions.size == 0) break
            if (debug) println("---------------- ITERATION ${iterations + 1} -----------------")

            var bestCandidate = neighbourSolutions.last()
            var bestCandidateObjFunction = getObjectFunction(bestCandidate)

            for (candidate in neighbourSolutions) {
                val candidateObjFunction = getObjectFunction(candidate)
                if (candidateObjFunction < bestCandidateObjFunction) {
                    if (inTabu(candidate)) {
                        continue
                    } else {
                        bestCandidate = candidate
                        bestCandidateObjFunction = candidateObjFunction
                    }
                }
            }
            this.bestSolution = bestCandidate
            this.bestObjectFunction = bestCandidateObjFunction
            printSequence(this.bestSolution, "New best solution")
            addToTabu(bestCandidate)
            iterations++
        }
        return this.bestSolution
    }

    private fun addToTabu(sequence: Sequence) {
        this.tabuList.add(sequence.toString())
        if (this.tabuList.size > this.tabuLength) {
            this.tabuList.removeFirst()
        }
    }

    private fun inTabu(sequence: Sequence): Boolean {
        val found = this.tabuList.find { it == sequence.toString() }
        if (found.isNullOrEmpty())
            return false
        return true
    }

    private fun getNeighbourSolutions(sequence: Sequence): MutableList<Sequence> {
        val originalSeq = copy(sequence)
        val candidates = getCandidates(originalSeq)
        val neighbourSolutions = mutableListOf<Sequence>()
        var swaps = 0
        var replaces = 0

        for (i in candidates.indices) {
            val swapMoves = getSwapMoves(candidates[i], sequence)
            swaps += swapMoves.size
            neighbourSolutions.addAll(swapMoves)
        }

        for (i in candidates.indices) {
            val replaceMoves = getReplaceMoves(candidates[0], sequence)
            replaces += replaceMoves.size
            neighbourSolutions.addAll(replaceMoves)
        }

        val insertMoves = getInsertMoves(sequence)
        neighbourSolutions.addAll(insertMoves)

        val removeMoves = getRemoveMoves(candidates, sequence)
        neighbourSolutions.addAll(removeMoves)

        if (debug) println("Found ${neighbourSolutions.size} possible neighbour solutions (swaps: $swaps, replaces: $replaces, inserts: ${insertMoves.size}, reduces: ${removeMoves.size})")
        return neighbourSolutions
    }

    private fun getCandidates(sequence: Sequence): MutableList<Oligonucleotide> {
        val newSeq = copy(sequence)
        newSeq.refreshPositions()
        newSeq.calcAllOffsets()
        val sumOffsets = newSeq.getSequence()
        for (i in 0 until sumOffsets.size - 1) {
            val offsetBeforeMe = sumOffsets[i].getOffset()
            val offsetAfterMe = sumOffsets[i + 1].getOffset()
            sumOffsets[i].setOffset(offsetBeforeMe + offsetAfterMe)
        }
        val topOffsets = sumOffsets.sortedWith(compareByDescending { it.getOffset() }).toMutableList()
        topOffsets.removeAll { it.getOffset() == 2 }
        return topOffsets
    }

    private fun getSwapMoves(_elem: Oligonucleotide, _sequence: Sequence): MutableList<Sequence> {
        val sequence = copy(_sequence)
        val elem = deepCopy(_elem)
        val possibleMoves = mutableListOf<Sequence>()

        val elemIndex = sequence.getSequence().indexOfFirst { it.getValue() == elem.getValue() }

        for (i in sequence.getSequence().indices) {
            if (elem.getValue() == sequence.getSequence()[i].getValue()) continue
            val newSolution = copy(sequence)
            newSolution.swapElements(elemIndex, i)
            if (lengthOK(newSolution)) {
                newSolution.calcAllOffsets()
                newSolution.refreshPositions()
                possibleMoves.add(newSolution)
            }
        }

        return possibleMoves
    }

    private fun getReplaceMoves(_elem: Oligonucleotide, _sequence: Sequence): MutableList<Sequence> {
        val sequence = copy(_sequence)
        val elem = deepCopy(_elem)
        val possibleMoves = mutableListOf<Sequence>()

        val unusedElems = getUnusedElements(sequence)
        val elemIndex = sequence.getSequence().indexOfFirst { it.getValue() == elem.getValue() }

        for (i in unusedElems.indices) {
            val newSolution = copy(sequence)
            newSolution.replaceElement(elemIndex, unusedElems[i])
            if (lengthOK(newSolution)) {
                newSolution.calcAllOffsets()
                newSolution.refreshPositions()
                possibleMoves.add(newSolution)
            }
        }

        return possibleMoves
    }

    private fun getInsertMoves(_sequence: Sequence): MutableList<Sequence> {
        val sequence = copy(_sequence)
        val unusedElems = getUnusedElements(sequence)
        val possibleMoves = mutableListOf<Sequence>()

        for (i in unusedElems.indices) {
            for (j in sequence.getSequence().indices) {
                val newSolution = copy(sequence)
                newSolution.insertElement(j, unusedElems[i])
                if (lengthOK(newSolution)) {
                    newSolution.calcAllOffsets()
                    newSolution.refreshPositions()
                    possibleMoves.add(newSolution)
                }
            }
        }

        return possibleMoves
    }

    private fun getRemoveMoves(_candidates: MutableList<Oligonucleotide>, _sequence: Sequence): MutableList<Sequence> {
        val sequence = copy(_sequence)
        val candidates = _candidates.map { deepCopy(it) }.toMutableList().take(minOf((_candidates.size.toDouble() / 5.0).toInt(), 8))
        val possibleMoves = mutableListOf<Sequence>()

        val newSolution = copy(sequence)
        for (i in candidates.indices) {
            val elemIndex = sequence.getSequence().indexOfFirst { it.getValue() == candidates[i].getValue() }
            newSolution.removeElementAt(elemIndex)
        }
        newSolution.refreshPositions()
        newSolution.calcAllOffsets()
        possibleMoves.add(newSolution)

        return possibleMoves
    }

    private fun loadInstance(filename: String) {
        val dataReader = DataReader()
        dataReader.readInstance(filename)
        this.instance = dataReader.getSet()
    }

    private fun setMaxLength(value: Int) {
        this.maxLength = value
    }

    private fun getInitSolution(): Sequence {
        val greedy = GreedyAlgorithm(this.instance)
        val sequence = greedy.getSequence(this.maxLength, (0 until this.instance.size).random())
        printSequence(sequence, "Initial solution")
        return sequence
    }

    private fun getUnusedElements(_sequence: Sequence): MutableList<Oligonucleotide> {
        val sequence = copy(_sequence).getSequence()
        val unused = this.instance.map { deepCopy(it) }.toMutableList()

        for (i in sequence.indices) {
            val index = unused.indexOfFirst { it.getValue() == sequence[i].getValue() }
            unused.removeAt(index)
        }
        return unused
    }

    private fun getObjectFunction(sequence: Sequence): Double {
        val avgOffset = sequence.getAverageOffset()
        val unusedElemsCount = instance.size - sequence.getSize()
        return unusedElemsCount.toDouble() + avgOffset
    }

    private fun lengthOK(sequence: Sequence): Boolean {
        if (sequence.getLength() <= this.maxLength)
            return true
        return false
    }

    private fun printSequence(sequence: Sequence, message: String) {
        if (debug) println(message+", size: ${sequence.getSize()}, length: ${sequence.getLength()}, avgOffset: ${sequence.getAverageOffset()}")
    }

    private fun copy(sequence: Sequence): Sequence {
        return Sequence(sequence.getSequence())
    }

    private fun deepCopy(oligonucleotide: Oligonucleotide): Oligonucleotide {
        val new = Oligonucleotide(oligonucleotide.getValue())
        new.setPosition(oligonucleotide.getPosition())
        new.setOffset(oligonucleotide.getOffset())
        return new
    }
}
