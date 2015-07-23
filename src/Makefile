JFLAGS = -g -Xlint
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	CompressedHashTrie.java \
	DoubleHashedHashMap.java \
	Entry.java \
	Set.java \
	Boost.java \
	QueryEntry.java \
	ByteArrayCharSequence.java \
	TypeAheadSearchDriver.java \
	MinPQ.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm *.class

run:
	java TypeAheadSearchDriver