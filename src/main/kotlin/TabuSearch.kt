class TabuSearch(filename: String, maxLength: Int) {
    private var maxLength = 0
    private lateinit var instance: MutableList<Oligonucleotide>
    private lateinit var unusedElements: MutableList<Oligonucleotide>
    private lateinit var currentSolution: Sequence

    init {
        loadInstance(filename)
        setMaxLength(maxLength)
        getInitSolution()
        getUnusedElements()
    }

    fun loadInstance(filename: String) {
        val dataReader = DataReader()
        dataReader.readInstance(filename)
        this.instance = dataReader.getSet()
    }

    fun setMaxLength(value: Int) {
        this.maxLength = value
    }

    fun getInitSolution() {
        val greedy = GreedyAlgorithm(this.instance)
        this.currentSolution = greedy.getSequence(this.maxLength, (0 until this.instance.size).random())
        println("Initial solution nucleotides: ${this.currentSolution.getSize()}")
    }

    fun getUnusedElements() {
        this.unusedElements = this.instance.minus(this.currentSolution.getSequence().toHashSet()).toMutableList()
        println("Unused elements: ${this.unusedElements.size}")
    }

    fun swapElements(index1: Int, index2: Int): Sequence {
        val newSolution = Sequence(this.currentSolution.getSequence())
        newSolution.swapElements(index1, index2)
        println("Swapped nucleotides at indexes $index1 and $index2, length: ${newSolution.getLength()}")
        return newSolution
    }

    fun replaceElement(index: Int, newElem: Oligonucleotide) {
        val newSolution = Sequence(this.currentSolution.getSequence())
        newSolution.replaceElement(index, newElem)
        println("Replaced nucleotide at index $index with $newElem, length: ${newSolution.getLength()}")
    }

    fun insertElement(index: Int, newElem: Oligonucleotide) {
        val newSolution = Sequence(this.currentSolution.getSequence())
        newSolution.insertElement(index, newElem)
        println("Inserted nucleotide $newElem at index $index, length: ${newSolution.getLength()}")
    }
}
