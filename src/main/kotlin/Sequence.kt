class Sequence {
    lateinit var set: MutableList<Oligonucleotide>;

    fun test(){
        println(calcOffset("ATGA", "TGAD"))
        println(calcOffset("ACTG", "TGAG"))
        println(calcOffset("ATTA", "AATG"))
        println(calcOffset("ATGA", "CGAT"))
    }

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
                val offset = calcOffset(set[i-1].getValue(), set[i].getValue());
                set[i-1].setOffset(set[i-1].getOffset() + offset);
                set[i].setOffset(set[i].getOffset() + offset);
            }
        }
    }

    override fun toString(): String {
        return "Sequence(set=$set)"
    }


}