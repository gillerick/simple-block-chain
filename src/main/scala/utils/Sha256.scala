package io.iohk.atala.swetest
package utils

import models.Hash
import utils.Base.Bytes

import java.security.MessageDigest

object Sha256 {
  val NumberOfBytes = 32
  val TheDigest: MessageDigest = MessageDigest.getInstance("SHA-256")

  val Zero_Hash: Hash = Sha256(Array.fill[Byte](32)(0))


  /**
   * Applies the SHA-256 hash algorithm to the given byte arrays and returns the resulting hash.
   *
   * @param bytess The byte arrays to hash.
   * @return The computed SHA-256 hash.
   */
  def apply(bytess: Bytes*): Hash = {
    for (bytes <- bytess) {
      TheDigest.update(bytes)
    }

    val hash = TheDigest.digest()
    assert(hash.length == NumberOfBytes)

    Hash(hash)
  }
}
