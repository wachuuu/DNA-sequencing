fun main(args: Array<String>) {
    val sequence = Sequence("pos_random_209+80-2.txt");
    println(sequence.getStringLength());
    val sequence2 = sequence.initSequence(209);
    println(sequence2.getStringLength());
    println(sequence2.toString())
    println(sequence2.getSize());
    println("length = ${sequence2.getStringLength()}")
    println("quality = ${sequence2.getSize().toFloat()/(sequence.getSize().toFloat()-80)}")
}