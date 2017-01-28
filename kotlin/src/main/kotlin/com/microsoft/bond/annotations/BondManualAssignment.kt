// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.bond.annotations

import kotlin.reflect.KClass

/**
 * Mark a field that must be assigned manually.
 *
 * This is a field to be used in a Bond struct with generic type like
 * below. Bond semantic requires a default value of T object created
 * when creating Record. Both C++ and C# can do this, but Java/Kotlin
 * does not, due to the limitation of Java/Kotlin generic.
 *
 * There's no way right now to support "new T()" syntax. So we have to
 * apply a less guarantee: we assign this field to null by default, and
 * inform serializer to throw runtime exception, if the field is not
 * assigned a valid object. This is weaker than C++/C# approach, but
 * guarantees validity of sent buffer.
 *
 * struct Record<T>
 * {
 *     0: T value;
 * }
 *
 * Any 'T value' fields in Bond IDL can be converted to property in
 * Kotlin:
 *
 *     @BondFieldId(x) @BondManaualAssignment var value : T? = null
 * 
 */
annotation class BondManualAssignment

