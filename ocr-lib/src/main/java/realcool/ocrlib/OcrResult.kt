package realcool.ocrlib

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

open class OcrOutput

object OcrStop : OcrOutput()
object OcrFailed : OcrOutput()

data class OcrResult(
    val dbNetTime: Double,
    val textBlocks: ArrayList<TextBlock>,
    var boxImg: Bitmap?,
    var detectTime: Double,
    var strRes: String?
) : Parcelable, OcrOutput() {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        TODO("textBlocks"),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readDouble(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(dbNetTime)
        parcel.writeParcelable(boxImg, flags)
        parcel.writeDouble(detectTime)
        parcel.writeString(strRes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OcrResult> {
        override fun createFromParcel(parcel: Parcel): OcrResult {
            return OcrResult(parcel)
        }

        override fun newArray(size: Int): Array<OcrResult?> {
            return arrayOfNulls(size)
        }
    }
}


data class Point(var x: Int, var y: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(x)
        parcel.writeInt(y)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Point> {
        override fun createFromParcel(parcel: Parcel): Point {
            return Point(parcel)
        }

        override fun newArray(size: Int): Array<Point?> {
            return arrayOfNulls(size)
        }
    }
}

data class TextBlock(
    val boxPoint: ArrayList<Point>, var boxScore: Float,
    val angleIndex: Int, val angleScore: Float, val angleTime: Double,
    val text: String?, val charScores: FloatArray?, val crnnTime: Double,
    val blockTime: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("boxPoint"),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.createFloatArray(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextBlock

        if (boxPoint != other.boxPoint) return false
        if (boxScore != other.boxScore) return false
        if (angleIndex != other.angleIndex) return false
        if (angleScore != other.angleScore) return false
        if (angleTime != other.angleTime) return false
        if (text != other.text) return false
        if (!charScores.contentEquals(other.charScores)) return false
        if (crnnTime != other.crnnTime) return false
        if (blockTime != other.blockTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = boxPoint.hashCode()
        result = 31 * result + boxScore.hashCode()
        result = 31 * result + angleIndex
        result = 31 * result + angleScore.hashCode()
        result = 31 * result + angleTime.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + charScores.contentHashCode()
        result = 31 * result + crnnTime.hashCode()
        result = 31 * result + blockTime.hashCode()
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(boxScore)
        parcel.writeInt(angleIndex)
        parcel.writeFloat(angleScore)
        parcel.writeDouble(angleTime)
        parcel.writeString(text)
        parcel.writeFloatArray(charScores)
        parcel.writeDouble(crnnTime)
        parcel.writeDouble(blockTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TextBlock> {
        override fun createFromParcel(parcel: Parcel): TextBlock {
            return TextBlock(parcel)
        }

        override fun newArray(size: Int): Array<TextBlock?> {
            return arrayOfNulls(size)
        }
    }
}
