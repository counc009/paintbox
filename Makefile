SOURCES=$(shell find . -name '*.java')

all: $(SOURCES)
	./build.sh

clean:
	rm -rf build

.PHONY: clean all
