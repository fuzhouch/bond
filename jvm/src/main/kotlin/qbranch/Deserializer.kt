// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch

import qbranch.protocols.TaggedProtocolReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Deserialize objects of given type.
 */
class Deserializer<T>(klass: Class<T>, charset: Charset) {
    constructor(klass: Class<T>) : this(klass, StandardCharsets.UTF_8)

    val cls = klass
    private val deserializerImpl = StructDeserializer(klass, charset)

    fun deserialize(reader: TaggedProtocolReader): T {
        val obj = cls.newInstance()
        return cls.cast(deserializerImpl.deserialize(obj as Any, reader, false))
    }
}