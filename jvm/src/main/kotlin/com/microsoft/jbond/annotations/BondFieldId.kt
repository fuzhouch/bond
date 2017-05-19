// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.annotations

/**
 * Annotation to tag ID of field defined in Bond IDL.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BondFieldId(val id : Int)
