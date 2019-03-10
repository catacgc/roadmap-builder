package roadmap

//http://tools.medialab.sciences-po.fr/iwanthue/index.php
object Colors {
    private var idx = 0
    private var picked = hashMapOf<String, Color>()

    private fun getColor(): Color {
        val (r, g, b) = PALETTE[idx % PALETTE.size]
        idx++
        return Color(r, g, b, 1.0)
    }

    fun getColor(item: String): Color {
        if (item in picked) {
            return picked[item]!!
        }

        picked[item] = getColor()

        return picked[item]!!
    }

    private val PALETTE = """
rgb(109,126,202)
rgb(93,184,79)
rgb(227,209,44)
rgb(255,149,88)
rgb(220,0,0)
rgb(68,175,255)
rgb(55,183,40)
rgb(79,172,216)
rgb(216,157,72)
rgb(174,104,163)
rgb(141,111,50)
rgb(231,134,152)
rgb(165,73,86)
    """.trimIndent()
            .split("\n")
            .map { it.trim() }
            .map { it.split("rgb(")[1].split(")")[0].split(",").map { it.toInt() } }
}

data class Color(val red: Int, val green: Int, val blue: Int, val alfa: Double) {
    fun transparent() = copy(alfa = 0.3)

    override fun toString()= "rgba($red, $green, $blue, $alfa)"
}