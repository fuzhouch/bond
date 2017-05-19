// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.annotations

/**
 * Annotation to tag an attribute marked from Bond IDL.
 *
 * @param attrName Vame of Bond attribute.
 * @param value Given string value of Bond attribute.
 */
annotation class BondAttribute(val attrName : String, val value : String)

