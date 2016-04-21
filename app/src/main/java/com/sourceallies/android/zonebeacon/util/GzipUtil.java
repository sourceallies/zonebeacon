/*
 * Copyright (C) 2016 Source Allies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sourceallies.android.zonebeacon.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Helps with gzipping and un-gzipping string for nearby communication.
 */
public class GzipUtil {

    /**
     * Compresses a string into a byte array with gzip.
     *
     * @param str the string to compress.
     * @return the gzipped string.
     */
    public static byte[] gzip(String str) throws IOException {
        byte[] blockcopy = ByteBuffer
                .allocate(4)
                .order(java.nio.ByteOrder.LITTLE_ENDIAN)
                .putInt(str.length())
                .array();

        ByteArrayOutputStream os = new ByteArrayOutputStream(str.length());
        GZIPOutputStream gos = new GZIPOutputStream(os);
        gos.write(str.getBytes());
        gos.close();
        os.close();

        byte[] compressed = new byte[4 + os.toByteArray().length];
        System.arraycopy(blockcopy, 0, compressed, 0, 4);
        System.arraycopy(os.toByteArray(), 0, compressed, 4,
                os.toByteArray().length);

        return compressed;
    }

    /**
     * Decompresses the byte array into a string through gzip.
     *
     * @param bytes the gzipped bytes.
     * @return the uncompressed string.
     */
    public static String ungzip(byte[] bytes) throws IOException {
        GZIPInputStream gzipInputStream = new GZIPInputStream(
                new ByteArrayInputStream(bytes, 4,
                        bytes.length - 4));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int value = 0; value != -1; ) {
            value = gzipInputStream.read();
            if (value != -1) {
                baos.write(value);
            }
        }

        gzipInputStream.close();
        baos.close();
        String string = new String(baos.toByteArray(), "UTF-8");

        return string;
    }

}
