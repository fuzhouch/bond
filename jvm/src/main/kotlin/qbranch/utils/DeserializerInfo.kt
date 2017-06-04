// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package qbranch.utils

import qbranch.StructDeserializer

internal data class DeserializerInfo(val klass : Class<*>, val deserializer : StructDeserializer)