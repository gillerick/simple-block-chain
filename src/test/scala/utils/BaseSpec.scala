package io.iohk.atala.swetest
package utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BaseSpec extends AnyFlatSpec with Matchers {
  "toHexString" should "convert an array of bytes to a hexadecimal string" in {
    val bytes = Array[Byte](0x00.toByte, 0x0F.toByte, 0x1A.toByte, 0xFF.toByte)

    val expectedHex = "0x000F1AFF"
    val hexString = Base.toHexString(bytes)
    hexString should be(expectedHex)
  }

  "toHexString" should "return the correct hexadecimal string for a single byte" in {
    val bytes = Array[Byte](0x0A.toByte)
    val expectedHex = "0x0A"
    val hexString = Base.toHexString(bytes)
    hexString should be(expectedHex)
  }

  "toHexString" should "return an empty string for an empty array" in {
    val bytes = Array[Byte]()
    val expectedHex = "0x"
    val hexString = Base.toHexString(bytes)
    hexString should be(expectedHex)
  }


}
