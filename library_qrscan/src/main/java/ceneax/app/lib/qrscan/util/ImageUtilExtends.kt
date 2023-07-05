package ceneax.app.lib.qrscan.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.datamatrix.DataMatrixWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.EnumMap
import java.util.Locale

/**
 *Description :
 *@author : KenningChen
 *Date : 2023-06-07
 */
object ImageUtilExtends {
    /**
     * (91) wms系统无该编码对应字段内容
     * (21) 序列号
     * (17) 到期日期(根据实际需要日期格式显示内容)
     * (11) 生产日期(同上)
     * (10) 批号(字符串内容，不固定为日期)
     * (01) 商品的唯一标识符
     *
     * @return Bitmap? GS1标准的DM二维码图片
     */
    fun generateGS1DataMatrixCode(
        productCode: String,//商品唯一码
        productionDate: Date?,//生产日期
        expirationDate: Date?,//到期日期
        goodsNumber: String?,//批号
        serialNumber: String?,//序列号
        mSimpleDateFormat:SimpleDateFormat = SimpleDateFormat("yyMMdd", Locale.getDefault()),
        width: Int = 300,
        height: Int = 300
    ): Bitmap? {
        try {

            val gs1_productionDate = if (productionDate == null){
                ""
            }else{
                "(11)${mSimpleDateFormat.format(productionDate)}"
            }

            val gs1_expirationDate = if (expirationDate == null){
                ""
            }else{
                "(17)${mSimpleDateFormat.format(expirationDate)}"
            }

            val gs1_goodsNumber = if (goodsNumber.isNullOrEmpty()){
                ""
            }else{
                "(10)$goodsNumber"
            }

            val gs1_serialNumber = if (serialNumber.isNullOrEmpty()){
                ""
            }else{
                "(21)$serialNumber"
            }

            val gs1Data = """
                (01)$productCode${gs1_productionDate}${gs1_expirationDate}${gs1_goodsNumber}${gs1_serialNumber}
            """.trimIndent()

            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.MARGIN] = 1

            val writer = DataMatrixWriter()
            val result = writer.encode(gs1Data, BarcodeFormat.DATA_MATRIX, width, height, hints)

            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (result.get(x, y)) Color.BLACK else Color.WHITE
                }
            }

            return Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565).apply {
                setPixels(pixels, 0, width, 0, 0, width, height)

            }
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * 解析UID码
     * (91) wms系统无该编码对应字段内容
     * (21) 序列号
     * (17) 到期日期(根据实际需要日期格式显示内容)
     * (15) (保质期)到期日期(根据实际需要日期格式显示内容) (15和17 只会存在一个)
     * (11) 生产日期(同上)
     * (10) 批号(字符串内容，不固定为日期)
     * (01) 商品的唯一标识符
     * @param code String
     */
    fun analyzeGS1Code(code:String){

    }
}