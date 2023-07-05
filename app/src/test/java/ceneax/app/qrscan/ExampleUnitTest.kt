package ceneax.app.qrscan

import android.util.Log
import ceneax.app.lib.qrscan.util.gs1_decoder.Gs1Decoder
import org.junit.Test

import org.junit.Assert.*
import kotlin.math.log

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)

        testAnalyzeUIDCode("(01)12312432(17)20220717(21)00001")
    }
}

private fun testAnalyzeUIDCode(code:String){

    val gs1Decoder = Gs1Decoder()
    val msg = gs1Decoder.decodeCode(code)
    println(msg.getItemByAi("01").data)
}