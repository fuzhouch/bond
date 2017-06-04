// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

@file:JvmName("TypeChecker")

package qbranch.types

import qbranch.annotations.BondGeneratedCode

/** A function to check if given class is a generated bond class.
 *  @param cls Java Class object to verify
 *  @return True if cls is Bond generated, and False if not.
 */
fun isBondGenerated(cls : Class<*>) : Boolean {
    return cls.getAnnotation(BondGeneratedCode::class.java) != null
}