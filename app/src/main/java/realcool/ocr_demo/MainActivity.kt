package realcool.ocr_demo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import realcool.ocr_demo.engine.OCR
import realcool.ocr_demo.engine.OCRCallback
import realcool.ocr_demo.ui.theme.OcrdemoTheme
import realcool.ocr_demo.utils.FileUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OcrdemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val engine = OCR(ctx)
    val btnInitText = "ocr初始化"
    val btnDetectText = "ocr检测"
    var content by remember {
        mutableStateOf("")
    }
    val input = FileUtils.getAssetsByFilename(ctx, "images/s3.png")
    var output by remember {
        mutableStateOf(Bitmap.createBitmap(input.width, input.height, Bitmap.Config.ARGB_8888))
    }
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            content = ""
        }) {
            Text(text = "清空文字")
        }
        Button(onClick = {
            engine.init(ctx.assets, object : OCRCallback {
                override fun onSuccess(s:String) {
                    content += "初始化成功!\n"
                    content += "耗时$s\n"
                    output = Bitmap.createBitmap(input.width, input.height, Bitmap.Config.ARGB_8888)
                    Log.e("ocr", "初始化成功!")
                }

                override fun onFail() {
                    content += "初始化失败!\n"
                    Log.e("ocr", "初始化失败!")
                }

            })
        }) {
            Text(text = btnInitText)
        }

        Button(onClick = {
            engine.detect(input, output, object : OCRCallback {
                override fun onSuccess(s:String) {
                    content += "检测成功\n"
                    content += "耗时$s\n"
                    Log.e("ocr", "检测成功!")
                }

                override fun onFail() {
                    content += "检测失败!\n"
                    Log.e("ocr", "检测失败!")
                }

            })
        }) {
            Text(text = btnDetectText)
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(50.dp, 20.dp)
                .background(Color(0xFFF1F1F1))
                .height(400.dp)
        ) {
            Text(text = content)
        }

        Image(bitmap = output.asImageBitmap(), contentDescription = "output")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OcrdemoTheme {
        Greeting("Android")
    }
}