class Sequence {
    var set: MutableList<Oligonucleotide>;

    constructor(list: MutableList<Oligonucleotide>) {
        this.set = list;
    }

    constructor(fileName: String) {
        val dataReader = DataReader();
        dataReader.readInstance(fileName)
        var instance = dataReader.getSet();
        this.set = instance;
        calcAllOffset();
    }

    fun addElement(element: Oligonucleotide) {
        this.set += element;
    }

    fun isNotEmpty(): Boolean {
        return this.set.isNotEmpty();
    }

    fun removeElementAt(index: Int) {
        this.set.removeAt(index);
    }

    fun initSequence(wantedLength: Int): Sequence {
        var elemCount = 0;
        var bestSeq = this;
        for (i in this.set.indices) {
            var currentSeq = greedyAlghoritm(this.set, i, wantedLength);
            if(elemCount < currentSeq.getSize()) {
                bestSeq = currentSeq;
            }
            println(currentSeq.getSize())
        }
        return bestSeq;
    }

    private fun greedyAlghoritm(nucleotidesSet: MutableList<Oligonucleotide>, startIndex: Int, wantedLength: Int): Sequence {
        var seq = Sequence(mutableListOf<Oligonucleotide>());
        val instance = nucleotidesSet.toMutableList()
        var current = instance[startIndex];
        seq.addElement(current);
        instance.removeAt(startIndex);
        while (instance.isNotEmpty()) {
            var min = 1000; // we won't handle oligonucleotides as long as 1000 char
            var minIndex = -1;
            for (i in instance.indices) {
                if(calcOffset(current.getValue(), instance[i].getValue()) < min) {
                    min = calcOffset(current.getValue(), instance[i].getValue());
                    minIndex = i;
                }
            }
            if(seq.getStringLength() + calcOffset(current.getValue(), instance[minIndex].getValue()) > wantedLength) {
                break;
            }
            else
            {
                seq.addElement(instance[minIndex]);
                current = instance[minIndex];
                instance.remove(instance[minIndex]);
            }
        }
        seq.calcAllOffset();
        return seq;
    }

    private fun calcOffset(string1: String, string2: String): Int {
        var offset = string1.length;
        for (i in 0..string2.length - 2) {
            var substr1 = string2.subSequence(0, string2.length - i - 1)
            var substr2 = string1.subSequence(i + 1, string1.length)
            if (substr1 == substr2) {
                offset = string1.length - substr1.length;
                break;
            }
        }
        return offset;
    }

    private fun calcAllOffset() {
        for (i in set.indices) {
            if (i == 0) {
                set[i].setOffset(0)
            } else {
                set[i].setOffset(0);
                val offset = calcOffset(set[i - 1].getValue(), set[i].getValue());
                //set[i - 1].setOffset(set[i - 1].getOffset() + offset);
                set[i].setOffset(set[i].getOffset() + offset);
            }
        }
    }

    fun getString(): String {
        var sequence = set[0].getValue()
        for (i in 1 until set.size) {
            var offset = calcOffset(set[i-1].getValue(), set[i].getValue())
            sequence += set[i].getValue().subSequence(set[i].getValue().length - offset, set[i].getValue().length)
        }
        return sequence;
    }

//  TODO: consider calculating length based on offsets, not on sequence string

//    fun getSequenceLength(): Int {
//
//    }
    fun getStringLength(): Int {
        val string = getString()
        return string.length
    }

    fun getSequence(): MutableList<Oligonucleotide> {
        return this.set;
    }

    fun getSize(): Int {
        return set.size
    }

    fun getElement(index: Int): Oligonucleotide {
        return set[index];
    }

    fun swap(index1: Int, index2: Int) {
        val temp = set[index1];
        set[index1] = set[index2];
        set[index2] = temp;
        calcAllOffset();

    }

    fun getOffsetSum(): Int {
        var sum = 0;
        for (i in set.indices)
            sum += set[i].getOffset()
        return sum;
    }

    override fun toString(): String {
        return "Sequence(set=$set)"
    }


}