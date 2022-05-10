fun main(args: Array<String>) {
    val sequence = Sequence();
    sequence.initSequence("instance_test.txt");
    println(sequence.toString());
    println(sequence.getOffsetSum())
    println(sequence.getString())
    println(sequence.getStringLength())
    println(sequence.toString());
}