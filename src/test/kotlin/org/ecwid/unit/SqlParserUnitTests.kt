package org.ecwid.unit

import org.assertj.core.api.Assertions.assertThat
import org.ecwid.domain.core.SqlParser
import org.ecwid.query.models.AbstractQuery
import org.ecwid.query.parts.Join
import org.ecwid.query.parts.Sort
import org.ecwid.query.parts.Source
import org.ecwid.query.parts.WhereClause
import org.ecwid.query.types.QueryType
import org.ecwid.query.utils.QueryUtils
import org.ecwid.service.SqlParserImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SqlParserUnitTests {

    private lateinit var sqlParser: SqlParser

    @BeforeEach
    fun setUp() {
        sqlParser = SqlParserImpl()
    }

    @Test
    fun shouldResolveCorrectQueryType() {
        val selectQuery = """
        SELECT * FROM user u
    """.trimIndent()

        val insertQuery = """
        INSERT INTO user VALUES()
    """.trimIndent()

        val updateQuery = """
        UPDATE users 
        SET name = 'John Doe', age = 30 
        WHERE id = 1
    """.trimIndent()

        val deleteQuery = """
        DELETE FROM user u WHERE id = 1
    """.trimIndent()

        val selectQueryType = QueryUtils.resolveQueryType(selectQuery)
        val insertQueryType = QueryUtils.resolveQueryType(insertQuery)
        val updateQueryType = QueryUtils.resolveQueryType(updateQuery)
        val deleteQueryType = QueryUtils.resolveQueryType(deleteQuery)

        assertThat(selectQueryType).isEqualTo(QueryType.SELECT)
        assertThat(insertQueryType).isEqualTo(QueryType.INSERT)
        assertThat(updateQueryType).isEqualTo(QueryType.UPDATE)
        assertThat(deleteQueryType).isEqualTo(QueryType.DELETE)

        assertThrows<IllegalArgumentException> {
            QueryUtils.resolveQueryType("UNKNOWN QUERY")
        }
    }

    @Test
    fun shouldParseProperJoins() {
        val rawSql: String = """
       SELECT id, name, lastname FROM user u
                    LEFT JOIN customers c ON u.id = c.id
                    INNER JOIN products p ON u.id = p.id
                    WHERE user.balance > 1000
                    GROUP BY name
                    LIMIT 10
    """.trimIndent()

        val query = sqlParser.parseSql(rawSql)

        val expected: List<Join> = mutableListOf(
            Join("LEFT JOIN", "customers", "u.id = c.id"),
            Join("INNER JOIN", "products", "u.id = p.id"),
        )

        assertThat(query.joins).isEqualTo(expected)
    }

    @Test
    fun shouldParseProperWhereClause() {
        val rawSql: String = """
       SELECT id, name, lastname FROM user u
                    LEFT JOIN customers c ON u.id = c.id
                    WHERE user.balance > 1000
    """.trimIndent()

        val query = sqlParser.parseSql(rawSql)

        assertThat(query.whereClause).isEqualTo(WhereClause("user.balance > 1000"))
    }

    @Test
    fun shouldParseProperLimit() {
        val rawSql: String = """
        SELECT id, name, lastname FROM user u
                    LEFT JOIN customers c ON u.id = c.id
                    WHERE user.balance > 1000
                    GROUP BY name
                    LIMIT 10
                    OFFSET 100
    """.trimIndent()

        val query = sqlParser.parseSql(rawSql)

        assertThat(query.limit).isEqualTo(10)
    }

    @Test
    fun shouldParseProperOffset() {
        val rawSql: String = """
        SELECT id, name, lastname FROM user u
                    LEFT JOIN customers c ON u.id = c.id
                    WHERE user.balance > 1000
                    GROUP BY name
                    LIMIT 10
                    OFFSET 100
    """.trimIndent()

        val query = sqlParser.parseSql(rawSql)

        assertThat(query.offset).isEqualTo(100)
    }

    @Test
    fun shouldParseProperOrderBy() {
        val rawSql: String = """
        SELECT * FROM user u ORDER BY id DESC      
    """.trimIndent()

        val query = sqlParser.parseSql(rawSql)

        val expected: List<Sort> = mutableListOf(Sort("id", "DESC"))

        assertThat(query.orderByColumns).isEqualTo(expected)
    }

    @Test
    fun shouldParseProperGroupBy() {
        val rawSql: String = """
        SELECT id, name, lastname FROM user u
                    LEFT JOIN customers c ON u.id = c.id
                    WHERE user.balance > 1000
                    GROUP BY name
                    LIMIT 10
                    OFFSET 100
    """.trimIndent()

        val query = sqlParser.parseSql(rawSql)

        val expected: List<String> = listOf("name")

        assertThat(query.groupByColumns).isEqualTo(expected)
    }

    @Test
    fun shouldParseProperNestedQuery() {
        val rawSql: String = """
        SELECT * FROM user u WHERE (SELECT name FROM customers WHERE id=1) = a
    """.trimIndent()

        val query = sqlParser.parseSql(rawSql)

        val expected = AbstractQuery(
            columns = mutableListOf("name"),
            fromSource = Source("customers"),
            whereClause = WhereClause("id=1"),
            type = QueryType.SELECT
        )

        query.nestedQueries.forEach {
            assertThat(it).isEqualTo(expected)
        }
    }
}
