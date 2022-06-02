class Oligonucleotide(private val value: String) {
    private var offset = 0

    fun getValue(): String {
        return this.value
    }

    fun getOffset(): Int {
        return this.offset
    }

    fun setOffset(offset: Int) {
        this.offset = offset
    }

    override fun toString(): String {
        return "Oligonucleotide($value', $offset)"
    }
}
