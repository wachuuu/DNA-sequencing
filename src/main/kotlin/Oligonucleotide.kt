data class Oligonucleotide(private val value: String) {
    private var offset = 0
    private var position = -1

    fun getValue(): String {
        return this.value
    }

    fun getOffset(): Int {
        return this.offset
    }

    fun setOffset(offset: Int) {
        this.offset = offset
    }

    fun getPosition(): Int {
        return this.position
    }

    fun setPosition(value: Int) {
        this.position = value
    }

    override fun toString(): String {
        return "Oligonucleotide($value, $offset, $position)"
    }
}
