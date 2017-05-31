// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root
// for full license information.

package com.microsoft.jbond.exceptions

class EndOfStreamException(expectedLen: Int, actualLen: Int):
        Exception("UnexpectedEndOfStream:expected=$expectedLen,actual=$actualLen") {
    val expectedLength = expectedLen
    val returnedLength = actualLen
}