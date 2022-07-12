package org.whsv26.playground.stdlib.collection

import scala.annotation.tailrec
import scala.collection.concurrent
import scala.collection.mutable
import scala.collection.immutable
import scala.concurrent.ExecutionContext

mutable.HashMap // Hash table
mutable.TreeMap // Red-Black tree
immutable.HashMap // Compressed Hash-Array Mapped Trie
immutable.TreeMap // Red-Black tree
concurrent.TrieMap // Hash-Array Mapped Trie with CAS

mutable.HashSet // Hash table
mutable.BitSet // Bitmap
mutable.TreeSet // Red-Black tree
mutable.LinkedHashSet // hash table with linked entries
immutable.HashSet // Compressed Hash-Array Mapped Trie
immutable.BitSet // Bitmap
immutable.TreeSet // Red-Black tree
immutable.ListSet

mutable.ArrayBuffer // Growable and Shrinkable
mutable.ArrayBuilder // Growable

mutable.StringBuilder // java StringBuilder class wrapper

mutable.Queue // array backed
immutable.Queue // in and out lists with pivoting

mutable.Stack // array backed
// immutable.Stack is actually immutable.List

mutable.Set(1).getClass
immutable.Set(1).getClass

