package com.microsoft.jbond.utils

import com.microsoft.jbond.StructDeserializer

internal data class DeserializerInfo(val klass : Class<*>, val deserializer : StructDeserializer)