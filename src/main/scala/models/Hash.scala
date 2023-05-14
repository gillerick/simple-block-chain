package io.iohk.atala.swetest
package models

import utils.Base
import utils.Base.{Bytes, Number}

/**
 * Represents a hash in the mini-chain.
 * It is treated as an immutable array of bytes that can be viewed as a number of a hexadecimal string.
 *
 * @param bytes The byte array representing the hash
 */
// The idea behind any cryptographic hash representation in "mini-chain"
// is to treat it as an immutable array of bytes that can be also viewed

case class Hash(bytes: Bytes) {
  /**
   * Converts the hash to a number representation.
   *
   * @return The number representation of the hash.
   */
  def toNumber: Number = Number(1, bytes)

  /**
   * Converts the hash to a hexadecimal string representation for logging purposes.
   *
   * @return The hexadecimal string representation of the hash.
   */
  def toHexString: String = Base.toHexString(bytes)
}
