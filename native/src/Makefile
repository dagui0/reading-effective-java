LDD					= ldd

JNI_HEADER_DIR		= ../../build/generated/sources/headers/java/main

CFLAGS				= -Wall -I../include
JNI_CFLAGS			= -I$(JNI_HEADER_DIR) -I"$(JAVA_HOME)/include"
DL_CFLAGS			=
LDFLAGS				=
DL_LDFLAGS			=

HEADERS				= ../include/slist.h
SRCS				= slist.c
OBJECTS				= slist.o

LIBNAME				= slist
SLIB				= a
EXE					= out
ifeq ($(OS),Windows_NT) # assume Msys2
  EXE				= exe
  DLIB				= dll
  JNI_OS_CFLAGS		= -I"$(JAVA_HOME)/include/win32"
  DL_LDFLAGS		= -shared -Wl,--subsystem,windows
  INSTALL_DIR		= ../lib/windows/$(shell echo $$MSYSTEM | tr A-Z a-z)
else
  UNAME_S			= $(shell uname -s | tr A-Z a-z)
  UNAME_M			= $(shell uname -m | tr A-Z a-z)
  INSTALL_DIR		= ../lib/$(UNAME_S)/$(UNAME_M)
  ifeq ($(UNAME_S),darwin)
    DLIB			= dylib
    JNI_OS_CFLAGS	= -I"$(JAVA_HOME)/include/darwin"
    DL_LDFLAGS		= -dynamiclib -fvisibility=hidden
    LDD				= otool -L
  else # liunx
    DLIB			= so
    JNI_OS_CFLAGS	= -I"$(JAVA_HOME)/include/linux"
    DL_CFLAGS		= -fPIC
    DL_LDFLAGS		= -shared -Wl,-soname,$@
  endif
endif

.PHONY:		all test clean checkos install

OUTPUTS			= lib$(LIBNAME).$(DLIB) lib$(LIBNAME).$(SLIB) jni_$(LIBNAME).$(DLIB)
TESTS			= test_func.$(EXE) test_dl.$(EXE)

all: $(OUTPUTS)
test: $(TESTS)

checkos:
	@echo "\$$OS=$(OS) \$$(uname -s)=$(UNAME_S) \$$(INSTALL_DIR)=$(INSTALL_DIR)"

install: $(OUTPUTS)
	mkdir -p $(INSTALL_DIR) && \
	cp -f $(OUTPUTS) $(INSTALL_DIR)

slist.o: slist.c $(HEADERS)
	$(CC) -c -o $@ $< -D SLIST_EXPORTS $(CFLAGS) $(DL_CFLAGS)

jni_slist.o: jni_slist.c $(JNI_HEADER_DIR)/com_yidigun_utils_JniSlist.h
	$(CC) -c -o $@ $< $(CFLAGS) $(DL_CFLAGS) $(JNI_CFLAGS) $(JNI_OS_CFLAGS)

lib$(LIBNAME).$(DLIB): $(OBJECTS)
	$(CC) -o $@ $(OBJECTS) $(LDFLAGS) $(DL_LDFLAGS) && \
	LD_LIBRARY_PATH=. $(LDD) $@

jni_$(LIBNAME).$(DLIB): jni_slist.o lib$(LIBNAME).$(DLIB)
	$(CC) -o $@ $< $(LDFLAGS) $(DL_LDFLAGS) -L. lib$(LIBNAME).$(DLIB) && \
	LD_LIBRARY_PATH=. $(LDD) $@

lib$(LIBNAME).$(SLIB): $(OBJECTS)
	$(AR) cr $@ $(OBJECTS)

test_func.$(EXE): test_func.c $(SRCS) $(HEADERS)
	$(CC) -o $@ $< $(SRCS) -D DEBUG -D SLIST_STATIC $(CFLAGS) $(LDFLAGS) && \
	./$@

test_dl.$(EXE): test_dl.c lib$(LIBNAME).$(DLIB)
	$(CC) -o $@ $< -D DEBUG $(CFLAGS) $(LDFLAGS) -L. lib$(LIBNAME).$(DLIB) && \
	LD_LIBRARY_PATH=. $(LDD) $@ && \
	LD_LIBRARY_PATH=. ./$@

clean:
	rm -f *.o $(OUTPUTS) $(TESTS)
