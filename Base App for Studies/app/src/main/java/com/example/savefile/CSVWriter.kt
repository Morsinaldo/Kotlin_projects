package com.example.savefile

import java.io.Closeable
import java.io.OutputStream

class CSVWriter(
    private val out: OutputStream,
    private val separator: Char = ',',
    private val quoteChar: Char = '"',
    private val escapeChar: Char = '"',
    private val lineEnd: String = "\n"
) : Closeable {
    companion object {
        const val NO_QUOTE_CHARACTER = '\u0000'
        const val NO_ESCAPE_CHARACTER = '\u0000'
    }

    fun writeNext(nextLine: Array<String>?) {
        val shouldQuote = quoteChar != NO_QUOTE_CHARACTER
        val shouldEscape = escapeChar != NO_ESCAPE_CHARACTER
        if (nextLine == null) {
            return
        }

        val sb = StringBuffer()
        nextLine.forEachIndexed { idx, element ->
            if (idx != 0) {
                sb.append(separator)
            }
            if (shouldQuote) {
                sb.append(quoteChar)
            }
            element.forEach {
                if (shouldEscape && (it == quoteChar || it == escapeChar)) {
                    sb.append(escapeChar)
                }
                sb.append(it)
            }
            if (shouldQuote) {
                sb.append(quoteChar)
            }
        }
        sb.append(lineEnd)
        out.write(sb.toString().toByteArray())
    }

    override fun close() {
        out.close()
    }

    fun flush() {
        out.flush()
    }
}