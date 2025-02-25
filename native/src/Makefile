CLFAGS			= -Wall -I../include
LDFLAGS			=

HEADERS			= ../include/slist.h
SRCS			= slist.c
OBJECTS			= slist.o

LIBNAME			= slist
SLIBEXT			= a
ifeq ($(OS),Windows_NT)
  EXE			= exe
  DLIBEXT		= dll
  DL_CLFAGS		= -D SLIST_EXPORTS $(CLFAGS)
  DL_LDFLAGS	= -shared -Wl,--subsystem,windows
else
  UNAME_S		= $(shell uname -s)
  EXE			= out
  ifeq ($(UNAME_S),Darwin)
    DLIBEXT		= dylib
    DL_CLFAGS	= -D SLIST_EXPORTS $(CLFAGS)
    DL_LDFLAGS	= -dynamiclib -fvisibility=hidden
  else # liunx
    DLIBEXT		= so
    DL_CLFAGS	= -D SLIST_EXPORTS -fPIC $(CLFAGS)
    DL_LDFLAGS	= -shared -Wl,-soname,lib$(LIBNAME).so
  endif 
endif

.PHONY:		all test clean checkos

OUTPUTS			= lib$(LIBNAME).$(DLIBEXT) lib$(LIBNAME).$(SLIBEXT)
TESTS			= test_func.$(EXE) test_dl.$(EXE)

ifeq ($(UNAME_S),Darwin)
  TESTS			= test_func.$(EXE) test_dylib.$(EXE)
endif

all: $(OUTPUTS)
test: $(TESTS)

checkos:
	@echo "\$$OS=$(OS) \$$(uname -s)=$(UNAME_S)"

.cpp.o:
	$(CC) -c -o $@ $< $(CLFAGS)

slist.o: slist.c
	$(CC) -c -o $@ $< $(DL_CLFAGS)

lib$(LIBNAME).$(DLIBEXT): $(OBJECTS)
	$(CC) -o $@ $(OBJECTS) $(DL_LDFLAGS)

lib$(LIBNAME).$(SLIBEXT): $(OBJECTS)
	$(AR) cr $@ $(OBJECTS)

test_func.$(EXE): test_func.c $(SRCS) $(HEADERS)
	$(CC) -o $@ $< $(SRCS) -D DEBUG -D SLIST_STATIC $(CLFAGS) $(LDFLAGS) && \
	./$@

test_dl.$(EXE): test_dl.c lib$(LIBNAME).$(DLIBEXT)
	$(CC) -o $@ $< -D DEBUG $(CLFAGS) $(LDFLAGS) -L. lib$(LIBNAME).$(DLIBEXT) && \
	LD_LIBRARY_PATH=. ldd $@ && \
	LD_LIBRARY_PATH=. ./$@

# macos
test_dylib.$(EXE): test_dylib.cpp lib$(LIBNAME).$(DLIBEXT)
	$(CC) -o $@ $< -D DEBUG $(CLFAGS) $(LDFLAGS) && \
	LD_LIBRARY_PATH=. otool -L $@ && \
	LD_LIBRARY_PATH=. ./$@

clean:
	rm -f *.o $(OUTPUTS) $(TESTS)
