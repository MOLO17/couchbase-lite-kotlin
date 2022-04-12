# Couchbase Lite Kotlin

Kotlin-friendly extensions for Couchbase Lite Android and Java SDKs.

Proudly made by [MOLO17 Srl](https://molo17.com/) 🚀

## Kotlin extensions for Couchbase Lite

Couchbase Lite Kotlin is a lightweight library that adds convenient
extension functions to [Couchbase Lite](https://docs.couchbase.com/couchbase-lite/current/java-android.html)
Android and Java SDKs.

This library introduces a lightweight wrapper on top of N1QL query language,
Document creation and much more. Also, it provides support for a more
fluent listener API, introducing first-class support for
[Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) and the
[Flow API](https://kotlinlang.org/docs/reference/coroutines/flow.html).

## Installing

Add JitPack as repository for your project:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

And then to your module `build.gradle` file:

```groovy
implementation "com.github.MOLO17.couchbase-lite-kotlin:kotlin:1.2.1"
```

For Android use

```groovy
implementation "com.github.MOLO17.couchbase-lite-kotlin:android-ktx:1.2.1"
```

## Contents

Here are the main features that Couchbase Lite Kotlin provides for
boosting the integration of the Couchbase Lite SDK with Kotlin.

- [QueryBuilder extensions](#querybuilder-extensions)
- [Document builder DSL](#document-builder-dsl)
- [Query Flow support](#query-flow-support)
- [ResultSet model mapping](#resultset-model-mapping)
- [Database extensions](#database-extensions)
- [Replicator extensions](#replicator-extensions)

### QueryBuilder extensions

Syntax for building a query has gone more straight-forward thanks to the
`infix` function support.

```kotlin
select(all()) from database where { "type" equalTo "user" }
```

Or just a bunch of fields:

```kotlin
select("name", "surname") from database where { "type" equalTo "user" }
```

You can even do more powerful querying:

```kotlin
select("name", "type")
  .from(database)
  .where { 
    ("type" equalTo "user" and "name" equalTo "Damian") or
      ("type" equalTo "pet" and "name" like "Kitt") 
  }
  .orderBy { "name".ascending() }
  .limit(10)
```

### Document builder DSL

For creating a new `MutableDocument` ready to be saved, you can now use
a new Kotlin DSL:

```kotlin
val document = MutableDocument {
  "name" to "Damian"
  "surname" to "Giusti"
  "age" to 24
  "pets" to listOf("Kitty", "Kitten", "Kitto")
  "type" to "user"
}

database.save(document)
```

### Query Flow support

Now Couchbase Lite queries become Flows. Couchbase Lite Kotlin ports
the coroutines Flow stream API for observing Live Queries results.

```kotlin
select(all())
  .from(database)
  .where { "type" equalTo "user" }
  .asFlow()
  .collect { value: ResultSet -> 
    // consume ResultSet
  }
```

As plus, you receive automatic LiveQuery cancellation when the Flow tears down.

### ResultSet model mapping

Thanks to [Map delegation](https://kotlinlang.org/docs/reference/delegated-properties.html#storing-properties-in-a-map),
mapping a ResultSet to a Kotlin class has never been so easy.

The library provides the `ResultSet.toObjects()` and `Query.asObjectsFlow()` (with android version `Query.asKtxObjectsFlow()`)
extensions for helping to map results given a factory lambda.

Such factory lambda accepts a `Map<String, Any?>` and returns an instance
of a certain type. Those requirements fits perfectly with a Map-delegated
class.

```kotlin
class User(map: Map<String, Any?>) {
  val name: String by map
  val surname: String by map
  val age: Int by map
}

val users: List<User> = query.execute().toObjects(::User)
```

```kotlin
class User(map: Map<String, Any?>) {
  val name: String by map
  val surname: String by map
  val age: Int by map
}

val users: Flow<List<User>> = query.asObjectsFlow(::User)
```

### Database extensions

As seen with the Query extensions, the Database has also been powered up.

You can now observe the `DatabaseChange` and `DocumentChangeEvents` using
the Kotlin Flow API.

```kotlin
val changes: Flow<DatabaseChange> = database.changesFlow()
val documentChanges: Flow<DocumentChange> = database.documentChangesFlow(docId)
```

Also, a new useful syntax has been introduced when performing batch operations:

```kotlin
database.doInBatch {
  save(document)
  delete(otherDocument)
}
```

The `Database` is used as receiver in the specified lambda.

### Replicator extensions

Replicator syntax has been boosted with the Kotlin Flow API. You can
observe events using the following Flows:

```kotlin
val changesFlow: Flow<ReplicatorChange> = replicator.changesFlow()
val replicationFlow: Flow<DocumentReplication> = replicator.documentReplicationFlow()
```

In addition, for Android users, you can now bind the Replicator
`start()` and `stop()` methods to be performed automatically when your
[Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)-enabled
component gets resumed or paused.

```kotlin
// Binds the Replicator to the Application lifecycle.

replicator.bindToLifecycle(ProcessLifecycleOwner.get().lifecycle)
```

```kotlin
// Binds the Replicator to the Activity/Fragment lifecycle.
{
    // inside an Activity or a Fragment ...
    
    replicator.bindToLifecycle(lifecycle)
}
```

That's it! Replicator will be automatically started when your component
passes the `ON_RESUME` state, and it will be stopped when the component
passes the `ON_PAUSED` state. As you may imagine, no further action will
be made after the `ON_DESTROY` state.

## Authors

- [Damiano Giusti](https://github.com/damianogiusti/)
- [MOLO17 Srl](https://molo17.com/)
