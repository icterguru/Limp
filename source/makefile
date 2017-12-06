# A general java project makefile
# Author: Michael Walker (mjwalker3@crimson.ua.edu)
# create: 11/27/2014

# Set the file name of your jar package:
JAR_PKG = lexer.jar

# Set your entry point of your java app:
ENTRY_POINT = LiMp

RES_DIR = no
# Set your source file

SOURCE_FILES = \
Types.java \
Lexeme.java \
Lexer.java \
Parser.java \
Environment.java \
Interpreter.java \
LiMp.java

# Set your java compiler here:
JAVAC = javac
# 
JFLAGS = -encoding UTF-8 

# Guide：
# make new: 
# ├── a.jar
# ├── bin
# │     └── test
# │             ├── A.class
# │             └── B.class
# ├── makefile
# ├── res
# │     └── doc
# │            └── readme.txt
# └── src
#        └── test
#                ├── A.java
#                └── B.java

# make sss:
# make build: in bin, generate java classes
# make clean: 
# make rebuild: 
# make run: run test
# make jar: generate executable jar package

#############Do not change below codes####################

vpath %.class bin
vpath %.java src

# show help message by default
Default:$(SOURCE_FILES:.java=.class)

# pattern rule
%.class: %.java
	$(JAVAC)   $(JFLAGS) $<

rebuild: clean build

.PHONY: new clean run jar

new:
ifeq ($(RES_DIR),yes)
	mkdir -pv  bin res
else
	mkdir -pv  bin
endif

clean:
	rm -frv *.class

source-conditionals:
	cat conditionals.txt
conditionals:
	java  $(ENTRY_POINT) conditionals.txt
source-recursion:
	cat recursion.txt
recursion:
	java  $(ENTRY_POINT) recursion.txt
source-iteration:
	cat iteration.txt
iteration:
	java  $(ENTRY_POINT) iteration.txt
source-functions:
	cat iteration.txt
functions:
	java  $(ENTRY_POINT) functions.txt
source-lists:
	cat lists.txt
lists:
	java  $(ENTRY_POINT) lists.txt
run:
	java  $(ENTRY_POINT) rpn.txt

jar:
ifeq ($(RES_DIR),yes)
	jar cvfe $(JAR_PKG) $(ENTRY_POINT)  -C bin . res
else
	jar cvfe $(JAR_PKG) $(ENTRY_POINT) -C bin .
endif
