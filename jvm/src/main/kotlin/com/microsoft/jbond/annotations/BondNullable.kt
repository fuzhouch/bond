// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.annotations

/**
 * Annotation to tag a Nullable Bond class.
 *
 * Unlike C#, BondNullable does not require passing class object
 * due to the limitation of both Kotlin and Java. The behavior
 * can be changed in the future.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BondNullable

