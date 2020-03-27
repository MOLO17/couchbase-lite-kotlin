/*
 * Copyright (c) 2020 MOLO17
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.molo17.couchbase.lite

import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.DataSource
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.couchbase.lite.Expression
import com.couchbase.lite.Ordering
import com.couchbase.lite.QueryBuilder
import com.couchbase.lite.SelectResult
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import java.util.Date
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Created by Damiano Giusti on 26/03/2020.
 */
class QueryBuilderExtensionsKtTest {

    @JvmField @Rule val tempDir = TemporaryFolder()

    private val database by lazy { createDatabase() }

    @BeforeTest
    fun setup() {
        CouchbaseLite.init()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Projection
    ///////////////////////////////////////////////////////////////////////////

    @Test
    fun `select all`() {
        val expected = QueryBuilder.select(SelectResult.all()).from(DataSource.database(database))
        val actual = select(all()) from database
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select some fields`() {
        val expected = QueryBuilder
            .select(
                SelectResult.property("name"),
                SelectResult.property("age")
            )
            .from(DataSource.database(database))
        val actual = select("name", "age") from database
        assertEquals(expected.explain(), actual.explain())
    }

    ///////////////////////////////////////////////////////////////////////////
    // Selection
    ///////////////////////////////////////////////////////////////////////////

    @Test
    fun `select all where equalTo`() {
        val expected = singleSelectionWhere("type", Expression.string("user"), Expression::equalTo)
        val actual = select(all()) from database where { "type" equalTo "user" }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where notEqualTo`() {
        val expected = singleSelectionWhere("type", Expression.string("user"), Expression::notEqualTo)
        val actual = select(all()) from database where { "type" notEqualTo "user" }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where lessThan`() {
        val expected = singleSelectionWhere("age", Expression.intValue(40), Expression::lessThan)
        val actual = select(all()) from database where { "age" lessThan 40 }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where lessThanOrEqualTo`() {
        val expected = singleSelectionWhere("age", Expression.intValue(40), Expression::lessThanOrEqualTo)
        val actual = select(all()) from database where { "age" lessThanOrEqualTo 40 }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where greaterThan`() {
        val expected = singleSelectionWhere("age", Expression.intValue(40), Expression::greaterThan)
        val actual = select(all()) from database where { "age" greaterThan 40 }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where greaterThanOrEqualTo`() {
        val expected = singleSelectionWhere("age", Expression.intValue(40), Expression::greaterThanOrEqualTo)
        val actual = select(all()) from database where { "age" greaterThanOrEqualTo 40 }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where like`() {
        val expected = singleSelectionWhere("name", Expression.string("dam"), Expression::like)
        val actual = select(all()) from database where { "name" like "dam" }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where with AND condition`() {
        val expected = QueryBuilder.select(SelectResult.all())
            .from(DataSource.database(database))
            .where(
                Expression.property("type").equalTo(Expression.string("user"))
                    .and(Expression.property("name").equalTo(Expression.string("damiano")))
            )
        val actual = select(all())
            .from(database)
            .where { ("type" equalTo "user") and ("name" equalTo "damiano") }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where with two AND condition joined with OR`() {
        val expected = QueryBuilder.select(SelectResult.all())
            .from(DataSource.database(database))
            .where(
                Expression.property("type").equalTo(Expression.string("user"))
                    .and(Expression.property("name").equalTo(Expression.string("damiano")))
                    .or(
                        Expression.property("type").equalTo(Expression.string("pet"))
                            .and(Expression.property("name").equalTo(Expression.string("kitty")))
                    )
            )
        val actual = select(all())
            .from(database)
            .where {
                (("type" equalTo "user") and ("name" equalTo "damiano")) or
                    (("type" equalTo "pet") and ("name" equalTo "kitty"))
            }
        assertEquals(expected.explain(), actual.explain())
    }

    ///////////////////////////////////////////////////////////////////////////
    // Selection data types
    ///////////////////////////////////////////////////////////////////////////

    @Test
    fun `select all where equalTo String`() {
        val expected = singleSelectionWhere("type", Expression.string("user"), Expression::equalTo)
        val actual = select(all()) from database where { "type" equalTo "user" }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where equalTo int`() {
        val expected = singleSelectionWhere("age", Expression.intValue(24), Expression::equalTo)
        val actual = select(all()) from database where { "age" equalTo 24 }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where equalTo long`() {
        val expected = singleSelectionWhere("age", Expression.longValue(24), Expression::equalTo)
        val actual = select(all()) from database where { "age" equalTo 24L }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where equalTo float`() {
        val expected = singleSelectionWhere("age", Expression.floatValue(24.50F), Expression::equalTo)
        val actual = select(all()) from database where { "age" equalTo 24.50F }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where equalTo double`() {
        val expected = singleSelectionWhere("age", Expression.doubleValue(24.50), Expression::equalTo)
        val actual = select(all()) from database where { "age" equalTo 24.50 }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where equalTo boolean`() {
        val expected = singleSelectionWhere("isBorn", Expression.booleanValue(true), Expression::equalTo)
        val actual = select(all()) from database where { "isBorn" equalTo true }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where equalTo map`() {
        val map = mutableMapOf<String, Any>("key" to "value")
        val expected = singleSelectionWhere("properties", Expression.map(map), Expression::equalTo)
        val actual = select(all()) from database where { "properties" equalTo map }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where equalTo list`() {
        val list = listOf("key", "value")
        val expected = singleSelectionWhere("channels", Expression.list(list), Expression::equalTo)
        val actual = select(all()) from database where { "channels" equalTo list }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where equalTo date`() {
        val date = Date()
        val expected = singleSelectionWhere("channels", Expression.date(date), Expression::equalTo)
        val actual = select(all()) from database where { "channels" equalTo date }
        assertEquals(expected.explain(), actual.explain())
    }

    ///////////////////////////////////////////////////////////////////////////
    // Ordering
    ///////////////////////////////////////////////////////////////////////////

    @Test
    fun `select all where orderBy ascending`() {
        val expected = singleSelectionWhere("type", Expression.string("user"), Expression::equalTo)
            .orderBy(Ordering.property("name").ascending())
        val actual = select(all())
            .from(database)
            .where { "type" equalTo "user" }
            .orderBy { "name".ascending() }
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where orderBy descending`() {
        val expected = singleSelectionWhere("type", Expression.string("user"), Expression::equalTo)
            .orderBy(Ordering.property("name").descending())
        val actual = select(all())
            .from(database)
            .where { "type" equalTo "user" }
            .orderBy { "name".descending() }
        assertEquals(expected.explain(), actual.explain())
    }

    ///////////////////////////////////////////////////////////////////////////
    // Limit
    ///////////////////////////////////////////////////////////////////////////

    @Test
    fun `select all where limit`() {
        val expected = singleSelectionWhere("type", Expression.string("user"), Expression::equalTo)
            .limit(Expression.intValue(1))
        val actual = select(all()).from(database).where { "type" equalTo "user" }.limit(1)
        assertEquals(expected.explain(), actual.explain())
    }

    @Test
    fun `select all where orderBy limit`() {
        val expected = singleSelectionWhere("type", Expression.string("user"), Expression::equalTo)
            .orderBy(Ordering.property("name").ascending())
            .limit(Expression.intValue(1))
        val actual = select(all())
            .from(database)
            .where { "type" equalTo "user" }
            .orderBy { "name".ascending() }
            .limit(1)
        assertEquals(expected.explain(), actual.explain())
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private methods
    ///////////////////////////////////////////////////////////////////////////

    private fun createDatabase(): Database {
        val name = "test-database.db"
        val config = DatabaseConfiguration().setDirectory(tempDir.root.absolutePath)
        return Database(name, config)
    }

    private inline fun singleSelectionWhere(field: String, valueExpression: Expression, operator: Expression.(Expression) -> Expression) =
        QueryBuilder.select(SelectResult.all())
            .from(DataSource.database(database))
            .where(Expression.property(field).operator(valueExpression))
}