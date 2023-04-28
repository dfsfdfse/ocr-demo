package realcool.ocr_demo.engine

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import realcool.ocrlib.OcrEngine
import kotlin.math.max

class OCR(ctx: Context) {
    private var ocr: OcrEngine = OcrEngine(ctx)

    @MainThread
    fun init(assetManager: AssetManager, callback: OCRCallback) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch(Dispatchers.IO) {
            initSync(assetManager).fold(
                {
                    coroutineScope.launch(Dispatchers.Main) {
                        callback.onSuccess(it)
                    }
                },
                { coroutineScope.launch(Dispatchers.Main) { callback.onFail() } })
        }
    }

    @WorkerThread
    fun initSync(assetManager: AssetManager): Result<String> {
        val start = System.currentTimeMillis()
        ocr.init(assetManager, 4)
        val end = System.currentTimeMillis()
        Log.e("初始化时间", "${end - start}ms")
        return Result.success("${end - start}ms")
    }

    @MainThread
    fun detect(input: Bitmap, output: Bitmap, callback: OCRCallback) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch(Dispatchers.IO) {
            detectSync(input, output).fold(
                {
                    coroutineScope.launch(Dispatchers.Main) {
                        callback.onSuccess(it)
                    }
                },
                { coroutineScope.launch(Dispatchers.Main) { callback.onFail() } })
        }
    }

    @WorkerThread
    fun detectSync(input: Bitmap, output: Bitmap): Result<String> {
        val start = System.currentTimeMillis()
        ocr.detect(input, output, max(input.width, input.height))
        val end = System.currentTimeMillis()
        Log.e("检测时间", "${end - start}ms")
        return Result.success("${end - start}ms")
    }
}