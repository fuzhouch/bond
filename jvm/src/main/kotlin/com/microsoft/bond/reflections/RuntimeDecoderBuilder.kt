package com.microsoft.bond.reflections

import com.microsoft.bond.annotations.BondSchema
import kotlin.reflect.KClass
import java.io.Serializable
import java.lang.Class

class RuntimeDecoderBuilder<T:Any>(jclass: Class<T>) : Serializable {
    constructor(klass: KClass<T>) : this(klass.java) {}
    val jcls = jclass

    fun createDecoder() {
        // 1. Confirmed this is a Bond schema
        val bondSchema = jcls.getDeclaredAnnotation(BondSchema::class.java)
        if (bondSchema == null) {
            throw Exception("NotABondSchema:${jcls.name}")
        }
        true
    }
}
