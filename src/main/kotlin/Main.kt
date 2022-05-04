fun main(args: Array<String>) {
    val dataReader = DataReader();
    dataReader.readInstance("instance1.txt");
    println(dataReader.getSet());
}