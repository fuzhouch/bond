package com.microsoft.jbond.utils;

/**
 * Contains util functions for decoding/encoding ZigZag format.
 */
public class ZigZag {
    // Doc REF: https://developers.google.com/protocol-buffers/docs/encoding
    // In other words, each value n is encoded using

    // (n << 1) ^ (n >> 31)
    // for sint32s, or
    // (n << 1) ^ (n >> 63)
    // for the 64-bit version.
}
