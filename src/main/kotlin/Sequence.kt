class Sequence {
    lateinit var set: MutableList<Oligonucleotide>;

    fun initSequence(fileName: String) {
        val dataReader = DataReader();
        dataReader.readInstance(fileName)
        this.set = dataReader.getSet()
        calcALLOffset();
    }
    private fun calcOffset(string1: String, string2: String): Int{
        var offset = string1.length;
        for (i in 0..string2.length-2) {
            var substr1 = string2.subSequence(0, string2.length-i-1)
            var substr2 = string1.subSequence(i+1,string1.length)
            if(substr1 == substr2) {
                offset = string1.length - substr1.length;
                break;
            }
        }
        return offset;
    }
    private fun calcALLOffset(){
        for (i in set.indices) {
            if(i==0) {
                set[i].setOffset(0)
            }
            else {
                set[i].setOffset(0);
                val offset = calcOffset(set[i-1].getValue(), set[i].getValue());
                set[i-1].setOffset(set[i-1].getOffset() + offset);
                set[i].setOffset(set[i].getOffset() + offset);
            }
        }
    }

    fun getSequence(): MutableList<Oligonucleotide> {
        return this.set;
    }

    fun elementsNumber(): Int {
        return set.size
    }

    fun getElement(index: Int): Oligonucleotide {
        return set[index];
    }

    fun swap(index1: Int, index2: Int) {
        val temp = set[index1];
        set[index1] = set[index2];
        set[index2] = temp;
        calcALLOffset()
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