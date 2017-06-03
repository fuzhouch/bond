// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.exceptions

class UnsupportedBondTypeException(klass : Class<*>) : Exception("type=${klass.name}") {
    val cls = klass
}