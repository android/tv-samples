package com.google.jetcatalog.colorutils


/**
 * HCT, hue, chroma, and tone. A color system that provides a perceptually accurate color
 * measurement system that can also accurately render what colors will appear as in different
 * lighting environments.
 */
class Hct private constructor(argb: Int) {
    private var hue = 0.0
    private var chroma = 0.0
    private var tone = 0.0
    private var argb = 0

    init {
        setInternalState(argb)
    }

    fun getHue(): Double {
        return hue
    }

    fun getChroma(): Double {
        return chroma
    }

    fun getTone(): Double {
        return tone
    }

    fun toInt(): Int {
        return argb
    }

    /**
     * Set the hue of this color. Chroma may decrease because chroma has a different maximum for any
     * given hue and tone.
     *
     * @param newHue 0 <= newHue < 360; invalid values are corrected.
     */
    fun setHue(newHue: Double) {
        setInternalState(HctSolver.solveToInt(newHue, chroma, tone))
    }

    /**
     * Set the chroma of this color. Chroma may decrease because chroma has a different maximum for
     * any given hue and tone.
     *
     * @param newChroma 0 <= newChroma < ?
     */
    fun setChroma(newChroma: Double) {
        setInternalState(HctSolver.solveToInt(hue, newChroma, tone))
    }

    /**
     * Set the tone of this color. Chroma may decrease because chroma has a different maximum for any
     * given hue and tone.
     *
     * @param newTone 0 <= newTone <= 100; invalid valids are corrected.
     */
    fun setTone(newTone: Double) {
        setInternalState(HctSolver.solveToInt(hue, chroma, newTone))
    }

    private fun setInternalState(argb: Int) {
        this.argb = argb
        val cam = Cam16.fromInt(argb)
        hue = cam.hue
        chroma = cam.chroma
        tone = ColorUtils.lstarFromArgb(argb)
    }

    companion object {
        /**
         * Create an HCT color from hue, chroma, and tone.
         *
         * @param hue 0 <= hue < 360; invalid values are corrected.
         * @param chroma 0 <= chroma < ?; Informally, colorfulness. The color returned may be lower than
         * the requested chroma. Chroma has a different maximum for any given hue and tone.
         * @param tone 0 <= tone <= 100; invalid values are corrected.
         * @return HCT representation of a color in default viewing conditions.
         */
        fun from(hue: Double, chroma: Double, tone: Double): Hct {
            val argb: Int = HctSolver.solveToInt(hue, chroma, tone)
            return Hct(argb)
        }

        /**
         * Create an HCT color from a color.
         *
         * @param argb ARGB representation of a color.
         * @return HCT representation of a color in default viewing conditions
         */
        fun fromInt(argb: Int): Hct {
            return Hct(argb)
        }
    }
}

