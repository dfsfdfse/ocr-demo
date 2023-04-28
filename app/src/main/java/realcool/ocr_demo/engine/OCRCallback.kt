package realcool.ocr_demo.engine

interface OCRCallback {
    fun onSuccess(s:String)

    fun onFail()
}