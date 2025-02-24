CXXFLAGS	= -Wall -I../include
LDFLAGS		= -lstdc++

HEADERS		= ../include/SimpleLinkedList.h
SRCS		= SimpleLinkedList.cpp
OBJECTS		= SimpleLinkedList.o
LIBNAME		= libslist
EXEEXT		= .exe
LIBEXT		= .dll

.PHONY:		clean

.cpp.o:
	$(CXX) -c -o $@ $< $(CXXFLAGS)

all: $(LIBNAME)$(LIBEXT)

$(LIBNAME).def: $(OBJECTS)
	dlltool --output-def $@ --kill-at --dllname $(LIBNAME)$(LIBEXT) $(OBJECTS)

$(LIBNAME).la: $(LIBNAME).def $(OBJECTS)
	dlltool --output-lib $@ --input-def $(LIBNAME).def --kill-at --dllname $(LIBNAME)$(LIBEXT) $(OBJECTS)

$(LIBNAME)$(LIBEXT): $(LIBNAME).la $(LIBNAME).def $(OBJECTS)
	$(LD) -o $@ -shared $(LIBNAME).la $(LDFLAGS)

test1$(EXEEXT): test1.cpp $(SRCS) $(HEADERS)
	$(CXX) -o $@ $< $(SRCS) -DDEBUG=1 $(CXXFLAGS) $(LDFLAGS)

clean:
	rm -f *.o *$(EXEEXT) *.a *.la *.def *$(LIBEXT)
