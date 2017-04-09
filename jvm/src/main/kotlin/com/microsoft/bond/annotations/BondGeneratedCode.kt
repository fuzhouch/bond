// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.bond.annotations

/**
 * Annotation to tag a class generated from a Bond IDL. 
 *
 * @param compilerName Compiler of Bond IDL. Typically "gbc".
 * @param version Build version of compiler of Bond IDL.
 */
@Target(AnnotationTarget.CLASS)
annotation class BondGeneratedCode(
        val compilerName : String,
        val version : String)
