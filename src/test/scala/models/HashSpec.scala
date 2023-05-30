package io.iohk.atala.swetest
package models

import utils.Base

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class HashSpec extends AnyFlatSpec with Matchers {
  val bytes: Array[Byte] = Array[Byte](92, -11, 20, -83, 113, -39, -112, 81, 80, 74, -17, 50, -41, -10, 89, -19, -62, -124, -11, 12, -29, 27, 77, -61, 94, 91, -15, -23, 65, -37, -34, -54)
  val hash: Hash = Hash(bytes)

  "toNumber" should "convert the hash to a number representation" in {
    val expectedNumber = Base.Number(1, bytes)
    hash.toNumber shouldEqual expectedNumber
  }

  "toHexString" should "convert the hash to a hexadecimal string representation" in {
    val expectedHexString = "0x5CF514AD71D99051504AEF32D7F659EDC284F50CE31B4DC35E5BF1E941DBDECA"
    hash.toHexString shouldEqual expectedHexString
  }

}
