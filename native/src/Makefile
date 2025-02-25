CXXFLAGS	= -Wall -I../include
LDFLAGS		= -lstdc++

HEADERS		= ../include/slist.h
SRCS		= slist.cpp
OBJECTS		= slist.o

LIBNAME		= slist

# windows: .exe, linux: (empty), mac: (empty)
EXE			= .exe
# windows: dll, linux: so, mac: dylib
DLIBEXT		= dll
SLIBEXT		= a

.PHONY:		all test clean

all: lib$(LIBNAME).$(DLIBEXT) lib$(LIBNAME).$(SLIBEXT)
test: test_slistfunc$(EXE) test_dynamic$(EXE)

.cpp.o:
	$(CXX) -c -o $@ $< $(CXXFLAGS)

slist.o: slist.cpp
	$(CXX) -c -o $@ $< -D SLIST_EXPORTS $(CXXFLAGS)

lib$(LIBNAME).$(DLIBEXT): $(OBJECTS)
	$(CXX) -o $@ $(OBJECTS) -shared -Wl,--subsystem,windows

lib$(LIBNAME).$(SLIBEXT): $(OBJECTS)
	$(AR) cr $@ $(OBJECTS)

test_slistfunc$(EXE): test_slistfunc.cpp $(SRCS) $(HEADERS)
	$(CXX) -o $@ $< $(SRCS) -D DEBUG -D SLIST_STATIC $(CXXFLAGS) $(LDFLAGS) && \
	./$@

test_dynamic$(EXE): test_dynamic.cpp lib$(LIBNAME).$(DLIBEXT)
	$(CXX) -o $@ $< -D DEBUG $(CXXFLAGS) $(LDFLAGS) -L. lib$(LIBNAME).$(DLIBEXT) && \
	ldd $@ && \
	./$@

clean:
	rm -f *.o test_*$(EXE) *.$(SLIBEXT) *.$(DLIBEXT)
