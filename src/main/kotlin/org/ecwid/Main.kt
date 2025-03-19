package org.ecwid

import org.ecwid.domain.core.SqlParser
import org.ecwid.service.SqlParserImpl

fun main(args: Array<String>) {
    val query1: String = """
        SELECT id, name, lastname FROM user u
        LEFT JOIN customers c ON u.id = c.id
        WHERE user.balance > 1000
        LIMIT 10
    """.trimIndent()

    val query2: String = """
       SELECT id, name, lastname FROM user u
                    LEFT JOIN customers c ON u.id = c.id
                    INNER JOIN products p ON u.id = p.id
                    WHERE user.balance > 1000
                    GROUP BY name
                    LIMIT 10
    """.trimIndent()

    val query3: String = """
        SELECT id, name, lastname FROM user u
                    LEFT JOIN customers c ON u.id = c.id
                    WHERE user.balance > 1000
    """.trimIndent()

    val query4: String = """
        SELECT id, name, lastname FROM user u
                    LEFT JOIN customers c ON u.id = c.id
                    WHERE user.balance > 1000
                    GROUP BY name
                    LIMIT 10
                    OFFSET 100
    """.trimIndent()

    val query5: String = """
        SELECT * FROM user u
    """.trimIndent()

    val query6: String = """
        SELECT * FROM user WHERE (SELECT name FROM customers c WHERE id=1) = a
    """.trimIndent()

    val query7: String = """
        SELECT * FROM user u ORDER BY id DESC      
    """.trimIndent()

    val query8: String = """
        SELECT * FROM user u ORDER BY id ASC
    """.trimIndent()

    val parser: SqlParser = SqlParserImpl()

    println(parser.parseSql(query1))
    println(parser.parseSql(query2))
    println(parser.parseSql(query3))
    println(parser.parseSql(query4))
    println(parser.parseSql(query5))
    println(parser.parseSql(query6))
    println(parser.parseSql(query7))
    println(parser.parseSql(query8))
}