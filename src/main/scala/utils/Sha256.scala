package io.iohk.atala.swetest
package utils

import models.Hash
import utils.Base.Bytes

import java.security.MessageDigest

object Sha256 {
  val NumberOfBytes = 32
  val TheDigest: MessageDigest = MessageDigest.getInstance("SHA-256")

  val ZeroHash: Hash = Hash(Array.fill[Byte](NumberOfBytes)(0))


  /**
   * Applies the SHA-256 hash algorithm to the given byte arrays and returns the resulting hash.
   *
   * @param bytes The byte arrays to hash.
   * @return The computed SHA-256 hash.
   */
  def apply(bytes: Bytes*): Hash = {
    for (byte <- bytes) {
      TheDigest.update(byte)
    }

    val hash = TheDigest.digest()
    assert(hash.length == NumberOfBytes)

    Hash(hash)
  }
}
