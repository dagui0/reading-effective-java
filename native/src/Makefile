CXXFLAGS	= -Wall -I../include
LDFLAGS		= 

HEADERS		= ../include/slist.h
SRCS		= slist.cpp
OBJECTS		= slist.o

LIBNAME		= slist
SLIBEXT		= a
ifeq ($(OS),Windows_NT)
  EXE		= .exe
  DLIBEXT	= dll
  DL_CXXFLAGS	= -D SLIST_EXPORTS $(CXXFLAGS)
  DL_LDFLAGS	= -shared -Wl,--subsystem,windows
else
  UNAME_S	= $(shell uname -s)
  EXE		=
  ifeq ($(UNAME_S),Darwin)
    DLIBEXT	= dylib
    DL_CXXFLAGS	= -D SLIST_EXPORTS $(CXXFLAGS)
    DL_LDFLAGS	= -dynamiclib -fvisibility=hidden
  else # liunx
    DLIBEXT	= so
    DL_CXXFLAGS	= -D SLIST_EXPORTS -fPIC $(CXXFLAGS)
    DL_LDFLAGS	= -shared -Wl,-soname,lib$(LIBNAME).so
  endif 
endif

.PHONY:		all test clean checkos

OUTPUTS		= lib$(LIBNAME).$(DLIBEXT) lib$(LIBNAME).$(SLIBEXT)
TESTS		= test_slistfunc$(EXE) test_dynamic$(EXE)

ifeq ($(UNAME_S),Darwin)
  TESTS		= test_slistfunc$(EXE) test_dylib$(EXE)
endif

all: $(OUTPUTS)
test: $(TESTS)

checkos:
	@echo "\$$OS=$(OS) \$$(uname -s)=$(UNAME_S)"

.cpp.o:
	$(CXX) -c -o $@ $< $(CXXFLAGS)

slist.o: slist.cpp
	$(CXX) -c -o $@ $< $(DL_CXXFLAGS)

lib$(LIBNAME).$(DLIBEXT): $(OBJECTS)
	$(CXX) -o $@ $(OBJECTS) $(DL_LDFLAGS)

lib$(LIBNAME).$(SLIBEXT): $(OBJECTS)
	$(AR) cr $@ $(OBJECTS)

test_slistfunc$(EXE): test_slistfunc.cpp $(SRCS) $(HEADERS)
	$(CXX) -o $@ $< $(SRCS) -D DEBUG -D SLIST_STATIC $(CXXFLAGS) $(LDFLAGS) && \
	./$@

test_dynamic$(EXE): test_dynamic.cpp lib$(LIBNAME).$(DLIBEXT)
	$(CXX) -o $@ $< -D DEBUG $(CXXFLAGS) $(LDFLAGS) -L. lib$(LIBNAME).$(DLIBEXT) && \
	LD_LIBRARY_PATH=. ldd $@ && \
	LD_LIBRARY_PATH=. ./$@

# macos
test_dylib$(EXE): test_dylib.cpp lib$(LIBNAME).$(DLIBEXT)
	$(CXX) -o $@ $< -D DEBUG $(CXXFLAGS) $(LDFLAGS) && \
	otool -L $@ && \
	./$@

clean:
	rm -f *.o $(OUTPUTS) $(TESTS)
