# Type Ahead Search

Michael Miller, Jeffrey Sham
Quora Programming Challenge #1 - Type Ahead Search

--------------------
Compressed Hash Trie
--------------------
In this implemenation of the trie data structure. We took a slightly different approach than the normal linked or double array trie structure. This was due to multiple reasons, as will be explained through this design documentation.
In the array-structured trie implementation, each node contains an array containing all possible characters, in this case 126 ASCII characters. The benefit of this, is that when inserting into the trie it takes O(L) time, where L is the length of the string being inserted. In addition, a string search will also only take O(L) time. Delete is also as easy as following a string down to its last node then recursively deleting all its nodes. This again takes O(L) time. The major drawback to this implementation is that its space efficiency is grossly large. USE BIG-O NOTATION TO SHOW THIS...
In the list-structured trie implementation, each node of the trie is linked to both the next node and another node. This saves space, as only the keys that are needed are used in the trie. However, retrieval and insert time becomes slower as one might have to traverse through many single character nodes to reach the starting position of a string.
GO INTO THE PROS AND CONS OF BOTH STRUCTURES MORE... BE MORE COHERENT

In the Compressed Hash Trie, we tried to combat the issues of grossly poor space efficiency of the array-structured trie implementation while still mainitaining the array-structured trie's constant insert, retrieval, and delete. To do this, we made each node of the trie a hashtable. This hashtable holds TrieHashNodes that are hashed by the key character they contain. In this way, each TrieNode only grows 1.75 times bigger than the number of characters it is storing (so as to maintain a load factor of 0.75). 
