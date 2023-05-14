package io.iohk.atala.swetest

import java.security.MessageDigest


// Some base definitions
object Base {
  // When you see Unknown, replace it with appropriate type on a per
  // case basis. So if you see:
  //
  //   def append(block: Block): Unknown
  //
  // and later in the code:
  //
  //   def findByIndex(index: Int): Unknown
  //
  // and we ask you to implement the methods, you do not necessarily
  // have to replace Unknown with the same type in both cases.
  type Unknown = Nothing

  type Nonce = Long

  type Bytes = Array[Byte]
  val Bytes = new Array[Byte](_: Int)

  type Number = BigInt
  val Number = BigInt

  def toHexString(bytes: Array[Byte]): String =
    "0x" + bytes.map(b => String.format("%02X", Byte.box(b))).mkString("")
}