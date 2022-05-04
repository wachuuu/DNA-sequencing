class Oligonucleotide {
    private val value: String;
    private var offset = 0;

    constructor(value: String) {
        this.value = value
    }

    fun getValue(): String {
        return this.value;
    }
    fun getOffset(): Int {
        return this.offset;
    }
    fun setOffset(offset: Int) {
        this.offset = offset;
    }

    override fun toString(): String {
        return "Oligonucleotide(value='$value', offset=$offset)"
    }


}