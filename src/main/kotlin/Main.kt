fun main(args: Array<String>) {
    val sequence = Sequence();
    sequence.initSequence("instance_test.txt");
    println(sequence.toString());
    println(sequence.getOffsetSum())
    sequence.swap(0,1)
    println(sequence.getOffsetSum())
    println(sequence.toString());
}