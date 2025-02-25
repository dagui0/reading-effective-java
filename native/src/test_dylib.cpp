#include <iostream>
#include <dlfcn.h>
#include <slist.h>

using namespace std;

int main(void) {
    void *lib_handle = dlopen("libslist.dylib", RTLD_LOCAL);
    if (!lib_handle) {
        cerr << "[" << __FILE__ << "] main: Unable to open library: " << dlerror() << endl;
        return EXIT_FAILURE;
    }

    SimpleLinkedList_creator *NewSimpleLinkedList = (SimpleLinkedList_creator*)dlsym(lib_handle, "NewSimpleLinkedList");
    if (!NewSimpleLinkedList) {
        cerr << "[" << __FILE__ << "] main: Unable to find new function: " << dlerror() << endl;
        return EXIT_FAILURE;
    }

    SimpleLinkedList_disposer *DeleteSimpleLinkedList = (SimpleLinkedList_disposer*)dlsym(lib_handle, "DeleteSimpleLinkedList");
    if (!DeleteSimpleLinkedList) {
        cerr << "[" << __FILE__ << "] main: Unable to find delete function: " << dlerror() << endl;
        return EXIT_FAILURE;
    }

    SimpleLinkedList *list = (SimpleLinkedList*)NewSimpleLinkedList();
    list->add(1);
    list->add(2);
    list->add(3);
    list->add(4);

    list->print_dump();

    try {
        list->insertAt(10000, 1000);
    }
    catch (SimpleLinkedList::Error *e) {
        cout << "caught exception: code=" << e->code << ", index=" << e->index << ", data=" << e->data << endl;
        delete e;
    }

    DeleteSimpleLinkedList(list);

    if (dlclose(lib_handle) != 0) {
        cerr << "[" << __FILE__ << "] main: Unable to close library: "
             << dlerror() << "\n";
    }

    return 0;
}
