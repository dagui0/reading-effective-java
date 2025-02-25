#include <iostream>
#include <slist.h>

using namespace std;

int main(void) {
    SimpleLinkedList *list = new SimpleLinkedList();

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

    delete list;

    return 0;
}
