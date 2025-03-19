package org.ecwid.query.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class RegexUtils {
    companion object {
        fun matches(query: String, regex: String, flag: Int): Matcher {
            return Pattern.compile(regex, flag).matcher(query)
        }
    }
}