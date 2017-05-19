// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.annotations

/**
 * Mark a field that must be assigned manually.
 *
 * This is a tag attribute on a field with generic type. Bond's
 * semantic requires a default value of T object initialized
 * when creating Record. Both C++ and C# can do this, but Java/Kotlin
 * does not, due to the limitation of Java/Kotlin generic.
 *
 * There's no way right now to support "new T()" syntax. So we have to
 * apply a weak guarantee: we assign this field to null by default, and
 * inform serializer to throw runtime exception, if the field is not
 * assigned a valid object. This is less powerful than C++/C#
 * approach, but it guarantees correctness of sent buffer.
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
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BondManualAssignment

