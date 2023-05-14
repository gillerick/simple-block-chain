package io.iohk.atala.swetest
package utils

/**
 * Provides the basic types and utility functions used in the mini-chain implementation.
 */
object Base {
  /**
   * Represents an unknown type.
   * This type is typically used when there is no meaningful value to assign.
   */
  type Unknown = Nothing

  /**
   * Represents a nonce value used in mining.
   * It is a long integer value.
   */
  type Nonce = Long

  /**
   * Represents an array of bytes.
   */
  type Bytes = Array[Byte]
  val Bytes = new Array[Byte](_: Int)

  /**
   * Represents a numeric value.
   * It is a BigInt value.
   */
  type Number = BigInt
  val Number: BigInt.type = BigInt

  /**
   * Converts an array of bytes to a hexadecimal string representation.
   *
   * @param bytes The array of bytes to convert.
   * @return The hexadecimal string representation of the bytes.
   */
  def toHexString(bytes: Array[Byte]): String =
    "0x" + bytes.map(b => String.format("%02X", Byte.box(b))).mkString("")
}
